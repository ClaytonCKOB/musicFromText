package util;

import classes.Action;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TextMappingTest {

    @Test
    void testBpmGetActions() {
        String inputText = "BPM+";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals("increaseBPM", result.getName());
        assertEquals(80, result.getValue());
    }

    @Test
    void testNotesGetActions() {
        String inputText = "ABCDEFG";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("playNote", result.get(0).getName());
        assertEquals(9, result.get(0).getValue());

        assertEquals("playNote", result.get(1).getName());
        assertEquals(11,  result.get(1).getValue());

        assertEquals("playNote", result.get(2).getName());
        assertEquals(0,  result.get(2).getValue());

        assertEquals("playNote", result.get(3).getName());
        assertEquals(2,  result.get(3).getValue());

        assertEquals("playNote", result.get(0).getName());
        assertEquals(4, result.get(4).getValue());

        assertEquals("playNote", result.get(1).getName());
        assertEquals(5,  result.get(5).getValue());

        assertEquals("playNote", result.get(2).getName());
        assertEquals(7,  result.get(6).getValue());
    }

    @Test
    void testSilentGetActions() {
        String inputText = " ";
        String expected = "rest";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());
    }

    @Test
    void testVolumeGetActions() {
        String inputText = "+-";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("volume", result.get(0).getName());
        assertEquals(1, result.get(0).getValue());

        assertEquals("volume", result.get(1).getName());
        assertEquals(0,  result.get(1).getValue());
    }

    @Test
    void testVogalsGetActions() {
        String inputText = "AO+u";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("playNote", result.get(0).getName());
        assertEquals(9, result.get(0).getValue());

        assertEquals("playNote", result.get(1).getName());
        assertEquals(9,  result.get(1).getValue());

        assertEquals("volume", result.get(2).getName());
        assertEquals(1,  result.get(2).getValue());

        assertEquals("telephone", result.get(3).getName());
    }

    @Test
    void testOctaveGetActions() {
        String inputText = "R+R-";

        String firstNameExpected = "octave";
        String secondNameExpected = "octave";
        int firstValueExpected = 1;
        int secondValueExpected = -1;
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals(firstNameExpected, result.get(0).getName());
        assertEquals(firstValueExpected, result.get(0).getValue());

        assertEquals(secondNameExpected, result.get(1).getName());
        assertEquals(secondValueExpected, result.get(1).getValue());
    }

    @Test
    void testRandomNoteGetActions() {
        String inputText = "?";
        String expected = "playNote";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());
    }

    @Test
    void testInstrumentGetActions() {
        String inputText = "\n";
        String expected = "instrument";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());

    }

    @Test
    void testRandomBpmGetActions() {
        String inputText = ";";
        String expected = "setBPM";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());

    }
}