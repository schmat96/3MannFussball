package models;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * The Class Lobby.
 */
public class Lobby {
	
	/** The number of games. */
	int numberOfGames = 0;
	
	/** The number of players. */
	int numberOfPlayers = 0;
	
	/** The number of old games. */
	private int numberOfOldGames = 0;
	
	/** The games. */
	private Hashtable<Integer, Game> games;
	
	/** The players. */
	private Hashtable<Integer, Player> players;
	
	/** The oldgames. */
	private Hashtable<Integer, OldGame> oldgames;
	
	
	/**
	 * Constructor for Lobby. Calls all Hashtable and resets them.
	 */
	public Lobby(){
		games = new Hashtable<Integer, Game>();
		players = new Hashtable<Integer, Player>();
		oldgames = new Hashtable<Integer, OldGame>();
	}

	/**
	 * Adds the game.
	 *
	 * @param game the game
	 * @throws IllegalArgumentException the illegal argument exception
	 * @Deprecated 
	 */


	/**
	 * Add a game to the lobby
	 * @param game
	 */
	public void addGame(Game game) throws IllegalArgumentException{
		if(game == null) {
			throw new IllegalArgumentException("Cannot add Game: null");			
		}
		games.put(game.getGameID(), game);
	}
	
	/**
	 * Add a player to the lobby.
	 *
	 * @param player the player
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void addPlayer (Player player) throws IllegalArgumentException{
		if(player == null) {
			throw new IllegalArgumentException("Cannot add Player: null");
		}
		players.put(player.getPlayerID(), player);
	}
	
	/**
	 * remove game with id from lobby.
	 *
	 * @param gameID the game id
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void removeGame (int gameID) throws IllegalArgumentException{
		if(this.games.containsKey(gameID) == false)
			throw new IllegalArgumentException("Es kann kein Spiel entfernt werden, der nicht existiert");
		games.remove(gameID);
	}
	
	/**
	 * remove player with id from lobby.
	 *
	 * @param playerID the player id
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void removePlayer (int playerID) throws IllegalArgumentException{
		if(this.players.containsKey(playerID) == false)
			throw new IllegalArgumentException("Es kann kein Spieler entfernt werden, der nicht existiert");
		players.remove(playerID);	
	}

	/**
	 * Updates the player name with the given playerID.
	 *
	 * @param playerID the player id
	 * @param newName the new name
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updatePlayerName(int playerID, String newName) throws IllegalArgumentException {
		if (this.players.containsKey(playerID)){
			this.players.get(playerID).setPlayerName(newName);
		}
	}
	
	/**
	 * Updates the Players. F.E. if the readyflag Boolean changes.
	 *
	 * @param playerID the player id
	 * @param newName Sets newName
	 * @param currentGameID 0 if the player is currently in no game
	 * @param readyflag true or false
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void updatePlayer(int playerID, String newName, int currentGameID, int readyflag) throws IllegalArgumentException {
		if (this.players.containsKey(playerID)){
			this.players.get(playerID).setPlayerName(newName);
			this.players.get(playerID).setCurrentGameID(currentGameID);
			if(readyflag == 1){
			this.players.get(playerID).setReadyflag(true);
			} else if(readyflag == 0) {
			this.players.get(playerID).setReadyflag(false);
			} else {
			throw new IllegalArgumentException("Als letztes Argument muss entwerder 0 oder 1 mitgegeben werden");
			}
		}
	}
	
	/**
	 * Retruns the player if it exists.
	 * @param playerID the playerID for the check.
	 * @return the Player for the param playerID
	 */
	public boolean playerIDExists(int playerID){
		return this.players.containsKey(playerID);
	}

	/**
	 * Clears all Player and Games Hashtables.
	 */
	public void clearAll() {
		// TODO Auto-generated method stub
		this.players = new Hashtable<Integer, Player>();
		this.games = new Hashtable<Integer, Game>();

	}

	/**
	 * Gets the games.
	 *
	 * @return the games
	 */
	public Hashtable<Integer, Game> getGames() {
		// TODO Auto-generated method stub
		return games;
	}
	
	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Hashtable<Integer, Player> getPlayers() {
		return players;
	}
	
