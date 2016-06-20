package models;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Game.
 */
public class Game {
	
	/** The game id. */
	private int gameID;
	
	/** The lobby. */
	private Lobby lobby;
	
	/** The player i ds. */
	private ArrayList<Integer> playerIDs;
	
	/** The player leader. */
	private int playerLeader;
	
	/** The bots boolean. */
	private boolean botsBoolean = false;
	
	/** The gamerunning. */
	private boolean gamerunning = false;
	
	/**
	 * Instantiates a new game.
	 *
	 * @param gameID the game id
	 * @param lobby the lobby
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public Game (int gameID, Lobby lobby) throws IllegalArgumentException{
		if(lobby == null)
			throw new IllegalArgumentException("Jedes Game-Objekt muss einer Lobby angehoeren");
		this.playerIDs = new ArrayList<Integer>();
		this.gameID = gameID;
		this.lobby = lobby;
	}

	/**
	 * Gets the player i ds.
	 *
	 * @return the player i ds
	 */
	public ArrayList<Integer> getPlayerIDs() {
		return this.playerIDs;
	}
	
	/**
	 * Adds PlayerID with ID to game.
	 *
	 * @param playerID the player id
	 * @return false if game has three players, true else
	 */
	public boolean addPlayer(int playerID) {
	if(playerID != 0) {
		if (this.playerIDs.size() <3) {
			if (this.playerIDs.size() == 0) {
				this.playerLeader = playerID;
			}
			this.playerIDs.add(new Integer(playerID));
			lobby.playerJoinedGame(playerID, gameID);
			return true;
		}
	}
		return false;
	}
	
	/**
	 * Removes a player from the game
	 * 
	 * Make sure to set players current gameID to 0 before calling this.
	 *
	 * @param playerID the player id
	 * @return true, if successful
	 */
	public boolean removePlayer(int playerID){
	
		this.playerIDs.remove(new Integer(playerID));
		this.lobby.playerLeftGame(playerID);
		
		if (countPlayers()==0) {
			return false;
		} else {
			if (playerID == this.playerLeader) {
				this.playerLeader=this.playerIDsToArray()[0];
			}
			return true;
		}
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return gameID;
	}
	
	/**
	 * Returns The game's ID followed by the player's IDs. 0 if there is a free spot
	 * @return {GameID, FirstPlayerID, SecondPlayerID, ThirdPlayerID, gameIsRunnning}
	 */
	public String[] toArray() {
		String[] gamereturn = new String[5];
		gamereturn[0] = this.gameID+"";
		gamereturn[1] = 0+"";
		gamereturn[2] = 0+"";
		gamereturn[3] = 0+"";
		
		int n = 1;
		ArrayList<Integer> playerIDs = this.getPlayerIDs();
		for(Integer playerID: playerIDs){
			gamereturn[n]=playerID+"";
			n++;
		}
		if (this.gamerunning) {
			gamereturn[4] = 1+"";
		} else {
			gamereturn[4] = 0+"";
		}
		return gamereturn;
	}
	
	/**
	 * Returns the PlayerIDs in the game as Integer.
	 * @return {FirstPlayerID, SecondPlayerID, ThirdPlayerID}
	 */
	public int[] playerIDsToArray() {
		int[] gamereturn = new int[3];
		gamereturn[0] = 0;
		gamereturn[1] = 0;
		gamereturn[2] = 0;
		int n = 0;
		ArrayList<Integer> playerIDs = this.getPlayerIDs();
		for(Integer playerID: playerIDs){
			gamereturn[n]=playerID;
			n++;
		}
		return gamereturn;
	}

	/**
	 * Count players.
	 *
	 * @return the int
	 */
	public int countPlayers() {
		return playerIDs.size();
	}

	/**
	 * Sets the flag ready.
	 *
	 * @param playerID the player id
	 * @return the int
	 */
	public int setFlagReady(int playerID) {
		Player player = this.lobby.getPlayers().get(playerID);
		player.changereadyflag();
		int n=0;
		ArrayList<Integer> playerIDs = this.getPlayerIDs();
		for(Integer playerIDcheck: playerIDs){
			Player playercheck = this.lobby.getPlayers().get(playerIDcheck);
			if (playercheck.isReadyflag()) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Player namesto string.
	 *
	 * @return the string[]
	 */
	public String[] playerNamesToString() {
		String[] gamereturn = new String[3];
		gamereturn[0] = 0+"";
		gamereturn[1] = "Computer";
		gamereturn[2] = "Computer";
		int n = 0;
		ArrayList<Integer> playerIDs = this.getPlayerIDs();
		for(Integer playerID: playerIDs){
			gamereturn[n]=this.lobby.getPlayerNamefromID(playerID);
			n++;
		}
		return gamereturn;
	}

	/**
	 * Checks if is gamerunning.
	 *
	 * @return true, if is gamerunning
	 */
	public boolean isGamerunning() {
		return gamerunning;
	}

	/**
	 * Sets the gamerunning.
	 *
	 * @param gamerunning the new gamerunning
	 */
	public void setGamerunning(boolean gamerunning) {
		this.gamerunning = gamerunning;
	}

	/**
	 * Gets the show text.
	 *
	 * @return the show text
	 */
	public String getShowText() {
		StringBuilder sb = new StringBuilder(5);
		String[] a = this.playerNamesToString();
		sb.append("ID: "+this.gameID+" ");
		sb.append(a[0]+" ");
		sb.append(a[1]+" ");
		sb.append(a[2]+" Game is running:");
		sb.append(this.isGamerunning());
		return sb.toString();
	}

	/**
	 * Gets the game id.
	 *
	 * @return the game id
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * Sets the game id.
	 *
	 * @param gameID the new game id
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * Gets the player leader.
	 *
	 * @return the player leader
	 */
	public int getPlayerLeader() {
		return playerLeader;
	}

	/**
	 * Sets the player leader.
	 *
	 * @param playerLeader the new player leader
	 */
	public void setPlayerLeader(int playerLeader) {
		this.playerLeader = playerLeader;
	}

	/**
	 * Checks if is bots boolean.
	 *
	 * @return true, if is bots boolean
	 */
	public boolean isBotsBoolean() {
		return botsBoolean;
	}

	/**
	 * Sets the bots boolean.
	 *
	 * @param botsBoolean the new bots boolean
	 */
	public void setBotsBoolean(boolean botsBoolean) {
		this.botsBoolean = botsBoolean;
	}

	/**
	 * Sets the player i ds.
	 *
	 * @param playerIDs the new player i ds
	 */
	public void setPlayerIDs(ArrayList<Integer> playerIDs) {
		this.playerIDs = playerIDs;
	}

}
