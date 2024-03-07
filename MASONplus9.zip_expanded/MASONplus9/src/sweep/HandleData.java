package sweep;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

public class HandleData {
	protected Write write;  
	protected Write writeSummary;
	protected DataMeanSD data;
	protected SimStateSweep state;
	public ArrayList<double[]> squares;
	protected String fileName;
	protected String summaryFileName;
	protected String folderName;
	protected  String precision= "%.4f";

	
	public String getFileName() {
		return fileName;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getPrecision() {
		return precision;
	}

	public HandleData(SimStateSweep state, String folderName, String fileName,DataMeanSD data,Write write, String precision){
		this.precision = precision;
		this.data = data;
		this.write = write;
		this.fileName = fileName;
		this.summaryFileName = "Summary"+fileName;
		writeSummary = new Write(folderName, this.summaryFileName);
		this.folderName = folderName;
		this.state = state;
	}

	public static void printPramsToConsole(Object o){
		Field f = null;
		Class a =  o.getClass(); // gets the runtime class
		Field[] x = a.getFields(); // gets the declared fields, which we will use 
		// to print out the parameter values
		String b = " ";
		System.out.println("Parameter values for the class "+ a.toString());
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
						|| cp.equals(long.class) || cp.equals(char.class) || cp.equals(byte.class))
					System.out.println(b);
			}


		catch(NullPointerException e){
			//System.out.println("NullPointerException; " + e + " " + f);

		}
		catch(IllegalArgumentException e){
			System.out.println("IllegalArgumentException; " + e);
		}

		catch (IllegalAccessException e){
			System.out.println("IllegalAccessException; " + e);
		}
	}

	public void printPramsToFile(Object o, boolean summary) {
		Write write;
		if(summary){
			write = this.writeSummary;
		}
		else{
			write = this.write;
		}
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
	
	public void printPramsToFile(Write writer, Object o) {
		Write write= writer;
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

	public boolean printSweepHeader(String simulationTitle, boolean summary){
		Write write;
		if(summary){
			write = this.writeSummary;
		}
		else{
			write = this.write;
		}
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
	
	public boolean printSweepHeader(Write writer, String simulationTitle, boolean summary){
		Write write = writer;
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

	public void saveDataMeanSD (DataMeanSD data, int sweepNumber) {
		double[][] means = data.getMeans();
		double[][] sds = data.getSds();
		ArrayList<String> headers = data.getHeaders();
		final int sweepPosition = sweepNumber + 2; //adjust for location
		ArrayList<ArrayList<Object>> sweepTable = state.pramSweeper.sweepParameters;
		//First print sweepTable parameters
		String formatString = precision;
		String s ="\n" +"Sweep Number:\t"+ sweepNumber + "\n";
		for(int i=0;i<sweepTable.size();i++){
			ArrayList<Object> line = sweepTable.get(i);
			s += (String)line.get(2) + "\t" 
					+ line.get(sweepPosition).toString()+"\n";
		}
		write.writeString(s);
		String h="";
		for(int i=0;i<headers.size();i++){
			if(i < headers.size() - 1){
				h += headers.get(i) + "\t";
			}
			else{
				h+= headers.get(i) + "\n";
			}
		}
		write.writeString(h);
		write.writeDoubleMat(means, precision);
		write.writeString("\n"+h);
		write.writeDoubleMat(sds, precision);
		//System.out.println(means.length + "  " +means[0].length);
	}
	
	public void saveRunInfo (Write write, int sweepNumber) {
		final int sweepPosition = sweepNumber + 2; //adjust for location
		ArrayList<ArrayList<Object>> sweepTable = state.pramSweeper.sweepParameters;
		//First print sweepTable parameters
		String formatString = precision;
		String s ="\n" +"Sweep Number:\t"+ sweepNumber + "\n";
		for(int i=0;i<sweepTable.size();i++){
			ArrayList<Object> line = sweepTable.get(i);
			s += (String)line.get(2) + "\t" 
					+ line.get(sweepPosition).toString()+"\n";
		}
		write.writeString(s);
	}

	public void saveSummaryDataMeanSD (DataMeanSD data, int sweepNumber) {
		double[][] means = data.getMeans();
		double[][] sds = data.getSds();
		ArrayList<String> headers = data.getHeaders();
		ArrayList<ArrayList<Object>> sweepTable = state.pramSweeper.sweepParameters;
		if(sweepNumber <= (ParameterSweeper.SWEEPSTART+1)){
			
			String s="";
			for(int i=0;i<sweepTable.size();i++){
				ArrayList<Object> line = sweepTable.get(i);
				s += (String)line.get(2) + "\t" 
						+ "Value" + "\t";
			}
			String h="";
			for(int i=0;i<headers.size();i++){
				h += headers.get(i) + "\t";
			}
			h = s + h + h+"\n";
			writeSummary.writeString(h);
		} //end SWEEPSTART
		String s="";
		for(int i=0;i<sweepTable.size();i++){
			ArrayList<Object> line = sweepTable.get(i);
			s += (String)line.get(2) + "\t" 
					+ line.get(sweepNumber+2).toString()+"\t";
		}
		int last = means.length-1;
		double[] line = means[last];
		writeSummary.writeString(s);
		this.writeSummary.writeLineOfDouble(precision, line,false);
		write.writeString("\t");//add a tab
		line = sds[last];
		this.writeSummary.writeLineOfDouble(precision, line,true);
	}

	public static void main(String[] args) {

	}
}
