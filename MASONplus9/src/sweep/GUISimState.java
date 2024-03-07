package sweep;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;


/**
 * The SweepGUI class extends GUIState in MASON and allows you to
 * easily create a basic graphical user interface for SimStateSweep
 * Simulation state.  If you are using Eclipse, create your class using
 * SweepGUI as the parent class.  You can use Eclipse to generate creator
 * method such as the one below:
 * 
 * <dl>
 * <dt>	public AgentsWithGUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
 *			boolean agentPortrayal) {</dt>
 *		<dd>super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);</dd>
 *	}
 * </dl>
 * 
 * Next write a main method that uses initialize as illustrated below:
 * 	
 * 
 *  <dl>
 *  <dt>public static void main(String[] args) {</dt>
 *		<dd>initialize (Environment.class,Observer.class, 400, 400, Color.WHITE, Color.BLUE,true,true);</dd>
 *	}
 *</dl>
 *
 * See initialize for additional explanation of the parameters.
 * @author jcschank
 *
 */

public class GUISimState extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	public static ObjectGridPortrayal2D agentsPortrayalObject = null;
	public static SparseGridPortrayal2D agentsPortrayalSparseGrid = null;
	public static ContinuousPortrayal2D agentsPortrayalContnuous = null;
	public  int gridWidth = 400; //default value
	public  int gridHeight = 400;
	public  Color backdrop = Color.WHITE;
	public  Color agentDefaultColor = Color.RED;
	public  boolean agentPortrayal = true;
	public static Class theClass = null;
	public  String spaceName = null;
	public static GUIState  gui = null;


	/*When extending this class for your simulation, you should include a main method like
	 * the one below, which uses reflection to create a simulation with a graphical user
	 * interface.
	 */

	/*public static void main(String[] args) {

	  For example use 
	  initialize (Environment.class,Observer.class, 400, 400, Color.WHITE, Color.BLUE,true);
	}*/

	/**
	 * The initialize static method allows you to create a basic user
	 * interface by supplying the appropriate parameters below.
	 * @param simstate  --> This is the class for the simulation state
	 *                           you created by extending SimStateSweep, for
	 *                           example, you would pass MySimStateSweep.class
	 * @param observer   --> This is the class for the observer or experimenter
	 *                       you created by extending Observer, for example,
	 *                       you would pass MyObserver.class or Experimenter.class
	 *@param theGUIsubClass -->This is the subclass of GUIState used in a simulation
	 * @param gridWidth  --> The width of the display window space.  The default is 400.
	 * @param gridHeight  --> The height of the display window space.  The default is 400.
	 * @param backdrop   -->  The color of the background of the window space. This
	 *                        parameter is of type Color.  For example, enter Color.WHITE
	 *                        for a white backdrop or Color.BLACK for a black backdrop.
	 * @param agentDefaultColor  --> If you do not have a portrayal for your agents, you can
	 *                               create a colored oval portrayal for all of them.  This
	 *                               parameter is a Color parameters.  For example, all blue
	 *                               agents would be created by Color.BLUE.
	 * @param agentPortrayal  --> If you want to use default portrayals with the color specified
	 *                            in the previous parameter, set to true, else false if you want
	 *                            to use your own portrayals.
	 * @param objspace   -->  If true, you are using a ObjectGrid2Dex space which is an extended
	 *                        class of ObjectGrid2D.  Make sure that the simulation state you are
	 *                        using also creates an ObjectGrid2Dex.
	 */

	public static SimState initialize (Class simstate, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal, String spaceName){
		SimState s = null;
		theClass = theGUIsubClass;
		long seed = System.currentTimeMillis();
		try {
			s = (SimState)(simstate.getConstructor(long.class).newInstance(seed));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	
		Class oC = null;
		try {
			Class c = s.getClass();
			Field f = c.getField(spaceName);
			oC=f.getType();
		}	catch (Throwable e) {
			System.err.println("Error in get object: " + e);
		}
		
		if(oC == null) System.out.println("o = null");
		if(s != null && oC != null){
			Class key = oC;
			if(key.equals(ObjectGrid2D.class)) agentsPortrayalObject = new ObjectGridPortrayal2D();
			else if(key.equals(SparseGrid2D.class))agentsPortrayalSparseGrid =  new SparseGridPortrayal2D();
			else if (key.equals(Continuous2D.class)) agentsPortrayalContnuous = new ContinuousPortrayal2D();
			else System.out.println("Failed to create Portrayal for space");

			GUISimState ex = null;
			try {
				ex = (GUISimState)(theGUIsubClass.getConstructor(SimState.class,String.class,int.class,int.class,Color.class,Color.class,boolean.class).newInstance(s,spaceName, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			gui = ex;
			Console c = new Console(ex);
			c.setVisible(true);
			ex.spaceName = spaceName;
			System.out.println("Start Simulation");
		}
		else{
			System.out.println("Failed to create instance of SimState.");
		}
		return s;
	}

	public static Object getInfo()
	{
		java.net.URL url = theClass.getResource("index.html");
		if (url == null)
			return "<html><head><title></title></head><body bgcolor=\"white\"></body></html>";
		else return url;
	}


	public GUISimState(SimState state, String spaceName, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state);
		this.state = state;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.backdrop = backdrop;
		this.agentDefaultColor = agentDefaultColor;
		this.agentPortrayal = agentPortrayal;
	}


	public GUISimState(SimState state) {
		super(state);
	}

	public static String getName() { 
		return "Uses SimSweep"; 
		// return a name for what this simulation is about
	}

	public void quit() {
		super.quit(); // Use the already defined quit method

		if (displayFrame!=null) displayFrame.dispose(); 
		displayFrame = null;  // when quiting get rid of the display
		display = null;       
	}

	public void start() {
		super.start(); // use the predefined start method
		setupPortrayals(); // add setupPortrayals method below
	}


	public void load(SimState state) {
		super.load(state); // load the simulation into the interface
		setupPortrayals(); // call setuPortrayals
	}

	public final static Object getObject(Object o, String prameterName){
		Object obj = null;
		try {
			Class c = o.getClass();
			Field f = c.getField(prameterName);
			obj = f.get(o);
		}	catch (Throwable e) {
			System.err.println("Error in get object: " + e);
		}
		return obj;
	}

	/*
	 * This is the one method we will change the most for each
.    *  simulation.  Specifically, we determine what agents 
	 * look like.  
	 */ 

	public void setupPortrayals() {
		Object obj = getObject(state,spaceName);
		if(agentsPortrayalObject != null) {

			ObjectGrid2D so = (ObjectGrid2D)obj;
			agentsPortrayalObject.setField(so);  /** <---- Change to your extended SimState class   */
			/** Assumes your SparseGid2D space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				agentsPortrayalObject.setPortrayalForAll(o);
				display.reset();
				display.repaint(); // call the repaint method

			}
			return;
		}
		if(agentsPortrayalSparseGrid != null) {
			SparseGrid2D sg = (SparseGrid2D)obj;
			agentsPortrayalSparseGrid.setField(sg);  /** <---- Change to your extended SimState class   */
			/** Assumes your SparseGid2D space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				agentsPortrayalSparseGrid.setPortrayalForAll(o);  //sets all the agents to red by default
			}

			display.reset();
			display.repaint(); // call the repaint method
			return;
		}

		if(agentsPortrayalContnuous != null) {
			Continuous2D sc = (Continuous2D)obj;
			agentsPortrayalContnuous.setField(sc);  /** <---- Change to your extended SimState class   */
			/** Assumes your continuousSpace space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				agentsPortrayalContnuous.setPortrayalForAll(o);  //sets all the agents to red by default
			}

			display.reset();
			display.repaint(); // call the repaint method
			return;
		}

	}


	/**
	 * Creates an oval portarayal 2D and color and opacity are set by the parameters below.
	 * @param (Object) obj
	 * @param (float) red 
	 * @param (float) green
	 * @param (float) blue
	 * @param (float) opacity
	 */
	public void setOvalPortrayal2DColor(Object obj,float red, float green, float blue, float opacity){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c);
		if( agentsPortrayalObject != null) { agentsPortrayalObject.setPortrayalForObject(obj, o);
		return;
		}
		if(agentsPortrayalSparseGrid != null) {agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		return;
		}
	
		if(agentsPortrayalContnuous !=null) { agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		return;
		}
	}	

	/**
	 * creates an oval portarayal 2D and color and opacity are set by the parameters below.
	 * @param (Object) obj
	 * @param (float)red
	 * @param (float)green
	 * @param (float)blue
	 * @param (float)opacity
	 * @param (boolean) filled
	 */
	public void setOvalPortrayal2DColor(Object obj, float red, float green, float blue, float opacity, boolean filled){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c, filled);
		if( agentsPortrayalObject != null) { agentsPortrayalObject.setPortrayalForObject(obj, o);
		return;
		}
		if(agentsPortrayalSparseGrid != null) {agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		return;
		}
	
		if(agentsPortrayalContnuous !=null) { agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		return;
		}
	}

	/**
	 * * creates an oval portarayal 2D and color and opacity are set by the parameters below.
	 * @param (Object) obj
	 * @param (float)red
	 * @param (float)green
	 * @param (float)blue
	 * @param (float)opacity
	 * @param (boolean)filled
	 * @param (double)scale
	 */
	public void setOvalPortrayal2DColor(Object obj, float red, float green, float blue, float opacity, boolean filled, double scale){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c, scale, filled);
		if( agentsPortrayalObject != null) { agentsPortrayalObject.setPortrayalForObject(obj, o);
		return;
		}
		if(agentsPortrayalSparseGrid != null) {agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		return;
		}
	
		if(agentsPortrayalContnuous !=null) { agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		return;
		}
	}

	/**
	 * Creates an oval portarayal 2D and color and opacity are set by the parameters below.
	 * @param (Object) obj
	 * @param (float) red 
	 * @param (float) green
	 * @param (float) blue
	 * @param (float) opacity
	 */

	public void init(Controller c){
		super.init(c);  // use the predefined method to initialize the
		// controller, c
		display = new Display2D(gridWidth,gridHeight,this); // make the display
		// that is 800 x 400 pixels.  You can set it to other values
		displayFrame = display.createFrame(); // create the frame
		c.registerFrame(displayFrame);   // let the controller control the
		//frame
		displayFrame.setVisible(true);  // make it visible
		display.setBackdrop(backdrop); // make the background white
		if( agentsPortrayalObject != null) { display.attach(agentsPortrayalObject,"Agent");
		return;
		}
		if(agentsPortrayalSparseGrid != null) {display.attach(agentsPortrayalSparseGrid,"Agent");
		return;
		}
	
		if(agentsPortrayalContnuous !=null) { display.attach(agentsPortrayalContnuous,"Agent");
		return;
		}
		// attach the display
	}

	public Object getSimulationInspectedObject() {
		return state; // This returns the simulation
	}
}
