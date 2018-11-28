public class Main {
	final static double GRAVITY_CONST = 9.81; 
	
	public static void main(String args[]) throws Exception {
		// Output a header for our CSV style output.
		//  The output is standard out, but it is
		//  easy to copy and paste into a new file.
		print("Data Size, Brute Force Time (ms), Multipole Time (ms), Average Error (%)");
		
		// This loop controls the size of the data set
		for (int NUMBER_OF_POINTS = 10000; NUMBER_OF_POINTS <= 12000; NUMBER_OF_POINTS += 10000) {
			
			// Use the HaltonSequenceGenerator from the Apache Software Foundation
			//  Source: http://home.apache.org/~luc/commons-math-3.6-RC2-site/jacoco/org.apache.commons.math3.random/HaltonSequenceGenerator.java.html
			//  Modified to remove dependencies (Now throws exception instead of custom classes, and doesn't implement its interface)
			// The argument is the dimension of the vector space, we are using 1 and just taking a single number at a time 
			HaltonSequenceGenerator rand = new HaltonSequenceGenerator(1); 
			
			// This loop makes it so we take 10 data points for each data size.
			for (int data_point = 0; data_point < 10; data_point++) {
				

				double errorPercentageSum = 0; // We need the sum so we can calculate the average to output
				Particle[] points = new Particle[NUMBER_OF_POINTS];
				long timeForBrute, timeForMultipole, startTime;
						
				
				for (int i = 0; i < NUMBER_OF_POINTS; i++) {
					// Create a random particle by taking a few random data points
					//  we use these for x, y, and mass. There is no real reason for
					//  multiplying mass by a scalar except that it looks like a nicer
					//  to have larger masses
					points[i] = new Particle(
						rand.nextVector()[0], 
						rand.nextVector()[0], 
						rand.nextVector()[0] * 5);
						
			    }
				
				// Time the Brute Force calculation
				startTime = System.nanoTime();
				Vector[] bruteForceResult = bruteForce(points);
				timeForBrute = System.nanoTime() - startTime;
				
				// Time the Multipole calculation
				startTime = System.nanoTime();
				Vector[] multipoleResult = FastMultipole.calculateNetForces(points);
				timeForMultipole = System.nanoTime() - startTime;
				
				// Sum up the margin of error of all particles 
				for (int i = 0; i < NUMBER_OF_POINTS; i++) {
					errorPercentageSum += getMarginOfError(bruteForceResult[i], multipoleResult[i]);
			    }
				
				// Output this result as a line of our CSV style output
				print("" + NUMBER_OF_POINTS + ", "           //Data Size
						 + (timeForBrute/1000000) + ", "     //Brute Force Time (ms)
						 + (timeForMultipole/1000000) + ", " //Multipole Time (ms)
						 + (errorPercentageSum/NUMBER_OF_POINTS)); //Average Error Percentage
				
				//print("Time for brute: " + (timeForBrute/1000000) + " ms");
				//print("Time for multipole: " + (timeForMultipole/1000000) + " ms");
				//print("Average Error: " + (avgError/NUMBER_OF_POINTS));
			}
		}
	}
	
	public static double getMarginOfError(Vector real, Vector approximation) {
		// The error we consider is a percentage.
		//  We find the diff between the two vectors,
		//  and divide its magnitude by the magnitude
		//  of the real vector. This gives us a factor 
		//  of "Percent off target" 
		
		return new Vector(real).subtract(approximation).getMagnitude() / real.getMagnitude() * 100;
	}
	
	// Shortcut!
	public static void print(String str) {
		System.out.println(str);
	}
	
	// Solves the N-body problem in n^2 time
	public static Vector[] bruteForce(Particle[] points) {
		Vector[] netForces = new Vector[points.length];
		
		// Initialize all of the forces to zero
		for (int v = 0; v < netForces.length; v++)
			netForces[v] = new Vector(0.0, 0.0);
		
		// Loop at every particle we were given to calculate the forces on it
		for (int i = 0; i < points.length; i++) {
			Particle a = points[i];
			
			// Compare this particle to every other particle
			//  as an optimization, we only compare to factors 
			//  after this one, and then also subtract the 
			//  kernal result from the particle we are comparing to.
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
		// Grab the normal vector in the director of (a - b)
		Vector force = new Vector(b).subtract(a).normalized();

		double xSum = a.getX() + b.getX();
		double ySum = a.getY() + b.getY();
		
		// Calculate the scale of the force to return
		double scalar = (GRAVITY_CONST * a.getMass() * b.getMass()) / ((xSum * xSum) + (ySum * ySum));
		
		return force.scale(scalar);
	}
}