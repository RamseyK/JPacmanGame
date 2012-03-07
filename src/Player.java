import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Player (pacman) is the object controlled by the human playing the game
 * Player is a subclass of Actor
 * 
 * @author Ramsey Kant
 */
public class Player extends Actor {
	// State
	private int m_iScore; // Current score - Only valid for the current life / level. StateGame will pull this on death or on level change
	private boolean isPowered; // Powered up
	private long poweredExpireTime;
	
	/**
	 * Class Constructor for Player
	 * 
	 * @param m Reference to the map object
	 * @param x X coordiante to spawn the player at
	 * @param y Y coordinate to spawn the player at
	 */
	public Player(Map m, int x, int y) {
		super(OBJECT_PLAYER, Color.yellow, m, x, y);
		
		// State
		m_iScore = 0;
		isPowered = false;
		poweredExpireTime = 0;
	}
	
	// Getters and Setters
	
	/**
	 * Increment score by amount. The is the current level score, not the entire session score
	 * This function is typically called inside an Item's use() function when the player picks up an item like a dot
	 * 
	 * @param amt Amount to increment
	 */
	public void incrementScore(int amt) {
		m_iScore += amt;
	}
	
	/**
	 * Get the current level score of the player
	 * 
	 * @return the score
	 */
	public int getScore() {
		return m_iScore;
	}
	
	/**
	 * Returns the isPowered flag which determines whether or not the player is powered up and invicible to ghosts
	 * 
	 * @return True if the player is powered up
	 */
	public boolean isPoweredUp() {
		return isPowered;
	}
	
	/**
	 * Set powered up state and start the expirtation time for when the powerup wears off
	 * 
	 * @param x True if powered up, false if otherwise
	 * @see Player#isPoweredUp()
	 */
	public void setPowerUp(boolean x) {
		isPowered = x;
		// If powered up, start the timer and increase speed temporarily
		if(isPowered) {
			poweredExpireTime = System.currentTimeMillis() + 10000;
			speed = 6;
		} else {
			speed = 5;
		}
	}
	
	/**
	 * Player act() method
	 * This should evaluate if there is:
	 * - a collission with a ghost and how to handle that interaction
	 * - a dot or cherry being eaten (call use() on the item)
	 * - a next movement
	 */
	public void act() {
		// If there is a ghost at the players location, this player is dead unless the player is powered up (then the ghost dies)
		Actor a = map.getActor(positionX, positionY, true);
		if(a != null && a.getType() == GameObject.OBJECT_GHOST) {
			// Notify the State of the loss if pacman isn't powered up
			if(!isPowered) {
				setDead(true);
				return;
			} else {
				a.setDead(true);
			}
		}
		
		// Check for powerup expire
		if(System.currentTimeMillis() > poweredExpireTime) {
			setPowerUp(false);
		}
		
		// Use item at current location
		boolean itemDestroy = false;
		Item item = map.getItem(positionX, positionY);
		if(item != null)
			itemDestroy = item.use(this);
		
		// Update the item's state in the map (remove if itemDestroy is true)
		if(itemDestroy)
			map.removeItem(positionX, positionY);
		
		// Based on the direction, increment the movement delta and set the appropriate orientation
		// The delta's represent the screen position (in pixels) since the last official change in position on the grid
		// When a delta in a certain direction passes the CELL_SIZE, the object can change position in the map grid. This makes for smooth transitions between tiles
		switch(moveDir) {
			case MOVE_UP:
				// The next position is blocked, so stand still
				if(!map.canMove(this, positionX, positionY-1)) {
					deltaX = 0;
					deltaY = 0;
				} else { // We can continue to move
					deltaX = 0;
					deltaY -= speed;
					// The delta has surpassed the # of pixels for the cell, meaning we can officially change the coordinate
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						deltaY = 0;
						move(positionX, positionY-1);
					}
				}
				dirOrient = 90;
				break;
			case MOVE_RIGHT:
				if(!map.canMove(this, positionX+1, positionY)) {
					deltaX = 0;
					deltaY = 0;
				} else {
					deltaX += speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(positionX+1, positionY);
					}
				}
				dirOrient = 0;
				break;
			case MOVE_DOWN:
				if(!map.canMove(this, positionX, positionY+1)) {
					deltaX = 0;
					deltaY = 0;
				} else {
					deltaX = 0;
					deltaY += speed;
					if(Math.abs(deltaY) >= map.CELL_SIZE) {
						deltaY = 0;
						move(positionX, positionY+1);
					}
				}
				dirOrient = -90;
				break; 
			case MOVE_LEFT:
				if(!map.canMove(this, positionX-1, positionY)) {
					deltaX = 0;
					deltaY = 0;
				} else {
					deltaX -= speed;
					deltaY = 0;
					if(Math.abs(deltaX) >= map.CELL_SIZE) {
						deltaX = 0;
						move(positionX-1, positionY);
					}
				}
				dirOrient = 180;
				break;
			default:
				// MOVE_NONE (stand still)
				deltaX = 0;
				deltaY = 0;
				break;
		}
	}
	
	/**
	 * Draw & animate pacman
	 * 
	 * @param g The graphics context
	 * @see Actor#act()
	 */
	public void paint(Graphics2D g) {
		// Change the position of pacman on screen by the offsets m_fDelta
		int screenX = (int)((map.CELL_SIZE * positionX) + deltaX);
		int screenY = (int)((map.CELL_SIZE * positionY) + deltaY);
			
		g.setColor(objColor);

		// Animate Pacman's mouth
		// When the player is half-way through a tile, close the flap. Open it back up when the flap clears a tile.
		// This essentially creates an eating animation
		if((Math.abs(deltaX) >= map.CELL_SIZE/2) || Math.abs(deltaY) >= map.CELL_SIZE/2)
			g.fillArc(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE, 0+dirOrient, 360); // flap closed
		else
			g.fillArc(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE, 35+dirOrient, 270);	
	}
}
