import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The Ghost class is the primary enemy in Pacman. Intelligent decisions of ghosts are made by the AIManager class
 * Ghost is a subclass of Actor
 * 
 * @author Ramsey Kant
 */
public class Ghost extends Actor {
	// Movement
	private Path path;
	private int nextStepIdx; // The next index in the path object, used to pull the next coordinate to navigate to
	private boolean needNewPath;
	
	// State
	private final boolean originalTrapped;
	private boolean trapped;
	private boolean inFear;
	private boolean fearBlink;
	private boolean debugDrawPath;
	
	/**
	 * Class constructor
	 * 
	 * @param color Color of the ghost's 'body'
	 * @param m Reference to the map
	 * @param x X coordinate to spawn at
	 * @param y Y coordinate to spawn at
	 * @param trap Set trapped status
	 */
	public Ghost(Color color, Map m, int x, int y, boolean trap) {
		super(GameObject.OBJECT_GHOST, color, m, x, y);
		needNewPath = true;
		inFear = false;
		fearBlink = false;
		originalTrapped = trap;
		trapped = trap;
		debugDrawPath = false;
	}
	
	/**
	 * Return the fear status of the ghost
	 * 
	 * @return True if fearful
	 */
	public boolean isInFear() {
		return inFear;
	}
	
	/**
	 * Set fear status. AIManager interperates this setting for behavior
	 * 
	 * @param f Fear status, true if fearful
	 * @param fb Fear blink, true if the ghost should be drawn in blinking mode (player's powerup is about to expire)
	 */
	public void setFear(boolean f, boolean fb) {
		inFear = f;
		fearBlink = fb;
	}
	
	/**
	 * Get the current trapped status
	 * 
	 * @return True if the ghost is currently in the spawn-jail
	 */
	public boolean isTrapped() {
		return trapped;
	}
	
	/**
	 * Get the initial (on map reset) trapped status.
	 * 
	 * @return True if the ghost is originally one of the trapped ghosts
	 */
	public boolean isOriginalTrapped() {
		return originalTrapped;
	}
	
	/**
	 * Set the current trapped status
	 * 
	 * @param t Trye uf the ghost is in the spawn-jail
	 */
	public void setTrapped(boolean t) {
		trapped = t;
	}
	
	/**
	 * Flag that is set to true when the path reaches the last possible step
	 * 
	 * @return True if the AIManager needs to assign a new path
	 */
	public boolean needsNewPath() {
		return needNewPath;
	}
	
	/**
	 * Update the Path object for the ghost to follow'
	 * 
	 * @param p Path object generated in process() by the AIManager
	 * @see AIManager#process()
	 */
	public void updatePath(Path p) {
		nextStepIdx = 1;
		path = p;
		needNewPath = false;
	}
	
	/**
	 * Direct's the paint() function to draw the current path of the ghost on the map
	 * 
	 * @param d If true, debug is on and the path will be drawn
	 * @see AIManager#setDebugEnabled
	 */
	public void setDebugDrawPath(boolean d) {
		debugDrawPath = d;
	}
	
	/**
	 * Run a think cycle for the AI. Major decisions are made by the AIManager (pathing), this just determines movement and screen draw deltas
	 * 
	 * @see Actor#act()
	 */
	@Override
	public void act() {
		// Move to the next step
		if(path != null && nextStepIdx < path.getLength()) {
			
			// Figure out the direction by calculating the difference between the next position and current position
			if((path.getY(nextStepIdx)-positionY) < 0)
				moveDir = MOVE_UP;
			else if((path.getY(nextStepIdx)-positionY) > 0)
				moveDir = MOVE_DOWN;
			else if((path.getX(nextStepIdx)-positionX) > 0)
				moveDir = MOVE_RIGHT;
			else
				moveDir = MOVE_LEFT;
			
			// Based on the direction, move the screen delta's and the X,Y coordinates if the # of pixels for the cell have been surpassed
			switch(moveDir) {
				case MOVE_UP:
					deltaX = 0;
					deltaY -= speed;
					// The delta has surpassed the # of pixels for the cell, meaning we can officially change the coordinate
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						// Reset the change in Y since we have switched tiles and are no longer in an "intermediate" moving state across the screen
						deltaY = 0;
						
						// Move to the next position in the path
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						
						// Increment the step index so the next time act() is called, the new position will be retrieved
						nextStepIdx++;
					}
					dirOrient = 90;
					break;
				case MOVE_RIGHT:
					deltaX += speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = 0;
					break;
				case MOVE_DOWN:
					deltaX = 0;
					deltaY += speed;
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						deltaY = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = -90;
					break; 
				case MOVE_LEFT:
					deltaX -= speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(path.getX(nextStepIdx), path.getY(nextStepIdx));
						nextStepIdx++;
					}
					dirOrient = 180;
					break;
				default:
					// MOVE_NONE (stand still)
					deltaX = 0;
					deltaY = 0;
					break;
			}
		} else {
			needNewPath = true;
		}
	}
	
	/**
	 * Draw the ghost
	 * 
	 * @see GameObject#paint(Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
		// Change the position of pacman on screen by the offsets m_fDelta
		int screenX = (int)((map.CELL_SIZE * positionX) + deltaX);
		int screenY = (int)((map.CELL_SIZE * positionY) + deltaY);
			
		g.setColor(objColor);
		
		// Body
		if(inFear) {
			if(fearBlink)
				g.setColor(Color.GRAY);
			else
				g.setColor(Color.WHITE);
		}
			
		g.fillArc(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE, 0, 360);
		g.fillRect((int)((map.CELL_SIZE * positionX) + deltaX), (int)((map.CELL_SIZE * positionY)+(map.CELL_SIZE/2) + deltaY), map.CELL_SIZE, map.CELL_SIZE/2);
		
		// Eyes
		if(inFear)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.WHITE);
		g.fillOval((int)((map.CELL_SIZE * positionX)+6 + deltaX), (int)((map.CELL_SIZE * positionY)+4 + deltaY), 8, 10);
		g.fillOval((int)((map.CELL_SIZE * positionX)+16 + deltaX), (int)((map.CELL_SIZE * positionY)+4 + deltaY), 8, 10);
		
		// Eyeballs
		g.setColor(Color.BLUE);
		g.fillOval((int)((map.CELL_SIZE * positionX)+8 + deltaX), (int)((map.CELL_SIZE * positionY)+6 + deltaY), 4, 4);
		g.fillOval((int)((map.CELL_SIZE * positionX)+18 + deltaX), (int)((map.CELL_SIZE * positionY)+6 + deltaY), 4, 4);
		
		// Debug draw path
		if(debugDrawPath && path != null) {
			for(int i = 0; i < path.getLength(); i++) {
				Path.Step s = path.getStep(i);
				g.setColor(objColor);
				g.drawLine(map.CELL_SIZE * s.getX(), map.CELL_SIZE * s.getY(), (map.CELL_SIZE * s.getX())+map.CELL_SIZE, (map.CELL_SIZE * s.getY())+map.CELL_SIZE);
			}
		}
	}
}
