package sweep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class LoadRunTimeFile {
	public final static int FIXEDROWS = 5;//number of rows
	public final static int FIXCOLUMNS = 2;//number of columns
	public static String[] rowNames = CreateRunTimeFile.ROW_NAMES;

	static  RandomAccessFile readTextFile(String fileName) {
		RandomAccessFile fileReader = null;
		try {
			fileReader = new RandomAccessFile(fileName, "rw");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
		return fileReader;
	}

	static void close(FileReader rd){
		try{
			rd.close();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public static ArrayList<ArrayList<String>> readRuntTimeFile(String fileName){
		ArrayList<ArrayList<String>> spreadsheet = null;
		try {
			spreadsheet = tokenize(fileName);
		} catch (IOException e) {
			System.out.println("tokenize error");
			e.printStackTrace();
		}
		return check(spreadsheet);
	}

	public static ArrayList<ArrayList<String>> tokenize(String fileName) throws IOException{
		FileReader fileReader = null;
		StreamTokenizer st = null;
		ArrayList<ArrayList<String>> spreadsheet = null;
		ArrayList<String> line = null;
		/*
		 * For some systems, it is not so easy to edit a text file without ".txt", so I have introduced a hack where
		 * ".txt" can be added to the runtime file.  To handle this hack, I introduced a test for the existence of 
		 * a runfile with a possible ".txt" at the end.  If no file is found, then ".txt" is added to the file name
		 * otherwise if it still doesn't exist, it will be created as before. (2-17-2020).
		 */
		try {//initially test to see if someone added ".txt" to the end of the file for convience of edition
			FileReader testFileReader = new FileReader(fileName);
		}catch (FileNotFoundException fnf) {
			fileName = fileName +".txt";
		}
		try {
			fileReader = new FileReader(fileName);
			st = tokenizeFileStream(fileReader);
			spreadsheet = new ArrayList<ArrayList<String>>(FIXEDROWS);
			line = new ArrayList<String>(FIXCOLUMNS);
			// print the stream tokens
			boolean eof = false;
			do {
				int token = st.nextToken();
				switch (token) {
				case StreamTokenizer.TT_EOF:
					eof = true;
					break;
				case StreamTokenizer.TT_EOL:
					spreadsheet.add(line);
					line = new ArrayList<String>(2); //make a new line for the next line
					break;
				case StreamTokenizer.TT_WORD:
					line.add(st.sval);
					break;
				case StreamTokenizer.TT_NUMBER:
					Integer x = (int)st.nval;
					line.add(x.toString());
					break;
				default:
					if (token == '!') {
						eof = true;
					}
				}
			} while (!eof);
			fileReader.close();
			 
		}catch (FileNotFoundException fnf) {
			System.out.println("runTimeFile.txt not found, will attempt to create a runTimeFile.txt");
			boolean fileCreated = false;
			try {
				fileCreated = CreateRunTimeFile.createRunTimeFile(fileName);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(fileCreated)
				System.out.println("runTimeFile.txt created.");
			else
				System.out.println("runTimeFile.txt not created.");
			return null;
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
		return check(spreadsheet);
	}

	static StreamTokenizer tokenizeFileStream(FileReader rd){
		Reader r = new BufferedReader(rd);
		StreamTokenizer st = null;
		if(r != null){
			st = new StreamTokenizer(r);
			// Prepare the tokenizer for Java-style tokenizing rules
			if(st != null) {
				st.parseNumbers();
				st.wordChars('_', '_');
				//st.ordinaryChar(Character.getNumericValue('%'));
				st.eolIsSignificant(true); // can detect end of lines
				st.slashSlashComments(true); // makes "//" line ignored
				st.slashStarComments(true);  // does /* ... */ comments ignored
				st.quoteChar('"');
				st.quoteChar(Character.getNumericValue('"'));
			
			}
		}
		return st;
	}
	
	public static void print(ArrayList<ArrayList<String>> list){
		for(int i=0;i<list.size();i++){
			ArrayList<String> line = list.get(i);
			for(int j = 0; j< line.size(); j++){
				System.out.print(line.get(j)+ " ");
			}
			System.out.println();
		}
			
	}
	
	public static ArrayList<String> findLine(ArrayList<ArrayList<String>> list, String key){
		if(list.size() == 0){
			System.out.println("List too small "+list.size());
		}
		ArrayList<String> line = null;
		for(int i = 0; i< list.size();i++){
			line = list.get(i);
			if(line.size()>1 && key.equals(line.get(0)))
				break;
		}
		
		return line;
	}
	
	public static ArrayList<ArrayList<String>> check(ArrayList<ArrayList<String>> list){
		if(list == null){
			System.out.println("Null list");
			return null;
		}
		if(list.size() < FIXEDROWS){
			System.out.println("List too small: "+ list.size());
			return null;
		}
		ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>(FIXEDROWS);
		for(int i=0;i<rowNames.length;i++){
			ArrayList<String> l = findLine(list, rowNames[i]);
			if(l == null){
				System.out.println("Could not find "+rowNames[i]+" in the runTimeFile");
				return null;
			}
			else{
				newList.add(l);
			}
		}
		return newList;
	}

	public static void main(String[] args) {

	}

}
