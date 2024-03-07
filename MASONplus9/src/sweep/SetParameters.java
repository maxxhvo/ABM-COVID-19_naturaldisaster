package sweep;

import java.lang.reflect.*;
import java.util.ArrayList;

/*
 * This class is used to set the parameters of an object.  As I will build agent-based
 * models, mutable parameters we be loaded in a chromosome with a variable name
 * identifying the parameter to be looked up in the appropriate agent and set at
 * runtime.  Non mutable parameters can be done the same way.  My plan is to have 
 * the parameters in agents be integers that look up values in the chromosomes or
 * parameter arrays attached to them.  This will make substituting chromosomes easy
 * once the chromosome is indexed in each agent.
 */

public final class SetParameters {

	public final static void setDouble(Object o, String prameterName, double value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.setDouble(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}
	
	public final static Object getObject(Object o, String prameterName){
		Object obj = null;
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			obj = f.get(o);
		}	catch (Throwable e) {
			System.err.println("Error in get object: " + e);
		}
		return obj;
	}

	public final static void setShort(Object o, String prameterName, short value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.setShort(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}

	public final static void setInt(Object o, String prameter, int value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameter);
			f.setInt(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}

	public final static void setLong(Object o, String prameterName, long value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.setLong(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}

	public final static void setFloat(Object o, String prameterName, float value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.setFloat(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}

	public final static void setBoolean(Object o, String prameterName, boolean value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.setBoolean(o, value);
		}	catch (Throwable e) {
			System.err.println("SetBoolean: " + e);
		}
	}

	public final static void setString(Object o, String prameterName, String value){
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			f.set(o, value);
		}	catch (Throwable e) {
			System.err.println(e);
		}
	}

	public final static void setFixeParameters(Object o, ArrayList<ArrayList<Object>> fixedParameters){
		for(int i=0;i<fixedParameters.size();i++){
			ArrayList<Object> line = fixedParameters.get(i);
			String type = (String)line.get(1);
			String parameterName = (String)line.get(2);
			Object value = line.get(3);
			if(type.equals(int.class.toString()))
				setInt(o, parameterName, (int)value);
			else if(type.equals(long.class.toString()))
				setLong(o, parameterName, (long)value);
			else if(type.equals(short.class.toString()))
				setShort(o, parameterName, (short)value);
			else if(type.equals(double.class.toString()))
				setDouble(o, parameterName, (double)value);
			else if(type.equals(float.class.toString()))
				setFloat(o, parameterName, (float)value);
			else if(type.equals(boolean.class.toString()))
				setBoolean(o, parameterName, (boolean)value);
			else if(type.equals("String")) //String.class.toString() returns "class java.lang.String"
				setString(o, parameterName, (String)value);
		}
		
	}
	
	public final static void setSweepParameters(Object o, ArrayList<ArrayList<Object>> sweepParameters, int run){
		final int sweepParameterPosition = run +2;
		for(int i=0;i<sweepParameters.size();i++){
			ArrayList<Object> line = sweepParameters.get(i);
			String type = (String)line.get(1);
			String parameterName = (String)line.get(2);
			Object value = line.get(sweepParameterPosition);
			if(type.equals(int.class.toString()))
				setInt(o, parameterName, (int)value);
			else if(type.equals(long.class.toString()))
				setLong(o, parameterName, (long)value);
			else if(type.equals(short.class.toString()))
				setShort(o, parameterName, (short)value);
			else if(type.equals(double.class.toString()))
				setDouble(o, parameterName, (double)value);
			else if(type.equals(float.class.toString()))
				setFloat(o, parameterName, (float)value);
			else if(type.equals(boolean.class.toString()))
				setBoolean(o, parameterName, (boolean)value);
			else if(type.equals("String")) //String.class.toString() returns "class java.lang.String"
				setString(o, parameterName, (String)value);
		}

	}
	
	public static void main (String args[]) {
		Float i = (float)1;
		Object o = getObject(i, "BYTES");
		System.out.println(o.getClass());
	}

}
