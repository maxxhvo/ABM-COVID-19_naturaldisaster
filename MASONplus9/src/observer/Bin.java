package observer;

public class Bin {
	public double[] bin;// array for binned data
	public double[] binN;//number of elements in each location
	double[] binIntervals;// intervals binned
	double min;
	int size; // size of bin
	double interval; // interval binned

	public Bin(int size, double interval, double min) {
		super();
		this.size = size;
		this.interval = interval;
		this.min = min;
		bin = new double[size];// initialize bin
		binN = new double[size];
		binIntervals = new double[size];
		for (int i = 0; i < size; i++) {
			bin[i] = 0.0;// zero the bin
			binN[i]=0.0;
			binIntervals[i] = 0.0;
		}
	
		binIntervals[1] = interval;
		for (int i = 1; i < size; i++)
			binIntervals[i] = binIntervals[i - 1] + interval;
	}
	
	public Bin(double[] array) {
		super();
		this.size = array.length;
		bin = new double[size];// initialize bin
		binN = new double[size];
		binIntervals = new double[size];
		for (int i = 0; i < size; i++) {
			bin[i] = 0.0;// zero the bin
			binN[i]=0.0;
			binIntervals[i] = array[i];
		}
	}
	
	public void clear() {
		this.size = 0;
		this.interval = 0;
		this.min = 0;
		bin = null;// initialize bin
		binN = null;
		binIntervals = null;
	}
	
	public void initialize(int size, double interval, double min) {
		this.size = size;
		this.interval = interval;
		this.min = min;
		bin = new double[size];// initialize bin
		binN = new double[size];
		binIntervals = new double[size];
		for (int i = 0; i < size; i++) {
			bin[i] = 0.0;// zero the bin
			binN[i]=0.0;
			binIntervals[i] = 0.0;
		}
	
		binIntervals[0] = interval;
		for (int i = 1; i < size; i++)
			binIntervals[i] = binIntervals[i - 1] + interval;
	}
	
	public double getMean() {
		double mean = 0;
		double total = 0;
		for (int i = 0; i < size; i++) {
			mean += binIntervals[i] * bin[i];
			total+= binN[i];
		}
		if(total > 0)
			return mean/total;
		else
			return 0.0;
	}

	public boolean binDataF(double data) {
		int check = 0;
		for (int i = 0; i < size; i++) {
			if (data <= binIntervals[i]) {
				bin[i]++;
				binN[i]++;
				return true;
			}
			check++;
		}
		System.out.println("Failed to bin: "+"binInterval: "+check+" data: "+data);
		return false;//failed to bin
	}
	
	public boolean binData(double key, double data) {
		int check = 0;
		for (int i = 0; i < size; i++) {
			if (key <= binIntervals[i]) {
				bin[i]+=data;
				binN[i]++;
				return true;
			}
			check++;
		}
		System.out.println("Failed to bin: "+"binInterval: "+check+" key: "+key);
		return false;//failed to bin
	}
	
	public double[] getFrequencyDis() {
		double[] array = new double[size];
		double total = 0.0;
		for(int i= 0; i<size;i++) {
			total+=binN[i];
		}
		for(int i= 0; i<size;i++) {
			if(total>0) {
				array[i]=bin[i]/total;
			}
			else {
				array[i]=0.0;
			}
		}
		return array;
	}
	
	public double[] getFrequencyBin() {
		double[] array = new double[size];
	
		for(int i= 0; i<size;i++) {
			if(binN[i]>0) {
				array[i]=bin[i]/binN[i];
			}
			else {
				array[i]=0.0;
			}
		}
		return array;
	}
	
	public double[] getNormalizedFrequencyDis() {
		double[] array = getFrequencyDis();
		double max = 0;
		for(int i= 0; i<size;i++) {
			if(array[i]> max)
				max = array[i];
		}
		for(int i= 0; i<size;i++) {
			if(max > 0)
				System.out.println(" array " +(array[i]=array[i]/max));
			else
				array[i]=0.0;
		}
		
		return array;
	}

}
