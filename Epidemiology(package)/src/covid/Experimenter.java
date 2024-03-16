package covid;

import observer.Observer;
import sim.engine.Steppable;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
    public int infected = 0; // Track the number of infected agents
    public int susceptible = 0; // Track the number of susceptible agents
    public int recovered = 0; // Track the number of recovered agents
    public double numHospitalized = 0;
    
    public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
            String precision, String[] headers) {
        super(fileName, folderName, state, sweeper, precision, headers);
    }

    public void countStrategies(Environment state) {
        Bag agents = state.sparseSpace.getAllObjects();
        
        for (int i = 0; i < agents.numObjs; i++) {
            Agent a = (Agent) agents.objs[i];
            switch (a.status) {
            case SUSCEPTIBLE:
            	susceptible++;
            	break;
            case INFECTED:
                infected++;
                break;
            case EXPOSED:
                break;
            case RECOVERED:
                recovered++;
                break;
            }
        }
    }
    
	public boolean reset(SimState state) {
		susceptible =0;
		infected = 0;
		recovered = 0;
		numHospitalized = 0;
		return true;
	}
	
	public boolean nextInterval() {
		double total = susceptible + infected + recovered;
		data.add(total);
		data.add(susceptible/total);
		data.add(infected/total);
		data.add(recovered/total);
		data.add(numHospitalized/total);
		return false;
	}

    public void step(SimState state) {
    	super.step(state);
	    Environment eState = (Environment) state;
	    if (eState.paramSweeps && getdata) {
	    	reset(state);
	    	countStrategies(eState);
	    	numHospitalized = (int) eState.hospitalizedAgents.numObjs;
	    	nextInterval();
	    }
    }
}
