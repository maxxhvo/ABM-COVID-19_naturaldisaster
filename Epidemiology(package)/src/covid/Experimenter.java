package covid;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {

	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}

	//TODO IMPORTANT still need to implement GUI-experimenter and time-series chart
	
	public void numberOfFrozenAgents(Environment state) {
        Bag agents = state.sparseSpace.getAllObjects();
        int n = 0;//variable for counting frozen agents
        for(int i=0; i< agents.numObjs; i++) {
            Agent a = (Agent)agents.objs[i];
            if(a.frozen) { //need to reference the frozen agents
                n++;//another frozen agent
             }
        }
       double time = (double)state.schedule.getTime();//get the current time
       this.upDateTimeChart(0,time, n, true, 1000);//update the chart with up to a 1000 milisecond delay
	}
	
	public void step(SimState state) {
	       super.step(state);
	       if(step %this.state.dataSamplingInterval == 0) { // remember "%" is the remainder operator, so this logical statement translates as "is this step a multiple of the interval we set?"            numberOfFrozenAgents((Environment) state);
	       }
	}
}
