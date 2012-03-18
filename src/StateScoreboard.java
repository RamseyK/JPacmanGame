import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * StateScoreboard is a mode of the program that allows the user to view HighScores in the ScoreFile
 * StateScoreboard is a subclass of State
 * 
 * @author Ramsey Kant
 */
public class StateScoreboard extends State {

	private ScoreFile g_scoreFile;

	/**
	 * Class Constructor
	 * @param g Reference to the Game class
	 */
	public StateScoreboard(Game g) {
		super(g);
		g_scoreFile = g.getScoreFile();
	}
	
	// Public Functions
	
	/**
	 * Setup the scoreboard by pulling score data from the games Scores class
	 * 
	 * @see State#reset()
	 */
	@Override
	public void reset() {
	}

	/**
	 * Cleanup objects
	 * 
	 * @see State#end()
	 */
	@Override
	public void end() {
	}

	/**
	 * Render the Scoreboard and perform any updates
	 */
	@Override
	public void logic() {
		Graphics2D g = game.getGraphicsContext();
		
		// Draw title
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 72));
		g.fillArc(156, 92, 100, 100, 35, 270); // First pacman
		g.drawString("Scores", 450, 180);
		g.fillArc(960, 92, 100, 100, 35, 270);
		g.fillRect(150, 200, 910, 5);
		
		// Draw "back to main menu" text at the bottom left
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		g.drawString("(m) Main Menu", 50, 900);
		
		// Draw scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		
		int numScores = g_scoreFile.getNumScores();
		Score s = null;
		for(int i = 0; i < numScores; i++) {
			s = g_scoreFile.getScore(i);
			g.drawString((i+1) + ". " + s.getName(), 150, 240+(i*40));
			g.drawString(s.getScore() + " ", 960, 240+(i*40));
		}
	}
	
	// Input functions

	/**
	 * Process input on the scoreboard (exit)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_M:
				game.requestChangeState(STATE_MENU);
				break;
			default:
				break;
		}
	}

}
