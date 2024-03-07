package observer;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import spaces.ObjectGrid2Dex;
import spaces.Spaces;
import spaces.SparseGrid2Dex;
import sweep.DataMeanSD;
import sweep.HandleData;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;
import sweep.Write;

public class Observer implements ObserverInterface {
	public Spaces spaces;
	public SparseGrid2Dex sparseSpaceGrid = null; //offspring class of SparseGrid2D
	public ObjectGrid2Dex objectSpace = null; //offspring class of ObjectGrid2D
	public Continuous2D continuousSpace = null;
	protected long step;
	protected boolean getdata = false;
	public Stoppable event = null;
	protected DataMeanSD data;
	public Write write; //need to change file and folder names
	public HandleData handler;
	protected SimStateSweep state;
	protected ParameterSweeper sweeper;
	protected int observationInterval =100; //default
	protected String precision= "%.3f";
	protected String[] headers = null; //default headers
	public String fileName;
	public String folderName;
	protected Bag agents;
	protected boolean stopWhenNoAgents = true;
	public SaveData dataToSave = null; //if a SaveData object is assigned to this variable, it will be printed out.
	public boolean customSweepOnly = false; //if true, user supplies a data structure to print out data during a sweep. See dataToSave above

	public void setCustomSweepOnly(boolean customSweepOnly) {
		this.customSweepOnly = customSweepOnly;
	}

	public int getObservationInterval() {
		return observationInterval;
	}

	public void setObservationInterval(int observationInterval) {
		this.observationInterval = observationInterval;
	}

	public void setState(SimStateSweep state) {
		this.state = state;
	}
	
	public boolean getGetData() {
		return getdata;
	}


	public Spaces getSpaces() {
		return spaces;
	}


	public void setSpaces(Spaces spaces) {
		this.spaces = spaces;
	}


	public boolean isStopWhenNoAgents() {
		return stopWhenNoAgents;
	}


	public void setStopWhenNoAgents(boolean stopWhenNoAgents) {
		this.stopWhenNoAgents = stopWhenNoAgents;
	}


	public void setSweeper(ParameterSweeper sweeper) {
		this.sweeper = sweeper;
	}

	public void getName(){
		System.out.println(this.toString());
	}


	public void setSparseSpaceGrid(SparseGrid2Dex sparseSpaceGrid) {
		this.sparseSpaceGrid = sparseSpaceGrid;
	}


	public void setObjectSpace(ObjectGrid2Dex objectSpace) {
		this.objectSpace = objectSpace;
	}
	
	/**
	 * This method is called in the obsever initialize method.
	 * @param state
	 */
	public void reSet(SimStateSweep state) {
		//ToDo  add any code for between simulations
	}

	public  Observer(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper, String precision, String[] headers) {
		super();
		this.precision = precision;
		this.state = state;
		this.sweeper = sweeper;
		this.headers = headers;
		this.fileName = fileName;
		this.folderName = folderName;
		data = new DataMeanSD(state,this.headers);
		write = new Write(folderName, fileName);
		handler = new HandleData(state, folderName,fileName, data, write,  precision);
		observationInterval = this.state.getDataSamplingInterval();
	}

	public void scheduleRepeating(){
		event=state.schedule.scheduleRepeating(0, 100, this);
	}

	public void initSparseGrid(SparseGrid2Dex sparseSpaceGrid){
		this.sparseSpaceGrid = sparseSpaceGrid;
		event=state.schedule.scheduleRepeating(0, 100, this);

	}

	public void initObjectGrid(ObjectGrid2Dex objectSpace){
		this.objectSpace = objectSpace;
		event=state.schedule.scheduleRepeating(0, 100, this);

	}
	
	public void initialize(Object space, Spaces spaces){
		switch(spaces){
		case OBJECT: objectSpace = (ObjectGrid2Dex)space;
			break;
		case SPARSE: sparseSpaceGrid = (SparseGrid2Dex)space;
			break;
		case  CONTINUOUS: continuousSpace = (Continuous2D)space;
			break;
		}
		event=state.schedule.scheduleRepeating(0, 100, this);
		this.spaces = spaces;
		reSet(state);
	}
	/**
	 * This method is used when a time interval is used.  There appears to be a bug when scheduling one or more
	 * agents as steps and others with time intervals in the same simulation.
	 * @param space
	 * @param spaces
	 * @param timeInterval
	 */
	public void initialize(Object space, Spaces spaces, double timeInterval){
		switch(spaces){
		case OBJECT: objectSpace = (ObjectGrid2Dex)space;
			break;
		case SPARSE: sparseSpaceGrid = (SparseGrid2Dex)space;
			break;
		case CONTINUOUS: continuousSpace = (Continuous2D)space;
			break;
		}
		event=state.schedule.scheduleRepeating(1.0,100,this,  timeInterval);
		this.spaces = spaces;
		reSet(state);
	}
	
