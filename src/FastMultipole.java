import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FastMultipole {
	private static final int divs = 4;
	public static Vector[] calculateNetForces(Particle[] particles){
		
		Particle[][] centerOfGravityPoints = new Particle[divs][divs];
		
		for(int i = 0; i < divs; i++){
			for(int j = 0; j < divs; j++){
				// Init the centerpoints in the proper position
				centerOfGravityPoints[i][j] = new Particle(1.0 / (double)divs * 2.0 + (1.0/(double)divs * i), 1.0 / (double)divs * 2.0 + (1.0/divs * j), 0);
			}
		}
		
		
		//Particles within each square
		LinkedList<ParticleAndForce>[][] square = new LinkedList[divs][divs];
		int X, Y; // Declaring here for efficiency
		
		// Initializing the linked lists and the ordering
		for(int i = 0; i < divs; i++){
			for(int j = 0; j < divs; j++){
				square[i][j] = new LinkedList<ParticleAndForce>();
			}
		}
		
		// Iterating through all points to add their masses to the proper center point, then 
		// adding the point to the linked list of the square they are in
		for(int i = 0; i < particles.length; i++){
			X = (int) (particles[i].getX() * (double)divs);
			Y = (int) (particles[i].getY() * (double)divs);
			centerOfGravityPoints[X][Y].addMass(particles[i].getMass());
			square[X][Y].add(new ParticleAndForce(particles[i], new Vector(0,0), i));
		}
		
		Vector[][] forceApprox = new Vector[divs][divs];
		
		for(int i = 0; i < divs; i++){
			for(int j = 0; j < divs; j++){
				forceApprox[i][j] = new Vector(0,0);
			}
		}
		
		// Perform forces of the midpoints on each other, excluding their neighbors
		for(int approx_x = 0; approx_x < divs; approx_x++){
			for(int approx_y = 0; approx_y < divs; approx_y++){
				for(int square_x = 0; square_x < divs; square_x++){
					for(int square_y = 0; square_y < divs; square_y++){
						
						if(square_x - approx_x > 1 || square_x - approx_x < -1 ||
								square_y - approx_y > 1 || square_y - approx_y < -1){
							forceApprox[approx_x][approx_y].add(
									Main.G(centerOfGravityPoints[approx_x][approx_y],
											centerOfGravityPoints[square_x][square_y]));
						}
						
					}
				}
			}
		}
		
		// Apply midpoint forces back onto the particles within their square
		for(int i = 0; i < divs; i++){
			for(int j = 0; j < divs; j++){
				for(ParticleAndForce p : square[i][j]){
					p.netForce = p.netForce.add(forceApprox[i][j])
							.scale(p.particle.getMass()/centerOfGravityPoints[i][j].getMass());
				}
			}
		}
		
		
		// Final summation - for neighboring squares
		for(int x = 0; x < divs; x++){
			for(int y = 0; y < divs; y++){
				
				// Special iteration
				int neighbor_x, neighbor_y = 1; // ASK: Why aren't we checking vertical neighbors
				for(neighbor_x = -1; neighbor_x <= 1; neighbor_x++){
					if(x + neighbor_x < divs && x + neighbor_x >= 0 && y + neighbor_y < divs){
						
						for(ParticleAndForce pf1 : square[x][y]){
							for(ParticleAndForce pf2 : square[x + neighbor_x][y + neighbor_y]){
								Vector force = Main.G(pf1.particle, pf2.particle);
								pf1.netForce.add(force);
								pf2.netForce.subtract(force);
							}
						}
						
					}
				}
				
				neighbor_x = 1;
				neighbor_y = 0;
				
				if(x + neighbor_x < divs && x + neighbor_x >= 0 && y + neighbor_y < divs){
					
					for(ParticleAndForce pf1 : square[x][y]){
						for(ParticleAndForce pf2 : square[x + neighbor_x][y + neighbor_y]){
							Vector force = Main.G(pf1.particle, pf2.particle);
							pf1.netForce.add(force);
							pf2.netForce.subtract(force);
						}
					}
					
				}
			}
		}
		
		//Final summation - for forces internally to a square
		for(int x = 0; x < divs; x++){
			for(int y = 0; y < divs; y++){
				Iterator<ParticleAndForce> it = square[x][y].iterator();
				int iterations = 0;
				
				while(it.hasNext()){
					ParticleAndForce pf = it.next();
					Iterator<ParticleAndForce> it2 = square[x][y].listIterator(iterations+1);
					
					while(it2.hasNext()){
						ParticleAndForce pf2 =  it2.next();
						Vector force = Main.G(pf.particle, pf2.particle);
						pf.netForce.add(force);
						pf2.netForce.subtract(force);
					}
					
					iterations++;
				}
			}
		}
		
		Vector[] results = new Vector[particles.length];
		
		// Put the resulting forces back into their proper order
		for(int i = 0; i < divs; i++){
			for(int j = 0; j < divs; j++){
				for(ParticleAndForce pf : square[i][j]){
					results[pf.order] = pf.netForce;
				}
			}
		}
		
		return results;
		
	}
	
	
	// "Order" keeps track of the original particle's location in the array passed to the primary 
	// method to enable returning the set of vectors in the correct order
	private static class ParticleAndForce {
		public Vector netForce;
		public Particle particle;
		public int order;
		
		public ParticleAndForce(Particle p, Vector f, int i){
			particle = p;
			netForce = f;
			order = i;
		}
	}

}
