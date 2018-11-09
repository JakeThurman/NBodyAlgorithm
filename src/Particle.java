public class Particle {
	private Vector location;
	private double mass;
	private double forceMagnitudeOnMe;
	
	public Particle(Vector location, double mass) {
		this.location = location;
		this.mass = mass;
		this.forceMagnitudeOnMe = 0;
	}
	
	public Vector getLocation() {
		return location;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void addForce(double vectorMagnitude) {
		forceMagnitudeOnMe += vectorMagnitude;
	}
	
	public double getForceSum() {
		return forceMagnitudeOnMe;
	}
	
	public String toString() {
		return "{ location: " + location + ", mass: " + mass + "}";
	}
}
