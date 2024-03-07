package sweep;


public class CreateRunTimeFile {
	public final static String[] ROW_NAMES = {"Scriptname","DataFolder","DataFile", "CheckSweepOptions","ColumnHeaders","Precision","stopWhenNoAgents"};
	public final static String[] DEFAULT_NAMES = {"script.txt","data","results.txt","false","N, item1, item2","3","true"};
	public final static String[] COMMENTS = {" // The name of the file for parameter sweeps", 
			" //Folder holding simulation results", " //Name of results file.", " //Determines whether parameter sweeps is automatically checked.",
			" //The column headers for the data file, 'steps' and 'rep' are prepended to this list", "  //Numerical precision for data, must be an integer.",
			"  //The simulation will stop when there are no agents left, else it will run to the stopping time even if there are not agents."};
	
	public static String[] scriptInformation =
		{"/* This runtime file allows the user to specify a simulation script file for simulation.\n",
		"It also allows the user to specify the data output folder and file name. The column headers\n",
		"for the data can also be specified. The form of the script is specified below:*/\n\n\n",
		"\n"};

	public static boolean createRunTimeFile(String fileName) {

		Write write = new Write(fileName, false);
		
		for(int i=0;i<scriptInformation.length;i++)
			write.writeString(scriptInformation[i]);
		for(int i=0;i<ROW_NAMES.length;i++)
			write.writeStringln(ROW_NAMES[i]+": "+DEFAULT_NAMES[i] + COMMENTS[i]);
			
		return true;
	}

	public static void main(String[] args) {

	}
}
