package sweep;

/*
 * An extension of SimState, SimStatSweep implements a parameter sweeper and creates an
 * observer for gathering and saving data from systematic parameter sweeps.
 */
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import observer.Observer;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import spaces.ObjectGrid2Dex;
import spaces.Spaces;
import spaces.SparseGrid2Dex;

public class SimStateSweep extends SimState {
	// Simulation control parameters
	public SparseGrid2Dex sparseSpace = null; //offspring class of SparseGrid2D
	public ObjectGrid2Dex objectSpace = null; //offspring class of ObjectGrid2D
	public Continuous2D continuousSpace = null;
	public String nameSpace = "SPARSE";//space names are: SPARSE,OBJECT,CONTINUOUS
	public Spaces spaces = Spaces.valueOf(nameSpace); //get the selected space
	//public boolean objSpace = true;
	/** the space ultimately create**/
	public Object space = null;
	public int gridWidth = 100;
	public int gridHeight = 100;
	public GUIStateSweep gui = null;
	public Class observerClass = null;
	public boolean paramSweeps = true; 
	public long simLength = 1001;
	public int simNumber = 10;
	protected int parameterSweeps = 1;
	protected boolean started = false;
	public ParameterSweeper pramSweeper = null;
	public Observer observer = null;
	public String fileDataName;
	public String folderDataName;
	protected String scriptName ;
	protected String precision = "%.3f";
	public String[] dataFileHeaders;
	protected String simulationTitle = "Parameter Sweeps";
	public int dataSamplingInterval = 1;
	public int burnIn = 0;
	public double scheduleTimeInterval = 1.0;
	protected boolean stopWhenNoAgents = true;
	protected boolean runTimeFileLoaded = false;
	protected boolean scriptFileLoaded = false;
	protected boolean observerCreated = false;
	public String[] rowNames = CreateRunTimeFile.ROW_NAMES;
	public boolean closeProgramAtend = true;  //closes the simulation at the end if true
	public boolean customSweepOnly = false; //if true, user supplies a data structure to print out data during a sweep

	public SimStateSweep(long seed) {
		super(seed);
	}

	public SimStateSweep(long seed, Class observer) {
		super(seed);
		observerClass = observer;
		ArrayList<ArrayList<String>> simStartData = null;
		if(observerClass!= null) {
			try {
				simStartData = LoadRunTimeFile.tokenize("runTimeFile");
			} catch (IOException e) {
				System.out.println("Failed to opent runTimeFile.");
				e.printStackTrace();
			}
		}
		if(simStartData != null){
			runTimeFileLoaded = true;
			scriptName = txtString(findResult(simStartData,rowNames[0]));
			folderDataName = findResult(simStartData,rowNames[1]);
			fileDataName = txtString(findResult(simStartData,rowNames[2]));
			paramSweeps = getBoolean(findResult(simStartData,rowNames[3]));
			dataFileHeaders = getHeaders(simStartData,rowNames[4]);
			String temp = findResult(simStartData,rowNames[5]);
			int m = Integer.parseInt(temp);
			if(m<0) m = 1;
			if(m>20) m = 20;
			precision = "%."+m+"f";
			stopWhenNoAgents  = getBoolean(findResult(simStartData,rowNames[6]));
			pramSweeper = new ParameterSweeper(this, scriptName);
			//printRunTimeFile();
		}
		else{
			runTimeFileLoaded = false;
			scriptFileLoaded = false;
		}
		if(observer != null && runTimeFileLoaded){
			try {

				this.observer = (Observer)observer.getConstructor(fileDataName.getClass(), folderDataName.getClass(), SimStateSweep.class,pramSweeper.getClass(), precision.getClass(), dataFileHeaders.getClass()).newInstance(fileDataName,folderDataName,this,pramSweeper,precision,dataFileHeaders);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				System.out.println("Attempted to create observer, but could not.");
				e.printStackTrace();
			}
		}
		else{
			//System.out.println("Insufficient information to create an Observer.");
			observerCreated = false;
		}
		if(this.observer != null){
			pramSweeper.setObserver(this.observer);
			this.observer.setSweeper(pramSweeper);
			this.observer.setStopWhenNoAgents(stopWhenNoAgents);//cludgy fix to stopping problem
			this.observer.setCustomSweepOnly(customSweepOnly);
			//this.observer.getName();
			//observer.event=schedule.scheduleRepeating(1, 100, observer);
		}
	}

