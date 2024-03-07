package replicator;

import aggregator.AggregatorEnvironment;

/*
 * Semiasbstract environment for generic biological agents that can reproduce
 */

public class ReplicatorEnvironment extends AggregatorEnvironment {
	public int maxAgents = 1000;
	public int reproductionRadius = 1;
	public double reproductionResources = 100; //resources required for reproduction
	public double maxResources = 150; //the maximum resources an agent can carry
	public int averagelifeSpan = 150;
	public int lifeSpanSD = 10;
	public double probabilityOfDying = 0.0;
	public double parentalInvestment = 0.5;
	public boolean reproduceUniqueLocation = true;
	public boolean carryingCapacity = true;

	public int getMaxAgents() {
		return maxAgents;
	}

	public void setMaxAgents(int maxAgents) {
		this.maxAgents = maxAgents;
	}

	public int getReproductionRadius() {
		return reproductionRadius;
	}

	public void setReproductionRadius(int reproductionRadius) {
		this.reproductionRadius = reproductionRadius;
	}

	public double getReproductionResources() {
		return reproductionResources;
	}

	public void setReproductionResources(double reproductionResources) {
		this.reproductionResources = reproductionResources;
	}


	public double getMaxResources() {
		return maxResources;
	}

	public void setMaxResources(double maxResources) {
		this.maxResources = maxResources;
	}

	public int getAveragelifeSpan() {
		return averagelifeSpan;
	}

	public void setAveragelifeSpan(int averagelifeSpan) {
		this.averagelifeSpan = averagelifeSpan;
	}

	public int getLifeSpanSD() {
		return lifeSpanSD;
	}

	public void setLifeSpanSD(int lifeSpanSD) {
		this.lifeSpanSD = lifeSpanSD;
	}

	public double getProbabilityOfDying() {
		return probabilityOfDying;
	}

	public void setProbabilityOfDying(double randomDeathProb) {
		this.probabilityOfDying = randomDeathProb;
	}

	public double getParentalInvestment() {
		return parentalInvestment;
	}

	public void setParentalInvestment(double parentalInvestment) {
		this.parentalInvestment = parentalInvestment;
	}

	public boolean isReproduceUniqueLocation() {
		return reproduceUniqueLocation;
	}

	public void setReproduceUniqueLocation(boolean reproduceUniqueLocation) {
		this.reproduceUniqueLocation = reproduceUniqueLocation;
	}

	public boolean isCarryingCapacity() {
		return carryingCapacity;
	}

	public void setCarryingCapacity(boolean carryingCapacity) {
		this.carryingCapacity = carryingCapacity;
	}

	public ReplicatorEnvironment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public void start(){
		super.start();
	}
}
