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
		return "(" + x + ", " + y + ")";
	}
	
	public static Vector diff(Vector a, Vector b) {
		return new Vector(a.x - b.x, a.y - b.y);
	}
}
