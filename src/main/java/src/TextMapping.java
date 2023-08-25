package src;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class TextMapping {

    private static List<HashMap<String, Integer>> setAction(List<HashMap<String, Integer>> actions, String type, Integer value)
    {
        actions.add(new HashMap<String, Integer>() {{
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

        return notes.get(text.toUpperCase());
    }

    private static Integer getOctave(String text, Integer indice)
    {
        Character next_char = text.charAt(indice + 1);

        if(next_char == '+'){
            return 1;
        }else if(next_char == '-'){
            return 0;
        }

        return null;
    }

    private static Integer getRandomInstrument()
    {
        return new Random().nextInt(127) + 1;
    }

    private static Integer getRandomBpm()
    {
        return new Random().nextInt(200 - 40 + 1) + 40;
    }

    private static Integer getRandomNote()
    {
        int[] notes = {69, 71, 60, 62, 64, 66, 68};

        return notes[new Random().nextInt(notes.length)];
    }

    private static boolean isOctave(String text, Integer indice)
    {
        return text.charAt(indice) == 'R' && indice++ < text.length();
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
        return text.equals("\n");
    }

    private static boolean isDoubleVolume(String text)
    {
        return text.equals("+");
    }

    private static boolean isDefaultVolume(String text)
    {
        return text.equals("-");
    }

    private static boolean isRandomNote(String text)
    {
        return text.equals("?");
    }

    private static boolean isRandomBpm(String text)
    {
        return text.equals(";");
    }

    private static boolean isHigherBpm(String text, Integer indice)
    {
        return (
                indice + 3 < text.length() &&
                        text.substring(indice, indice + 3 + 1) == "BPM+"
        );
    }

    private static boolean isSilent(String text)
    {
        return text.equals(" ");
    }

    public static List<HashMap<String, Integer>> getActions(String text)
    {

        List<HashMap<String, Integer>> actions = new ArrayList<>();

        int i = 0;
        while(i < text.length()){
            String character = Character.toString(text.charAt(i));
            String beforeLetter = "";


            if(isHigherBpm(text, i)){
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


            }else if(isOctave(text, i)){
                actions = setAction(actions, "octave", getOctave(text, i));
                i++;


            }else if(isInstrument(character)){
                actions = setAction(actions, "instrument", getRandomInstrument());


            }else if(isDoubleVolume(character)){
                actions = setAction(actions, "volume", 1);


            }else if(isDefaultVolume(character)){
                actions = setAction(actions, "volume", 0);


            }else if(isRandomNote(character)){
                actions = setAction(actions, "frequency", getRandomNote());


            }else if(isRandomBpm(character)){
                actions = setAction(actions, "bpm", getRandomBpm());


            }else if(isSilent(character)){
                actions = setAction(actions, "frequency", 0);

            }else{
                actions.add(new HashMap<>(actions.get(actions.size() - 1)));
            }

            beforeLetter = character;
            i++;
        }

        return actions;
    }

    public static void main(String[] args) {
        List<HashMap<String, Integer>> actions = getActions("R+R-");

        for (HashMap<String, Integer> action : actions) {
            System.out.println("Action:");
            for (String key : action.keySet()) {
                System.out.println(key + ": " + action.get(key));
            }
            System.out.println();
        }
    }
}