import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.HashMap;

public class TextMappingTest {
   
    @Test
    public void testGetActions() {
        String inputText = "BPM+";
        List<HashMap<String, Integer>> result = TextMapping.getActions(inputText);

        for (HashMap<String, Integer> action : result) {
            System.out.println("Action:");
            for (String key : action.keySet()) {
                System.out.println(key + ": " + action.get(key));
            }
            System.out.println();
        }

        assertEquals("bpm", result.get(0).get("key"));
        assertEquals(Integer.valueOf(80), result.get(0).get("value"));
    }
}
