package covid;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Agent implements Steppable {
	Status status;
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
        if (isInHospital) {
            return;
        }
        int tempx = state.sparseSpace.stx(x + xdir);
        int tempy = state.sparseSpace.sty(y + ydir);
	
        if (state.oneAgentPerCell) {
            Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
            	if (b != null) { //bag is not empty ~
            		return; 
                } //pass conditions successfully
        }
        x = tempx;
        y = tempy;
        state.sparseSpace.setObjectLocation(this, x, y);
    }
	
	public void move(Environment state) {
		if (isInHospital) return;
		
		// 0.5 could be further changed -> probability of changing direction
		if (state.random.nextBoolean(0.5)) {
			xdir = state.random.nextInt(3) - 1;
			ydir = state.random.nextInt(3) - 1;
		}
        
		placeAgent(state); //implement ~ frozen in placeAgent
	}
	
	
	public boolean infect(Environment state, Bag neighbors) { //need to define location
		int numInfectedInBag = 0;
		
		for (int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent)neighbors.objs[i];
			if (a.status == status.INFECTED) {
				numInfectedInBag++;
			}
		}
		
		double p_catch = numInfectedInBag * state.p_spread;
		p_catch = Math.min(p_catch, (double)1);
		
		if(state.random.nextBoolean(p_catch)) return true;
		else return false;
	}
	
	public void changeStatus(Environment state, boolean updateStatus) {
		// if the agent's status is not yet to be changed, then return/exit this function
		if (!updateStatus) return;
		
		switch(this.status) {
		case SUSCEPTIBLE:
			this.status = status.EXPOSED;
			break;
		case EXPOSED:
			state.infectedAgents++;
			this.status = status.INFECTED;
			break;
		case INFECTED:
			this.status = status.RECOVERED;
			break;
		}
		
		colorByStatus(state);
	}
	
	public boolean updateInfectionTime(Environment state) {
		if (isInHospital) {
			// agent in the hospital
			if (this.infectedTimeHos >= state.recovery_h) {
				// now fully recovered, change its isInHospital and status
				this.isInHospital = false;
				state.hospitalizedAgents.remove(this);
				return true;
			} else {
				// agent is not yet fully recovered, increment its time
				this.infectedTimeHos++;
			}
		} else {
			// agent in Space
			if (this.infectedTimeNorm >= state.recovery_natural) {
				return true;
			} else {
				this.infectedTimeNorm++;
			}
		}
		return false;
	}
	
	public void colorByStatus(Environment state) {
		switch(this.status) {
		case SUSCEPTIBLE:
			// blue
			state.gui.setOvalPortrayal2DColor(this, (float)0, (float)0, (float)1, (float)1);
			break;
		case EXPOSED: 
			state.gui.setOvalPortrayal2DColor(this, (float)1, (float)0, (float)1, (float)1);
			break;
		case INFECTED:
			// color for red
			if (isInHospital) state.gui.setOvalPortrayal2DColor(this, (float)1, (float)0, (float)0, (float)0.5);
			// color for red should be dimmer (agent, r, g, b, opacity)
			else state.gui.setOvalPortrayal2DColor(this, (float)1, (float)0, (float)0, (float)1);
			break;
		case RECOVERED:
			// green
			state.gui.setOvalPortrayal2DColor(this, (float)0, (float)1, (float)0, (float)1);
			break;
		}
	}
	
	public Bag findNeighbors(Environment state) {
		Bag agents = state.sparseSpace.getMooreNeighbors(x, y, state.searchRadius, state.sparseSpace.TOROIDAL,false);
		return agents;
	}
	
	// TODO implement freezing aggregation
	// Commented just because I want to talk what this function is for
	/*
	public void setFrozen(boolean frozen) {
	    this.frozen = frozen;
	}
	public void isFrozen(){
	return frozen
	}
	*/
	
	@Override
	public void step(SimState state) {
		Environment eState = (Environment) state;
		move(eState);
		
		// variable to check if we need to update current agent's status
		boolean updateStatus;
		switch(status) {
		case EXPOSED:
			changeStatus(eState, true);
			// if break here, then we are adding recoveryTime++ in the second round
			// no break if we want to immediately add the recoveryTime++
			break;
		case SUSCEPTIBLE:
			Bag neighbors = findNeighbors(eState);
			if (neighbors.isEmpty()) {
				return;
			}
			updateStatus = infect(eState, neighbors);
			changeStatus(eState, updateStatus);
			break;// so like maybe we could have a bool variable to check if the infect returned true or false then change susceptible's state
		case INFECTED:
			// just check if their infection time has passed and update
			updateStatus = updateInfectionTime(eState);
			changeStatus(eState, updateStatus);  // change status accordingly
			break;
		case RECOVERED:;
		}
	}

}




/*
 * Note regarding Agent specifics (Correct me if I'm wrong):
 * - Agent moves on its own, has three possible states: susceptible, infected, and recovered
 * - When the agent 
 */
