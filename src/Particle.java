import java.util.LinkedList;

public class Particle {
	private Vector location;
	private double mass;
	private LinkedList<Vector> forcesOnMe;
	
	public Particle(Vector location, double mass) {
		this.location = location;
		this.mass = mass;
		this.forcesOnMe = new LinkedList<Vector>();
	}
	
	public Vector getLocation() {
		return location;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void addForce(Vector vector) {
		forcesOnMe.add(vector);
	}
	
	public double calculate() {
		// TODO: ???
	}
}
