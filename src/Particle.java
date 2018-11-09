import java.text.DecimalFormat;

public class Particle extends Vector {
	private double mass;
	
	public Particle(double x, double y, double mass) {
		super(x, y);
		
		this.mass = mass;
	}
	
	public double getMass() {
		return mass;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0000");

		return "{ x: " + df.format(getX()) + ", y: " + df.format(getY()) + ", mass: " + df.format(mass) + " }";
	}
}
