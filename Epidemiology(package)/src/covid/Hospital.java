package covid;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Hospital implements Steppable {

	public Hospital() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
		
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