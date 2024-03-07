package observer;


/*
 * An object with this interface can be passed to the Observer's save method to systematically save
 * other data for specific simulations.  If multiple subclasses are created, then create a wrapper class
 * that contains these subclasses to be passed for saving data.
 */
public interface SaveData {

	public void saveas();
	public void save(int sweepNumber);
}
