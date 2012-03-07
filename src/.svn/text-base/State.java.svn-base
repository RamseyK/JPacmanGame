import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A State is a mode of the program where input and functionality are radically different from others portions of the program. We can then effectively separate
 * different logical facilities into their own State subclasses
 * 
 * @author Ramsey Kant
 */
public abstract class State implements KeyListener {
	// Game States
	public static final int STATE_MENU = 1;
	public static final int STATE_SCOREBOARD = 2;
	public static final int STATE_GAME = 4;
	public static final int STATE_DEAD = 8;
	//public static final int STATE_GAMEOVER = 16;
	public static final int STATE_EDITOR = 32;
	public static final int STATE_EXITING = 64;
	
	protected Game game;
	
	/**
	 * Class Constructor
	 * @param g Reference to the game
	 */
	public State(Game g) {
		game = g;
		reset();
	}
	
	/**
	 * Return the reference to the game object
	 * @return Reference to the game object
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Start or reset the state
	 * 
	 * Can be called either by the Supervisor or the state itself
	 */
	public abstract void reset();
	
	/**
	 * Primary logic function called in the mainThreadLoop
	 * 
	 * Called only by the Supervisor
	 */
	public abstract void logic();
	
	/**
	 * Signals the state to terminate. Any final updates should be performed here
	 * THIS IS ONLY CALLED INSIDE CHANGESTATE() - DO NOT CALL THIS ANYWHERE ELSE
	 */
	public abstract void end();
	
	/*
	 * Human Input default
	 */
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// Esc
		switch(e.getKeyChar()) {
			case 27:
				game.requestChangeState(STATE_EXITING);
				break;
			default:
				break;
		}
	}
}