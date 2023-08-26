package util;
import classes.Action;

import javax.sound.midi.*;
import java.util.List;


public class SongCreator {
    private static final int MILLISECONDS_IN_A_MINUTE = 60000;

    private static Synthesizer synthesizer = null;
    private static MidiChannel[] channel;

    private static int instrument = 0;
    private static int default_volume = 80;
    private static int current_volume = default_volume;
    private static int octave = 4;
    private static int BPM = 60;
    private static void open()  {
        if (synthesizer == null) {
            try {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                channel = synthesizer.getChannels();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        setVolume(default_volume);
    }

    private static void close() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }

    private static void setInstrument(int value) {
        instrument = value;
    }

    private static void setVolume(int value) {
        current_volume = value;
    }
    private static void doubleBPM() {
        current_volume *= 2;
    }


    private static void increaseBpm(int value) {
        BPM += value;
    }

    private static void setBpm(int value) {
        BPM = value;
    }

    private static void rest() throws InterruptedException
    {
        Thread.sleep(MILLISECONDS_IN_A_MINUTE/BPM);
    }

    public static void playFromList(List<Action> actions){

        open();

        for (Action action : actions) {
            switch (action.getName()) {
                case "playNote":
                    playNote(action.getValue());
                    break;

                case "instrument":
                    setInstrument(action.getValue());
                    break;

                case "volume":
                    if (action.getValue() == 1) {
                        setVolume(current_volume * 2);
                    } else {
                        setVolume(default_volume);
                    }
                    break;

                case "increaseBPM":
                    increaseBpm(action.getValue());
                    break;
                case "setBPM":
                    setBpm(action.getValue());
                    break;
                case "octave":
                    octave += action.getValue();
                    break;

                case "rest":
                    try {
                        rest();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                default:
                    break;
            }
        }

        close();
    }

    private static void playNote(int note) {
        try {
            channel[instrument].noteOn(note + octave * 12, current_volume);
            Thread.sleep(MILLISECONDS_IN_A_MINUTE/BPM);
            channel[instrument].noteOff(note);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String text[])
    {
        playFromList(TextMapping.getActions("BPM+FFFCDDC AAGGF"));
    }
}