	public void initSpace(Object space, Spaces spaces){
		switch(spaces){
		case OBJECT: objectSpace = (ObjectGrid2Dex)space;
			break;
		case SPARSE: sparseSpaceGrid = (SparseGrid2Dex)space;
			break;
		case  CONTINUOUS: continuousSpace = (Continuous2D)space;
			break;
		}
		event=state.schedule.scheduleRepeating(0, 100, this);
		this.spaces = spaces;
		reSet(state);
	}

	public String[] getHeaders() {
		return headers;
	}


	public void setHeaders(String[] headers) {
		this.headers = headers;
	}


	public double sd(double sum, double square, double n){
		double var=0;
		final double mean = sum/n;
		if(n > 3){
			var = (square/n - mean*mean)/(n-1);//sample correction
			if(var > 0)
				return Math.sqrt(var);
			else
				return 0;
		}
		else{
			return 0;
		}
	}

	public void flush(String fileName, String folderName){
		data = new DataMeanSD(state,headers);
		write = new Write(folderName, fileName);
		handler = new HandleData(state, folderName,fileName, data, write,  precision);
		observationInterval = this.state.getDataSamplingInterval();
	}

	public void reSetObserver(){
		write = new Write(state.folderDataName, state.fileDataName);
		handler = new HandleData(state, state.folderDataName,state.fileDataName, data, write,  precision);
		observationInterval = this.state.getDataSamplingInterval();
	}


	public boolean reset() {
		data.moveToTop();
		return true;
	}
	
	public boolean saveas() {
		if(this.customSweepOnly) {
			if(dataToSave !=null) {
				dataToSave.saveas();;
			}
		}
		else {
			this.handler.printDate("Start");
			this.handler.printSweepHeader(state.getSimulationTitle(),false);
			this.handler.printPramsToFile(state,false);
			this.handler.printSweepHeader(state.getSimulationTitle(),true);
			this.handler.printPramsToFile(state,true);
			if(dataToSave !=null) {
				dataToSave.saveas();
			}
		}
		return false;
	}


	public boolean save(int sweepNumber) {
		if(this.customSweepOnly) {
			if(dataToSave !=null) {
				dataToSave.save(sweepNumber);
			}
			this.data.resetSimulationCount();
		}
		else {
			this.handler.saveDataMeanSD(data, sweepNumber);
			this.handler.saveSummaryDataMeanSD(data,sweepNumber);
			System.out.println("dataToSave:  "+dataToSave);
			if(dataToSave !=null) {
				dataToSave.save(sweepNumber);
			}
			this.data.resetSimulationCount();
		}
		return false;
	}

	@Override
	public boolean nextStepCount() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void upDateTimeChart(double x, double y, boolean notify, long upDateInterval ) {
		if(state.gui.chartTimeSeries == null) {
			System.out.println("Time series chart is null!");
			return;//nothing to do
		}
		state.gui.series.add(x,y,true);
		state.gui.chartTimeSeries.updateChartWithin((long)x, upDateInterval);  // update within one second (1000 millise
	}
	
	public void upDateTimeChart(int index, double x, double y, boolean notify, long upDateInterval ) {
		if(state.gui.arrayChartTimeSeries == null) {
			System.out.println("Time series array chart is null!");
			return;//nothing to do
		}
		state.gui.arraySeries[index].add(x,y,true);
		state.gui.arrayChartTimeSeries[index].updateChartWithin((long)x, upDateInterval);  // update within one second (1000 millise
	}
	
	public void upDateHistogramChart(int time, double [] data, long upDateInterval ) {
		if(!state.gui.chartTypeH) {
			System.out.println("Histogram chart is null!");
			return;//nothing to do
		}
		state.gui.chartHistogram.updateSeries(0, data);
		state.gui.chartHistogram.updateChartWithin(time, upDateInterval);  // update within one second (1000 milliseconds))
	}
	
	public void upDateHistogramChart(int index,int time, double [] data, long upDateInterval ) {
		if(!state.gui.arrayChartTypeH) {
			System.out.println("Histogram chart array is null!");
			return;//nothing to do
		}
		state.gui.arrayChartHistogram[index].updateSeries(0, data);
		state.gui.arrayChartHistogram[index].updateChartWithin(time, upDateInterval);  // update within one second (1000 milliseconds))
	}

	

	public void step(SimState state) {
		step = state.schedule.getSteps();
		getdata = false;
		//boolean sweep = ((SimStateSweep)state).paramSweeps;//fixed 11-23-2019
		if(/*sweep &&*/ step % observationInterval == 0 || step == 1){ //get the first one too
			getdata = true;
		}
		switch(spaces){
		case OBJECT:
			agents = objectSpace.getAllObjects();
			if(stopWhenNoAgents && agents.numObjs == 0){
				this.event.stop();
				//state.kill();
			}
			break;
		case SPARSE:
			agents = sparseSpaceGrid.getAllObjects();
			if(stopWhenNoAgents && agents.numObjs == 0){
				this.event.stop();
				state.schedule.clear();
			}
			break;
		case CONTINUOUS:
			agents = continuousSpace.getAllObjects();
			if(stopWhenNoAgents && agents.numObjs == 0){
				this.event.stop();
				//state.kill();
			}
		}
	}
}
