package sweep;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import org.jfree.data.xy.XYSeries;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Bag;
import sim.util.media.chart.HistogramGenerator;
import sim.util.media.chart.TimeSeriesChartGenerator;
import spaces.Spaces;

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

public class GUIStateSweep extends GUIState {
	public Display2D display;
	//Chart stuff begin
	public Display2D displayChartXY = null;//display chart XY
	public Display2D[] arrayDisplayChartXY = null;
	public Display2D displayChartH = null;
	public Display2D[] arragDisplayChartH = null;
	public JFrame displayFrame;
	public JFrame displayFrameChartXY = null;
	public JFrame displayFrameChartH = null;
	public JFrame[] arrayDisplayFrameChartXY = null;
	public JFrame[] arrayDisplayFrameChartH = null;
	public static TimeSeriesChartGenerator chartTimeSeries = null;
	public static TimeSeriesChartGenerator[] arrayChartTimeSeries = null;
	public static XYSeries series = null;
	public static XYSeries[] arraySeries = null;
	public static HistogramGenerator chartHistogram = null;
	public static HistogramGenerator[] arrayChartHistogram = null;
	public static int bins = 10; //for histogram charts
	public static int[] arrayBins = {10};//default
	public static String chartTitleXY = "";
	public static String[] arrayChartTitleXY = null;
	public static String xLabelXY = "";
	public static String[] arrayXLabelXY = null;
	public static String yLabelXY = "";
	public static String[] arrayYLabelXY = null;
	public static String chartTitleH = "";
	public static String[] arrayChartTitleH = null;
	public static String xLabelH = "";
	public static String yLabelH = "";
	public static String[] arrayXLabelH = null;
	public static String[] arrayYLabelH = null;
	//public static Charts chartType = Charts.NOCHART; //default no chart
	public static boolean chartTypeXY = false;
	public static boolean arrayChartTypeXY = false;
	public static boolean chartTypeH = false;
	public static boolean arrayChartTypeH = false;
	public static int numberOfTimeSeriesCharts = 2;
	public static int numberOfHCharts = 2;
	//Chart stuff end
	public static ObjectGridPortrayal2D agentsPortrayalObject;
	public static SparseGridPortrayal2D agentsPortrayalSparseGrid;
	public static ContinuousPortrayal2D agentsPortrayalContnuous;
	public SimStateSweep sweepState;
	public  int gridWidth = 400; //default value
	public  int gridHeight = 400;
	public  Color backdrop = Color.WHITE;
	public  Color agentDefaultColor = Color.RED;
	public  boolean agentPortrayal = true;
	public static Spaces spaces;
	public static Class theClass = null;


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
	 * @param simstatesweep  --> This is the class for the simulation state
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
	 * @param space   -->  Enter the space type: Spaces.OBJECT, Spaces.SPARSE, or Spaces.Continous.
	 */
	


