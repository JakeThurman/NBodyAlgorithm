
public class Main {
	
	final static double GRAVITY_CONST = 9.81; 
	
	public static void main(String args[]){
		int NUMBER_OF_POINTS = 100;
		
		Particle[] my_points = new Particle[NUMBER_OF_POINTS];
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			my_points[i] = new Particle(new Vector(Math.random(), Math.random()), Math.random());
	    }
		
		bruteForce(my_points);
		
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			print(my_points[i].getLocation().toString() + " = " + my_points[i].calculate());
	    }
	}
	
	// Shortcut!
	public static void print(String str)
	{
		System.out.println(str);
	}
	
	// Solves the N-body problem in n^2 time
	public static void bruteForce(Particle[] points) {
		for (int i = 0; i < points.length; i++) {
			Particle a = points[i];
			
			for (int j = 0; j < points.length; j++) {
				Particle b = points[j];
				
				Vector diff = Vector.diff(a.getLocation(), b.getLocation());
				
				double distanceSquared = diff.getX() * diff.getX() 
				                       + diff.getY() * diff.getY();
				
				// TODO: What is the direction of this?
				a.addForce(new Vector(1, GRAVITY_CONST * points[i].getMass() * points[j].getMass() / distanceSquared));
			}
		}
	}
}