import java.util.*;
import java.io.*;

/**
 * ScoreFile tracks all of the games High-Scores and keeps the scorefile in sync
 * 
 * @author Ramsey Kant
 */
public class ScoreFile {
	
	private ArrayList<Score> scores;

	/**
	 * Class Constructor
	 */
	public ScoreFile() {
		scores = new ArrayList<Score>();
		readScores();
	}
	
	// Public Functions

	/**
	 * Add a score to the internal scores List and save the changed scores file
	 * 
	 * @param name Name of the player that made the high score
	 * @param score Actual session score to associate with the player
	 */
	public void addScore(String name, int score) {
		// Create an instance of the scores class and add it to the list
		Score s = new Score(name, score);
		scores.add(s);
		
		// Write back to the Scores file (doing so will also sort the List)
		saveScores();
	}

	/**
	 * Returns a Score object from the internal scores ArrayList at a particular index
	 * Note: The ArrayList contains scores sorted in descending order (ie. index 0 will be the highest)
	 * 
	 * @param index Index of the Score
	 * @see ScoreFile#getNumScores()
	 * @return Score Score object at the requested index
	 */
	public Score getScore(int index) {
		return scores.get(index);
	}

	/**
	 * Returns the number of Scores stored in the ScoreFile
	 * 
	 * @return int Number of Score objects stored in the internal ArrayList
	 */
	public int getNumScores() {
		return scores.size();
	}
	
	/**
	 * Write all tracked Score objects back to the pacman.scores file
	 */
	public void saveScores() {
		FileOutputStream fout;
		DataOutputStream data;

		// Sort scores so the top 10 are at the beginning of the List
		Collections.sort(scores);
		
		try {
			fout = new FileOutputStream("pacman.scores");
			data = new DataOutputStream(fout);
			
			// Write the score file magic
			data.writeUTF("RKPACSCORESv2");
			
			// Cap the number of scores to write to 10 (first 10 are sorted and the highest)
			int numScores = scores.size();
			if(numScores > 10)
				numScores = 10;

			// Write # of scores in the file, then the actual scores
			data.writeInt(numScores);
			for(int i = 0; i < numScores; i++) {
				data.writeUTF(scores.get(i).getName());
				data.writeInt(scores.get(i).getScore());
			}
			
			data.close();
			fout.close();
		} catch(IOException e) {
			System.out.println("Failed to write score file: " + e.getMessage());
		}
	}
	
	/**
	 * Read the scores file to populate the internal ArrayList of scores
	 */
	public void readScores() {
		FileInputStream fin = null;
		DataInputStream data = null;
		
		// Clear the current scores List
		scores.clear();
		
		try {
			fin = new FileInputStream("pacman.scores");
			data = new DataInputStream(fin);
			
			// Check for the magic
			if(!data.readUTF().equals("RKPACSCORESv2")) {
				System.out.println("Not a score file!");
			} else {
			
				// Read in a maximum of 10 scores from the file
				String n;
				int s;
				int numScores = data.readInt();
				if (numScores > 10)
					numScores = 10;
				for(int i = 0; i < numScores; i++) {
					n = data.readUTF();
					s = data.readInt();
					scores.add(new Score(n, s));
				}
			}
			
			if(data != null)
				data.close();
			
			if(fin != null)
				fin.close();
		} catch(IOException e) {
			System.out.println("Failed to read score file: " + e.getMessage());
		}

		// Sort scores so the top 10 are at the beginning of the List
		Collections.sort(scores);
	}
}
