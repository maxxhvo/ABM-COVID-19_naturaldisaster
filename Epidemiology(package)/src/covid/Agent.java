package covid;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Agent implements Steppable {

	//variables
	String status; //2 states w/ 3rd technical state OR  
		boolean isInfected = false;
	int ID, x, y, xdir, ydir;
	int recoveryTime = null; //if infected assign this and track recovery time ~ should we implement this way, since we can implement via recovery_nat
	boolean isInHospital = false; //(false if in space, true if in the hospital)
	boolean frozen = false;
	
	//constructor method
	public Agent(int ID, int x, int y, int xdir, int ydir) {
		super();
		this.ID = ID;
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
	public void setFrozen(boolean frozen) {
	    this.frozen = frozen;
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
