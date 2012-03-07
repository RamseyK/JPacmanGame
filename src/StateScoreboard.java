import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * StateScoreboard is a mode of the program that allows the user to view (and sometimes modify) scores set in StateGame
 * StateScoreboard is a subclass of State
 * 
 * @author Ramsey Kant
 */
public class StateScoreboard extends State {
	
	private String[] names;
	private int[] scores;
	private int numScores;

	/**
	 * Class Constructor
	 * @param g Reference to the Game class
	 */
	public StateScoreboard(Game g) {
		super(g);
	}
	
	// Public Functions
	
	/**
	 * Setup the scoreboard by loading the current score file
	 * 
	 * @see State#reset()
	 */
	@Override
	public void reset() {
		// Only the top 10 scores will be displayed
		names = new String[10];
		scores = new int[10];
		numScores = 0;
		
		// Read in the scores
		//readScores();
	}

	/**
	 * Cleanup objects and write back the scores
	 * 
	 * @see State#end()
	 */
	@Override
	public void end() {
		//saveScores();
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
		
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		
		// Draw scores
		for(int i = 0; i < names.length; i++) {
			if(names[i] == null)
				continue;
			
			g.drawString(names[i], 150, 210);
			g.drawString(scores[i] + " ", 960, 210);
		}
	}
	
	/**
	 * Output names and corresponding scores to the pacman.scores file
	 */
	public void saveScores() {
		FileOutputStream fout;
		DataOutputStream data;
		
		try {
			fout = new FileOutputStream("pacman.scores");
			data = new DataOutputStream(fout);
			
			// Write the score file magic
			data.writeUTF("RKPACSCORES");
			
			// Write # of scores in the file, then the actual scores
			data.writeInt(numScores);
			for(int i = 0; i < numScores; i++) {
				if(names[i] == null)
					break;
				data.writeUTF(names[i]);
				data.writeInt(scores[i]);
			}
			
			data.close();
			fout.close();
		} catch(IOException e) {
			System.out.println("Failed to write score file: " + e.getMessage());
		}
	}
	
	/**
	 * Populate names and scores from the pacman.scores file
	 */
	public void readScores() {
		FileInputStream fin;
		DataInputStream data;
		
		try {
			fin = new FileInputStream("pacman.scores");
			data = new DataInputStream(fin);
			
			// Check for the magic
			if(!data.readUTF().equals("RKPACSCORES")) {
				System.out.println("Not a score file!");
				return;
			}
			
			// Read in scores
			numScores = data.readInt();
			if (numScores > 10)
				numScores = 10;
			for(int i = 0; i < numScores; i++) {
				names[i] = data.readUTF();
				scores[i] = data.readInt();
			}
			
			data.close();
			fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read score file: " + e.getMessage());
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
			case KeyEvent.VK_0:
				game.requestChangeState(STATE_MENU);
				break;
			default:
				break;
		}
	}

}
