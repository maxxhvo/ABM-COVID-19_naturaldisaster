package aggregator;

import randomWalker.RandomWalkerEnvironment;

/**
 * A basic environment used only to implement the BasicAgent class and should be 
 * inherited when inheriting BasicAgent.  A SparseGrid2D space is already defined
 * so in a subclass of BasicEnvironment all that is needed are the variables for
 * the new subclass and needed setters and getters.
 *  
 * @author Jeff Schank 2017
 *
 */

public class AggregatorEnvironment extends RandomWalkerEnvironment{
	public boolean aggregate = true;
	public boolean hyperAgg = false;
	public int searchRadius =1;
	public int degree = 1;

	
	public AggregatorEnvironment(long seed, Class observer) {
		super(seed, observer);
		
	}

	public boolean isAggregate() {
		return aggregate;
	}


	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}


	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public boolean isHyperAgg() {
		return hyperAgg;
	}


	public void setHyperAgg(boolean hyperAgg) {
		this.hyperAgg = hyperAgg;
	}

	public int getSearchRadius() {
		return searchRadius;
	}


	public void setSearchRadius(int searchRadius) {
		if(searchRadius > 0 && searchRadius <= gridWidth && searchRadius <= gridHeight)
			this.searchRadius = searchRadius;
	}
	
	public void start(){
		super.start();

	}
}