	public SimStateSweep(long seed, Class observer, String runTimeFileName) {
		super(seed);
		observerClass = observer;
		ArrayList<ArrayList<String>> simStartData = null;
		if(observerClass!= null) {
			try {
				simStartData = LoadRunTimeFile.tokenize(runTimeFileName);
			} catch (IOException e) {
				System.out.println("Failed to opent runTimeFile.");
				e.printStackTrace();
			}
		}
		if(simStartData != null){
			runTimeFileLoaded = true;
			scriptName = txtString(findResult(simStartData,rowNames[0]));
			folderDataName = findResult(simStartData,rowNames[1]);
			fileDataName = txtString(findResult(simStartData,rowNames[2]));
			paramSweeps = getBoolean(findResult(simStartData,rowNames[3]));
			dataFileHeaders = getHeaders(simStartData,rowNames[4]);
			String temp = findResult(simStartData,rowNames[5]);
			int m = Integer.parseInt(temp);
			if(m<0) m = 1;
			if(m>20) m = 20;
			precision = "%."+m+"f";
			stopWhenNoAgents  = getBoolean(findResult(simStartData,rowNames[6]));
			pramSweeper = new ParameterSweeper(this, scriptName);
			//printRunTimeFile();
		}
		else{
			runTimeFileLoaded = false;
			scriptFileLoaded = false;
		}
		if(observer != null && runTimeFileLoaded){
			try {

				this.observer = (Observer)observer.getConstructor(fileDataName.getClass(), folderDataName.getClass(), SimStateSweep.class,pramSweeper.getClass(), precision.getClass(), dataFileHeaders.getClass()).newInstance(fileDataName,folderDataName,this,pramSweeper,precision,dataFileHeaders);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				System.out.println("Attempted to create observer, but could not.");
				e.printStackTrace();
			}
		}
		else{
			//System.out.println("Insufficient information to create an Observer.");
			observerCreated = false;
		}
		if(this.observer != null){
			pramSweeper.setObserver(this.observer);
			this.observer.setSweeper(pramSweeper);
			this.observer.setStopWhenNoAgents(stopWhenNoAgents);//cludgy fix to stopping problem
			this.observer.setCustomSweepOnly(customSweepOnly);
			//this.observer.getName();
			//observer.event=schedule.scheduleRepeating(1, 100, observer);
		}
	}

	public void initRunTimeFile(Class observer){
		ArrayList<ArrayList<String>> simStartData = null;
		if(observer != null) {
			try {
				simStartData = LoadRunTimeFile.tokenize("runTimeFile");
			} catch (IOException e) {
				System.out.println("Failed to opent runTimeFile.");
				e.printStackTrace();
			}
		}
		if(simStartData != null){
			runTimeFileLoaded = true;
			scriptName = txtString(findResult(simStartData,rowNames[0]));
			folderDataName = findResult(simStartData,rowNames[1]);
			fileDataName = txtString(findResult(simStartData,rowNames[2]));
			paramSweeps = getBoolean(findResult(simStartData,rowNames[3]));
			dataFileHeaders = getHeaders(simStartData,rowNames[4]);
			String temp = findResult(simStartData,rowNames[5]);
			int m = Integer.parseInt(temp);
			if(m<0) m = 1;
			if(m>20) m = 20;
			precision = "%."+m+"f";
			stopWhenNoAgents  = getBoolean(findResult(simStartData,rowNames[6]));
			pramSweeper = new ParameterSweeper(this, scriptName);
			//printRunTimeFile();
		}
		else{
			runTimeFileLoaded = false;
			scriptFileLoaded = false;
		}
		if(observer != null && runTimeFileLoaded){
			try {

				this.observer = (Observer)observer.getConstructor(fileDataName.getClass(), folderDataName.getClass(), SimStateSweep.class,pramSweeper.getClass(), precision.getClass(), dataFileHeaders.getClass()).newInstance(fileDataName,folderDataName,this,pramSweeper,precision,dataFileHeaders);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				System.out.println("Attempted to create observer, but could not.");
				e.printStackTrace();
			}
		}
		else{
			//System.out.println("Insufficient information to create an Observer.");
			observerCreated = false;
		}
		if(this.observer != null){
			pramSweeper.setObserver(this.observer);
			this.observer.setSweeper(pramSweeper);
			this.observer.setStopWhenNoAgents(stopWhenNoAgents);//cludgy fix to stopping problem
			this.observer.setCustomSweepOnly(customSweepOnly);
			//this.observer.getName();
			//observer.event=schedule.scheduleRepeating(1, 100, observer);
		}
	}


	public void setDataSamplingInterval(int dataSamplingInterval) {
		this.dataSamplingInterval = dataSamplingInterval;
	}

	public double getScheduleTimeInterval() {
		return scheduleTimeInterval;
	}

