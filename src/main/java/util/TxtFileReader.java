package util;

import java.io.*;
import java.util.Scanner;


public class TxtFileReader{
	public static String fileToString(File f)
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