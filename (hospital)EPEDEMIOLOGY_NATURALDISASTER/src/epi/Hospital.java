package epi;


import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Hospital implements Steppable {
	int capacity;

	public Hospital(int hospitalCapacity) {
		this.capacity = hospitalCapacity;
	}
	
	public void randomSelection(Environment state) {
		// if hospital is full don't choose any agents
		if (state.hospitalizedAgents.numObjs >= capacity) return;
		// if the infectedAgent (num of infected agents) has not exceeded the burnin period, exit
		if (state.infectedAgents < (state.n * state.waitTime)) return;
		int slotsOpen = capacity - state.hospitalizedAgents.numObjs;
		Bag pulled = new Bag();
		Bag allAgents = state.allAgents;
		
		// randomly generate id's and add corresponding agents
		for (int i = 0; i < slotsOpen; i++) {
			// i.e. choose 0 to 249
			int index = state.random.nextInt(state.n -1);
			Agent a = (Agent) allAgents.objs[index];
			// If the agent is already in the hospital, choose different agent
			while (a.isInHospital == true) {
				index = state.random.nextInt(state.n - 1);
				a = (Agent)allAgents.objs[index];
			}

			pulled.add(a);
		}
		
		// loop through the pulled bag and ONLY add the infected agents
		for (int j = 0; j < pulled.numObjs; j++) {
			Agent a = (Agent) pulled.objs[j];
			if (a.status == Status.INFECTED) {
				// add to the hospitalized bag
				state.hospitalizedAgents.add(a);
				a.isInHospital = true;
				
			}
		}
	}

	@Override
	public void step(SimState state) {
		Environment eState = (Environment) state;
		randomSelection(eState);
	}
}
