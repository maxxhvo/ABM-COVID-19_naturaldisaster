package sweep; 
import java.io.*;


public final class Write implements Serializable {
	public  FileWriter file = null;
	public  String fileName = "";
	public  String folderName ="data";

	public void setFileandFolderNames(String fileName,String folderName){
		this.fileName = fileName;
		this.folderName = folderName;
		File mfile = null;
		try{
			mfile =  new File("");
			String s = new String(mfile.getAbsolutePath()+File.separator +this.folderName);
			this.folderName = s;
			boolean mdir = new File(s).mkdir();
			this.fileName = this.folderName + File.separator + this.fileName;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}  
	}

	public Write (String fileName){
		this.fileName = fileName;
		File mfile = null;
		try{
			mfile =  new File("");
			String s = new String(mfile.getAbsolutePath()+File.separator +this.folderName);
			this.folderName = s;
			boolean mdir = new File(s).mkdir();
			this.fileName = folderName + File.separator + this.fileName;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}        
	}

	public Write (String folderName, String fileName){
		this.fileName = fileName;
		this.folderName = folderName;
		File mfile = null;
		try{
			mfile =  new File("");
			String s = new String(mfile.getAbsolutePath()+File.separator +this.folderName);
			this.folderName = s;
			boolean mdir = new File(s).mkdir();
			this.fileName = this.folderName + File.separator + this.fileName;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}  
	}
	
	public Write (String fileName, boolean folder){
		this.fileName = fileName;
		File mfile = null;
		if(folder){
			try{
				mfile =  new File("");
				String s = new String(mfile.getAbsolutePath()+File.separator +this.folderName);
				this.folderName = s;
				boolean mdir = new File(s).mkdir();
				this.fileName = folderName + File.separator + this.fileName;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}     
		}
		else {//no folder
			this.fileName = fileName;
		}
	}


	public  void makePaths (String file){
		fileName = file;
		File mfile = null;
		try{
			mfile =  new File("");
			folderName = new String(mfile.getAbsolutePath() + File.separator + folderName);
			fileName = folderName + File.separator + fileName;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}        
	}

	public  void makePaths (String folder, String file){
		fileName = file;
		folderName = folder;
		File mfile = null;
		try{
			mfile =  new File("");
			folderName = new String(mfile.getAbsolutePath()+File.separator + folderName);
			fileName = folderName + File.separator + fileName;
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}  
	}

	public boolean openFile (String f) {
		boolean outcome = true;
		fileName = f;
		try {
			file = new FileWriter(fileName, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outcome;  
	}

	public void writeInt (int x) {        
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				Integer i = new Integer(x);
				file.write(i.toString());                 
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}

	public void writeIntln (int x) {        
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				Integer i = new Integer(x);
				file.write(i.toString()+"\n");                     
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}

	public void writeLong (long x) {        
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				Long i = new Long(x);
				file.write(i.toString());                 
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");
	}


	public void writeLongln (Long x) {        
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				Long i = new Long(x);
				file.write(i.toString()+"\n");                     
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}

	public void writeLineOfDouble(int presion, double[] nums) {
		String s ="";
		String formatString = new String("%1." + presion + "f");
		for(int i=0; i < nums.length;i++)
			if(i<nums.length-1)
				s = s + String.format(formatString,nums[i]) + "\t";
			else
				s = s + String.format(formatString,nums[i]) + "\n";
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				file.write(s);                          
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");   

	}
	
	public void writeLineOfDouble(String precision, double[] nums) {
		String s ="";
		String formatString = precision;
		for(int i=0; i < nums.length;i++)
			if(i<nums.length-1)
				s = s + String.format(formatString,nums[i]) + "\t";
			else
				s = s + String.format(formatString,nums[i]) + "\n";
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				file.write(s);                          
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");   

	}
	
	public void writeLineOfDouble(String precision, double[] nums, boolean newLine) {
		String s ="";
		String formatString = precision;
		for(int i=0; i < nums.length;i++)
			if(i<nums.length-1)
				s = s + String.format(formatString,nums[i]) + "\t";
			else{
				if(newLine)
					s = s + String.format(formatString,nums[i]) + "\n";
				else
					s = s + String.format(formatString,nums[i]) + "\t";
			}
		if(fileName.length() != 0){
			try{
				openFile (fileName);
				file.write(s);                          
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");   

	}


	public void writeDouble (int presion, double x) {        
		if(fileName.length() != 0){
			String formatString = new String("%1." + presion + "f");
			try{
				openFile (fileName);
				file.write(String.format(formatString,x));                          
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}


	public void writeDoubleln (int precison, double x) {        
		if(fileName.length() != 0){
			String formatString = new String("%1." + precison + "f");
			try{    
				openFile (fileName);
				file.write(String.format(formatString,x)+"\n");                
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");
	}


	public void writeString (String x) {        
		if(fileName.length() != 0){
			openFile (fileName);
			try{
				file.write(x);                        
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");
	}


	public void writeStringln (String x) {        
		if(fileName.length() != 0){
			openFile (fileName);
			try{            
				file.write(x +"\n");                           
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}


	public void writeBytes (byte[] b) {        
		if(fileName.length() != 0){
			openFile (fileName);
			try{
				file.write(b.toString());                    
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}

	public void writeBytesln (byte[] b) {        
		if(fileName.length() != 0){
			openFile (fileName);
			try{            
				file.write(b.toString() +"\n");                           
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");                             
	}

	public void tab () {
		if(fileName.length() != 0){
			openFile (fileName);
			try{            
				file.write("\t");                  
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");
	}

	public void newLine () {
		if(fileName.length() != 0){
			try{     
				openFile (fileName);
				file.write("\n");                  
				file.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("File does not exit");
	}
	
	public void writeDoubleMat(double[][] mat, int precision) {
		for(int i=0;i<mat.length;i++){
			double[] line = mat[i];
			writeLineOfDouble(precision, line);
		}
	}
	
	public void writeDoubleMat(double[][] mat, String precision) {
		for(int i=0;i<mat.length;i++){
			double[] line = mat[i];
			writeLineOfDouble(precision, line);
		}
	}
	

	public static void main(String[] args) {
System.out.println(String.class.toString());
	}
}
