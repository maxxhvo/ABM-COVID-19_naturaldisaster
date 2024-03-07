package randomWalker;

import sim.util.Double2D;
import spaces.Spaces;

@SuppressWarnings("serial")
public class EnvironmentContinuous extends RandomWalkerContinousEnvironment{
	public int n = 10;	

	
	public EnvironmentContinuous(long seed, Class observer) {
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
			RandomWalkerContinuous a = new RandomWalkerContinuous(this,x,y,stepSize,spaces, false);
			schedule.scheduleRepeating(a);
			Double2D location = new Double2D(a.x,a.y);
			if(uniqueLocation){
				if(i >= gridWidth * gridHeight)
					break; //no need to continue
				while(continuousSpace.getObjectsAtLocation(location)!=null){
					a.x = random.nextInt(gridWidth);
					a.y = random.nextInt(gridHeight);
					location = new Double2D(a.x,a.y);
				}
				continuousSpace.setObjectLocation(a, location);
			}
			else{
				continuousSpace.setObjectLocation(a, location);
			}
		}
	}

	public void start(){
		super.start();
		spaces = Spaces.CONTINUOUS;
		make2DSpace(spaces,discretation, gridWidth,gridHeight);//create the agent space
		makeAgents(); //make the agents
	}

}
