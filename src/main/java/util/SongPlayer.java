package util;

import javax.sound.midi.*;
import java.util.List;
import classes.Action;
public class SongPlayer {
    private static final int MICROSECONDS_IN_A_MINUTE = 60000000;

    private static Sequencer sequencer;
    private static MidiChannel[] channels;
    private static final int DEFAULT_OCTAVE = 4;
    private static final int DEFAULT_BPM = 4;
    private static final int DEFAULT_INSTRUMENT = 0;
    private static final int DEFAULT_VOLUME = 80;

    private static int instrument = DEFAULT_INSTRUMENT;

    private static int currentVolume = DEFAULT_VOLUME;
    private static int octave = DEFAULT_OCTAVE;
    private static int bpm = DEFAULT_BPM;
    private static final byte SET_TEMPO_MESSAGE_CODE = 0x51;

    private static int tick;

    private static final int QUARTER_NOTE_LENGTH = 24;

    public static void open() throws MidiUnavailableException {
        if (sequencer == null || !sequencer.isOpen()) {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            if (sequencer instanceof Synthesizer synthesizer) {
                channels = synthesizer.getChannels();
            }
        }
        resetVolume();
    }

    public static void close() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.close();
        }
        currentVolume = DEFAULT_VOLUME;
        octave = DEFAULT_OCTAVE;
        bpm = DEFAULT_BPM;
        instrument = DEFAULT_INSTRUMENT;

    }

    private static void setInstrument(int value) {
        instrument = value;
    }

    private static void resetVolume() {
        currentVolume = DEFAULT_VOLUME;
    }

    private static void doubleVolume() {
        currentVolume *= 2;
    }

    private static byte[] getSetTempoMIDIMessage(int bpm){
        int tempoValue = MICROSECONDS_IN_A_MINUTE / bpm;

        byte[] data = new byte[3];

        data[0] = (byte) ((tempoValue >> 16) & 0xFF);
        data[1] = (byte) ((tempoValue >> 8) & 0xFF);
        data[2] = (byte) (tempoValue & 0xFF);

        return data;
    }

    private static void setBpm(Track track, int bpm) {

        MetaMessage metaMessage = new MetaMessage();
        byte[] data = getSetTempoMIDIMessage(bpm);

        try {
            metaMessage.setMessage(SET_TEMPO_MESSAGE_CODE, data, data.length );
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        track.add(new MidiEvent(metaMessage, tick));
    }

    private static void rest() throws InterruptedException {
        tick += QUARTER_NOTE_LENGTH;
    }

    public static void play(String text) throws MidiUnavailableException {
        play(TextMapping.getActions(text));

    }

    public static void play(List<Action> actions) throws MidiUnavailableException {

        open();

        Sequence sequence;
        try {
            sequence = new Sequence(Sequence.PPQ, QUARTER_NOTE_LENGTH);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return;
        }
        Track track = sequence.createTrack();

        for (Action action : actions) {
            executeAction(track, action);
        }

        try {
            sequencer.setSequence(sequence);
            sequencer.start();

            while (sequencer.isRunning()) {
                Thread.sleep(100);
            }

            sequencer.stop();
            Thread.sleep(200);
        } catch (InvalidMidiDataException | InterruptedException e) {
            e.printStackTrace();
        }


        close();
    }

    private static void increaseOctave(){
        if(octave < 10)
            octave += 1;
    }

    private static void increaseBPM(){
        bpm += 80;
    }
    private static void executeAction(Track track, Action action) {
        switch (action.getName()) {
            case "playNote" -> playNote(track, action.getValue());
            case "instrument" -> setInstrument(action.getValue());
            case "volume" -> {
                if (action.getValue() == 1) {
                    doubleVolume();
                } else {
                    resetVolume();
                }
            }
            case "increaseBPM" -> increaseBPM();
            case "setBPM" -> setBpm(track, action.getValue());
            case "octave" -> increaseOctave();
            case "rest" -> {
                try {
                    rest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            case "telephone" -> playTelephoneSound(track);
            default -> {
            }
        }
    }

    private static void playNote(Track track, int note) {
        track.add(createNoteEvent(ShortMessage.NOTE_ON, note + octave * 12, currentVolume, tick));
        tick += QUARTER_NOTE_LENGTH;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, note + octave * 12, 0, tick));
    }

    private static void playTelephoneSound(Track track) {
        track.add(createNoteEvent(ShortMessage.NOTE_ON, 125, currentVolume, tick));
        tick += QUARTER_NOTE_LENGTH;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, 125, 0, tick));
    }

    private static MidiEvent createNoteEvent(int command, int note, int velocity, int tick) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(command, instrument, note, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            System.out.println(note);
        }
        return new MidiEvent(message, tick);
    }
}