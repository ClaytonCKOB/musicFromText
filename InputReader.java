package application;

import java.io.*;


public class InputReader{
	
	private BufferedReader br;
	
	public InputReader(File file) {
		try{
			br = new BufferedReader(new FileReader(file));
		}catch(FileNotFoundException e){
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
	

}