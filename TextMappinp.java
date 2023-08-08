import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMapping {

    private static boolean isOctave(String text, Integer indice)
    {
        return Character.toString(text.charAt(i)).equals('R') && i++ < text.length();
    }

    private static boolean isNote(String text)
    {
        Pattern notes_char = Pattern.compile("[A-Ga-g]"); 

        return notes_char.matcher(text).find();
    }

    private static boolean isNoteOrPhone(String text)
    {
        Pattern vogals = Pattern.compile("[OIUoiu]");

        return vogals.matcher(text).find();
    }

    private static boolean isInstrument(String text)
    {
        return character.equals('\n');
    }

    private static List<HashMap<String, Integer>> setAction(List<HashMap<String, Integer>> actions, String type, Integer value)
    {
        actions.add(new HashMap<String, Object>() {{
                        put(type, value);
                    }});

        return actions;
    }

    private static Integer getNote(String text)
    {
        Map<String, Integer> notes = new HashMap<String, Integer>(){{
            put("A", 69);
            put("B", 71);
            put("C", 60);
            put("D", 62); 
            put("E", 64); 
            put("F", 66); 
            put("G", 68); 
        }};

        return notes.get(Character.toUpperCase(character))
    }

    private static Integer getOctave(String text, Integer indice)
    {
        String next_char = Character.toString(text.charAt(indice++));

        if(next_char.equals('+')){
            return 1;
        }else if(next_char.equals('-')){
            return 0;
        }
    }

    private static boolean isDoubleVolume(String text)
    {
        return text.equals('+')    
    }
    private static boolean isDefaultVolume(String text)
    {
        return text.equals('-')    
    }
    private static boolean isRandomNote(String text)
    {
        return text.equals('?')    
    }
    private static boolean isRandomBpm(String text)
    {
        return text.equals(';')    
    }

    public static List<HashMap<String, Integer>> getActions(String text)
    {

        List<HashMap<String, Integer>> actions = new ArrayList<>();

        Pattern AtoG = Pattern.compile("[A-Ga-g]"); 

        int i = 0
        while(i < text.length()){
            String character = Character.toString(text.charAt(i));
            String beforeLetter = '';


            if(isHigherBPM(text, i)){
                actions = setAction(actions, "bpm", 80);
                i = i + 3;


            }else if(isNote(character)){
                actions = setAction(actions, "frequency", getNote(character));


            }else if(isNoteOrPhone(character)){
                if(isNote(beforeLetter)){
                    actions = setAction(actions, "frequency", getNote(beforeLetter));

                }else{
                    actions = setAction(actions, "frequency", 125);

                }


            }else if(isOctave()){
                actions = setAction(actions, "octave", getOctave());
                i++;


            }else if(isInstrument(character)){
                actions = setAction(actions, "instrument", instruments.get(character));


            }else if(isDoubleVolume(character)){
                actions = setAction(actions, "volume", 1);
            
            
            }else if(isDefaultVolume(character)){
                actions = setAction(actions, "volume", 0);
            
            
            }else if(isRandomNote(character)){
                actions = setAction(actions, "frequency", getRandomNote());
            
            
            }else if(isRandomBpm(character)){
                actions = setAction(actions, "bpm", getRandomBpm());


            }else{
                actions = actions.add(actions.get(-1));
            }    

            beforeLetter = character;
            i++;
        }

        return actions;
    }
}