package covid;

public enum Status {
	SUSCEPTIBLE (0.1),
	EXPOSED (0.2),
	INFECTED (0.3),
	RECOVERED (0.4);
	
	private final double id;
	Status (double id) {
		this.id = id;
	}
	
	public double id() {
		return id;
	}
	
}
