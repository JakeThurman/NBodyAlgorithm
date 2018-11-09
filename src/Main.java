public class Main {
	
	final static double GRAVITY_CONST = 9.81; 
	final static int NUMBER_OF_POINTS = 3;
	
	public static void main(String args[]){		
		Particle[] my_points = new Particle[NUMBER_OF_POINTS];
				
		// Generate random particles using 
		//   the range (0, 1) for x and y and mass
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			my_points[i] = new Particle(Math.random(), Math.random(), Math.random() * 5);
	    }
		
		Vector[] bruteForceResult = bruteForce(my_points);
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			print("The net force on " + my_points[i] + " is " + bruteForceResult[i]);
	    }
	}
	
	// Shortcut!
	public static void print(String str) {
		System.out.println(str);
	}
	
	// Solves the N-body problem in n^2 time
	public static Vector[] bruteForce(Particle[] points) {
		Vector[] netForces = new Vector[points.length];
		
		for (int v = 0; v < netForces.length; v++)
			netForces[v] = new Vector(0.0, 0.0);
		
		for (int i = 0; i < points.length; i++) {
			Particle a = points[i];
			
			for (int j = i+1; j < points.length; j++) {
				Particle b = points[j];
				
				Vector kernalResult = G(a, b);
				
				netForces[i] = netForces[i].add(kernalResult);
				netForces[j] = netForces[j].subtract(kernalResult);
			}
		}
		
		return netForces;
	}
	
	public static Vector G(Particle a, Particle b) {
		Vector force = b.subtract(a).normalized();

		double xSum = a.getX() + b.getX();
		double ySum = a.getY() + b.getY();
		
		double scalar = (GRAVITY_CONST * a.getMass() * b.getMass()) / ((xSum * xSum) + (ySum * ySum));
		
		return new Vector(force.getX() * scalar, force.getY() * scalar);
	}
}