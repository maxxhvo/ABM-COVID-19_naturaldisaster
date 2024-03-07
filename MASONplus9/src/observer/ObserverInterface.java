package observer;

import sim.engine.Steppable;

public interface ObserverInterface extends Steppable {

	public boolean reset();
	
	public boolean save (int sweepNumber);
	
	public boolean nextStepCount();
	
}
