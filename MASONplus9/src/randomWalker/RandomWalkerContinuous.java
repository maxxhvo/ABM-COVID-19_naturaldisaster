package randomWalker;

import java.awt.Color;
import sim.engine.SimState;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Double2D;
import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;


public class RandomWalkerContinuous extends RandomWalkerContinuousAbstract {
	Spaces spaces;
	
	
	public RandomWalkerContinuous(SimState state, double x, double y, double stepSize,Spaces spaces, boolean bounded) {
		this.x = x;
		this.y = y;
		this.stepSize = stepSize;
		this.spaces = spaces;
		double randomAngle = state.random.nextDouble() * ((state.random.nextBoolean())?PI:-PI); //360 random angle
		xdir = Math.cos(randomAngle) * stepSize;
		ydir = Math.sin(randomAngle) * stepSize;
		this.bounded = bounded;
	}

	public void setColor(SimStateSweep state, float red, float green, float blue, float opacity){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c);
		GUIStateSweep guiState = (GUIStateSweep)state.gui;
		switch(spaces){
		case OBJECT: guiState.agentsPortrayalObject.setPortrayalForObject(this, o);
		break;
		case SPARSE: guiState.agentsPortrayalSparseGrid.setPortrayalForObject(this, o);
		break;
		case CONTINUOUS: guiState.agentsPortrayalContnuous.setPortrayalForObject(this, o);
		}

	}	
	
	/**
	 * Agents take a uniform random step in continuous 2D space.  The size of the step is determined by stepSize.
	 * StepSize could be made variable.  A random angle is generated and the direction vectors xdir and ydir are
	 * found by taking the cos and sin of the angle repsectively.  It is assumed that the agent is moving on a torus
	 * and the x and y coordinates are corrected for a toroidal space.
	 * @param state
	 * @return
	 */
	
	public void randomOrientedUniformStep(SimState state, double rotation){
		final double randomAngle = (state.random.nextBoolean()) ? state.random.nextDouble() * PI * rotation: -state.random.nextDouble() * PI * rotation; //a function of the sd and rotation
		final double currentAngle = getAngle(xdir,ydir);
		final double newAngle = currentAngle + randomAngle;
		xdir = Math.cos(newAngle) * stepSize;
		ydir = Math.sin(newAngle) * stepSize;
	}
	
	public double getAngle(final double xdir, final double ydir) {
		final double h = Math.sqrt(xdir*xdir + ydir*ydir);
		final double xcos = xdir/h;
		return (ydir > 0) ? Math.acos(xcos) :-Math.acos(xcos); //don't need to compute ycos
	}
	
	/**
	 * Agents take a Guassian random step in continuous 2D space.  The size of the step is determined by stepSize.
	 * StepSize could be made variable.  A random angle is generated from the rotation provided and the standard
	 * deviation (sd) for the Gaussian distribution and the direction vectors xdir and ydir are again
	 * found by taking the cos and sin of the angle respectively.  It is assumed that the agent is moving on a torus
	 * and the x and y coordinates are corrected for a toroidal space.
	 * @param state
	 * @param sd
	 * @param rotation
	 * @return
	 */
	
	public void randomOrientedGaussianStep(SimState state, double sd, double rotation){
		final double randomAngle = state.random.nextGaussian() * sd * rotation; //a function of the sd and rotation
		final double currentAngle = getAngle(xdir,ydir);
		final double newAngle = currentAngle + randomAngle;
		xdir = Math.cos(newAngle) * stepSize;
		ydir = Math.sin(newAngle) * stepSize;
	}
	
	public void step(SimState state) {
		RandomWalkerContinousEnvironment s = (RandomWalkerContinousEnvironment) state;
		Double2D newLocation;
		if(s.gaussian) {
			randomOrientedGaussianStep(state,s.gaussanStandardDeviation,s.rotation);
		}
		else {
			randomOrientedUniformStep(state, s.rotation);
		}
		
		x=  s.continuousSpace.stx(x+xdir);
		y = s.continuousSpace.sty(y+ydir);
		newLocation = new Double2D(x,y);
		s.continuousSpace.setObjectLocation(this, newLocation);
		
	}

}
