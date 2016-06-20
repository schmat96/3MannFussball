package models;

// TODO: Auto-generated Javadoc
/**
 * The Class OldGame.
 */
public class OldGame {
	
	/** names of the players. */
	private String playerName[];
	
	/** scores of the players. */
	private int score[];
	
	/** The index. */
	private int index;
	
	/** The date string. */
	private String dateString;

	/**
	 * Constructor for OldGame.
	 *
	 * @param playername2 the playername2
	 * @param playergoals the playergoals
	 * @param string the string
	 * @param index the index
	 */
	public OldGame(String[] playername2, int[] playergoals, String string, int index) {
		this.playerName = playername2;
		this.score = playergoals;
		this.dateString = string;
		this.index = index;
	}

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public String getResults() {
		String results;
		results = "Game was played on: " + dateString + ": \n";
		for (int i = 0; i < 3; i++) {
			results+= playerName[i] + " has gotten " + score[i] + " goals. \n";
		}
		return results;
	}

	/**
	 * Returns the oldGames as String[0..7]
	 * @return the oldGame
	 */
	public String[] toArray() {
		String[] oldGame = new String[7];
		oldGame[0]=playerName[0];
		oldGame[1]=playerName[1];
		oldGame[2]=playerName[2];
		oldGame[3]=score[0]+"";
		oldGame[4]=score[1]+"";
		oldGame[5]=score[2]+"";
		oldGame[6]=dateString;
		return oldGame;
	}

	/**
	 * Sets the index.
	 *
	 * @param i the new index
	 */
	public void setIndex(int i) {
		this.index = i;
		
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return this.dateString;
	}

	/**
	 * Gets the players and goals.
	 *
	 * @param a the a
	 * @return the players and goals
	 */
	public String getPlayersAndGoals(int a) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.playerName[a]+" : ");
		sb.append(this.score[a]+"");
		return sb.toString();
	}

	/**
	 * Gets the count goals.
	 *
	 * @return the count goals
	 */
	public int getCountGoals() {
		return this.score[2]+this.score[1]+this.score[0];
	}

	/**
	 * Gets the player names.
	 *
	 * @return the player names
	 */
	public String[] getPlayerNames() {
		return this.playerName;
	}

}
