import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * The Game Supervisor. This class implements that core program logic, state management, and graphics.
 * 
 * @author Ramsey Kant
 */
@SuppressWarnings("serial")
public class Game extends Canvas {
	// Debug vars
	private boolean debugEnabled;
	
	// Threading
	private boolean runMainThread;
	
	// Graphics variables
	private Frame frame;
	public final int RES_X;
	public final int RES_Y;
	private BufferStrategy gfxBuffer;
	
	// State
	private int stateId;
	private State currentState;
	private boolean changeStateRequested;
	private int requestedState;
	private String startMap;

	// Score file - global tracker of game scores
	private ScoreFile scoreFile;
	
	/**
	 * Class Constructor
	 * Set's up graphics and put's game logic into a startup state by calling init()
	 * 
	 * @param x Resolution X
	 * @param y Resolution Y
	 * @see Game#init()
	 */
	public Game(int x, int y) {		
		// Set resolution settings
		RES_X = x;
		RES_Y = y;
		
		// Init game
		init();
	}
	
	/**
	 * Startup functionality for the program called by the constructor
	 */
	private void init() {
		// Debug vars
		debugEnabled = false;
		
		startMap = "test.map";
		changeStateRequested = false;
		
		// Setup the game frame
		frame = new Frame("Pacman");
		frame.setLayout(null);
		setBounds(0, 0, RES_X, RES_Y);
		frame.add(this);
		frame.setSize(RES_X, RES_Y);
		frame.setResizable(false);
		frame.setVisible(true);
		
		// Set the exit handler with an anonymous class
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// Exit main thread
				runMainThread = false;
			}
		});
		
		// Setup double buffering
		setIgnoreRepaint(true); // We'll handle repainting
		createBufferStrategy(2);
		gfxBuffer = getBufferStrategy();

		// Load the scores file
		scoreFile = new ScoreFile();
		
		runMainThread = true;
	}
	
	// Getter and Setter methods
	
	/**
	 * Get the Frame object encapsulating the program
	 * 
	 * @return The frame
	 */
	public Frame getFrame() {
		return frame;
	}
	
	/**
	 * Get a 'handle' of the current graphics buffer for drawing
	 * 
	 * @return The Graphics2D buffer
	 */
	public Graphics2D getGraphicsContext() {
		return (Graphics2D) gfxBuffer.getDrawGraphics();
	}
	
	/**
	 * Get the name of the map to be loaded in StateGame
	 * 
	 * @return Map name (with .map extension)
	 */
	public String getStartMap() {
		return startMap;
	}
	
	/**
	 * Set the default starting map (set by menu)
	 * 
	 * @param m The name of the map to load (with the .map extension)
	 */
	public void setStartMap(String m) {
		startMap = m;
	}

	/**
	 * Get the global Score-file representation that interfaces directly with the .scores file
	 * 
	 * @return Score ScoreFile class instance
	 */
	public ScoreFile getScoreFile() {
		return scoreFile;
	}
	
	/**
	 * Return the current debug setting
	 * 
	 * @return True if debug setting is on
	 * @see Game#toggleDebug()
	 */
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	/**
	 * Toggle debugging. Facilities like AIManager use this flag to display diagnostic information like AI paths
	 */
	public void toggleDebug() {
		debugEnabled = !debugEnabled;
	}
	
	// Public Methods
	
	/**
	 * Called by other states to safely change currentState. This is done so the currentState's logic can finish
	 * 
	 * @see Game#mainThreadLoop()
	 */
	public void requestChangeState(int state) {
		requestedState = state;
		changeStateRequested = true;
	}
	
	/**
	 * The main game loop that handles graphics and game state determination
	 */
	public void mainThreadLoop() {
		while(runMainThread) {
			// If a state change was requested, execute it now
			if(changeStateRequested) {
				changeStateRequested = false;
				changeState(requestedState);
				continue;
			}
			
			Graphics2D g = getGraphicsContext();
			
			// Wipe the screen
			g.setColor(Color.black);
			g.fillRect(0, 0, RES_X, RES_Y);
			
			// Run the logic of the current game state here
			currentState.logic();
			
			// Show the new buffer
			g.dispose();
			gfxBuffer.show();
			
			// Synchronize framerate
			try {
				Thread.sleep(20); // Rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Private Methods
	
	/**
	 * Change the state of the game. Called in mainThreadLogic()
	 * 
	 * @param state The state to set. Must match the static vars above
	 * @see Game#requestChangeState(int)
	 * @see Game#mainThreadLoop()
	 */
	private void changeState(int state) {
		// Cleanup for the outgoing state
		if(currentState != null) {
			frame.removeKeyListener(currentState);
			removeKeyListener(currentState);
			currentState.end();
		}
		
		// Set the new state type
		stateId = state;
		
		// Instance the new state (reset() is called in the constructor)
		switch(stateId) {
			case State.STATE_GAME:
				currentState = new StateGame(this);
				break;
			case State.STATE_SCOREBOARD:
				currentState = new StateScoreboard(this);
				break;
			case State.STATE_EDITOR:
				currentState = new StateEditor(this);
				break;
			case State.STATE_MENU:
				currentState = new StateMenu(this);
				break;
			case State.STATE_EXITING:
				currentState = null;
				runMainThread = false;
				break;
			default:
				break;
		}
		
		// Setup input handler and reset()
		if(currentState != null) {
			frame.addKeyListener(currentState);
			addKeyListener(currentState);
		}
	}
}
