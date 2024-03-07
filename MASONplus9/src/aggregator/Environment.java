package aggregator;

import randomWalker.RandomWalkMechanics;
import spaces.Spaces;

public class Environment extends AggregatorEnvironment {
	

	public int n = 100; //number of agents
	
	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}


	public void placeAgents(){
		for(int i=0;i<n;i++){
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			Aggregator a = new Aggregator(this,x,y,RandomWalkMechanics.classicRules[rule]);
			schedule.scheduleRepeating(a);
			if(spaces == Spaces.OBJECT){
				if(i >= gridWidth * gridHeight)
					break; //no need to continue
				while(!objectSpace.addAgentToLocation(a, a.x, a.y)){
					a.x = random.nextInt(gridWidth);
					a.y = random.nextInt(gridHeight);
					a.ox = a.x;
					a.oy = a.y;
				}
			}
			else if(spaces == Spaces.SPARSE){
				if(uniqueLocation){
					if(i < gridWidth*gridHeight){
						place(a);
					}
					else{
						System.out.println("Too many agents for the dimensions of this space.");
						break;
					}
				}
				else{
					sparseSpace.setObjectLocation(a, x, y);
				}
			}
		}
	}
	
	public void start(){
		super.start();
		make2DSpace(spaces, gridWidth, gridHeight);
		placeAgents();
	}


}
