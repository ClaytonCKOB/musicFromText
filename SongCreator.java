import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongCreator {
    public static void main(String[] args) {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            MidiChannel channel = synthesizer.getChannels()[0];
            String beforeLetter = "";
            
            Map<String, Integer> notes = new HashMap<String, Integer>();
            notes.put("A", 69);
            notes.put("B", 71);
            notes.put("C", 60);
            notes.put("D", 62); 
            notes.put("E", 64); 
            notes.put("F", 66); 
            notes.put("G", 68); 

            Pattern AtoG = Pattern.compile("[A-G]"); 
            Pattern atog = Pattern.compile("[a-g]"); 
            Pattern vogais = Pattern.compile("[OIUoiu]");
            Pattern consoantes = Pattern.compile("[H-Z&&h-z]");

            String text = "ABCDEFGa";

            for(int i = 0; i < text.length(); i++){
                String character = Character.toString(text.charAt(i));

                if(AtoG.matcher(character).find()){
                    playNote(channel, notes.get(character));

                }else if(atog.matcher(character).find()){
                    if(AtoG.matcher(beforeLetter).find()){
                        playNote(channel, notes.get(beforeLetter));
                    }else{
                        playNote(channel, 0);
                    }
                }else if(vogais.matcher(character).find()){
                    channel.programChange(7);   

                }else if(consoantes.matcher(character).find()){
                    if(AtoG.matcher(beforeLetter).find()){
                        playNote(channel, notes.get(beforeLetter));
                    }else{
                        playNote(channel, 0);
                    }
                }else{
                    switch (character){
                        case "!":
                            channel.programChange(114);   
                            break;
                        
                        case ";":
                            channel.programChange(76);   
                            break;
                        
                        case ",":
                            channel.programChange(20);   
                            break;
                        
                        case " ":
                            channel.programChange(20);   
                            break;

                        case "\n":
                            channel.programChange(15);   
                            break;
                    }
                }    

                beforeLetter = character;
            }

            synthesizer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void playNote(MidiChannel channel, int note) {
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