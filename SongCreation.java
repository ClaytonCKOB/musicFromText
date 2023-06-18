import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class SongCreation {
    public static void main(String[] args) {
        // Parameters for the song
        int sampleRate = 44100;
        int numChannels = 1;
        long frameLengths = 0;

        // Create a list to store the audio streams
        List<AudioInputStream> audioStreams = new ArrayList<>();

        // Generate audio stream for the first note (e.g., A4)
        AudioInputStream note1Stream = generateNoteStream(440, 1, sampleRate, numChannels);
        audioStreams.add(note1Stream);

        // Generate audio stream for the second note (e.g., C5)
        AudioInputStream note2Stream = generateNoteStream(523.25, 1, sampleRate, numChannels);
        audioStreams.add(note2Stream);

        for(int i = 0; i < audioStreams.size(); i++){
            frameLengths += audioStreams.get(i).getFrameLength();
        }

        // Combine the audio streams
        Enumeration<AudioInputStream> audioStreamEnumeration = Collections.enumeration(audioStreams);
        AudioInputStream combinedStream = new AudioInputStream(
                new SequenceInputStream(audioStreamEnumeration), audioStreams.get(0).getFormat(),
                frameLengths);

        try {
            // Write the combined audio stream to a file
            AudioSystem.write(combinedStream, AudioFileFormat.Type.WAVE, new File("song.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AudioInputStream generateNoteStream(double frequency, double duration, int sampleRate,
                                                      int numChannels) {
        // Create an array to store the audio samples
        byte[] audioData = new byte[(int) (sampleRate * duration * numChannels * 2)]; // 2 bytes per sample

        // Generate audio samples for the note (using a sine wave)
        for (int i = 0; i < audioData.length / 2; i += 2) {
            double time = i / (double) sampleRate;
            short amplitude = (short) (Math.sin(2 * Math.PI * frequency * time) * Short.MAX_VALUE);
            audioData[i] = (byte) (amplitude & 0xFF);
            audioData[i + 1] = (byte) ((amplitude >> 8) & 0xFF);
        }

        // Create an AudioFormat
        AudioFormat format = new AudioFormat(sampleRate, 16, numChannels, true, false);

        // Create an AudioInputStream from the audio data
        return new AudioInputStream(new ByteArrayInputStream(audioData), format, audioData.length / format.getFrameSize());
    }

    public void textMapping(String text){
        char beforeLetter = '';
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("A", 444);
        map.put("B", 498,4);
        map.put("C", 528);
        map.put("D", 296,2); 
        map.put("E", 332,6); 
        map.put("F", 352,4); 
        map.put("G", 395,5); 
        
        for(int i = 0; i < text.length; i++){
            text.charAt(i);
        }
    }
}
