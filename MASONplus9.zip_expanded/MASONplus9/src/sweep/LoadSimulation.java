package sweep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class LoadSimulation {
	public final static int FIXED = 4;
	public final static int MAX_SWEEPS = 3;
	public final static int MIN_SWEEPS = 1;

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

	public static ArrayList<ArrayList<Object>> tokenize(Object o,String fileName) throws IOException{
		FileReader fileReader = null;
		StreamTokenizer st = null;
		ArrayList<ArrayList<Object>> spreadsheet = null;
		ArrayList<Object> line = null;
		try {
			fileReader = new FileReader(fileName);
			st = tokenizeFileStream(fileReader);
			spreadsheet = new ArrayList<ArrayList<Object>>(100);
			line = new ArrayList<Object>(2);
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
					line = new ArrayList<Object>(2); //make a new line for the next line
					break;
				case StreamTokenizer.TT_WORD:
					line.add(st.sval);
					break;
				case StreamTokenizer.TT_NUMBER:
					line.add(st.nval);;
					break;
				default:
					if (token == '!') {
						eof = true;
					}
				}
			} while (!eof);
			fileReader.close();
			 
		}catch (FileNotFoundException fnf) {
			System.out.println("Script file not found, will attempt to create scriptfile.");
			boolean fileCreated = false;
			try {
				fileCreated = CreateScript.createScriptFile(fileName, o);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if(fileCreated)
				System.out.println("Script file created.");
			else
				System.out.println("Script file not created.");
			return null;
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
		return spreadsheet;
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
				st.eolIsSignificant(true); // can detect end of lines
				st.slashSlashComments(true); // makes "//" line ignored
				st.slashStarComments(true);  // does /* ... */ comments ignored
				st.quoteChar('"');
			}
		}
		return st;
	}

	public static boolean typeTest(String t){
		if(t.equals(int.class.toString()) || t.equals(long.class.toString()) || t.equals(double.class.toString()) 
				|| t.equals(float.class.toString())|| t.equals(short.class.toString()) 
				|| t.equals("String") || t.equals(boolean.class.toString()))
			return true; //String.class.toString() returns "class java.lang.String"
		else
			return false;
	}

	public static ArrayList<ArrayList<Object>> clean(ArrayList<ArrayList<Object>> spreadsheet){
		ArrayList<ArrayList<Object>> sps = new ArrayList<ArrayList<Object>>(spreadsheet.size());
		for(int i=0;i<spreadsheet.size();i++){
			ArrayList<Object> line = spreadsheet.get(i);
			if(line.size()>0){
				String token = line.get(0).toString();
				if(token.equals("public") && line.size() > 1 && typeTest(line.get(1).toString())){
					ArrayList<Object> nl = new ArrayList<Object>(line.size());
					nl.add(token);
					for(int j=1; j< line.size();j++){
						nl.add(line.get(j));
					}
					sps.add(nl);
				}//if
			}//for
		}//if

		return sps;
	}


	public static ArrayList<ArrayList<Object>> convertValues(ArrayList<ArrayList<Object>> spreadsheet){
		ArrayList<ArrayList<Object>> nss = new ArrayList<ArrayList<Object>>(spreadsheet.size());
		for(int i=0;i<spreadsheet.size();i++){
			ArrayList<Object> line = spreadsheet.get(i);
			String t = (String) line.get(1);
			ArrayList<Object> l = new ArrayList<Object>(line.size());
			l.add(line.get(0));
			l.add(line.get(1));
			l.add(line.get(2));

			if(t.equals(int.class.toString())){
				for(int j=3;j<line.size();j++){
					double n = (Double) line.get(j);
					int v = (int)n;
					l.add(v);
				}
			}
			else if(t.equals(short.class.toString())){
				for(int j=3;j<line.size();j++){
					double n = (Double) line.get(j);
					short v = (short)n;
					l.add(v);
				}
			}
			else if(t.equals(long.class.toString())){
				for(int j=3;j<line.size();j++){
					double n = (Double)line.get(j);
					long v = (long)n;
					l.add(v);
				}
			}
			else if(t.equals(double.class.toString())){
				for(int j=3;j<line.size();j++){
					double n = (Double) line.get(j);
					l.add(n);
				}
			}
			else if(t.equals(boolean.class.toString())){
				for(int j=3;j<line.size();j++){
					String s = (String) line.get(j);
					boolean b ;
					if(s.equals("true"))
						b = true;
					else
						b = false;
					l.add(b);
				}
			}
			else if(t.equals("String")){ //String.class.toString() returns "class java.lang.String"
				for(int j=3;j<line.size();j++){
					String s = (String) line.get(j);
					l.add(s);
				}
			}
			else if(t.equals(float.class.toString())){
				for(int j=3;j<line.size();j++){
					double n = (Double) line.get(j);
					float v = (float)n;
					l.add(v);
				}
			}

			nss.add(l);
		}
		return nss;
	}
	
	public static ArrayList<ArrayList<Object>> fixedTable(ArrayList<ArrayList<Object>> spreadsheet){
		ArrayList<ArrayList<Object>> temp = new ArrayList<ArrayList<Object>>(spreadsheet.size());
		
		for(int i =0; i< spreadsheet.size(); i++){
			ArrayList<Object> line = spreadsheet.get(i);
			if(line.size() == FIXED){
				ArrayList<Object> l = new ArrayList<Object>(line.size());
				for(int j=0;j<line.size();j++){
					l.add(line.get(j));
				}
				temp.add(l);
			}
		}
		return temp;
	}

	public static ArrayList<ArrayList<Object>> sweepTable(ArrayList<ArrayList<Object>> spreadsheet){
		ArrayList<ArrayList<Object>> temp = new ArrayList<ArrayList<Object>>(MAX_SWEEPS);
		ArrayList<ArrayList<Object>> table = null;
		for(int i =0; i< spreadsheet.size(); i++){
			ArrayList<Object> line = spreadsheet.get(i);
			if(line.size() > FIXED){
				ArrayList<Object> l = new ArrayList<Object>(line.size());
				for(int j=0;j<line.size();j++){
					l.add(line.get(j));
				}
				temp.add(l);
			}
		}
		if(temp.size() > MAX_SWEEPS){ //take the first three
			ArrayList<ArrayList<Object>> t = temp;
			temp = new ArrayList<ArrayList<Object>>(MAX_SWEEPS);
			for(int i=0;i<MAX_SWEEPS;i++)
				temp.add(t.get(i));
		}
		switch(temp.size()){
		case 1: table = temp;
			break;
		case 2:
			ArrayList<Object> line1 = temp.get(0);
			ArrayList<Object> line2 = temp.get(1);
			ArrayList<Object> nline1 = new ArrayList<Object>((line1.size()-3)*(line2.size()-3));
			ArrayList<Object> nline2 = new ArrayList<Object>((line1.size()-3)*(line2.size()-3));
			table = new ArrayList<ArrayList<Object>>(2);
			for(int i=0;i<FIXED-1;i++){
				nline1.add(line1.get(i));
				nline2.add(line2.get(i));
			}
			for(int i=FIXED-1;i<line1.size();i++){
				for(int j=FIXED-1;j<line2.size();j++){
					nline1.add(line1.get(i));
					nline2.add(line2.get(j));
				}
			}
			table.add(nline1);
			table.add(nline2);
			break;
		case 3:
			line1 = temp.get(0);
			line2 = temp.get(1);
			ArrayList<Object> line3 = temp.get(2);
			nline1 = new ArrayList<Object>((line1.size()-3)*(line2.size()-3)*(line3.size()-3));
			nline2 = new ArrayList<Object>((line1.size()-3)*(line2.size()-3)*(line3.size()-3));
			ArrayList<Object>nline3 = new ArrayList<Object>((line1.size()-3)*(line2.size()-3)*(line3.size()-3));
			table = new ArrayList<ArrayList<Object>>(3);
			for(int i=0;i<FIXED-1;i++){
				nline1.add(line1.get(i));
				nline2.add(line2.get(i));
				nline3.add(line3.get(i));
			}
			for(int i=FIXED-1;i<line1.size();i++){
				for(int j=FIXED-1;j<line2.size();j++){
					for(int k=FIXED-1;k<line3.size();k++){
						nline1.add(line1.get(i));
						nline2.add(line2.get(j));
						nline3.add(line3.get(k));
					}
				}
			}
			table.add(nline1);
			table.add(nline2);
			table.add(nline3);
			break;
		default: //empty table
			System.out.println("Sweep Error");
			return null;
		}
		return table;
	}
	


	public static void printSpreadSheet(ArrayList<ArrayList<Object>> spreadsheet){
		if(spreadsheet != null){
			for(int i=0; i<spreadsheet.size();i++){
				ArrayList<Object> line = spreadsheet.get(i);
				for(int j=0;j<line.size();j++){
					System.out.print(line.get(j)+ " ");
				}
				System.out.println();
			}
		}
		else{
			System.out.println("Spreadsheet is null");
		}

	}

	public static void main(String[] args) {

	}

}
