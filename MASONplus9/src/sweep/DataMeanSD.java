package sweep;

import java.util.ArrayList;

/**
 * This class provides a basic tool for collecting data from an agent-based model.  It assumes
 * that an ABM modeler is interested in collecting data on mean properties of agents over
 * step-intervals and calculating corresponding standard deviations.  The first two columns
 * correspond to the number of steps pulsed in a simulation followed by the number of repetitions.
 * The user then specifies via the header, how many other columns to add.  For each column, at
 * the end of a set of simulations, the column entries are divided by the number of replications
 * to get the corresponding means. The THIRD column is assumed to correspond to the number
 * of agents at that step in a simulation and all values in subsequent columns are divided
 * by this value at the end of each simulation.  Using the computational variance formula
 * not corrected for sample size, corresponding standard deviations are calculated.
 */

public class DataMeanSD {
	protected SimStateSweep state;
	public ArrayList<String> headers; //The list of column headers
	public ArrayList<double[]> sums;  //The raw sums of means that will converted to means
	public ArrayList<double[]> squares; //the raw sums of squares used in the computational variance formula.
	public double[][] means; //The matrix of means computed after each set and printed out when saved.
	public double[][] sds; //The Corresponding standard deviations.
	public int maxLength = 10000; //max data rows
	public int defaultLength = 100; //starting data length
	public int columns; //Calculated at runtime based on the size of the headers.
	protected int column = 0; //dynamically adjusted as data is saved.
	protected int row = 0;  //dynamically adujusted as data is saved.
	protected int rows = 0; //the current number of rows.
	final int STEPS = 0; //the column holding the steps
	final int REPS = 1; //the column holding the simulation replications
	final int START = 2; //Starting point for user added data
	
	
	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Creates a new row for sums and squares ArrayLists in this class.  It zeros the row.
	 * @param columns
	 * @return
	 */
	public static double[] newRow(int columns){
		double[] row = new double[columns];
		for(int i=0;i<columns;i++)
			row[i]=0;
		return row;
	}
	
	/**
	 * When a set of simulations are completed, this resets the data tool for 
	 * the next parameter sweep.  It assumes the data was saved because it is 
	 * destructive.
	 */
	public void resetSimulationCount(){
		sums = new ArrayList<double[]>(defaultLength);
		squares =  new ArrayList<double[]>(defaultLength);
		row = 0;
		rows = 0;
		column = 0;
	}
	
	/**
	 * After each simulation, adding data is reset to the top of the data matrix for
	 * the next simulation to be added in.
	 */
	public void moveToTop(){
		row = 0;
		column = 0;
	}

	/**
	 * Constructor method.
	 * @param state
	 * @param headers
	 */
	public DataMeanSD(SimStateSweep state, String[] headers) {
		super();
		this.state = state;
		this.headers = new ArrayList<String>(2);
		this.headers.add("Step");
		this.headers.add("Reps");
		for(int i=0;i<headers.length;i++)
			this.headers.add(headers[i]);
		columns = this.headers.size();
		sums = new ArrayList<double[]>(defaultLength);
		squares =  new ArrayList<double[]>(defaultLength);
	}
	
	/**
	 * Gets the headers for a column of data.
	 * @return
	 */
	public ArrayList<String> getHeaders() {
		return headers;
	}
	
	/**
	 * Takes a string array of column headers and adds them to the ArrayList of headers.
	 * It then creates the initial ArrayLists of sums and squares for a simulation.
	 * @param headers
	 */
	public void setHeaders(String[] headers){
		for(int i=0;i<headers.length;i++)
			this.headers.add(headers[i]);
		columns = this.headers.size();
		sums = new ArrayList<double[]>(defaultLength);
		squares =  new ArrayList<double[]>(defaultLength);
	}
	
	public void setHeaders(ArrayList<String> headers){
		this.headers = headers;
		columns = headers.size();
		sums = new ArrayList<double[]>(defaultLength);
		squares =  new ArrayList<double[]>(defaultLength);
	}
	
	public void add(double value){
		
		if(rows == 0){//make first rows/lines
			double[] lineSums = newRow(columns);
			double[] lineSquares = newRow(columns);
			sums.add(lineSums);
			squares.add(lineSquares);
			rows = sums.size();
		}
		if(column >= columns){
			column = STEPS; //reset to STEPS which is 0
			row++;
		}
		
		if(row < rows){
			double[] lineSums = sums.get(row);
			double[] lineSquares = squares.get(row);	
			if(column == STEPS){
				double steps = (double)state.schedule.getSteps();
				lineSums[column] =steps; //add to it
				lineSquares[column] =steps; //add square to it
				column++;
				lineSums[column] ++; //add to it
				lineSquares[column] ++; //add square to it
				column++;
			}
			lineSums[column] += value; //add to it
			lineSquares[column] += value*value; //add square to it
			column++;
		}
		else if (row < maxLength){//row == rows, so if less than maxLength
			double[] lineSums = newRow(columns);
			double[] lineSquares = newRow(columns);
			if(column == STEPS){
				double steps = (double)state.schedule.getSteps();
				lineSums[column] =steps; //add to it
				lineSquares[column] =steps; //add square to it
				column++;
				lineSums[column] ++; //add to it
				lineSquares[column] ++; //add square to it
				column++;
			}
			lineSums[column] += value; //add to it
			lineSquares[column] += value*value; //add square to it
			sums.add(lineSums);
			squares.add(lineSquares);
			rows = sums.size();
			column++;
		}
		else{
			System.out.println("Max data limit reached, no more data collected.");
		}
	}
	
	public double[][] getMeans(){
		means = new double[sums.size()][columns];
		for(int i=0;i<sums.size();i++){
			double[] line = sums.get(i);
			double reps = line[REPS];
			means[i][REPS]=reps;
			means[i][STEPS]=line[STEPS];
			for(int j=START;j<columns;j++){
				means[i][j] = line[j]/reps;
			}
		}
		return means;
	}
	
	/**
	 * Calculates the population standard deviation using the computational variance
	 * formula.
	 * @param sum
	 * @param square
	 * @param n
	 * @return
	 */
	public double sd(double sum, double square, double n){
		final double mean = sum/n;
		double var=0;
		if(n-1 > 0){
			var = (square/n - mean*mean)/(n-1);//sample correction
		}
		return Math.sqrt(var);
	}
	
	
	public double[][] getSds(){
		sds = new double[squares.size()][columns];
		for(int i=0;i<squares.size();i++){
			double[] lineSquares = squares.get(i);
			double[] lineSums = sums.get(i);
			double reps = lineSums[REPS];
			sds[i][REPS]=reps;
			sds[i][STEPS]=lineSums[STEPS];
			for(int j=START;j<columns;j++){
				sds[i][j] = sd(lineSums[j], lineSquares[j], reps);
			}
		}
		return sds;
	}
		
	public static void main(String[] args) {
		double[] test = newRow(10);
		for(double d:test) System.out.println(d);
	}
	
}
