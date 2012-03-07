import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Map class keeps track objects on the playing grid, helper methods to make movement decisions, and export/import methods for the editor
 * 
 * @author Ramsey Kant
 */
public class Map {	
	// Map parameters (width & height represent # of cells)
	private int mapWidth;
	private int mapHeight;
	public final int CELL_SIZE;
	
	// Instance vars
	private byte collideMap[][];
	private Item itemMap[][];
	private ArrayList<Actor> actorList;
	private int dotsRemaining;

	/**
	 * Class constructor, inits a blank map based on a width, height, and cell size
	 * Used in the editor
	 * 
	 * @param w Width of the map
	 * @param h Height of the map
	 * @param cs Size of individual cells in pixels
	 */
	public Map(int w, int h, int cs) {
		// Set map parameters
		mapWidth = w;
		mapHeight = h;
		CELL_SIZE = cs;
		dotsRemaining = 0;
		
		// Initialize collideMap, a 2D array that contains all static collidable GameObjects
		// We use this for fast lookup during collision detection and AI movement paths
		collideMap = new byte[mapWidth][mapHeight];
		
		// Initialize itemMap, a 2D array that contains items (dots, powerups, cherry) on the map
		itemMap = new Item[mapWidth][mapHeight];
		
		// Create m_objects, an arraylist with all actorList
		actorList = new ArrayList<Actor>();
	}
	
	/**
	 * Class Constructor that reads the map data from filename
	 * 
	 * @param filename The file name of the map to read contents from
	 * @param cs Size of individual cells in pixels. This is something that should be deteremined by graphics, not the mapfile
	 */
	public Map(String filename, int cs) {
		// Set the cell size
		CELL_SIZE = cs;
		
		// Read contents of the map file
		read(filename);
	}
	
	/**
	 * The width of the map originally set in the constructor
	 *
	 * @return The width of the map
	 */
	public int getWidth() {
		return mapWidth;
	}
	
	/**
	 * The height of the map originally set in the constructor
	 * 
	 * @return The height of the map
	 */
	public int getHeight() {
		return mapHeight;
	}
	
	/**
	 * Get the number of actorList on the map (the size of the actorList ArrayList)
	 * 
	 * @return Number of actorList
	 */
	public int getNumActors() {
		return actorList.size();
	}
	
	/**
	 * Return the collidable map (a 2d array of bytes which correspond to the collidable types defined in GameObject)
	 * 
	 * @return collidable map (collideMap)
	 */
	public byte[][] getCollidableMap() {
		return collideMap;
	}
	
	/**
	 * Return the item map (a 2D array of Item objects)
	 * 
	 * @return item map (itemMap)
	 */
	public Item[][] getItemMap() {
		return itemMap;
	}
	
	/**
	 * Return the number of dots remaining on the map. This is tracked by the dotsRemaining local var (not a loop and count in itemMap)
	 * 
	 * @return dots remaining
	 */
	public int getDotsRemaining() {
		return dotsRemaining;
	}
	
	/**
	 * Add a collidable (by type) to the collideMap
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param t Type of collidable
	 * @return True if successful
	 */
	public boolean addCollidable(int x, int y, byte t) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if theres already something there
		if(collideMap[x][y] > 0)
			return false;
		
