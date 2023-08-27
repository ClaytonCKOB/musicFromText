package util;

import java.util.*;
import classes.Action;


public class TextMapping {

    private static final Random random = new Random();
    private static final int NOTE_C = 0;
    private static final int NOTE_D = 2;
    private static final int NOTE_E = 4;
    private static final int NOTE_F = 5;
    private static final int NOTE_G = 7;
    private static final int NOTE_A = 9;
    private static final int NOTE_B = 11;
    private static final int noteAmount = 7;

    private static final List<Character> vowels = new ArrayList<>(Arrays.asList('A', 'E', 'I', 'O', 'U'));

    private static final Map<Character, Integer> notes = new HashMap<>(){{
        put('A', NOTE_A);
        put('B', NOTE_B);
        put('C', NOTE_C);
        put('D', NOTE_D);
        put('E', NOTE_E);
        put('F', NOTE_F);
        put('G', NOTE_G);
    }};
    public static final int MAX_RANDOM_BPM = 200;
    public static final int MAX_CHANNELS = 15;

    private static int getNote(char character)
    {
        return notes.get(Character.toUpperCase(character));
    }

    private static boolean shouldIncreaseOctave(char character){
       return character == '+';
    }

    private static boolean shouldDecreaseOctave(char character){
        return character == '-' ;
    }

    private static Integer getRandomInstrument()
    {
        return random.nextInt(MAX_CHANNELS) ;
    }

    private static Integer getRandomBpm()
    {
        return random.nextInt(MAX_RANDOM_BPM) ;
    }

    private static Integer getRandomNote()
    {
        int randomIndex = random.nextInt(noteAmount);

        char randomLetter = (char) ('A' + randomIndex);

        return notes.get(randomLetter);
    }

    private static boolean isOctave(String text, Integer indice)
    {
        return text.charAt(indice) == 'R' && ++indice < text.length();
    }

    private static boolean isNote(char character)
    {
        return character >= 'A' && character <= 'G';
    }

    private static boolean isVowel(char character)
    {
        return vowels.contains(character);
    }

    private static boolean isInstrument(char character)
    {
        return character == '\n';
    }

    private static boolean isDoubleVolume(char character)
    {
        return character == '+';
    }

    private static boolean isDefaultVolume(char character)
    {
        return character == '-';
    }

    private static boolean isRandomNote(char character)
    {
        return character == '?';
    }

    private static boolean isRandomBpm(char character)
    {
        return character == ';';
    }

    private static boolean isHigherBpm(String text, Integer indice)
    {
        return (
                indice + 3 < text.length() &&
                        text.substring(indice, indice + 3 + 1).equals("BPM+")
        );
    }

    private static boolean isRest(char character)
    {
        return character == ' ';
    }

    private static void repeatLastAction(List<Action> actionList){
        if (actionList.size() > 0)
            actionList.add(actionList.get(actionList.size() - 1));
    }

    public static List<Action> getActions(String text) {

        List<Action> actions = new ArrayList<>();

        int currentIndex = 0;
        char previousCharacter = '\0';
        while (currentIndex < text.length()) {
            char currentChar = Character.toUpperCase(text.charAt(currentIndex));

            if (isHigherBpm(text, currentIndex)) {
                actions.add(new Action("increaseBPM", 80));
                currentIndex = currentIndex + 3;

            } else if (isNote(currentChar)) {
                actions.add(new Action("playNote", getNote(currentChar)));

            } else if (isVowel(currentChar)) {
                if (isNote(previousCharacter)) {
                    actions.add(new Action("playNote", getNote(previousCharacter)));

                } else if (actions.size() > 0) {
                    actions.add(new Action("playTelephone"));

                }


            } else if (isOctave(text, currentIndex)) {

                if(shouldIncreaseOctave(text.charAt(currentIndex + 1))){
                    actions.add(new Action("increaseOctave"));
                    currentIndex++;
                }else if(shouldDecreaseOctave(text.charAt(currentIndex + 1))){
                    actions.add(new Action("decreaseOctave"));
                    currentIndex++;
                }else {
                    repeatLastAction(actions);
                }

            } else if (isInstrument(currentChar)) {
                actions.add(new Action("changeInstrument", getRandomInstrument()));


            } else if (isDoubleVolume(currentChar)) {
                actions.add(new Action("doubleVolume"));


            } else if (isDefaultVolume(currentChar)) {
                actions.add(new Action("resetVolume"));


            } else if (isRandomNote(currentChar)) {
                actions.add(new Action("playNote", getRandomNote()));


            } else if (isRandomBpm(currentChar)) {
                actions.add(new Action("setBPM", getRandomBpm()));


            } else if (isRest(currentChar)) {
                actions.add(new Action("rest"));

            } else {
                repeatLastAction(actions);

            }

            previousCharacter = currentChar;
            currentIndex++;
        }

        return actions;
    }
}