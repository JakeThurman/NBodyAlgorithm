import java.text.DecimalFormat;

// Holds a simple X, Y point
public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0000");

		return "{ x: " + df.format(x) + ", y: " + df.format(y) + " }";
	}
	
	public double getMagnitude() {
		return Math.sqrt((x * x) + (y * y));
	}
	
	public Vector normalized() {
		double mag = getMagnitude();
		return new Vector(x / mag, y / mag);
	}
	
	public Vector add(Vector v) {
		return new Vector(v.x + x, v.y + y);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}
}
