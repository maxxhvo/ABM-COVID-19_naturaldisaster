package randomWalker;

import spaces.Spaces;
import sweep.SimStateSweep;

@SuppressWarnings("serial")
public class RandomWalkerEnvironment extends SimStateSweep {

	public boolean openLocation = true; // find random open location if possible
	public boolean uniqueLocation = true; //find a unique location if possible
	public boolean toroidal = true; //if false, space is bounded: the only two possiblilities allowed
	protected int rule = 1; //default one-step random walk rule to zigzag for list
	public int walkRule = 193;//default, actual rule number for zigzag
	
	
	public RandomWalkerEnvironment(long seed, Class observer) { 
		super(seed, observer);
	}
	
	public int getRandomWalk(){	
		return rule;
	}
	
	public void setRandomWalk(int rule){
		this.rule = rule;
	}
	
	public Object domRandomWalk() 
	{ 
		return RandomWalkMechanics.classicRuleNames;
	}

	public boolean isOpenLocation() {
		return openLocation;
	}

	public void setOpenLocation(boolean openLocation) {
		this.openLocation = openLocation;
	}

	public boolean isUniqueLocation() {
		return uniqueLocation;
	}

	public void setUniqueLocation(boolean uniqueLocation) {
		this.uniqueLocation = uniqueLocation;
	}

	public boolean isToroidal() {
		return toroidal;
	}

	public void setToroidal(boolean toroidal) {
		this.toroidal = toroidal;
	}
	
	public void place(RandomWalker a){
		if(spaces == Spaces.OBJECT){
			while(!objectSpace.addAgentToLocation(a, a.x, a.y)){
				a.x = random.nextInt(gridWidth);
				a.y = random.nextInt(gridHeight);
				a.ox = a.x;
				a.oy = a.y;
			}
		}
		else if(spaces == Spaces.SPARSE){
			while(sparseSpace.getObjectsAtLocation(a.x, a.y) !=null){
				a.x = random.nextInt(gridWidth);
				a.y = random.nextInt(gridHeight);
			}
			sparseSpace.setObjectLocation(a, a.x, a.y);
		}
	}


	public void start(){
		super.start();
		
	}

}
