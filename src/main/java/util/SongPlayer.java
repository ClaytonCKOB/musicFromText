package util;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import classes.Action;
public class SongPlayer {
    private static final int MICROSECONDS_IN_A_MINUTE = 60000000;
    public static final int MIDI_FILE_TYPE = 1;
    public static final String DEFAULT_OUTPUT_FILE_NAME = "output.mid";
    public static final int DEFAULT_BPM_INCREASE = 80;
    public static final int MAX_OCTAVE = 10;
    public static final int MIN_OCTAVE = 1;
    public static final int TELEPHONE_NOTE_VALUE = 125;
    public static final int NOTES_IN_A_OCTAVE = 12;

    private static Sequencer sequencer;
    private static final int DEFAULT_OCTAVE = 4;
    private static final int DEFAULT_BPM = 120;
    private static final int DEFAULT_VOLUME = 80;

    private static int currentVolume = DEFAULT_VOLUME;
    private static int octave = DEFAULT_OCTAVE;
    private static int bpm = DEFAULT_BPM;
    private static final byte SET_TEMPO_MESSAGE_CODE = 0x51;

    private static int tick = 0;

    private static final int QUARTER_NOTE_LENGTH = 24;

    public static void startSequencer() throws MidiUnavailableException {
        if (sequencer == null || !sequencer.isOpen()) {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        }
        resetVolume();
    }

    public static void closeSequencer() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.close();
        }
        currentVolume = DEFAULT_VOLUME;
        octave = DEFAULT_OCTAVE;
        bpm = DEFAULT_BPM;
        tick = 0;
        resetVolume();
    }

    private static MidiEvent createProgramChangeEvent(int instrument) throws InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        return new MidiEvent(message, tick);
    }

    private static void setInstrument(Track track, int value) throws InvalidMidiDataException {
        track.add(createProgramChangeEvent(value));
    }

    private static void resetVolume() {
        currentVolume = DEFAULT_VOLUME;
    }

    private static void doubleVolume() {
        currentVolume *= 2;
        if(currentVolume > 127)
            currentVolume = 127;
    }

    private static byte[] getTempoMIDIMessage(int bpm){
        int tempoValue = MICROSECONDS_IN_A_MINUTE / bpm;

        byte[] data = new byte[3];

        data[0] = (byte) ((tempoValue >> 16) & 0xFF);
        data[1] = (byte) ((tempoValue >> 8) & 0xFF);
        data[2] = (byte) (tempoValue & 0xFF);

        return data;
    }
    private static void increaseBPM(Track track){

        setBpm(track,bpm + DEFAULT_BPM_INCREASE);
    }

    private static void setBpm(Track track, int newBPM) {
        bpm = newBPM;
        MetaMessage metaMessage = new MetaMessage();
        byte[] data = getTempoMIDIMessage(newBPM);

        try {
            metaMessage.setMessage(SET_TEMPO_MESSAGE_CODE, data, data.length );
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        track.add(new MidiEvent(metaMessage, tick));
    }

    private static void rest() {
        tick += QUARTER_NOTE_LENGTH;
    }


    public static void play(String text) {
        Thread thread = new Thread(() -> {
            try {
                List<Action> actions = TextMapping.getActions(text);
                startSequencer();
                Sequence sequence = createMidiSequence(actions);
                playSequence(sequence);
            } catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private static void playSequence(Sequence sequence) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        sequencer.setSequence(sequence);
        sequencer.start();

        while (sequencer.isRunning()) {
            Thread.sleep(100);
        }

        sequencer.stop();
        Thread.sleep(200);

        closeSequencer();

    }

    private static Sequence createMidiSequence(List<Action> actions) throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.PPQ, QUARTER_NOTE_LENGTH);
        Track track = sequence.createTrack();

        for (Action action : actions) {
            addActionToTrack(track, action);
        }
        return sequence;
    }

    private static void increaseOctave(){
        if(octave < MAX_OCTAVE)
            octave += 1;
    }
    private static void decreaseOctave(){
        if(octave > MIN_OCTAVE)
            octave -= 1;
    }


    private static void addActionToTrack(Track track, Action action) throws InvalidMidiDataException {
        switch (action.getName()) {
            case "playNote" -> playNote(track, action.getValue());
            case "changeInstrument" -> setInstrument(track, action.getValue());
            case "doubleVolume" ->  doubleVolume();
            case "resetVolume" -> resetVolume();
            case "increaseBPM" -> increaseBPM(track);
            case "setBPM" -> setBpm(track, action.getValue());
            case "increaseOctave" -> increaseOctave();
            case "decreaseOctave" -> decreaseOctave();
            case "rest" -> rest();
            case "playTelephone" -> playTelephoneSound(track);
            default -> {
            }
        }
    }

    private static void playNote(Track track, int note) {
        track.add(createNoteEvent(ShortMessage.NOTE_ON, note + octave * NOTES_IN_A_OCTAVE, currentVolume));
        tick += QUARTER_NOTE_LENGTH;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, note + octave * NOTES_IN_A_OCTAVE, 0));
    }

    private static void playTelephoneSound(Track track) {
        track.add(createNoteEvent(ShortMessage.NOTE_ON, TELEPHONE_NOTE_VALUE, currentVolume));
        tick += QUARTER_NOTE_LENGTH;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, TELEPHONE_NOTE_VALUE, 0));
    }

    private static MidiEvent createNoteEvent(int command, int note, int velocity) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(command, 0, note, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            System.out.println(note);
        }
        return new MidiEvent(message, tick);
    }


    public static void writeMidiFile(String text) throws InvalidMidiDataException, IOException {
        List<Action> actions = TextMapping.getActions(text);

        Sequence sequence = createMidiSequence(actions);

        File outputFile = new File(DEFAULT_OUTPUT_FILE_NAME);
        MidiSystem.write(sequence, MIDI_FILE_TYPE, outputFile);
    }
}