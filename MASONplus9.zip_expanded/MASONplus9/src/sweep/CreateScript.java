package sweep;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CreateScript {
	
	
	public static String[] scriptInformation =
		{"/* This script allows the user to perform parameter sweep of up to 3 parameters in a simulation\n",
		"session. To sweep a parameter for two or more values, simply list the values after the parameter\n",
		"as illustrated below:\n\n",
		"public double x = 2, 3.1, 4.2, 5;\n\n",
		"As mentioned aboveUp to 3 parameters can be swept in a single session, e.g.:\n\n",
		"public double x = 2.7, 3.1;\n",
		"public int y = 1, 2, 3;\n",
		"public boolean z = true, false;\n\n",
		"An x X y X z Cartisian crossproduct table is generated for conducting the parameter sweep:\n\n",
		"public double x = 2.7,  2.7,  2.7,  2.7,  2.7,  2,7,  3.1,  3.1,  3.1,  3.1,  3.1,  3.1;\n",
		"public int y =    1,    1,    2,    2,    3,    3,    1,    1,    2,    2,    3,    3;\n",
		"public boolean z =true, false,true, false,true, false,true, false,true, false,true, false;\n\n",
		"The table is generated from the first three parameters encountered with more than one value.\n",
		"After 3 parameters are encountered with more than one value, subsequent parameters with more than\n",
		"one value are ignored.*/\n\n\n"};

	public static boolean createScriptFile(String fileName, Object o) throws IllegalArgumentException, IllegalAccessException, IOException{
		Field[] f = null;
		Field fo = null;
		Write write = new Write(fileName, false);
		
		for(int i=0;i<scriptInformation.length;i++)
			write.writeString(scriptInformation[i]);
			
		try {
			Class c = o.getClass();
			f = c.getFields();
		}	catch (Throwable e) {
			e.printStackTrace();
		}

		for(int i=0;i<f.length;i++)
			try{                        //there can be errors that we must catch
				String b = f[i].toString();
				int j = b.lastIndexOf(".");
				b = b.substring(j+1);
				fo = f[i];
				int m = fo.getModifiers();
				String modifier = Modifier.toString(m);
				Class cp = fo.getType();
				if(modifier.equals("public") && cp.equals(int.class) || cp.equals(double.class)|| cp.equals(float.class)
						|| cp.equals(boolean.class) || cp.equals(short.class) || cp.equals(String.class)
						|| cp.equals(long.class) || cp.equals(char.class) || cp.equals(byte.class)){
					if(cp.equals(String.class)){
						b ="public "+ "String"+" "+ b + " = " + "\""+f[i].get(o).toString()+"\""+";";
					}
					else{
						b = "public "+ cp.toString()+" "+ b + " = " + f[i].get(o).toString()+";";
					}
					if(b != null)
						write.writeStringln(b);
				}
			}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		System.out.println("A script file  "+ "\"" +fileName+ "\"" + " did not exist in the project folder so it was created.");
		System.out.println("Please edit the file "+ "\"" +fileName+ "\"" + " for parameter sweeps as described in "+ "\"" +fileName+ "\""+"\n\n");
		return true;
	}

	public static void main(String[] args) {

	}
}
