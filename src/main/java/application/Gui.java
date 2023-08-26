package application;
import util.InputReader;
import util.SongPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Gui extends JFrame{
    private BorderLayout layout;
    private String trebleClefImagePath = "/trebleClef.png";
    private int targetFileHeight = 175;
    private JTextArea userText;

    private int guiWidth = 600;
    private int guiHeight = 600;

    public void createWindow() {
        this.setTitle("Music from text");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new BorderLayout(25,25);	//ESPAÇOS DE 25 PIXELS
        this.setLayout(layout);

        JButton openFileButton = new JButton("Open file");
        openFileButton.setPreferredSize(new Dimension(130, 40));

        JButton startButton = new JButton("Convert to music");
        startButton.setPreferredSize(new Dimension (130,40));

        JButton exportMIDIButton = new JButton("Export to MIDI");
        openFileButton.setPreferredSize(new Dimension(130, 40));


        userText = new JTextArea(20,30);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        openFileButton.addActionListener(e -> {
            selectFile();
        });

        startButton.addActionListener(e->{
            try{
                SongPlayer.play(userText.getText());
            }catch (Exception error){
                error.printStackTrace();
            }
        });

        buttonPanel.add(openFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(exportMIDIButton);


        BufferedImage trebleClefImage = loadImage(trebleClefImagePath);
        float resizeFactor = (float)trebleClefImage.getHeight()/targetFileHeight;
        int resizedImageWidth = Math.round(trebleClefImage.getWidth()/resizeFactor);
        Image resizedImage = trebleClefImage.getScaledInstance(resizedImageWidth,targetFileHeight, Image.SCALE_SMOOTH);

        // Exibir a imagem redimensionada
        ImageIcon imageIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);



        this.add(userText, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
        this.setSize(guiWidth, guiHeight);
        this.setVisible(true);


    }

    public void selectFile() {
    	JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            userText.setText(fileToString(f));
           

            // read  and/or display the file somehow. ....
        } else {
            // user changed their mind
        }
    }

    private BufferedImage loadImage(String imagePath){
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    private String fileToString(File f)
    {	
    	String contentAsString;
    	try {               	         
            Scanner scanner = new Scanner(f);
            StringBuilder fileContent = new StringBuilder();
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContent.append(line).append("\n");
            }
            
            scanner.close();     
            contentAsString = fileContent.toString();
            
            return contentAsString;
        } catch (FileNotFoundException e) 
    	{
            e.printStackTrace();
        }
    	return null;
    }

}