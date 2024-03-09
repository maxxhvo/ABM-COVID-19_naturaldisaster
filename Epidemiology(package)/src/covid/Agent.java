package covid;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Agent implements Steppable {
	public enum AgentStatus {
		SUSCEPTIBLE,
		INFECTED,
		RECOVERED
	}
	int id, x, y, xdir, ydir;
	
	/* infectedTime represents how long the Agent has been infected
	 *  if (!isInHospital) {
	 *  	// Agent is not in the hospital, so check if the agent is recovered in space
	 *  	if (infectedTimeNorm == recovery_normal) then change status
	 *  else {
	 *  	// Agent is in the hospital
	 *  	if (infectedTimeHos == recovery_h) then change status
	*/
	int infectedTimeNorm = 0;  
	int infectedTimeHos = 0;
	
	boolean isInHospital = false;  // false if in space, true if in the hospital

	private AgentStatus status;
	
	// What is this variable for??
	// boolean frozen = false;
	
	//constructor method
	public Agent(int id, int x, int y, int xdir, int ydir) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}

	
	public void placeAgent(Environment state) {
		//TODO
	}
	public void move(Environment state) {
		//TODO
	}
	public int decideX(Environment state) {
		return state.random.nextInt(3) - 1;
		//TODO
	}
	public int decideY(Environment state) {
		return state.random.nextInt(3) - 1;
		//TODO
	}
	
	public void infect(int ID,String status/*,location,*/Bag neighbors) { //need to define location
		//TODO
	}

	
	// TODO implement freezing aggregation
	// Commented just because I want to talk what this function is for
	/*
	public void setFrozen(boolean frozen) {
	    this.frozen = frozen;
	}
	*/
	
	public void setStatus(AgentStatus status) {
		this.status = status;
	}
	
	public AgentStatus getStatus() {
		return status;
	}
	
	@Override
	public void step(SimState state) {
		// TODO implement what occurs during each time step
		/*
		if(frozen) return;
			move((Environment)state);
		*/
	}

}




/*
 * Note regarding Agent specifics (Correct me if I'm wrong):
 * - Agent moves on its own, has three possible states: susceptible, infected, and recovered
 * - When the agent 
 */
