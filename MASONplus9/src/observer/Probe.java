package observer;

import sweep.SimStateSweep;

public class Probe {
	public SimStateSweep state = null;
	public final static double EPSILON = 0.0001;//Handles possible precision error in binning
	public double[] binIntervals;
	public double[] binData;
	public double [] binN;
	public double min;
	public double max;
	public double interval;
	public double burnIn;
	public double mean = 0;
	public double n = 0;


	public Probe(SimStateSweep state, int burnIn, double min, double max, double interval) {
		super();
		this.state = state;
		this.burnIn = burnIn;
		this.min = min;
		this.max = max;
		this.interval = interval;
		double distance = max - min;
		double value = distance/interval;
		double count = (int)(value);
		int bins;
		if(value - count > 0)
			bins = (int)count+1;
		else
			bins = (int)count;
		bins = (int)count + 1;//This handles for example min = 0, max = 1, interval = 0.1
		binIntervals =new double[bins];
		binData  = new double[bins];
		binN = new double[bins];
		double step = min+EPSILON;
		for(int i=0;i<bins;i++){
			binIntervals[i] = step;
			binData[i] = 0;
			binN[i] = 0;
			step+= interval;
		}
	}
	
	public void initBins(SimStateSweep state, double min, double max, double interval,double[] binIntervals,double[] binData, double[] binN) {
		double distance = max - min;
		double value = distance/interval;
		double count = (int)(value);
		int bins;
		if(value - count > 0)
			bins = (int)count+1;
		else
			bins = (int)count;
		binIntervals =new double[bins];
		binData  = new double[bins];
		binN = new double[bins];
		double step = min + interval;
		for(int i=0;i<bins;i++){
			binIntervals[i] = step;
			binData[i] = 0;
			binN[i] = 0;
			step+= interval;
		}
	}

	public double getBurnIn() {
		return burnIn;
	}

	public void setBurnIn(double burnIn) {
		this.burnIn = burnIn;
	}

	public double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}
	
	public double mean (final double sum, final double n){
		if(n > 0)
			return sum/n;
		else
			return 0;
	}
	
	public double sampleVariance (final double sum, final double sum2, final double n){
		if(n > 2){
			final double ss = sum2 - (sum*sum)/n;
			return ss/(n-1);
		}
		else
			return 0;
	}
	
	public double ss (final double sum, final double sum2, final double n){
		if(n > 2){
			final double ss = sum2 - (sum*sum)/n;
			return ss;
		}
		else
			return 0;
	}
	
	public double variance (final double sum, final double sum2, final double n){
		if(n > 2){
			final double ss = sum2 - (sum*sum)/n;
			return ss/n;
		}
		else
			return 0;
	}
	
	public double sampleSD(final double sum, final double sum2, final double n){
		if(n > 2){
			final double var = sampleVariance(sum,sum2,n);
			if(var > 0)
				return Math.sqrt(var);
			else 
				return 0;
		}
		else
			return 0;
	}
	
	public double correlation(double sXY, double sX, double sY, double sX2, double sY2, double n){
		if(n == 0)
			return 0;
		else {
			final double corr = (sXY - (sX*sY)/n)/Math.sqrt((sX2-(sX*sX)/n)*(sY2-(sY*sY)/n));
			return (sXY - (sX*sY)/n)/Math.sqrt((sX2-(sX*sX)/n)*(sY2-(sY*sY)/n));
		}
	}

	public void binData(double datum){
		if(state.schedule.getSteps() > burnIn){
			for(int i=0; i<binIntervals.length;i++){
				if(datum < binIntervals[i]){
					binData[i]+=datum;
					binN[i]++;
					break;
				}
			}
			mean+= datum;
			n ++;
		}
	}
	
	public void binData(double[] binIntervals,double[] binData, double[] binN,double datum){
		if(state.schedule.getSteps() > burnIn){
			for(int i=0; i<binIntervals.length;i++){
				if(datum < binIntervals[i]){
					binData[i]+=datum;
					binN[i]++;
					break;
				}
			}
		}
	}
	
	public double getBinDatumMean(int i, double[]binData, double[]binN) {
		if(binN[i]==0)
			return 0;
		return binData[i]/binN[i];
	}
	
	public double getBinDatumMeanFrequency(int i, double[]binData, double[]binN) {
		double total = 0;
		for(int j = 0; j<binN.length;j++) {
			total += binN[j];
		}
		if(total==0)
			return 0;
		return binData[i]/total;
	}
	
	public double getBinDatumMeanFrequency(int i, double[]binN) {
		double total = 0;
		for(int j = 0; j<binN.length;j++) {
			total += binN[j];
		}
		if(total==0)
			return 0;
		return binN[i]/total;
	}
	
	public double getBinDatumMeanFrequency(int i) {
		double total = 0;
		for(int j = 0; j<binN.length;j++) {
			total += binN[j];
		}
		if(total==0)
			return 0;
		return binN[i]/total;
	}
	
	public double getMeanSharing(){
		if(n>0)
			return mean/n;
		else
			return 0;
	}
	
	
	public static void main(String[] args) {
		//Probe p = new Probe (0.0,1.0,0.1);
		//for(int i=0;i<p.binIntervals.length;i++)
		//	System.out.println(p.binIntervals[i]);
	}
}