		// Add to the collideMap
		collideMap[x][y] = t;
		return true;
	}
	
	/**
	 * Put a new item to the item map
	 * 
	 * @param item Item
	 * @return True if successful
	 */
	public boolean addItem(Item item) {
		if(item == null) 
			return false;
		
		// Check bounds
		int x = item.getX();
		int y = item.getY();
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Add to the itemMap
		if(item.getType() == GameObject.OBJECT_DOT)
			dotsRemaining++;
		itemMap[x][y] = item;
		return true;
	}
	
	/**
	 * Put a new actor in the map (actorList ArrayList)
	 * 
	 * @param act Actor
	 * @return True if successful
	 */
	public boolean addActor(Actor act) {
		if(act == null) 
			return false;
		
		// Check bounds
		int x = act.getX();
		int y = act.getY();
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Add to the array list
		actorList.add(act);
		return true;
	}
	
	/**
	 * Return a value at (x,y) in the collision map
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Integer that represents the collision object
	 */
	public byte getCollidable(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return -1;
		
		return collideMap[x][y];
	}
	
	/**
	 * Return an item at coordinate (x,y) from within the item map (itemMap)
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Item the item that is found at (x,y)
	 */
	public Item getItem(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return null;
		
		return itemMap[x][y];
	}
	
	/**
	 * Return an actor at index in the actorList ArrayList
	 * 
	 * @param idx Index in actorList
	 * @return Actor (null if non-existant)
	 */
	public Actor getActor(int idx) {
		Actor act = null;
		try {
			act = actorList.get(idx);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return act;
	}
	
	/**
	 * Find and return the player object within the local actorList ArrayList
	 * 
	 * @return The player object. null if not found
	 */
	public Player getPlayer() {
		// Get from the object map
		for(Actor g : actorList) {
			if(g.getType() == GameObject.OBJECT_PLAYER)
				return (Player)g;
		}
		
		return null;
	}
	
	/**
	 * Return an actor at coordinate (x,y)
	 * 
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param notPlayer If true, ignore a "Player" actor at (x,y)
	 * @return Actor (null if an actor doesn't exist at the position)
	 */
	public Actor getActor(int x, int y, boolean notPlayer) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return null;
		
		// Get from the object map
		for(Actor g : actorList) {
			if(notPlayer && g.getType() == GameObject.OBJECT_PLAYER)
				continue;
			
			if(g.getX() == x && g.getY() == y) {
				return g;
			}
		}
		
		return null;
	}
	
	/**
	 * Remove an actor from actorList based on index. Be careful when using this! Just because an actor isn't in the map doesn't mean it's not 'alive'
	 * This is primarily for the editor
	 * 
	 * @param idx Index of the actor
	 */
	public void removeActor(int idx) {		
		actorList.remove(idx);
	}
	
	/**
	 * Remove an item from the item array by coordinate (x, y)
	 * 
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void removeItem(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return;
		
		if(itemMap[x][y].getType() == GameObject.OBJECT_DOT)
			dotsRemaining--;
		
		itemMap[x][y] = null;
	}
	
	/**
	 * Remove everything at coordiante (x,y)
	 * Used by the editor only
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return boolean True if something was removed, false if otherwise
	 */
	public boolean removeAnyAt(int x, int y) {
		boolean rm = false;
		
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Remove any collidable
		if(collideMap[x][y] != 0) {
			collideMap[x][y] = 0;
			rm = true;
		}
		
		// Remove any item
		if(itemMap[x][y] != null) {
			itemMap[x][y] = null;
			rm = true;
		}
		
		// Remove any actor
		for(int i = 0; i < actorList.size(); i++) {
			Actor a = actorList.get(i);
			if(a.getX() == x && a.getY() == y) {
				actorList.remove(i);
				a = null;
				i--;
				rm = true;
			}
		}
		
		return rm;
	}
	
	/**
	 * Find the distance (Manhattan) between two objects
	 * 
	 * @param start GameObject at the initial position
	 * @param end GameObject at the end position
	 * @return Distance (integer)
	 */
	public int findDistance(GameObject start, GameObject end) {
		return (int)Math.sqrt(Math.pow(Math.abs(start.getX()-end.getX()), 2) + Math.pow(Math.abs(start.getY()-end.getY()), 2));
	}
	
	/**
	 * Check if a coordinate is completely empty (void of actorList, items, and collissions)
	 * Used by the editor
	 * 
	 * @param x A x coordinate to move to
	 * @param y A y coordinate to move to
	 * @return True if empty. False if otherwise
	 */
	public boolean isEmpty(int x, int y) {
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if the Object is hitting something on the collideMap
		if(getCollidable(x, y) != 0)
			return false;
		
		// Check if object is hitting something on the itemMap
		if(getItem(x, y) != null)
			return false;
		
		// Actor collission
		if(getActor(x, y, false) != null)
			return false;
		
		return true;
	}
	
	/**
	 * Move attempt method. Changes the position the map of the game object if there are no obstructions
	 * 
	 * @param act The actor object trying to move
	 * @param x A x coordinate to move to
	 * @param y A y coordinate to move to
	 * @return True if the move succeeded. False if otherwise
	 */
	public boolean canMove(Actor act, int x, int y) {
		if(act == null)
			return false;
		
		// Check bounds
		if(x < 0 || y < 0 || x >= mapWidth || y >= mapHeight)
			return false;
		
		// Check if the Object is hitting something on the collideMap
		if(getCollidable(x, y) != 0)
			return false;
		
		// Allow the Actor to move
		return true;
	}
	
	/**
	 * Get the cost of moving through the given tile. This can be used to 
	 * make certain areas more desirable. A simple and valid implementation
	 * of this method would be to return 1 in all cases.
	 * 
	 * @param mover The mover that is trying to move across the tile
	 * @param sx The x coordinate of the tile we're moving from
	 * @param sy The y coordinate of the tile we're moving from
	 * @param tx The x coordinate of the tile we're moving to
	 * @param ty The y coordinate of the tile we're moving to
	 * @return The relative cost of moving across the given tile
	 */
	public float getCost(Actor mover, int sx, int sy, int tx, int ty) {
		return 1;
	}

	
	/**
	 * Write the contents of this map to a file in the correct format
	 * 
	 * @param filename File name of the map 
	 */
	public void write(String filename) {
		FileOutputStream fout;
		DataOutputStream data;
		
		try {
			fout = new FileOutputStream(filename);
			data = new DataOutputStream(fout);
			
			// Write the map file magic
			data.writeUTF("RKPACMAP");
			
			// Write map width & height
			data.writeInt(mapWidth);
			data.writeInt(mapHeight);
			
			// Write the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					data.write(collideMap[x][y]);
				}
			}
			
			// Write the item map
			Item item = null;
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					item = itemMap[x][y];
					
					// If an item doesnt exist at (x,y), write 'false' for nonexistant and continue
					if(item == null) {
						data.writeBoolean(false);
						continue;
					}
					data.writeBoolean(true);
					
					// Write properties of the item
					data.writeInt(item.getType());
					data.writeInt(item.getX());
					data.writeInt(item.getY());
					data.writeInt(item.getColor().getRGB());
					if(item.getType() == GameObject.OBJECT_TELEPORT) {
						data.writeInt(item.getTeleportX());
						data.writeInt(item.getTeleportY());
					}
				}
			}
			
			// Write the number of actorList, then all actor data
			data.writeInt(actorList.size());
			for(Actor a : actorList) {
				data.writeInt(a.getType());
				data.writeInt(a.getX());
				data.writeInt(a.getY());
				data.writeInt(a.getColor().getRGB());
				if(a.getType() == GameObject.OBJECT_GHOST) {
					data.writeBoolean(((Ghost) a).isTrapped());
				}
			}
			
			data.close();
			fout.close();
		} catch(IOException e) {
			System.out.println("Failed to write map file: " + e.getMessage());
		}
	}
	
	/**
	 * Read a file with map contents and set the properties in this map
	 * Called by the constructor.
	 * 
	 * @param filename File name of the map
	 */
	private void read(String filename) {
		
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream(filename);
			data = new DataInputStream(fin);
			
			// Check for the magic
			if(!data.readUTF().equals("RKPACMAP")) {
				System.out.println("Not a map file!");
				return;
			}
			
			// Read map width & height
			mapWidth = data.readInt();
			mapHeight = data.readInt();
			dotsRemaining = 0;
			
			// Initialize collideMap, a 2D array that contains all static collidable GameObjects
			// We use this for fast lookup during collision detection and AI movement paths
			collideMap = new byte[mapWidth][mapHeight];
			
			// Initialize itemMap, a 2D array that contains items (dots, powerups, cherry) on the map
			itemMap = new Item[mapWidth][mapHeight];
			
			// Create m_objects, an arraylist with all actorList
			actorList = new ArrayList<Actor>();
			
			// Read the collision map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					addCollidable(x, y, data.readByte());
				}
			}
			
			// Read the item map
			for(int x = 0; x < mapWidth; x++) {
				for(int y = 0; y < mapHeight; y++) {
					// If an item doesnt exist at (x,y), continue
					if(!data.readBoolean())
						continue;
					
					// Read and set properties of the item
					int t = data.readInt();
					int ix = data.readInt();
					int iy = data.readInt();
					Color c = new Color(data.readInt());
					addItem(new Item(t, c, this, ix, iy));
					if(t == GameObject.OBJECT_TELEPORT) {
						int teleX = data.readInt();
						int teleY = data.readInt();
						itemMap[ix][iy].setTeleport(teleX, teleY);
					}
				}
			}
			
			// Read the number of actorList, then all actor data
			int nActorsSize = data.readInt();
			for(int i = 0; i < nActorsSize; i++) {
				int t = data.readInt();
				int ix = data.readInt();
				int iy = data.readInt();
				Color c = new Color(data.readInt());
				if(t == GameObject.OBJECT_PLAYER) {
					addActor(new Player(this, ix, iy));
				} else if(t == GameObject.OBJECT_GHOST) {
					boolean trap = data.readBoolean();
					addActor(new Ghost(c, this, ix, iy, trap));
				} else {
					addActor(new Actor(t, c, this, ix, iy));
				}
			}
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read map file: " + e.getMessage());
		}
	}
	
}
