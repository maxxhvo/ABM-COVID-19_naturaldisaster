package covid;

import java.util.ArrayList;

import sim.util.Bag;
import sim.util.Int2D; //2D space
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep{

	//default values
	public int gridHeight = 100;
	public int gridWidth = 100;
	public int n = 250;
	public int n_infected = 1; //initial infected
	public boolean oneAgentPerCell = false;
	public double p_spread = 0.7; //baseline spread
	public boolean natural_disaster = false; //switch btw conditions (changes hospital capacity)
	public int hospital_capacity = 50; 
	public double burnin = 0.2; //proportion to implement
	public int recovery_h = 2;
	public int recovery_natural = 11;
	Bag allAgents = new Bag(); //bag of all agents
	Bag hospitalizedAgents = new Bag();
	public int searchRadius = 3;
	public int infectedAgents = 0;
	public boolean charts = true;
	
	public Environment(long seed) {
		super(seed);
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}

	 public void start() {
	       super.start();
	       spaces = Spaces.SPARSE;
	       makeSpace(gridWidth, gridHeight);
	       Hospital hospital = new Hospital(hospital_capacity);
	       makeAgents();
	       assignStatus();
	       if(observer != null) {
	           observer.initialize(sparseSpace, spaces);  //initialize the experimenter by calling initialize in the parent class
	       }
	 }

	public void makeAgents() {
		if (this.oneAgentPerCell) {
			int size = gridWidth * gridHeight;
			if (n > size) {
				n = size;
				System.out.println("change the number of agents to " + n);
			}
		}
		
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			
			if (this.oneAgentPerCell) {
				Bag b = sparseSpace.getObjectsAtLocation(x,y);
				while (b != null) {
					x = random.nextInt(gridWidth);
					y = random.nextInt(gridHeight);
					b = sparseSpace.getObjectsAtLocation(x,y);
				}
			}
			// not sure with directions (to be fixed)
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			// Assign agent its own id, x, y location and their direction
			Agent a = new Agent(i, x, y, xdir, ydir);
			a.status = Status.SUSCEPTIBLE;
			
			// Now, add all the agents into our "bag"
			allAgents.add(a);
			
			sparseSpace.setObjectLocation(a, x, y);
			a.colorByStatus(this);
			schedule.scheduleRepeating(a);
		}
	}
	
	public void assignStatus() {
		// Agent's id starts from 0 to n - 1 (249 for example)
		// loop since there is a possibility that we want more than 1 infected agent
		for (int i = 0; i < n_infected; i++) {
			int randomID = random.nextInt(n - 1);
			Agent a = (Agent) allAgents.objs[randomID];
			a.status = Status.INFECTED;
			a.colorByStatus(this);
			infectedAgents++;
		}
	}
	
	public Bag getAllAgents() {
		return allAgents;
	}

	public void setAllAgents(Bag allAgents) {
		this.allAgents = allAgents;
	}

	public Bag getHospitalizedAgents() {
		return hospitalizedAgents;
	}

	public void setHospitalizedAgents(Bag hospitalizedAgents) {
		this.hospitalizedAgents = hospitalizedAgents;
	}

}
