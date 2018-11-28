public class Main {
	
	final static double GRAVITY_CONST = 9.81; 
	final static int NUMBER_OF_POINTS = 1000000;
	
	public static void main(String args[]) throws Exception {
		for (int NUMBER_OF_POINTS = 10000; NUMBER_OF_POINTS < 10000000; NUMBER_OF_POINTS += 10000) {
			HaltonSequenceGenerator rand = new HaltonSequenceGenerator(1); 
			
			for (int data_point = 0; data_point < 10; data_point++) {
				double avgError = 0;
				Particle[] points = new Particle[NUMBER_OF_POINTS];
				long timeForBrute, timeForMultipole, startTime;
						
				//Random random = new Random();
				
				// Generate random particles using 
				//   the range (0, 1) for x and y and (0, 5) for mass
				for (int i = 0; i < NUMBER_OF_POINTS; i++) {
					points[i] = new Particle(
						rand.nextVector()[0], 
						rand.nextVector()[0], 
						rand.nextVector()[0] * 5);
						
			    }
				
				startTime = System.nanoTime();
				Vector[] bruteForceResult = bruteForce(points);
				timeForBrute = System.nanoTime() - startTime;
				
				startTime = System.nanoTime();
				Vector[] multipoleResult = FastMultipole.calculateNetForces(points);
				timeForMultipole = System.nanoTime() - startTime;
				
				for (int i = 0; i < NUMBER_OF_POINTS; i++) {
					//print("The net force on " + points[i] + " is " + bruteForceResult[i]);
					//print("Multipole estimation is " + multipoleResult[i]);
						
					//print("Error: " + getMarginOfError(bruteForceResult[i], multipoleResult[i]) + "%");
					avgError += getMarginOfError(bruteForceResult[i], multipoleResult[i]);
			    }
				
				print("" + NUMBER_OF_POINTS + ", " 
						 + (timeForBrute/1000000) + ", " 
						 + (timeForMultipole/1000000) + ", "
						 + (avgError/NUMBER_OF_POINTS));
				
				//print("Time for brute: " + (timeForBrute/1000000) + " ms");
				//print("Time for multipole: " + (timeForMultipole/1000000) + " ms");
				//print("Average Error: " + (avgError/NUMBER_OF_POINTS));
			}
		}
	}
	
	public static double getMarginOfError(Vector a, Vector b) {
		return a.clone().subtract(b).getMagnitude() / a.getMagnitude() * 100;
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