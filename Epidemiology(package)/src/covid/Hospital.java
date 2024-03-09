package covid;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import java.util.Random;

public class Hospital implements Steppable {
	
	Bag hospitalizedAgents;
	int capacity;

	//TODO implement agent coloring (4 states)
	
	public Hospital(int hospitalCapacity) {
		this.capacity = hospitalCapacity;
	}
	
	public void removeRecovered(Environment state) {
		for (int i = 0; i < hospitalizedAgents.numObjs; i++) {
			Agent a = (Agent) hospitalizedAgents.objs[i];
			if ((a.getStatus() == Agent.AgentStatus.RECOVERED) &&
					(a.isInHospital == false)) {
				hospitalizedAgents.remove(i);
			}
		}
	}
	
	public void randomSelection(Environment state) {
		// if hospital is full don't choose any agents
		if (hospitalizedAgents.numObjs >= capacity) return;
		
		int slotsOpen = capacity - hospitalizedAgents.numObjs;
		Bag pulled = null;
		Random random = new Random();
		Bag allAgents = state.allAgents;
		
		// randomly generate id's and add corresponding agents
		for (int i = 0; i < slotsOpen; i++) {
			// i.e. choose 0 to 249
			int index = random.nextInt(state.getN() - 1);
			Agent a = (Agent) allAgents.objs[index];
			pulled.add(a);
		}
		
		// loop through the pulled bag and ONLY add the infected agents
		for (int j = 0; j < pulled.numObjs; j++) {
			Agent a = (Agent) pulled.objs[j];
			if (a.getStatus() == Agent.AgentStatus.INFECTED) {
				// add to the hospitalized bag
				hospitalizedAgents.add(a);
			}
		}
	}

	@Override
	public void step(SimState state) {
		Environment eState = (Environment) state;
		removeRecovered(eState);
		randomSelection(eState);
	}
}


/*
 * Pseudocode for the hospital random selection of the agents:
 * If ((Bag) hospitalized.numObj >= capacity) return; // if the capacity is full, don't enter the function
 * 
 * slotsOpen = hospitalCapacity - hospitalized.numObj;
 * Bag pulled = new Bag;
 * for (int i = 0; i < slotsOpen; i++) {
 * 		index = randInt (pull random integer for the index;
 * 		// allAgent is a bag that you create in the Environment that consists of all agents
 * 		agent a = (Agent)allAgent.Obj(index);
 * 		pulled.addObj(a); // add the "agent (could be susceptible or infected)" to the pulled Bag
 * }
 * 
 * for all pulled (run through every agents in the "pulled" bag) {
 * 		Agent a = (Agent) pulled.Obj(i);
 * 		if (a.status == infected) {
 * 			add to the hospitalized bag
 * 		}
 * }
*/

/*
 * NOTE: There should be a global bag called "hospitalized bag" in this class and before the random
 * selection of the agents, we should remove all the agents whose state has been changed to "RECOVERED" or 
 * if the isInHospital has changed to FALSE
 * ** MAKE SURE TO DISABLE BOTH IN AGENT CLASS!!
 */