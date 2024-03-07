package sweep;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

public class CustomData {
	protected Write write;  
	protected SimStateSweep state;
	protected String fileName;
	protected String customName;
	protected String folderName;
	protected String precision= "%.5f";

	
	public String getFileName() {
		return fileName;
	}

	public CustomData(SimStateSweep state, String folderName, String fileName,String customName, String precision){
		this.precision = precision;
		this.fileName = fileName;
		this.folderName = folderName;
		this.customName=customName+this.fileName;
		this.state = state;
		this.write = new Write(folderName, customName);//make a new writer
	}

/**
 * Print the parameters of the simulation to the file.  Pass it the environment to print out the parameters.
 * @param o
 * @param summary
 */
	public void printPramsToFile(Object o) {
		Write write;
		write = this.write;
		String b;
		Field f = null;
		Class a =  o.getClass(); // gets the runtime class
		Field[] x = a.getFields(); // gets the declared fields, which we will use          
		// to print out the parameter values
		String header = "\n"+"Parameter values for the class "+ a.toString();
		write.writeStringln(header);
		for(int i=0;i<x.length;i++)
			try{                        //there can be errors that we must catch
				b = x[i].toString();
				int j = b.lastIndexOf(".");
				b = b.substring(j+1);
				b = b + "  " + x[i].get(o).toString();
				f = x[i];
				Class cp = f.getType();
				if(cp.equals(int.class) || cp.equals(double.class)|| cp.equals(float.class)
						|| cp.equals(boolean.class) || cp.equals(short.class) || cp.equals(String.class)
						|| cp.equals(long.class) || cp.equals(char.class) || cp.equals(byte.class)){
					if(b != null)
						write.writeStringln(b);
				}
			}
		catch(NullPointerException e){
			//System.err.println("NullPointerException, printPramsToFile; " + e + " " + f);
		}

		catch(IllegalArgumentException e){
			System.out.println("IllegalArgumentException; " + e);
		}

		catch (IllegalAccessException e){
			System.out.println("IllegalAccessException; " + e);
		}

	}

	public void printDate(String s){
		Date date = new Date();
		String d = date.toString();
		write.writeString(s+": " + d+ "\n\n");
	}

	public boolean printSweepHeader(String simulationTitle){
		Write write;
		write = this.write;
		write.writeString(simulationTitle+ "\n\n");
		ArrayList<ArrayList<Object>> sweepTable = state.pramSweeper.sweepParameters;
		if(sweepTable.size() ==0){
			System.out.println("Empty sweepTable.");
			return false;
		}
		String formatString = new String("%1." + 1 + "f");
		ArrayList<Object> o = sweepTable.get(0);
		int numberofparameters = o.size()-3; //deduct first three slots
		String s ="Number of parameters swept: " + numberofparameters + "\n\n";
		s+="Table of parameters swept \n";
		write.writeString(s);
		for(int i=0;i<sweepTable.size();i++){
			ArrayList<Object> line = sweepTable.get(i);
			s="";
			s += (String)line.get(0) + "\t" + (String)line.get(1) + "\t" +(String)line.get(2) + "\t";
			for(int j=3;j<line.size();j++){
				s += line.get(j).toString()+"\t";
			}
			s = s + "\n";
			write.writeString(s);
		}
		return true;
	}


	public static void main(String[] args) {

	}
}
