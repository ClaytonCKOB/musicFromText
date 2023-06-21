import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMapping {
    public static List<HashMap<String, Integer>> getActions(String text){

        List<HashMap<String, Integer>> actions = new ArrayList<>();

        Map<String, Integer> notes = new HashMap<String, Integer>(){{
            put("A", 69);
            put("B", 71);
            put("C", 60);
            put("D", 62); 
            put("E", 64); 
            put("F", 66); 
            put("G", 68); 
        }};

        Map<String, Integer> instruments = new HashMap<String, Integer>(){{
            put("O", 7);
            put("I", 7);
            put("U", 7);
            put("o", 7); 
            put("i", 7); 
            put("u", 7); 
            
            put("!", 114); 
            put(";", 76); 
            put(",", 20); 
            put("\\n", 15); 
        }};


        Pattern AtoG = Pattern.compile("[A-G]"); 
        Pattern atog = Pattern.compile("[a-g]"); 
        Pattern instrumentsrx = Pattern.compile("[OIUoiu!;,\\n]");
        Pattern consoantes = Pattern.compile("[H-Z&&h-z]");

        for(int i = 0; i < text.length(); i++){
            String character = Character.toString(text.charAt(i));

            // Notas definidas
            if(AtoG.matcher(character).find()){
                actions.add(new HashMap<String, Object>() {{
                    put("frequency", notes.get(character));
                }});

            // Sem definicao de nota e caracter anterior e uma nota
            }else if(
                (atog.matcher(character).find() && AtoG.matcher(beforeLetter).find()) ||
                (consoantes.matcher(character).find() && AtoG.matcher(beforeLetter).find())
            ){
                actions.add(new HashMap<String, Object>() {{
                    put("frequency", notes.get(beforeLetter));
                }});

            // Sem definicao de nota e anterior nao e uma nota ou qualquer outra consoante
            }else if(
                atog.matcher(character).find() || consoantes.matcher(character).find()
            ){
                actions.add(new HashMap<String, Object>() {{
                    put("frequency", 0);
                }});

            // Alteracao de intrumentos utilizados
            }else if(instrumentsrx.matcher(character).find()){
                actions.add(new HashMap<String, Object>() {{
                    put("instrument", instruments.get(character));
                }});

            // Nenhuma das situacoes anteriores
            }else{
                if(AtoG.matcher(beforeLetter).find()){
                    actions.add(new HashMap<String, Object>() {{
                        put("frequency", notes.get(beforeLetter));
                    }});
                }else{
                    actions.add(new HashMap<String, Object>() {{
                        put("frequency", 0);
                    }});
                }
            }    

            beforeLetter = character;
        }

        return hashMapList;
    }
}