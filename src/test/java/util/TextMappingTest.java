package util;

import classes.Action;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class TextMappingTest {

    @Test
    public void testBpmGetActions() {
        String inputText = "BPM+";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("bpm", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(80), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));
    }

    @Test
    public void testNotesGetActions() {
        String inputText = "ABCDEFG";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("frequency", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(69), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(1).keySet()).get(0));
        assertEquals(Integer.valueOf(71), result.get(1).get(new ArrayList<>(result.get(1).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(2).keySet()).get(0));
        assertEquals(Integer.valueOf(60), result.get(2).get(new ArrayList<>(result.get(2).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(3).keySet()).get(0));
        assertEquals(Integer.valueOf(62), result.get(3).get(new ArrayList<>(result.get(3).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(4).keySet()).get(0));
        assertEquals(Integer.valueOf(64), result.get(4).get(new ArrayList<>(result.get(4).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(5).keySet()).get(0));
        assertEquals(Integer.valueOf(66), result.get(5).get(new ArrayList<>(result.get(5).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(6).keySet()).get(0));
        assertEquals(Integer.valueOf(68), result.get(6).get(new ArrayList<>(result.get(6).keySet()).get(0)));

    }

    @Test
    public void testSilentGetActions() {
        String inputText = " ";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("frequency", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(0), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));
    }

    @Test
    public void testVolumeGetActions() {
        String inputText = "+-";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("volume", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(1), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));

        assertEquals("volume", new ArrayList<>(result.get(1).keySet()).get(0));
        assertEquals(Integer.valueOf(0), result.get(1).get(new ArrayList<>(result.get(1).keySet()).get(0)));
    }

    @Test
    public void testVogalsGetActions() {
        String inputText = "AO+u";
        List<HashMap<String, Integer>> result = TextMapping.getActions(inputText);

        assertEquals("frequency", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(69), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(1).keySet()).get(0));
        assertEquals(Integer.valueOf(125), result.get(1).get(new ArrayList<>(result.get(1).keySet()).get(0)));

        assertEquals("volume", new ArrayList<>(result.get(2).keySet()).get(0));
        assertEquals(Integer.valueOf(1), result.get(2).get(new ArrayList<>(result.get(2).keySet()).get(0)));

        assertEquals("frequency", new ArrayList<>(result.get(3).keySet()).get(0));
        assertEquals(Integer.valueOf(125), result.get(3).get(new ArrayList<>(result.get(3).keySet()).get(0)));
    }

    @Test
    public void testOctaveGetActions() {
        String inputText = "R+R-";

        String firstExpected = "playNote";
        String secondExpected = "playNote";
        List<Action> result = TextMapping.getActions(inputText);

        assertEquals("octave", new ArrayList<>(result.get(0).keySet()).get(0));
        assertEquals(Integer.valueOf(1), result.get(0).get(new ArrayList<>(result.get(0).keySet()).get(0)));

        assertEquals("octave", new ArrayList<>(result.get(1).keySet()).get(0));
        assertEquals(Integer.valueOf(0), result.get(1).get(new ArrayList<>(result.get(1).keySet()).get(0)));
    }

    @Test
    public void testRandomNoteGetActions() {
        String inputText = "?";
        String expected = "playNote";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());
    }

    @Test
    public void testInstrumentGetActions() {
        String inputText = "\n";
        String expected = "instrument";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());

    }

    @Test
    public void testRandomBpmGetActions() {
        String inputText = ";";
        String expected = "setBPM";
        Action result = TextMapping.getActions(inputText).get(0);

        assertEquals(expected, result.getName());

    }
}