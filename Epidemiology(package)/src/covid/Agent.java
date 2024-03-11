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
	boolean frozen = false;
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
        if (frozen) {
            state.sparseSpace.setObjectLocation(this, x, y);
            frozen = true;
            return;
        }
        x = state.sparseSpace.stx(x + xdir);
        y = state.sparseSpace.sty(y + ydir);
	
        if (state.oneAgentPerCell) {
            int tempx = x + xdir;
            int tempy = y + ydir;
            Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
            	if (b != null) { //bag is not empty ~
                return; } //pass conditions successfully
            x = tempx;
            y = tempy;
        }
        state.sparseSpace.setObjectLocation(this, x, y);
    }
	
	
	public int decideX(Environment state, Bag neighbors) {
		int posx = 0;
        int negx = 0;
      if(state.oneAgentPerCell) {
    	  	for(int i = 0; i < neighbors.numObjs; i++) {
    	  		Agent aRandom = (Agent) neighbors.objs[i];

    	  		if (aRandom.x > this.x) {
                // right side
                posx++;
    	  		} else if (aRandom.x < this.x) {
                negx++;
    	  		}
    	  	}
    	  	if (posx > negx) {
    	  		return 1;
    	  	} else if (negx > posx) {
    	  		return -1;
    	  	} else {
    	  		return state.random.nextInt(3) - 1;
    	  	}
      	}
       else {
        return state.random.nextInt(3) - 1;
        }
    }
	
	public int decideY(Environment state, Bag neighbors) {
		int posy = 0;
        int negy = 0;
      if(state.oneAgentPerCell) {
    	  	for (int i = 0; i < neighbors.numObjs; i++) {
    	  		Agent aRandom = (Agent) neighbors.objs[i];
    	  		if (aRandom.y > this.y) {
                posy++;
    	  		} else if (aRandom.y < this.y) {
                negy++;
    	  		}
    	  	}
    	  	if (posy > negy) {
    	  		return 1;
    	  	} else if (negy > posy) {
    	  		return -1;
    	  	} else {
    	  		return state.random.nextInt(3) - 1;
    	  	}
      	}
      else {
          return state.random.nextInt(3) - 1;
      	  }
	}
	
	
	
	public void move(Environment state) {
		if(!frozen) {
        xdir = state.sparseSpace.stx(x + xdir);
        ydir = state.sparseSpace.sty(y + ydir);
    }
        placeAgent(state); //implement ~ frozen in placeAgent
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
	public void isFrozen(){
	return frozen
	}
	*/
	
	public void setStatus(AgentStatus status) {
		this.status = status;
	}
	
	public AgentStatus getStatus() {
		return status;
	}
	
	
	/* bounded concepts
	 public int bx(int x, Environment state) {
	        // Adjust x to stay within bounds for a bounded space
	        return Math.max(0, Math.min(x, state.getGridWidth() - 1));
	    }

	 public int by(int y, Environment state) {
	        // Adjust y to stay within bounds for a bounded space
	        return Math.max(0, Math.min(y, state.getGridHeight() - 1));
	} */
	
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