	public static void initialize (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal, Spaces space){
		SimStateSweep s = null;
		theClass = theGUIsubClass;
		GUIStateSweep ex = null;
		long seed = System.currentTimeMillis();
		try {
			s = (SimStateSweep)(simstatesweep.getConstructor(long.class, Class.class).newInstance(seed,observer));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		spaces = space;
		if(s != null){
			switch(space){
			case OBJECT: agentsPortrayalObject = new ObjectGridPortrayal2D();
			break;
			case SPARSE: agentsPortrayalSparseGrid =  new SparseGridPortrayal2D();
			break;
			case CONTINUOUS: agentsPortrayalContnuous = new ContinuousPortrayal2D();
			}


			try {
				ex = (GUIStateSweep)(theGUIsubClass.getConstructor(SimStateSweep.class, int.class,int.class,Color.class,Color.class,boolean.class).newInstance(s, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			s.setGui(ex);
			Console c = new Console(ex);
			c.setVisible(true);
			System.out.println("Start Simulation");
		}
		else{
			System.out.println("Failed to create instance of SimStateSweep.");
		}
	}

	public static void initialize (Class simstatesweep, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal, Spaces space){
		SimStateSweep s = null;
		theClass = theGUIsubClass;
		long seed = System.currentTimeMillis();
		try {
			s = (SimStateSweep)(simstatesweep.getConstructor(long.class).newInstance(seed));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		spaces = space;
		if(s != null){
			switch(space){
			case OBJECT: agentsPortrayalObject = new ObjectGridPortrayal2D();
			break;
			case SPARSE: agentsPortrayalSparseGrid =  new SparseGridPortrayal2D();
			break;
			case CONTINUOUS: agentsPortrayalContnuous = new ContinuousPortrayal2D();
			}

			if(s != null ){
				switch(space){
				case OBJECT: agentsPortrayalObject = new ObjectGridPortrayal2D();
				break;
				case SPARSE: agentsPortrayalSparseGrid =  new SparseGridPortrayal2D();
				break;
				case CONTINUOUS: agentsPortrayalContnuous = new ContinuousPortrayal2D();
				}


				GUIStateSweep ex = null;
				try {
					ex = (GUIStateSweep)(theGUIsubClass.getConstructor(SimStateSweep.class, int.class,int.class,Color.class,Color.class,boolean.class).newInstance(s, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				s.setGui(ex);
				Console c = new Console(ex);
				c.setVisible(true);
				System.out.println("Start Simulation");
			}
			else{
				System.out.println("Failed to create instance of SimState.");
			}
		}

	}

	public static void initialize (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal, Spaces space, String runTimeFileName){
		SimStateSweep s = null;
		theClass = theGUIsubClass;
		GUIStateSweep ex = null;
		long seed = System.currentTimeMillis();
		try {
			s = (SimStateSweep)(simstatesweep.getConstructor(long.class, Class.class, String.class).newInstance(seed,observer,runTimeFileName));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		spaces = space;
		if(s != null){
			switch(space){
			case OBJECT: agentsPortrayalObject = new ObjectGridPortrayal2D();
			break;
			case SPARSE: agentsPortrayalSparseGrid =  new SparseGridPortrayal2D();
			break;
			case CONTINUOUS: agentsPortrayalContnuous = new ContinuousPortrayal2D();
			}


			try {
				ex = (GUIStateSweep)(theGUIsubClass.getConstructor(SimStateSweep.class, int.class,int.class,Color.class,Color.class,boolean.class).newInstance(s, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			Console c = new Console(ex);
			s.setGui(ex);
			c.setVisible(true);
			System.out.println("Start Simulation");
		}
		else{
			System.out.println("Failed to create instance of SimStateSweep.");
		}
	}

	public static Object getInfo()
	{
		java.net.URL url = theClass.getResource("index.html");
		if (url == null)
			return "<html><head><title></title></head><body bgcolor=\"white\"></body></html>";
		else return url;
	}

	/**
	 * The initialize static method allows you to create a basic user
	 * interface by supplying the appropriate parameters below.
	 * @param simstatesweep  --> This is the class for the simulation state
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
	 * @param space   -->  String name for the space Class used.  For a SparseGrid2, enter "sparseSpace"
	 *                     or "SparseGrid2D", for an ObjectGrid2D, enter "objectSpace" or "ObjectGrid2D" and for
	 *                     a Continous2D space, enter "continousSpace" or "Continous2D"
	 */

	public static void initialize (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal, String space){
		switch(space) {
		case "sparseSpace":
		case "SparseGrid2D":
			spaces = Spaces.SPARSE;
			break;
		case "objectSpace":
		case "ObjectGrid2D":
			spaces = Spaces.OBJECT;
			break;
		case "continousSpace":
		case "Continous2D":
			spaces = Spaces.CONTINUOUS;
			break;
		default: spaces = null;
		}
		initialize (simstatesweep,observer,theGUIsubClass, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal, spaces);
	}

	public static void initializeSparseGrid2D (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal){
		initialize (simstatesweep,observer,theGUIsubClass, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal, Spaces.SPARSE);
	}

	public static void initializeObjectGrid2D (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal){
		initialize (simstatesweep,observer,theGUIsubClass, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal, Spaces.OBJECT);
	}

	public static void initializeContinuous2D (Class simstatesweep,Class observer, Class theGUIsubClass, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal){
		initialize (simstatesweep,observer,theGUIsubClass, gridWidth, gridHeight, backdrop, agentDefaultColor,agentPortrayal, Spaces.CONTINUOUS);
	}

	public static void initializeTimeSeriesChart( String chart_Title, String x_Label, String y_Label) {
		chartTypeXY =true;
		chartTitleXY = chart_Title;
		xLabelXY = x_Label;
		yLabelXY = y_Label;
	}

	/**
	 * Labels for charts are given in arrays.  For example, for a chart_Title: {"chart1", "chart2", "chart3"}
	 * @param number
	 * @param chart_Title
	 * @param x_Label
	 * @param y_Label
	 */
	public static void initializeArrayTimeSeriesChart(int number, String[] chart_Title, String[] x_Label, String[] y_Label) {
		numberOfTimeSeriesCharts = number;
		arrayChartTypeXY =true;
		arrayChartTitleXY = chart_Title;
		arrayXLabelXY = x_Label;
		arrayYLabelXY = y_Label;
	}

	public static void initializeHistogramChart( String chart_Title, String x_Label, String y_Label, int bins_) {
		chartTypeH = true;
		chartTitleH = chart_Title;
		xLabelH = x_Label;
		yLabelH = y_Label;
		bins = bins_; 
	}
	
	public static void initializeArrayHistogramChart(int number, String[] chart_Title, String[] x_Label, String[] y_Label, int[] bins) {
		numberOfHCharts = number;
		arrayChartTypeH =true;
		arrayChartTitleH = chart_Title;
		arrayXLabelH = x_Label;
		arrayYLabelH = y_Label;
		arrayBins = bins;
	}


	public GUIStateSweep(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state);
		sweepState = state;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.backdrop = backdrop;
		this.agentDefaultColor = agentDefaultColor;
		this.agentPortrayal = agentPortrayal;
	}


	public GUIStateSweep(SimStateSweep state) {
		super(state);
		sweepState = state;
		// TODO Auto-generated constructor stub
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
		/*chartTimeSeries.removeAllSeries();
		String uniqueChartId = chartTimeSeries.getTitle();
		series = new XYSeries( uniqueChartId, false );
		chartTimeSeries.addSeries(series,null);*/

	}

	private void setUpChart() {
		String uniqueChartId;
		if(chartTypeXY) {
			chartTimeSeries.removeAllSeries();
			uniqueChartId = chartTimeSeries.getTitle();
			series = new XYSeries( uniqueChartId, false );
			chartTimeSeries.addSeries(series,null);
		}
		if(chartTypeH) {
			chartHistogram.removeAllSeries();
			uniqueChartId = chartHistogram.getTitle();
			double[] data = new double[bins];
			chartHistogram.addSeries(data, bins, uniqueChartId, null);
		}
		if(arrayChartTypeXY) {
			arraySeries = new XYSeries[numberOfTimeSeriesCharts];
			for(int i = 0; i< numberOfTimeSeriesCharts;i++) {
				arrayChartTimeSeries[i].removeAllSeries();
				uniqueChartId =arrayChartTimeSeries[i].getTitle();
				arraySeries[i]=new XYSeries( uniqueChartId, false );
				arrayChartTimeSeries[i].addSeries(arraySeries[i],null);
			}
		}
		if(arrayChartTypeH) {
			for(int i = 0; i< numberOfHCharts;i++) {
				arrayChartHistogram[i].removeAllSeries();
				uniqueChartId =arrayChartHistogram[i].getTitle();
				double[] data =new double[arrayBins[i]];
				arrayChartHistogram[i].addSeries(data, bins, uniqueChartId, null);
			}
		}
	}

	public void load(SimState state) {
		super.load(state); // load the simulation into the interface
		setupPortrayals(); // call setuPortrayals
	}


	/*
	 * This is the one method we will change the most for each
.    *  simulation.  Specifically, we determine what agents 
	 * look like.  
	 */ 

	public void setupPortrayals() {
		setUpChart();
		switch(spaces){
		case OBJECT:
			agentsPortrayalObject.setField(sweepState.objectSpace);  /** <---- Change to your extended SimState class   */
			/** Assumes your SparseGid2D space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				Bag agents = sweepState.objectSpace.getAllObjects();
				for(int i=0;i<agents.numObjs;i++)
					agentsPortrayalObject.setPortrayalForObject(agents.objs[i], o);  //sets all the agents to red by default

			}
			break;
		case SPARSE:
			if(sweepState.sparseSpace == null) System.out.println("Space is NULL");
			agentsPortrayalSparseGrid.setField(sweepState.sparseSpace);  /** <---- Change to your extended SimState class   */
			/** Assumes your SparseGid2D space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				agentsPortrayalSparseGrid.setPortrayalForAll(o);  //sets all the agents to red by default
			}
			break;
		case CONTINUOUS:
			agentsPortrayalContnuous.setField(sweepState.continuousSpace);  /** <---- Change to your extended SimState class   */
			/** Assumes your continuousSpace space is named "space" */

			if(agentPortrayal){
				OvalPortrayal2D o = new OvalPortrayal2D(agentDefaultColor);  
				agentsPortrayalContnuous.setPortrayalForAll(o);  //sets all the agents to red by default
			}
		}



		// This complicated statement makes an oval portrayal of
		// a given color
		display.reset(); 
		// call the predefined reset method for the display
		display.repaint(); // call the repaint method


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
		switch(spaces){
		case OBJECT: //ToDo;
			break;
		case SPARSE: agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		break;
		case CONTINUOUS: agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		break;
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
		switch(spaces){
		case OBJECT: //ToDo;
			break;
		case SPARSE: agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		break;
		case CONTINUOUS: agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		break;
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
		switch(spaces){
		case OBJECT: //ToDo;
			break;
		case SPARSE: agentsPortrayalSparseGrid.setPortrayalForObject(obj, o);
		break;
		case CONTINUOUS: agentsPortrayalContnuous.setPortrayalForObject(obj, o);
		break;
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
		initChart(c);
		display = new Display2D(gridWidth,gridHeight,this); // make the displa
		displayFrame = display.createFrame(); // create the frame
		c.registerFrame(displayFrame);   // let the controller control the
		//frame
		displayFrame.setVisible(true);  // make it visible
		display.setBackdrop(backdrop); // make the background white
		switch(spaces){
		case OBJECT: display.attach(agentsPortrayalObject,"Agent");
		break;
		case SPARSE: display.attach(agentsPortrayalSparseGrid,"Agent");
		break;
		case CONTINUOUS: display.attach(agentsPortrayalContnuous,"Agent");
		}


		// attach the display
	}
	private void setMetadata(final TimeSeriesChartGenerator chart, final String title, final String xLabel, final String yLabel) {
		chart.setTitle( title );
		chart.setXAxisLabel(xLabel);
		chart.setYAxisLabel(yLabel);

	}

	private void setMetadata(final HistogramGenerator chart, final String title, final String xLabel, final String yLabel) {
		chart.setTitle( title );
		chart.setXAxisLabel(xLabel);
		chart.setYAxisLabel(yLabel);

	}

	private void initChart(Controller c) {
		if(chartTypeXY) {
			displayChartXY = new Display2D(200,200,this);	
			chartTimeSeries = new TimeSeriesChartGenerator();
			setMetadata( chartTimeSeries, chartTitleXY, xLabelXY, yLabelXY );
			displayFrameChartXY = chartTimeSeries.createFrame();
			c.registerFrame(displayFrameChartXY);   // let the controller control the
			displayFrameChartXY.setVisible(true); 
		}
		if(chartTypeH) {
			displayChartH = new Display2D(200,200,this);	
			chartHistogram = new HistogramGenerator();
			setMetadata( chartHistogram, chartTitleH, xLabelH, yLabelH );
			displayFrameChartH = chartHistogram.createFrame();
			c.registerFrame(displayFrameChartH);   // let the controller control the
			displayFrameChartH.setVisible(true); 
		}
		if(arrayChartTypeXY) {
			arrayDisplayChartXY = new Display2D[numberOfTimeSeriesCharts];
			arrayChartTimeSeries = new TimeSeriesChartGenerator[numberOfTimeSeriesCharts];
			arrayDisplayFrameChartXY = new  JFrame[numberOfTimeSeriesCharts];
			for(int i=0;i<this.numberOfTimeSeriesCharts;i++) {
				arrayDisplayChartXY[i]=new Display2D(200,200,this);	
				arrayChartTimeSeries[i]= new TimeSeriesChartGenerator();
				setMetadata(arrayChartTimeSeries[i],arrayChartTitleXY[i],arrayXLabelXY[i], arrayYLabelXY[i]);
				arrayDisplayFrameChartXY[i] = arrayChartTimeSeries[i].createFrame();
				c.registerFrame(arrayDisplayFrameChartXY[i]);
				arrayDisplayFrameChartXY[i].setVisible(true);
			}
		}
		if(arrayChartTypeH) {
			arragDisplayChartH = new Display2D[numberOfHCharts];	
			arrayChartHistogram = new HistogramGenerator[numberOfHCharts] ;
			arrayDisplayFrameChartH = new JFrame[numberOfHCharts];
			for(int i=0;i<this.numberOfHCharts;i++) {
				arragDisplayChartH[i]=new Display2D(200,200,this);	
				arrayChartHistogram[i]= new HistogramGenerator();
				setMetadata(arrayChartHistogram[i],arrayChartTitleH[i],arrayXLabelH[i], arrayYLabelH[i]);
				arrayDisplayFrameChartH[i] = arrayChartHistogram[i].createFrame();
				c.registerFrame(arrayDisplayFrameChartH[i]);
				arrayDisplayFrameChartH[i].setVisible(true);
			}
		}
	}

	public Object getSimulationInspectedObject() {
		return state; // This returns the simulation
	}


}
