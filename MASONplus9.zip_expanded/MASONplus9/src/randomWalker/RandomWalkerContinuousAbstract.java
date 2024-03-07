package randomWalker;

import java.awt.Color;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.simple.OvalPortrayal2D;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;


public abstract class RandomWalkerContinuousAbstract implements Steppable {
	public final static double PI = Math.PI;
	public double x;
	public double y;
	public double xdir;
	public double ydir;
	public double stepSize;
	public boolean bounded;
	
	/**
	 * Given the current directions of movement xdir and ydir, this method calculates and returns the angle
	 * of orientation for xdir and ydir.  First the hypotenuse is calculated and then the cosine of the xdir.  From
	 * the cosine, the acosine can be used to find the angle.  However the appropriate angle depends on the sin of ydir.
	 * We do not have to calculate the sin(ydir), instead determine whether ydir > 0.  
	 * @param xdir
	 * @param ydir
	 * @return
	 */

	public double getAngle(final double xdir, final double ydir) {
		final double h = Math.sqrt(xdir*xdir + ydir*ydir);
		final double xcos = xdir/h;
		return (ydir >= 0) ? Math.acos(xcos) :-Math.acos(xcos); //don't need to compute ycos
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
	
	//colors


		public void setColor(SimStateSweep state, float red, float green, float blue, float opacity){
			Color c = new Color(red,green,blue,opacity);
			OvalPortrayal2D o = new OvalPortrayal2D(c);
			GUIStateSweep guiState = (GUIStateSweep)state.gui;
			switch(state.spaces) {
			case SPARSE:
				guiState.agentsPortrayalSparseGrid.setPortrayalForObject(this, o);
				break;
			case CONTINUOUS:
				guiState.agentsPortrayalContnuous.setPortrayalForObject(this, o);
				break;
			case OBJECT:
				guiState.agentsPortrayalObject.setPortrayalForObject(this, o);
			}
			
			

		}	
	
	public void step(SimState state) {

		
	}

}
