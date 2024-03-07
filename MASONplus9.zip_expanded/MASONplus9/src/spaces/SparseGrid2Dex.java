package spaces;

import java.util.ArrayList;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public class SparseGrid2Dex extends SparseGrid2D {
	public IntBag xPos = new IntBag();
	public IntBag yPos = new IntBag();

	public SparseGrid2Dex(int width, int height) {
		super(width, height);
	}
	
	public Int2D randomLocation(SimState state,final int x, final int y, final int mode, final int searchRadius, boolean includeOrigin){
		xPos.clear();
		yPos.clear();
		getMooreLocations(x, y, searchRadius, mode, includeOrigin, xPos, yPos);
		final int k = state.random.nextInt(xPos.numObjs);
		return new Int2D(xPos.objs[k],yPos.objs[k]);
	}
	
	public ArrayList<Int2D> getAllEmptyLocations(){
		ArrayList<Int2D> list = new ArrayList<Int2D>(1);
		boolean hit = false;
		for(int i = 0;i<width;i++)
			for(int j=0;j<height;j++){
				if(getObjectsAtLocation(i,j) == null){
					list.add(new Int2D(i,j));
					hit = true;
				}
			}
		if(hit)
			return list;
		else
			return null;
	}
	
	
	public Int2D randomEmptyLocation(SimState state,final int x, final int y, final int mode, final int searchRadius, boolean includeOrigin){
		Int2D location = null;
		xPos.clear();
		yPos.clear();
		getMooreLocations(x, y, searchRadius, mode, includeOrigin, xPos, yPos);
		if(xPos.numObjs == 0){
			return null; //no locations
		}
		
		final int startx = state.random.nextInt(xPos.numObjs);
		for(int i = startx;i< xPos.numObjs;i++){
			Bag b = getObjectsAtLocation(xPos.objs[i],yPos.objs[i]);
			if(b == null){
				location = new Int2D(xPos.objs[i],yPos.objs[i]);
				break;
			}
		}
		if(location == null){//try again
			for(int i = 0;i<startx;i++){
				Bag b = getObjectsAtLocation(xPos.objs[i],yPos.objs[i]);
				if(b == null){
					location = new Int2D(xPos.objs[i],yPos.objs[i]);
					break;
				}
			}
		}

		return location;
	}
	
	public void getMooreEmptyLocations( final int x, final int y, final int dist, int mode, boolean includeOrigin)
	{
		boolean toroidal = (mode == TOROIDAL);
		boolean bounded = (mode == BOUNDED);

		if (mode != BOUNDED && mode != UNBOUNDED && mode != TOROIDAL)
		{
			throw new RuntimeException("Mode must be either Grid2D.BOUNDED, Grid2D.UNBOUNDED, or Grid2D.TOROIDAL");
		}

		// won't work for negative distances
		if( dist < 0 )
		{
			throw new RuntimeException( "Distance must be positive" );
		}

		if( xPos == null || yPos == null )
		{
			throw new RuntimeException( "xPos and yPos should not be null" );
		}

		if( ( x < 0 || x >= width || y < 0 || y >= height ) && !bounded)
			throw new RuntimeException( "Invalid initial position" );

		xPos.clear();
		yPos.clear();

		// local variables are faster
		final int height = this.height;
		final int width = this.width;


		// for toroidal environments the code will be different because of wrapping arround
		if( toroidal )
		{
			// compute xmin and xmax for the neighborhood
			int xmin = x - dist;
			int xmax = x + dist;

			// next: is xmax - xmin humongous?  If so, no need to continue wrapping around
			if (xmax - xmin >= width)  // too wide, just use whole neighborhood
			{ xmin = 0; xmax = width - 1; }

			// compute ymin and ymax for the neighborhood
			int ymin = y - dist;
			int ymax = y + dist;

			// next: is ymax - ymin humongous?  If so, no need to continue wrapping around
			if (ymax - ymin >= height)  // too wide, just use whole neighborhood
			{ ymin = 0; ymax = width - 1; }

			for( int x0 = xmin ; x0 <= xmax ; x0++ )
			{
				final int x_0 = stx(x0);
				for( int y0 = ymin ; y0 <= ymax ; y0++ )
				{
					final int y_0 = sty(y0);
					if(getObjectsAtLocation(x_0, y_0) == null){ //only add null locations
						xPos.add( x_0 );
						yPos.add( y_0 );
					}
				}
			}
			//if (!includeOrigin) removeOriginToroidal(x,y,xPos,yPos); 
		}
		else // not toroidal
		{
			// compute xmin and xmax for the neighborhood such that they are within boundaries
			final int xmin = ((x-dist>=0) || !bounded ?x-dist:0);
			final int xmax =((x+dist<=width-1) || !bounded ?x+dist:width-1);
			// compute ymin and ymax for the neighborhood such that they are within boundaries
			final int ymin = ((y-dist>=0) || !bounded ?y-dist:0);
			final int ymax = ((y+dist<=height-1) || !bounded ?y+dist:height-1);
			for( int x0 = xmin; x0 <= xmax ; x0++ )
			{
				for( int y0 = ymin ; y0 <= ymax ; y0++ )
				{
					if(getObjectsAtLocation(x0, y0) == null){//add only null locations
						xPos.add( x0 );
						yPos.add( y0 );
					}
				}
			}
			//if (!includeOrigin) removeOrigin(x,y,xPos,yPos); 
		}
	}

	public Int2D getRandomEmptyMooreLocation (SimState state,final int x, final int y, final int dist, int mode, boolean includeOrigin){
		getMooreEmptyLocations(x,y,dist, mode, includeOrigin);
		if(xPos.numObjs == 0) {
			return null;
		}
		final int i = state.random.nextInt(xPos.numObjs);
		return new Int2D(xPos.objs[i],yPos.objs[i]);
	}


}
