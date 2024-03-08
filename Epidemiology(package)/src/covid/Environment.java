package covid;

import java.util.ArrayList;
import sim.util.Int2D; //2D space
import sweep.SimStateSweep;

public class Environment extends SimStateSweep{

	//default values
	int gridHeight = 100;
	int gridWidth = 100;
	int n = 250;
	int n_infected = 1; //initial infected
	boolean oneAgentPerCell = false;
	double p_spread = 0.7; //baseline spread
	boolean natural_disaster = false; //switch btw. conditions (changes hospital capacity)
	int hospital_capacity = 50; //we can implement it separately (one int amount for natural disaster and non-natural disaster conditions
	double burnin = 0.2; //proportion to implement
	int recovery_h = 2;
	int recovery_natural = 11;
	//TODO add logical parameters/variables
	
	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 public void start() {
	       super.start();
	       spaces = Spaces.SPARSE;
	       make2DSpace(spaces, gridWidth, gridHeight);
	       makeAgents();
	       if(observer != null) {
	           observer.initialize(sparseSpace, spaces);//initialize the experimenter by calling initialize in the parent class
	       }
	 }
	 */
	
	public void makeAgents() {
		if (this.oneAgentPerCell) {
			int size = gridWidth * gridHeight;
			if (n > size) {
				n = size;
				System.out.println("change the number of agents to " + n);
			}
		}
		//TODO consider frozen
	}
	
	//TODO create getters and setters when variables are finalized

}
