import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The EditorMarker is used by the StateEditor for navigation and selecting tiles on the map
 * EditorMarker is NOT tracked inside the Map
 * EditorMarker is a subclass of GameObject
 * 
 * @author Ramsey Kant
 */
public class EditorMarker extends GameObject {

	/**
	 * Class constructor for EditorMarker
	 * 
	 * @param color Color of the marker
	 * @param m Reference to the map object
	 * @param x X coordinate to initially place the marker
	 * @param y Y coordinate to initially place the marker
	 */
	public EditorMarker(Color color, Map m, int x, int y) {
		super(GameObject.OBJECT_MARKER, color, m, x, y);
	}
	
	// Public Methods
	
	/**
	 * Change tile is the EditorMarker's version of Actor's move() method. Called by keyPressed in StateEditor
	 * Moves the Marker on the screen
	 * 
	 * @param dx Amount to change the current X coordinate by
	 * @param dy Amount to change the current Y coordinate by
	 * @see StateEditor#keyPressed(KeyEvent)
	 */
	public void changeTile(int dx, int dy) {
		// Check bounds
		if(positionX+dx < 0 || positionY+dy < 0 || positionX+dx >= map.getWidth() || positionY+dy >= map.getHeight())
			return;
		
		positionX += dx;
		positionY += dy;
	}

	/**
	 * EditorMarker has a blank act() method
	 * 
	 * @see GameObject#act()
	 */
	@Override
	public void act() {
	}

	/**
	 * EditorMarker appears as a circle around the tile being edited. The color is set in the constructor
	 * 
	 * @see GameObject#paint(Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
		int screenX = (int)(map.CELL_SIZE * positionX);
		int screenY = (int)(map.CELL_SIZE * positionY);
			
		g.setColor(objColor);
		
		g.drawOval(screenX, screenY, map.CELL_SIZE, map.CELL_SIZE);
	}

}
