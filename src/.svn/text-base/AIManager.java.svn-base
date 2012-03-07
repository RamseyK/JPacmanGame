import java.util.ArrayList;

/**
 * Strategy management behind the AI (Ghost objects)
 * 
 * @author Ramsey Kant
 */
public class AIManager {
	// References
	private Map map;
	private Player player;
	
	// Logic
	private boolean debugEnabled;
	private PathFinder finder;
	private ArrayList<Ghost> ghosts;
	private long nextReleaseTime;
	
	/**
	 * Class Constructor
	 * 
	 * @param m Reference to the map object being used by the game
	 * @param pl Reference to the player
	 * @param debug Set the debug flag allowing the AI manager to direct ghosts to exibit diagnostic behavior
	 */
	public AIManager(Map m, Player pl, boolean debug) {
		// Set vars
		ghosts = new ArrayList<Ghost>();
		setReferences(m, pl);
		nextReleaseTime = System.currentTimeMillis() + 10000;
		debugEnabled = debug;
	}
	
	// Getters and Setters
	
	/**
	 * Direct ghosts to display diagnostic information
	 * 
	 * @param d If true, ghosts will enter debug mode
	 * @see Ghost#setDebugDrawPath
	 */
	public void setDebugEnabled(boolean d) {
		debugEnabled = d;
	}
	
	/**
	 * Set the global map and player references. Ghosts being tracked (in the 'ghosts' ArrayList) will be updated
	 * 
	 * @param m Reference to the map
	 * @param pl Reference to the player object
	 */
	public void setReferences(Map m, Player pl) {
		ghosts.clear();
		map = m;
		player = pl;
		finder = new PathFinder(m, 500, false);
		
		// Get a list of all AI on the map
		int nActors = map.getNumActors();
		for(int i = 0; i < nActors; i++) {
			Actor a = map.getActor(i);
			if(a.getType() == GameObject.OBJECT_GHOST) {
				ghosts.add((Ghost)a);
			}
		}
	}
	
	/**
	 * Run all logic required for AI operation; fear, ghost release, path updates.
	 * Ghost act() functions are called here
	 */
	public void process() {
		// Make sure the game is still running and there is a map
		if(map == null)
			return;
		
		// Determine if ghosts are fearful of the player
		boolean fear = false;
		if(map.getPlayer().isPoweredUp())
			fear = true;
		
		// Release the next ghost
		if(System.currentTimeMillis() > nextReleaseTime) {
			for(Ghost g : ghosts) {
				if(g.isTrapped()) {
					g.setTrapped(false);
					g.move(13, 11);
					nextReleaseTime = System.currentTimeMillis() + 8000;
					break;
				}
			}
		}
		
		// Go through a list of all AI on the map
		for(Ghost ghost : ghosts) {
			// If a ghost just died, send them to jail
			if(ghost.isDead()) {
				// Find an empty spot in the jail
				int x = 11;
				int y = 13;
				int z = 0;
				while(!map.isEmpty(x+z, y)) {
					z++;
					if(z > 4)
						break;
				}
				
				// Clear path and move to jail
				ghost.updatePath(null);
				ghost.move(x, y);
				ghost.setTrapped(true);
				ghost.setDead(false);
			}
			
			// Any ghost not trapped is given the current fear status
			if(!ghost.isTrapped()) {
				// If fear switches from false to true for this ghost, abandon their current (and likely) chase path
				if(!ghost.isInFear() && fear)
					ghost.updatePath(null);
				ghost.setFear(fear);
			} else {
				ghost.setFear(false);
			}
			
			// Develop path for ghost
			if(!ghost.isTrapped() && ghost.needsNewPath()) {
				int randx = player.getX();
				int randy = player.getY();
				// 45% chance of randomizing a destination, or if they are fearful
				if(fear || Math.random() < 0.45) {
					randx = (int)(Math.random()*map.getWidth());
					randy = (int)(Math.random()*map.getHeight());
				}
				Path p = finder.findPath(ghost, ghost.getX(), ghost.getY(), randx, randy);
				ghost.updatePath(p);
			}
			
			// Run an act()
			ghost.act();
			
			// If debug is enabled, force ghost to draw it's path
			ghost.setDebugDrawPath(debugEnabled);
		}
	}
}
