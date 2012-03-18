/**
 * Score represents a high score in the Score-File
 * 
 * @author Ramsey Kant
 */
public class Score implements Comparable<Score> {
	private String name;
	private int score;
	//private String dateStr;
	
	/**
	 * Default constructor. Initializes score to zero and sets the name as "Player"
	 */
	public Score() {
		name = "Player";
		score = 0;
	}
	
	/**
	 * Constructor. Name and score must be provided for this score object
	 * 
	 * @param n Name of the player with the high score
	 * @param s Actual session score to associate with the player
	 */
	public Score(String n, int s) {
		name = n;
		score = s;
	}

	/**
	 * CompareTo method that allows sorting methods to rank scores in ascending order.
	 * 
	 * @param other Other score object to compare to this score
	 * @return int Difference between the other score and this score
	 */
	public int compareTo(Score other) {
		return other.getScore() - score;
	}

	/**
	 * Returns the name associated with the Score object
	 * 
	 * @return String Name on the score
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the score associated with the Score object
	 * 
	 * @return int Session score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the name on the Score object
	 * 
	 * @param n Name as a string
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * Sets the actual score amount on the Score object
	 * 
	 * @param s Score as an int
	 */
	public void setScore(int s) {
		score = s;
	}
}

