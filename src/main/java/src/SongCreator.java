package src;
import javax.sound.midi.*;
import java.util.HashMap;
import java.util.List;


public class SongCreator {

    private static Synthesizer synthesizer = null;
    private static MidiChannel channel;
    private static int default_volume = 63;
    private static void open()  {
        if (synthesizer == null) {
            try {
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                channel = synthesizer.getChannels()[0];
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        setInstrument(25);
        setVolume(default_volume);
    }

    private static void close() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }

    private static void setInstrument(int value) {
        if (channel != null) {
            channel.programChange(value);
        }
    }

    private static void setVolume(int value) {
        if (channel != null) {
            if(value > 127){
                channel.controlChange(7, default_volume);
            }else{
                channel.controlChange(7, value);
            }
        }
    }

    private static void setBpm(int value) {
        if (channel != null) {
            int microseconds_per_minute = 60_000_000;

            int microseconds_per_beat = microseconds_per_minute / value;

            channel.controlChange(0x51, (microseconds_per_beat >> 16) & 0xFF);
            channel.controlChange(0x52, (microseconds_per_beat >> 8) & 0xFF);
            channel.controlChange(0x53, microseconds_per_beat & 0xFF);
        }
    }

    public static void playFromList(List<HashMap<String, Integer>> actions){

        open();

        for (HashMap<String, Integer> action : actions) {
            // Tocar uma nota
            if(action.containsKey("frequency")){
                playNote(action.get("frequency"));

                // Troca de instrumento
            } else if(action.containsKey("instrument")){
                setInstrument(action.get("instrument"));

            } else if(action.containsKey("volume")){
                if(action.get("volume") == 1){
                    setVolume(default_volume * 2);
                }else if(action.get("volume") == 1){
                    setVolume(default_volume);
                }

            } else if(action.containsKey("bpm")){
                setBpm(action.get("bpm"));
            }


        }

        close();
    }

    private static void playNote(int note) {
        try {
            channel.noteOn(note, 64);
            Thread.sleep(500);
            channel.noteOff(note);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String text[])
    {
        playFromList(TextMapping.getActions("AAU AAA AU AU AOU AO A"));
    }
}