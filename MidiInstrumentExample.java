import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MidiInstrumentExample {
    public static void main(String[] args) {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            MidiChannel channel = synthesizer.getChannels()[0];

            // Set the instrument to MIDI program number 7 (Trumpet)
            // int trumpetInstrument = 7;
            // channel.programChange(trumpetInstrument);

            int trumpetNote = 60; // C4
            int velocity = 64; // Medium velocity
            String beforeLetter = "";
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("A", 69);
            map.put("B", 71);
            map.put("C", 60);
            map.put("D", 62); 
            map.put("E", 64); 
            map.put("F", 66); 
            map.put("G", 68); 

            Pattern AtoG = Pattern.compile("[A-G]"); 
            Pattern atog = Pattern.compile("[a-g]"); 

            String text = "ABCDEFGa";
            for(int i = 0; i < text.length(); i++){
                String character = Character.toString(text.charAt(i));

                if(AtoG.matcher(character).find()){
                    channel.noteOn(map.get(character), velocity);
                    Thread.sleep(2000);
                    channel.noteOff(map.get(character));

                }else if(atog.matcher(character).find()){
                    if(AtoG.matcher(beforeLetter).find()){
                        channel.noteOn(map.get(beforeLetter), velocity);
                        Thread.sleep(2000);
                        channel.noteOff(map.get(beforeLetter));
                    }else{
                        channel.noteOn(0, velocity);
                        Thread.sleep(2000);
                        channel.noteOff(0);
                    }
                }

                beforeLetter = character;
            }

            synthesizer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}