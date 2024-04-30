package epi;

import sim.util.Bag;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {

	//default values
	public int gridHeight = 100;
	public int gridWidth = 100;
	public int n = 250;
	public int n_infected = 1; //initial infected
	public boolean oneAgentPerCell = false;
	public double p_spread = 0.7; //baseline spread
	public boolean natural_disaster = false; //switch btw conditions (changes hospital capacity)
	public int hospital_capacity = 50; 
	public double waitTime = 0.2; //proportion to implement
	public int recovery_h = 2;
	public int recovery_natural = 11;
	Bag allAgents = new Bag();
	Bag hospitalizedAgents = new Bag();
	public int searchRadius = 4;
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
	       makeSpace(gridWidth, gridHeight);
	       Hospital hospital = new Hospital(hospital_capacity);
	       schedule.scheduleRepeating(hospital, 1, 1);
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

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getN_infected() {
		return n_infected;
	}

	public void setN_infected(int n_infected) {
		this.n_infected = n_infected;
	}

	public boolean isOneAgentPerCell() {
		return oneAgentPerCell;
	}

	public void setOneAgentPerCell(boolean oneAgentPerCell) {
		this.oneAgentPerCell = oneAgentPerCell;
	}

	public double getP_spread() {
		return p_spread;
	}

	public void setP_spread(double p_spread) {
		this.p_spread = p_spread;
	}

	public boolean isNatural_disaster() {
		return natural_disaster;
	}

	public void setNatural_disaster(boolean natural_disaster) {
		this.natural_disaster = natural_disaster;
	}

	public int getHospital_capacity() {
		return hospital_capacity;
	}

	public void setHospital_capacity(int hospital_capacity) {
		this.hospital_capacity = hospital_capacity;
	}

	public double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}

	public int getRecovery_h() {
		return recovery_h;
	}

	public void setRecovery_h(int recovery_h) {
		this.recovery_h = recovery_h;
	}

	public int getRecovery_natural() {
		return recovery_natural;
	}

	public void setRecovery_natural(int recovery_natural) {
		this.recovery_natural = recovery_natural;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean isCharts() {
		return charts;
	}

	public void setCharts(boolean charts) {
		this.charts = charts;
	}

}
