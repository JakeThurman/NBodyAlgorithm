import java.util.Random;

public class Main {
	
	final static double GRAVITY_CONST = 9.81; 
	final static int NUMBER_OF_POINTS = 60000;
	
	public static void main(String args[]){		
		Particle[] points = new Particle[NUMBER_OF_POINTS];
		Random random = new Random(777); // change the number for a different data set
		long timeForBrute, timeForMultipole, startTime;
				
		// Generate random particles using 
		//   the range (0, 1) for x and y and mass
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			points[i] = new Particle(random.nextDouble(), random.nextDouble(), random.nextDouble() * 5);
	    }
		
		startTime = System.nanoTime();
		Vector[] bruteForceResult = bruteForce(points);
		timeForBrute = System.nanoTime() - startTime;
		
		startTime = System.nanoTime();
		Vector[] multipoleResult = FastMultipole.calculateNetForces(points);
		timeForMultipole = System.nanoTime() - startTime;
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			print("The net force on " + points[i] + " is " + bruteForceResult[i]);
			print("Multipole estimation is " + multipoleResult[i]);
	    }
		print("Time for brute: " + (timeForBrute/1000000) + " ms");
		print("Time for multipole: " + (timeForMultipole/1000000) + " ms");
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
		Vector force = new Vector(b).subtract(a).normalized();

		double xSum = a.getX() + b.getX();
		double ySum = a.getY() + b.getY();
		
		double scalar = (GRAVITY_CONST * a.getMass() * b.getMass()) / ((xSum * xSum) + (ySum * ySum));
		
		return force.scale(scalar);
	}
}