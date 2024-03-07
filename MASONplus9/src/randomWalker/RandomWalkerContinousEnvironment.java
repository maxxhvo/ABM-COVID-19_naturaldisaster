package randomWalker;

import sim.util.Double2D;
import spaces.Spaces;
import sweep.SimStateSweep;

@SuppressWarnings("serial")
public class RandomWalkerContinousEnvironment extends SimStateSweep {

	
	public boolean uniqueLocation = false; //find a unique location if possible
	public boolean toroidal = true; //if false, space is bounded: the only two possiblilities allowed
	public double stepSize = 1;
	public double discretation = 1;
	public boolean gaussian = false;
	public double rotation = 1; //north
	public double gaussanStandardDeviation = 1;
	
	
	public RandomWalkerContinousEnvironment(long seed, Class observer) {
		super(seed, observer);
	}
	
	
	public double getDiscretation() {
		return discretation;
	}


	public void setDiscretation(double discretation) {
		this.discretation = discretation;
	}


	public boolean isGaussian() {
		return gaussian;
	}


	public void setGaussian(boolean gaussian) {
		this.gaussian = gaussian;
	}


	public double getRotation() {
		return rotation;
	}


	public void setRotation(double rotation) {
		this.rotation = rotation;
	}


	public double getGaussanStandardDeviation() {
		return gaussanStandardDeviation;
	}


	public void setGaussanStandardDeviation(double gaussanStandardDeviation) {
		this.gaussanStandardDeviation = gaussanStandardDeviation;
	}


	public double getStepSize() {
		return stepSize;
	}


	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
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
	
	public void place(RandomWalkerContinuous a){
		Double2D location = new Double2D(a.x,a.y);
		while(this.continuousSpace.getObjectsAtLocation(location) !=null){
			a.x = random.nextInt(gridWidth);
			a.y = random.nextInt(gridHeight);
			location = new Double2D(a.x,a.y);
		}
		continuousSpace.setObjectLocation(a, location);
	}


	public void start(){
		super.start();
	}

}