	public void setScheduleTimeInterval(double scheduleTimeInterval) {
		this.scheduleTimeInterval = scheduleTimeInterval;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	/*public boolean isParamSweeps() {
		return paramSweeps;
	}

	public void setParamSweeps(boolean paramSweeps) {
		this.paramSweeps = paramSweeps;
	}

	public long getSimLength() {
		return simLength;
	}

	public void setSimLength(long simLength) {
		this.simLength = simLength;
	}

	public int getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(int simNumber) {
		this.simNumber = simNumber;
	}

	public int getParameterSweeps() {
		return parameterSweeps;
	}

	public void setParameterSweeps(int parameterSweeps) {
		this.parameterSweeps = parameterSweeps;
	}

	public String getFileDataName() {
		return fileDataName;
	}

	public void setFileDataName(String fileDataName) {
		this.fileDataName = fileDataName;
	}

	public String getFolderDataName() {
		return folderDataName;
	}

	public void setFolderDataName(String folderDataName) {
		this.folderDataName = folderDataName;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	 */
	public int getDataSamplingInterval() {
		return dataSamplingInterval;
	}
	/*
	public void setDataSamplingInterval(int dataSamplingInterval) {
		this.dataSamplingInterval = dataSamplingInterval;
	}

	public int getBurnIn() {
		return burnIn;
	}

	public void setBurnIn(int burnIn) {
		this.burnIn = burnIn;
	}
	 */
	public String getSimulationTitle() {
		return simulationTitle;
	}
	/*
	public void setSimulationTitle(String simulationTitle) {
		this.simulationTitle = simulationTitle;
	}
	 */
	public void setGui(GUIStateSweep gui) {
		this.gui = gui;
	}
	/*
	public GUIState getGuiState() {
		return gui;
	}*/

	String txtString(String fileName){
		if(fileName.endsWith(".txt"))
			return fileName;
		else
			return fileName+".txt";
	}

	boolean getBoolean(String b){
		String x = b.toLowerCase();
		if(x.equals("true") || x.equals("t") || x.equals("yes")){
			return true;
		}
		else{
			return false;
		}
	}

	String[] getHeaders(ArrayList<ArrayList<String>> list, String key){
		ArrayList<String> line = null;
		for(int i = 0; i< list.size();i++){
			line = list.get(i);
			if(key.equals(line.get(0))){
				break;
			}
			else{
				line = null;
			}
		}
		if(line != null  && line.size() > 1){
			String[] s = new String[line.size()-1];
			for(int i=0;i<line.size()-1;i++)
				s[i] = line.get(i+1);
			return s;
		}
		else{
			System.out.print("Could not create headers because ");
			if(line == null)
				System.out.println("line is null.");
			else
				System.out.println("line is length "+ line.size());
			return null;
		}
	}

	public static String findResult(ArrayList<ArrayList<String>> list, String key){
		ArrayList<String> line = null;
		String result = null;
		for(int i = 0; i< list.size();i++){
			line = list.get(i);
			if(key.equals(line.get(0))){
				result = line.get(1);
				break;
			}
		}

		return result;
	}

	public void printRunTimeFile(){
		System.out.println(rowNames[0] +": "+ scriptName);
		System.out.println(rowNames[1] +": "+ folderDataName);
		System.out.println(rowNames[2] +": "+ fileDataName);
		System.out.println(rowNames[3] +": "+ paramSweeps);
		System.out.print(rowNames[4] +": ");
		if(dataFileHeaders !=null){
			for(int i=0;i< dataFileHeaders.length;i++){
				System.out.print(dataFileHeaders[i] +", ");
			}
			System.out.println();
		}
		else{
			System.out.println("null");
		}

	}

	public  String checkforTXT(String fileName){
		if(fileName.contains(".txt"))
			return fileName;
		else
			return fileName+".txt";
	}

	/*	public void make2DSpace(boolean objSpace, int gridWidth, int gridHeight){
		if(objSpace){
			objectSpace = new ObjectGrid2Dex(gridWidth, gridHeight);
			this.objSpace = true;
		}
		else{
			sparseSpace = new SparseGrid2Dex(gridWidth, gridHeight);
			this.objSpace = false;
		}
	}*/

	public Object make2DSpace(Spaces space, double discretation, double gridWidth, double gridHeight){
		switch (space){
		case SPARSE:sparseSpace = new SparseGrid2Dex((int)gridWidth, (int)gridHeight);
		return sparseSpace;
		case OBJECT:objectSpace = new ObjectGrid2Dex((int)gridWidth, (int)gridHeight);
		return objectSpace;
		case CONTINUOUS: continuousSpace = new Continuous2D(discretation, gridWidth, gridHeight);
		return continuousSpace;
		default :
			System.out.println("Could not make a space: " + space);
			return null;
		}
	}

	public Object make2DSpace(Spaces space, int gridWidth, int gridHeight){
		if(space == Spaces.SPARSE || space == Spaces.OBJECT){
			return make2DSpace(space, 1, (double) gridWidth, (double) gridHeight); //discretation not used
		}
		else{
			System.out.println("Could not create space: Not SPARSE or OBJECT");
			return null;
		}
	}

	public void makeSpace(int gridWidth, int gridHeight) {
		space = make2DSpace(spaces,gridWidth, gridHeight);
	}

	public void start(){	
		super.start();
		if(observer == null){
			this.initRunTimeFile(observerClass);
		}
		if(pramSweeper!=null && paramSweeps)
			pramSweeper.prameterSweepController(this,gui,scriptName);
		spaces = Spaces.valueOf(nameSpace);
	}

}
