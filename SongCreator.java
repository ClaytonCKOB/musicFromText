import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongCreator {

    private static Synthesizer synthesizer = null;
    private static MidiChannel channel;

    private static void open() throws MidiUnavailableException {
        if (synthesizer == null) {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];
        }
    }

    private static void close() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }

    public static void playFromList(List<HashMap<String, Integer>> actions){
        
        open();
        
        for (HashMap<String, Object> action : actions) {
            // Tocar uma nota
            if(action.containsKey("frequency")){
                playNote(action.get("frequency"));

            // Troca de instrumento
            }else if(action.containsKey("instrument")){
                setInstrument(action.get("instrument"));
            }

            
        }

        close();
    }

    private static void playNote(int note) {
        try {
            channel.noteOn(note, 64);
            Thread.sleep(2000);
            channel.noteOff(note);
        } 
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}