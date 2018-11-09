public class Main {
	
	final static double GRAVITY_CONST = 9.81; 
	final static int NUMBER_OF_POINTS = 100;
	
	public static void main(String args[]){	
		Particle[] my_points = new Particle[NUMBER_OF_POINTS];
				
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			my_points[i] = new Particle(new Vector(Math.random(), Math.random()), Math.random());
	    }
		
		bruteForce(my_points);
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			print(my_points[i].toString() + " = " + my_points[i].getForceSum());
	    }
	}
	
	// Shortcut!
	public static void print(String str) {
		System.out.println(str);
	}
	
	// Solves the N-body problem in n^2 time
	public static void bruteForce(Particle[] points) {
		for (int i = 0; i < points.length; i++) {
			Particle a = points[i];
			
			for (int j = 0; j < points.length; j++) {
				if (i != j) {
					Particle b = points[j];
					
					Vector diff = Vector.diff(a.getLocation(), b.getLocation());
					
					double distanceSquared = diff.getX() * diff.getX() 
					                       + diff.getY() * diff.getY();
					
					a.addForce(GRAVITY_CONST * a.getMass() * b.getMass() / distanceSquared);
				}
			}
		}
	}
}