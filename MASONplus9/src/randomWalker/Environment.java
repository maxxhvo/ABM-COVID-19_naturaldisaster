package randomWalker;

import spaces.Spaces;

@SuppressWarnings("serial")
public class Environment extends RandomWalkerEnvironment {
	public int n = 10;	
	
	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

		
	public int getN(){
		return n;
	} 

	public void setN(int x){
		if(x>0){
			n = x;
		}
	}
	
	public void makeAgents(){
		for (int i =0; i<n;i++){		
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			RandomWalker a = new RandomWalker(this,x,y,RandomWalkMechanics.classicRules[rule]);
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
					if(i >= gridWidth * gridHeight)
						break; //no need to continue
					while(sparseSpace.getObjectsAtLocation(a.x, a.y)!=null){
						a.x = random.nextInt(gridWidth);
						a.y = random.nextInt(gridHeight);
					}
					sparseSpace.setObjectLocation(a, x, y);
				}
				else{
					sparseSpace.setObjectLocation(a, x, y);
				}
			}
		}
	}

	public void start(){
		super.start();
		make2DSpace(spaces, gridWidth,gridHeight);//create the agent space
		makeAgents(); //make the agents
	}

}
