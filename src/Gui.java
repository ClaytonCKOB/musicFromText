package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;

public class Gui extends JFrame{
    private BorderLayout layout;
    
    private void createWindow() {
        this.setTitle("Music from text");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new BorderLayout(5,5);	//ESPAÇOS DE 5 PIXELS
		this.setLayout(layout);

        JButton button = new JButton("Open file");
        button.setPreferredSize(new Dimension(40, 40));
        button.addActionListener(e -> {
            selectFile();
        });

        this.add(button, BorderLayout.CENTER);

        this.setLocationRelativeTo(null);
        this.setSize(400, 600);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Gui window = new Gui();
        window.createWindow();
    } 

    public void selectFile() {
        JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            
            // read  and/or display the file somehow. ....
        } else {
            // user changed their mind
        }
    }
}