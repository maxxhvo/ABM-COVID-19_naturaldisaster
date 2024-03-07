package sweep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import observer.Observer;
import sim.display.Console;
import sim.display.GUIState;
import spaces.ObjectGrid2Dex;
import spaces.SparseGrid2Dex;

public class ParameterSweeper {
	// Simulation control parameters
	public static final int SWEEPSTART = 0;//Set to 0 at start to get the column headers right on summary file, do not change 6-23-20
	public SparseGrid2Dex sparseSpaceGrid = null; //offspring class of SparseGrid2D
	public ObjectGrid2Dex objectSpace = null; //offspring class of ObjectGrid2D
	public boolean paramSweeps = true; 
	public long simLength = 1001;
	public int simNumber = 3;
	protected int simulationCount = 1; //used internally
	public int parameterSweeps = 1;
	protected int paramSweepCount = SWEEPSTART; //used internally
	boolean started = false;
	// Simulation control parameters end
	public String scriptFileName = "script.txt"; //default
	Console c = null;
	Observer observer = null;
	SimStateSweep state = null;
	public ArrayList<ArrayList<Object>> fixedParameters;
	public ArrayList<ArrayList<Object>> sweepParameters;

	public int getParamSweepCount() {
		return paramSweepCount;
	}
	 
	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public void changeParameter(){
		SetParameters.setSweepParameters(state, sweepParameters, paramSweepCount);
	}
	
	public void prameterSweepController(Object o, GUIState gui, String scriptName){
		if(gui == null){
			System.out.println("GUIState is null.");
			return;
		}
		Console c = (Console)gui.controller;
		scriptFileName = scriptName; 
		if(!started){
			started = this.initParameterSweeps(o, scriptFileName);
			if(started){
				observer.reSetObserver();
				c.setShouldRepeat(paramSweeps);
				c.setWhenShouldEnd(simLength);
				//c.setWhenShouldEndTime((double)simLength);
				c.refresh();
				observer.reset();
				simulationCount++;
				observer.handler.printDate("Start");
				observer.saveas();
				/*observer.handler.printSweepHeader(state.getSimulationTitle(),false);
				observer.handler.printPramsToFile(state,false);
				observer.handler.printSweepHeader(state.getSimulationTitle(),true);
				observer.handler.printPramsToFile(state,true);*/
				
				Date date = new Date();
				String d = date.toString();
				System.out.println("Starting date: "+d.toString());
				System.out.println("Parameter Sweep Count: "+ paramSweepCount);
			}
			else if(observer == null){
				System.out.println("Observer is null.");
				return;//end
			}
			else{
				c.setShouldRepeat(false);
				gui.finish();
				System.out.println("Did not start.");
			}
		}
		else if(started && simulationCount < simNumber){
			observer.reset();
			simulationCount++;
		}
		else if(started && simulationCount == simNumber && paramSweepCount < parameterSweeps){ //stop sweeps
			observer.save(paramSweepCount); //write the run file
			//observer.flush(state.fileDataName, state.folderDataName); //I think we should flush at this point
			observer.reset();
			simulationCount = 1;
			paramSweepCount++;
			changeParameter();
			System.out.println("Parameter Sweep Count: "+ paramSweepCount);
		}
		else if(started && simulationCount == simNumber  && paramSweepCount ==parameterSweeps){
			paramSweeps = false;
			c.setShouldRepeat(false);
			observer.save(paramSweepCount); //write the run file
			observer.flush(state.fileDataName, state.folderDataName);
			observer.reset();//probably should set the observer to null
			gui.finish();
			Date date = new Date();
			String d = date.toString();
			System.out.println("Finished: " + d.toString());
			observer.handler.printDate("\nFinished");
			observer.event.stop();//after finished, stop the observer to create another one for another sweep
			observer = null; //set to null
			state.observer=null; //set to null in SimStateSweep instance
			started = false;
			if(state.closeProgramAtend)
				c.doClose();
		}
	}

	public ParameterSweeper(SimStateSweep state, String scriptFileName) {
		super();
		if(!scriptFileName.isEmpty())
			this.scriptFileName = scriptFileName;
		this.state = state;
		//initParameterSweeps(state);
	}

	public boolean initParameterSweeps(Object o) {
		ArrayList<ArrayList<Object>> spreadsheet = null;
		try {
			spreadsheet = LoadSimulation.tokenize(o,scriptFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(spreadsheet == null)
			return false;
		spreadsheet = LoadSimulation.convertValues(LoadSimulation.clean(spreadsheet));
		fixedParameters = LoadSimulation.fixedTable(spreadsheet);
		sweepParameters = LoadSimulation.sweepTable(spreadsheet);
		if(sweepParameters == null)
			return false;
		SetParameters.setFixeParameters(o, fixedParameters);
		parameterSweeps = sweepParameters.get(0).size()-3;//total number of sweep runs
		this.simLength = (long)SetParameters.getObject(o, "simLength");
		this.simNumber = (int)SetParameters.getObject(o, "simNumber");
		this.paramSweeps = (boolean)SetParameters.getObject(o, "paramSweeps");
		this.simulationCount = 0;
		this.paramSweepCount = 1;
		started = false;
		SetParameters.setSweepParameters(o, sweepParameters, paramSweepCount);
		return true;
	}
	
	public boolean initParameterSweeps(Object o, String  scriptFileName) {
		System.out.println(scriptFileName + "  Script file name;");
		ArrayList<ArrayList<Object>> spreadsheet = null;
		try {
			spreadsheet = LoadSimulation.tokenize(o,scriptFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(spreadsheet == null)
			return false;
		spreadsheet = LoadSimulation.convertValues(LoadSimulation.clean(spreadsheet));
		fixedParameters = LoadSimulation.fixedTable(spreadsheet);
		sweepParameters = LoadSimulation.sweepTable(spreadsheet);
		SetParameters.setFixeParameters(o, fixedParameters);
		parameterSweeps = sweepParameters.get(0).size()-3;//total number of sweep runs
		this.simLength = (long)SetParameters.getObject(o, "simLength");
		this.simNumber = (int)SetParameters.getObject(o, "simNumber");
		this.paramSweeps = (boolean)SetParameters.getObject(o, "paramSweeps");
		this.simulationCount = 0;
		this.paramSweepCount = 1;
		started = false;
		SetParameters.setSweepParameters(o, sweepParameters, paramSweepCount);
		return true;
	}
}
