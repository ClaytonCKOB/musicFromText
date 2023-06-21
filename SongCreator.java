import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongCreator {

    private Synthesizer synthesizer = MidiSystem.getSynthesizer();
    private MidiChannel channel;

    private static void open(){
        synthesizer.open();
        channel = synthesizer.getChannels()[0];
    }

    private static void close(){
        synthesizer.close();
    }

    public static void playFromList(List<HashMap<String, Integer>> actions){
        
        open();
        
        for (HashMap<String, Object> action : actions) {
            String name = (String) hashMap.get("name");
            int age = (int) hashMap.get("age");
            System.out.println("Name: " + name + ", Age: " + age);

            if(action.containsKey("frequency")){
                playNote(action.get("frequency"));
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