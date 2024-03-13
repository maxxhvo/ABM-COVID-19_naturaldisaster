package covid;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	public int susceptible = 0;
	public int infected = 0;
	public int recovered = 0;
	
	// counting agents that are hospitalized
	public int numHospitalized = 0;

	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}

	public boolean reset(Environment state) {
		super.reset();
		susceptible =0;
		infected = 0;
		recovered = 0;
		return true;
	}
	
	// change Environment state to SimState statem -> not sure?
	public void countStrategies(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a =(Agent)state.allAgents.objs[i];
			switch(a.getStatus()) {
			case SUSCEPTIBLE:
				susceptible ++;
				break;
			case INFECTED:
				infected++;
				break;
			case RECOVERED:
				recovered++;
				break;
			}
		}
	}
	
	public boolean nextInterval() {
		double total = susceptible + infected + recovered;
		data.add(total);
		data.add(susceptible/total);
		data.add(infected/total);
		data.add(recovered/total);

		return false;
	}
		
	public void step(SimState state) {
	       super.step(state);
	       Environment eState = (Environment) state;
	       if (eState.paramSweeps && getdata) {
	    	   reset(eState);
	    	   countStrategies(eState);
	    	   numHospitalized = eState.hospitalizedAgents.numObjs;
	    	   nextInterval();
	       }
	}
}
