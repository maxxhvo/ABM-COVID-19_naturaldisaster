package observer;

import sweep.Write;

public class Bin2D {
	public double[][] bin2D;//the data
	public double[][] bin2DN;// matrix for counting binned data
	public double[] binIntervalsX; //intervals binned on x axis (rows)
	public double[] binIntervalsY; //intervals binned on y axis (columns)
	int xbin, ybin; // size of bin
	double xRange, yRange; // interval binned
	public  Write write = null;
	
	public Bin2D(Write write, int xbin, int ybin, double xRange, double yRange) {
		super();
		this.xbin = xbin;
		this.ybin = ybin;
		this.xRange = xRange;
		this.yRange = yRange;
		this.write= write;
		bin2D = new double[xbin+1][ybin+1];// initialize matrix
		bin2DN = new double[xbin+1][ybin+1];// initialize matrix
		binIntervalsX = new double[xbin+1];
		binIntervalsY = new double[ybin+1];
		for (int i = 0; i <= xbin; i++) {//index at 1
			double[] row = bin2D[i];
			double[] rowN = bin2DN[i];
			binIntervalsX[i] = 0.0;
			for(int j = 1; j<= ybin;j++) {
				row[j] = 0.0;// zero the bin
				rowN[j] = 0.0;// zero the bin
				binIntervalsY[j] = 0.0;
			}
		}
		double xinterval = xRange/(double)xbin;
		double yinterval = yRange/(double)ybin;
		binIntervalsX[1] = xinterval;
		binIntervalsX[xbin] = xRange;
		for (int i = 1; i < xbin; i++)
			binIntervalsX[i+1] = binIntervalsX[i] + xinterval;
		binIntervalsY[1] = yinterval;
		binIntervalsY[ybin] = yRange;
		for (int i = 1; i < ybin; i++)
			binIntervalsY[i+1] = binIntervalsY[i] + yinterval;
		
		double[] row = bin2D[0];
		double[] rowN = bin2DN[0];
		for(int i=1;i<=ybin;i++) {
			row[i]=binIntervalsY[i];
			rowN[i]=binIntervalsY[i];
		}
		for(int i= 1;i<=xbin;i++) {
			bin2D[i][0]= binIntervalsX[i];
			bin2DN[i][0]= binIntervalsX[i];
		}
		
	}

	
	public void clear() {
		this.xbin = 0;
		this.ybin = 0;
		this.xRange = 0;
		this.yRange = 0;
		bin2D = null;// initialize bin
		bin2DN = null;// initialize bin
		binIntervalsX = null;
		binIntervalsY = null;
		
	}
	
	public void initialize(int xbin, int ybin, double xRange, double yRange) {
		this.xbin = xbin;
		this.ybin = ybin;
		this.xRange = xRange;
		this.yRange = yRange;
		bin2D = new double[xbin+1][ybin+1];// initialize matrix
		bin2DN = new double[xbin+1][ybin+1];// initialize matrix
		binIntervalsX = new double[xbin+1];
		binIntervalsY = new double[ybin+1];
		for (int i = 0; i <= xbin; i++) {//index at 1
			double[] row = bin2D[i];
			double[] rowN = bin2DN[i];
			binIntervalsX[i] = 0.0;
			for(int j = 1; j<= ybin;j++) {
				row[j] = 0.0;// zero the bin
				rowN[j] = 0.0;// zero the bin
				binIntervalsY[j] = 0.0;
			}
		}
		double xinterval = xRange/(double)xbin;
		double yinterval = yRange/(double)ybin;
		binIntervalsX[1] = xinterval;
		binIntervalsX[xbin] = xRange;
		for (int i = 1; i < xbin; i++)
			binIntervalsX[i+1] = binIntervalsX[i] + xinterval;
		binIntervalsY[1] = yinterval;
		binIntervalsY[ybin] = yRange;
		for (int i = 1; i < ybin; i++)
			binIntervalsY[i+1] = binIntervalsY[i] + yinterval;
		
		double[] row = bin2D[0];
		double[] rowN = bin2DN[0];
		for(int i=1;i<=ybin;i++) {
			row[i]=binIntervalsY[i];
			rowN[i]=binIntervalsY[i];
		}
		for(int i= 1;i<=xbin;i++) {
			bin2D[i][0]= binIntervalsX[i];
			bin2DN[i][0]= binIntervalsX[i];
		}
		
	}
	

	
	public boolean binDataX(double dataX, double dataY, boolean binOverMax) {
		double[] row = null;
		double[] rowN = null;
		for (int i = 1; i <= xbin; i++) {
			if (dataX <= binIntervalsX[i]) {
				row = bin2D[i];
				rowN = bin2DN[i];
				break;
			}
			if(i == xbin && binOverMax) {
				row = bin2D[i];
				rowN = bin2DN[i];
			}
		}
		if(row == null) {
			System.out.println("Failed to bin: "+"binIntervalX: "+" dataX: "+dataX);
			return false;//failed to bin
		}
		for (int i = 1; i <= ybin; i++) {
			if (dataY <= binIntervalsY[i]) {
				row[i]+=dataX;
				rowN[i]++;
				return true;
			}
			if(i == ybin/*-1*/ && binOverMax) {
				row[i]+=dataX;
				rowN[i]++;
				return true;
			}
		}
		System.out.println("Failed to bin: "+"binIntervalY: "+" dataY: "+dataY);
		return false;//failed to bin
	}
	
	public boolean binDataY(double dataX, double dataY, boolean binOverMax) {
		double[] row = null;
		double[] rowN = null;
		for (int i = 0; i < xbin; i++) {
			if (dataX <= binIntervalsX[i]) {
				row = bin2D[i];
				rowN = bin2DN[i];
				break;
			}
			if(i == xbin/*-1*/ && binOverMax) {
				row = bin2D[i];
				rowN = bin2DN[i];
			}
		}
		if(row == null) {
			System.out.println("Failed to bin: "+"binIntervalX: "+" dataX: "+dataX);
			return false;//failed to bin
		}
		for (int i = 0; i < ybin; i++) {
			if (dataY <= binIntervalsY[i]) {
				row[i]+=dataY;
				rowN[i]++;
				return true;
			}
			if(i == ybin-1 && binOverMax) {
				row[i]+=dataY;
				rowN[i]++;
				return true;
			}
		}
		System.out.println("Failed to bin: "+"binIntervalY: "+" dataY: "+dataY);
		return false;//failed to bin
	}
	
	
	public double[][] getMeansDis() {
		double[][] array = new double[xbin+1][ybin+1];
		double total = 0.0;
		
		for(int i= 1; i<=xbin;i++) {
			for(int j = 1; j<=ybin;j++) {
				if(bin2DN[i][j] > 0)
					array[i][j] =bin2D[i][j]/bin2DN[i][j];
				else
					array[i][j] =0.0;
			}
		}
		double[] row = array[0]; //fill in the intervalse
		for(int i=1;i<=ybin;i++) {
			row[i]=binIntervalsY[i];
		}
		for(int i= 1;i<=xbin;i++) {
			array[i][0]= binIntervalsX[i];
		}
		return array;
	}
	
	public void writeData() {
		write.writeDoubleMat(this.bin2DN,1);
	}
	
	
	public void writeDataMeans() {
		write.writeStringln("");
		write.writeDoubleMat(getMeansDis() , 4);
		write.writeStringln("Matix of N values");
		write.writeDoubleMat(this.bin2DN,1);
	}

	
}
