package covid;

import java.util.ArrayList;

import sim.util.Bag;
import sim.util.Int2D; //2D space
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep{

	//default values
	int gridHeight = 100;
	int gridWidth = 100;
	int n = 250;
	int n_infected = 1; //initial infected
	boolean oneAgentPerCell = false;
	double p_spread = 0.7; //baseline spread
	boolean natural_disaster = false; //switch btw conditions (changes hospital capacity)
	int hospital_capacity = 50; 
	double burnin = 0.2; //proportion to implement
	int recovery_h = 2;
	int recovery_natural = 11;
	Bag allAgents = new Bag();
	Bag hospitalizedAgents = new Bag();
	int searchRadius = 1;
	
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

	 public void start() {
	       super.start();
	       spaces = Spaces.SPARSE;
	       make2DSpace(spaces, gridWidth, gridHeight);
	       Hospital hospital = new Hospital(hospital_capacity);
	       makeAgents();
	       assignStatus();
	       if(observer != null) {
	           observer.initialize(sparseSpace, spaces);//initialize the experimenter by calling initialize in the parent class
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
		for (int n = 0; n < n_infected; n++) {
			int randomID = random.nextInt(n - 1);
			// This might be incorrect if bag's index does not start from 0
			Agent a = (Agent) allAgents.objs[randomID];
			a.setStatus(Agent.AgentStatus.INFECTED);
		}
		
		// loop through all agents in the bag and assign the status susceptible
		for (int i = 0; i < allAgents.numObjs; i++) {
			Agent a = (Agent) allAgents.objs[i];
			if (a.getStatus() != Agent.AgentStatus.INFECTED) {
				a.setStatus(Agent.AgentStatus.SUSCEPTIBLE);
			}
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

	public int getN() {
		return n;
	}

}
