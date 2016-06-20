package models;


// TODO: Auto-generated Javadoc
/**
 * The Class Player.
 */
public class Player {
	
	/** The player id. */
	private int playerID;
	
	/** The player name. */
	private String playerName;
	
	/** The player name set. */
	private boolean playerNameSet = false;
	
	/** The current game id. */
	private int currentGameID = 0;
	
	/** The readyflag. */
	private boolean readyflag = false;
	
	/** The disconnected. */
	private boolean disconnected;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param id the id
	 */
	public Player (int id){
		this(id,"");
		this.playerNameSet = false;
	}
	
	/**
	 * Instantiates a new player.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public Player(int id, String name) {
		// TODO Auto-generated constructor stub
		this.playerID = id;
		this.playerName = name;
		this.playerNameSet = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return playerID + " " + playerName;
	}

	/**
	 * To array.
	 *
	 * @return the string[]
	 */
	public String[] toArray() {
		String ready;
		if (readyflag) {
			ready = "1";
		} else {
			ready = "0";
		}
		String [] returnArray = {playerID+"", playerName, currentGameID+"", ready};
		return returnArray;
	}

	/**
	 * Changereadyflag.
	 */
	public void changereadyflag() {
			readyflag = !readyflag;
	}

	/**
	 * Checks if is disconnected.
	 *
	 * @return true, if is disconnected
	 */
	public boolean isDisconnected() {
		return disconnected;
	}

	/**
	 * Sets the disconnected.
	 *
	 * @param disconnected the new disconnected
	 */
	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the player id.
	 *
	 * @param playerID the new player id
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Gets the player name.
	 *
	 * @return the player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Sets the player name.
	 *
	 * @param playerName the new player name
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void setPlayerName(String playerName) throws IllegalArgumentException  {
		if(playerName != null && playerName.length() > 2)
			this.playerName = playerName;
		else
			throw new IllegalArgumentException("Der Name muss mindesten Laenge drei haben");
	}

	/**
	 * Checks if is player name set.
	 *
	 * @return true, if is player name set
	 */
	public boolean isPlayerNameSet() {
		return playerNameSet;
	}

	/**
	 * Sets the player name set.
	 *
	 * @param playerNameSet the new player name set
	 */
	public void setPlayerNameSet(boolean playerNameSet) {
		this.playerNameSet = playerNameSet;
	}

	/**
	 * Gets the current game id.
	 *
	 * @return the current game id
	 */
	public int getCurrentGameID() {
		return currentGameID;
	}

	/**
	 * Sets the current game id.
	 *
	 * @param currentGameID the new current game id
	 */
	public void setCurrentGameID(int currentGameID) {
		this.currentGameID = currentGameID;
	}

	/**
	 * Checks if is readyflag.
	 *
	 * @return true, if is readyflag
	 */
	public boolean isReadyflag() {
		return readyflag;
	}

	/**
	 * Sets the readyflag.
	 *
	 * @param readyflag the new readyflag
	 */
	public void setReadyflag(boolean readyflag) {
		this.readyflag = readyflag;
	}
}