	/**
	 * Returns ID from PlayerName. If there is no Player with this Name, -1 is returned.
	 *
	 * @param name the name
	 * @return the player i dfrom name
	 */
	public int getPlayerIDfromName(String name) {
		 Set<Integer> keys = players.keySet();
		    //Obtaining iterator over set entries
		    Iterator<Integer> itr = keys.iterator();
		
		    while (itr.hasNext()) { 
		       int p = itr.next();
		       if (players.get(p).getPlayerName().equals(name)) {
		    	   return p;
		       }
		    }
		return -1;
	}
	
	/**
	 * returns Player Name from ID.
	 *
	 * @param id the id
	 * @return the player namefrom id
	 */
	public String getPlayerNamefromID(int id) {
		if (id == 0) {
			return "Computer";
		}
		Player p = players.get(id);
		return p.getPlayerName();
	}
	
	/**
	 * Used to test if player is currently in a game<br>.
	 *
	 * @param playerID the player id
	 * @return GameID if player is in game, else 0;
	 */
	public int playerInGame(int playerID){
		Player player = players.get(playerID);
		return player.getCurrentGameID();
	}

	/**
	 * Updates the players current gameID.
	 *
	 * @param playerID the player id
	 * @param gameID the game id
	 */
	public void playerJoinedGame(int playerID, int gameID) {
		Player player = players.get(playerID);
		player.setCurrentGameID(gameID);
	}
	
	/**
	 * Resets the players current gameID to 0.
	 *
	 * @param playerID the player id
	 */
	public void playerLeftGame(int playerID) {
		Player player = players.get(playerID);
		player.setCurrentGameID(0);
	}
	
	
	/**
	 * Gets the all old arrays.
	 *
	 * @return the all old arrays
	 */
	public String[] getAllOldArrays(){

		String[] allOldGames = new String[(oldgames.size()*7)];
		Set<Integer> keys = oldgames.keySet();
	    Iterator<Integer> itr = keys.iterator();
	    int number = 0;
	    while (itr.hasNext()) { 
	       int key = itr.next();
	       OldGame oldgame = oldgames.get(key);
	       String[] oldgamestringarray = oldgame.toArray();
	       for (int i=0;i<7;i++) {
				allOldGames[number*7+i] = oldgamestringarray[i];
			}
	       number++;
	    }
	    return allOldGames;
	}

	/**
	 * Adds a new Old Game to the Lobby Window.
	 * #TODO Make a new Window instead of a JPane.
	 *
	 * @param playername the 3 player names of the specific old game.
	 * @param playergoals how many goals they got.
	 * @param dates the dates
	 */
	public void addOldGame(String[] playername, int[] playergoals, String dates){
		OldGame oldgame = new OldGame(playername, playergoals, dates, getNumberOfOldGames()+1);
		oldgames.put(getNumberOfOldGames()+1, oldgame);
		setNumberOfOldGames(getNumberOfOldGames() + 1);
	}
	
	/**
	 * Checks if a player name exists with a given String s. Returns true if he exists.
	 *
	 * @param s the s
	 * @return true if a player with the given Strings exists.
	 */
	public boolean playerNameExists(String s) {
		Set<Integer> userKeys = players.keySet();
		for (Integer userID : userKeys) {
			if (players.get(userID).getPlayerName().equals(s)) {
				return true;
			}
		}
		return false;
	}
				


	/**
	 * Gets the number of old games.
	 *
	 * @return the number of old games
	 */
	public int getNumberOfOldGames() {
		return numberOfOldGames;
	}

	/**
	 * Sets the number of old games.
	 *
	 * @param numberOfOldGames the new number of old games
	 */
	public void setNumberOfOldGames(int numberOfOldGames) {
		this.numberOfOldGames = numberOfOldGames;
	}

	/**
	 * Gets the oldgames.
	 *
	 * @return the oldgames
	 */
	public Hashtable<Integer, OldGame> getOldgames() {
		return oldgames;
	}

	/**
	 * Sets the oldgames.
	 *
	 * @param oldgames the oldgames
	 */
	public void setOldgames(Hashtable<Integer, OldGame> oldgames) {
		this.oldgames = oldgames;
	}

	/**
	 * Sets the games.
	 *
	 * @param games the games
	 */
	public void setGames(Hashtable<Integer, Game> games) {
		this.games = games;
	}

	/**
	 * Gets the game.
	 *
	 * @param playerGameID the player game id
	 * @return the game
	 */
	public Game getGame(int playerGameID) {
		return this.games.get(playerGameID);
	}
	
}
