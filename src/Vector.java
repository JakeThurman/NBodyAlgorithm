import java.text.DecimalFormat;

// Holds a simple X, Y point
public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector(Vector copy){
		this.x = copy.x;
		this.y = copy.y;
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
		x /= mag;
		y /= mag;
		return this;
	}
	
	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector subtract(Vector v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public Vector scale(double scalar){
		x *= scalar;
		y *= scalar;
		return this;
	}
}
