package util;

import java.io.*;
import java.util.Scanner;


public class InputReader{
	
	private BufferedReader br;
	private String str;
	
	public InputReader(File file) {
		try{
			br = new BufferedReader(new FileReader(file));
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		try {
			str = fileToString(file);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public char getNextChar() {
		int r;
		try {
			r = br.read();
			if(r != -1) {
				return (char)r;
			}else {
				System.out.println("The file has ended.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("Error while reading the file.");
		}
		return '\0';
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
	
	public String getStr()
	{
		return str;
	}

}