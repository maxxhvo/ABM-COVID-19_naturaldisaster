package observer;


import sweep.SimStateSweep;
import sweep.Write;

public class ProbeBin2D implements SaveData {
	Observer observer;
	SimStateSweep state;
	public Bin2D bin2D;
	String fileName;
	String folderName;
	Write write;
	boolean saveAs;
	
	

	public ProbeBin2D(SimStateSweep state, String fileNamePrefix,double xRange, int xbins, double yRange, int ybins, boolean saveAs) {
		super();
		this.state = state;
		this.observer = state.observer;
		this.fileName = observer.fileName;
		this.folderName = observer.folderName;
		this.fileName = fileNamePrefix+this.fileName; //different name
		this.saveAs = saveAs;
		write = new Write(this.folderName, this.fileName);
		bin2D = new Bin2D(write,xbins, ybins, xRange, yRange);
	}
	

	@Override
	public void saveas() {
		observer.handler.printPramsToFile(write, state);
		
	}

	@Override
	public void save(int sweepNumber) {
		if(!saveAs) {
			saveas();
			saveAs = true;
		}
		observer.handler.saveRunInfo(write,sweepNumber);
		bin2D.writeDataMeans();
	}

}
