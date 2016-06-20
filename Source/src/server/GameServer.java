package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IllegalArgumentsException;
import util.IllegalPortNumberException;
import net.Command;
import net.Option;
import models.Game;
import models.Lobby;
import models.Player;
import server.user.User;
import logic.server.GameLogicServer;
import logic.server.PlayerGameServer;

// TODO: Auto-generated Javadoc
/**
 * The GameServer class is starting the server and has access communicate to all
 * the Clients through an ArrayList of all ClientThreads.
 * 
 * @author Maximilian Reber
 *
 */
public class GameServer {
	
	/** Server Port Number (Comes from command-line arguments). */
	private int portNumber;

	/**
	 * Basic Logger.
	 */
	Logger log = LoggerFactory.getLogger(GameServer.class);

	/** The lobby. */
	private Lobby lobby;

	/** while running, ServerSocket accepts Client connections, default set to true. */
	private volatile boolean running = true;

	/** ServerSocket object. */
	private ServerSocket serverSocket;

	/** List that stores all the ClientThreads, one for each Client. */
	private HashMap<Integer, User> userList;

	/**
	 * Flag to set if the logger should display raw messages from pings. The
	 * default value is false.
	 */
	private boolean logPingMessages = false;

	/**
	 * Flag to set if the logger should display basic messages. The default
	 * value is false.
	 */
	private boolean logBasicMessages = false;

	/** Thread that listens for console inputs. */
	ServerOptionsThread serverOptions;

	/** The server window. */
	private ServerWindow serverWindow;

	/** keeps track of highest PlayerID to ensure uniquenes. */
	private int highestPlayerID = 0;

	/** keeps track of highest GameID to ensure uniquenes. */
	private int highestGameID = 0;

	/** The game logic server. */
	private GameLogicServer[] gameLogicServer = new GameLogicServer[16];

	/**
	 * Starts the ServerSocket on requested port number.
	 *
	 * @throws IllegalPortNumberException the illegal port number exception
	 * @deprecated 
	 */
	public GameServer() throws IllegalPortNumberException {
		// TODO do we need a user list? all users should be in lobby
		userList = new HashMap<Integer, User>();
		this.lobby = new Lobby();

		serverWindow = new ServerWindow(200, 200, this);

		int portNumberInput = serverWindow.getPortNumber();

		/*
		 * stop if illegal port number is used
		 */
		if (portNumberInput < 1024 || portNumberInput > 65536) {
			throw new IllegalPortNumberException(portNumberInput);
		}
		/*
		 * else, save the port number in class variable and start the
		 * ServerSocket
		 */
		this.portNumber = portNumberInput;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			log.error("IOException when starting the Server:\n\t" + e.getMessage() + "");
		}

		/*
		 * Start a new Thread that waits for User input to interact with the
		 * Server and start it
		 */
		serverOptions = new ServerOptionsThread(this);
		serverWindow.openWindow();
		serverOptions.start();

		acceptConnectionsLoop();
	}

	/**
	 * Instantiates a new game server.
	 *
	 * @param string the string
	 * @throws IllegalPortNumberException the illegal port number exception
	 */
	public GameServer(String string) throws IllegalPortNumberException {
		userList = new HashMap<Integer, User>();
		this.lobby = new Lobby();

		serverWindow = new ServerWindow(200, 200, this);

		int portNumberInput = 0;

		try {
			portNumberInput = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new IllegalPortNumberException(portNumberInput);
		}

		/*
		 * stop if illegal port number is used
		 */
		if (portNumberInput < 1024 || portNumberInput > 65536) {
			throw new IllegalPortNumberException(portNumberInput);
		}
		/*
		 * else, save the port number in class variable and start the
		 * ServerSocket
		 */
		this.portNumber = portNumberInput;
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			log.error("IOException when starting the Server:\n\t" + e.getMessage() + "");
		}

		/*
		 * Start a new Thread that waits for User input to interact with the
		 * Server and start it
		 */
		serverOptions = new ServerOptionsThread(this);
		serverWindow.openWindow();
		serverOptions.start();

		acceptConnectionsLoop();
	}

	/**
	 * this method holds a while loop that waits for connections.
	 */
	public void acceptConnectionsLoop() {
		/*
		 * accept new connections
		 */
		try {
			while (running) {
				/*
				 * get the socket from the Client that connects
				 * ------------------------------------------------------------
				 * NOTE: this is also activated from the serverOptions Thread
				 * after setting the running flag to false
				 */
				Socket socket = serverSocket.accept();

				log.info("socket accepted");

				/*
				 * needed when stopping the loop from serverOptions. running
				 * will be false when server connects to itself to break the
				 * loop ...
				 */
				if (!running) {
					log.info("stopping...");
					break;
				}

				/*
				 * ... else, it is a client trying to connect - a Thread is
				 * started to communicate with it. This Thread is then added to
				 * the Threads list
				 */
				User ct = new User(socket, this, ++highestPlayerID);
				this.lobby.addPlayer(ct.getPlayer());
				userList.put(ct.getPlayer().getPlayerID(), ct);
			}

			/*
			 * if we land here, the server is being shut down. now we need to
			 * close all the resources
			 */

			// close the serverSocket since it is no longer needed
			serverSocket.close();

			// close all the resources from and the client sockets itself
			// #TODO Still works with an arrayList. Rework it to a hashmap.
			for (int i = userList.size() - 1; i >= 0; i--) {
				kickUser(i);
				userList.remove(i);
			}
		}
		// error with accepting a client connection
		catch (IOException e) {
			log.error("Error with accepting Client connection:\n\t" + e.getMessage());
		}
	}

	/**
	 * Gets the server Port Number (Comes from command-line arguments).
	 *
	 * @return the server Port Number (Comes from command-line arguments)
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * Method to stop the server: The running flag is set to false.
	 */
	public void stopServer() {
		this.running = false;
		serverOptions.interrupt();
		try {
			new Socket("localhost", getPortNumber()).close();
		} catch (IOException e) {
			log.error(e.getMessage());
			log.error("couldnt close Server");
		}
	}

	/**
	 * Gets the flag to set if the logger should display raw messages from pings.
	 *
	 * @return the flag to set if the logger should display raw messages from pings
	 */
	public boolean getLogPingMessages() {
		return logPingMessages;
	}

	/**
	 * Sets the flag to set if the logger should display raw messages from pings.
	 *
	 * @param logPingMessages the new flag to set if the logger should display raw messages from pings
	 */
	public void setLogPingMessages(boolean logPingMessages) {
		this.logPingMessages = logPingMessages;
	}

	/**
	 * Gets the flag to set if the logger should display basic messages.
	 *
	 * @return the flag to set if the logger should display basic messages
	 */
	public boolean getLogBasicMessages() {
		return logBasicMessages;
	}

	/**
	 * Sets the flag to set if the logger should display basic messages.
	 *
	 * @param logBasicMessages the new flag to set if the logger should display basic messages
	 */
	public void setLogBasicMessages(boolean logBasicMessages) {
		this.logBasicMessages = logBasicMessages;
	}

	/**
	 * Returns true if the given user name is available.
	 *
	 * @param name            User name to check for availability
	 * @return true if available
	 */
	public boolean nameIsAvailable(String name) {
		Set<Integer> userKeys = userList.keySet();
		for (Integer userID : userKeys) {
			if (userList.get(userID).getPlayer().getPlayerName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sends a message to every User.
	 *
	 * @param command the command
	 * @param option the option
	 * @param arguments the arguments
	 */
	public void sendMessageToLobby(Command command, Option option, String[] arguments) {
		Set<Integer> userKeys = userList.keySet();
		for (Integer userID : userKeys) {
			userList.get(userID).sendMessageToClient(command, option, arguments);

		}
	}

	/**
	 * Sends a message to every User in a specific game.
	 *
	 * @param gameID            ID of the specific game
	 * @param command the command
	 * @param option the option
	 * @param arguments the arguments
	 */
	public void sendMessageToGame(int gameID, Command command, Option option, String[] arguments) {

		Game game = this.getGames().get(gameID);
		if (game != null) {

			for (int playerID : game.getPlayerIDs()) {
				User user = this.userList.get(playerID);
				if (user != null) {
					user.sendMessageToClient(command, option, arguments);
				}
			}

		} else {
			// TODO game not found error
		}
	}

	/**
	 * Send message to player.
	 *
	 * @param recipientID the recipient id
	 * @param command the command
	 * @param option the option
	 * @param arguments the arguments
	 */
	public void sendMessageToPlayer(int recipientID, Command command, Option option, String[] arguments) {
		try {
			User user = this.userList.get(recipientID);
			user.sendMessageToClient(command, option, arguments);
		} catch (IndexOutOfBoundsException e) {
			// Player Does not exist
			// Should not happen, Should be caught earlier
			// TODO Handle
		}
	}

	/**
	 * Gets the list that stores all the ClientThreads, one for each Client.
	 *
	 * @return the list that stores all the ClientThreads, one for each Client
	 */
	public HashMap<Integer, User> getUserList() {
		return this.userList;
	}

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Hashtable<Integer, Player> getPlayers() {
		return this.lobby.getPlayers();
	}

	/**
	 * Gets the games.
	 *
	 * @return the games
	 */
	public Hashtable<Integer, Game> getGames() {
		return this.lobby.getGames();
	}

	/**
	 * Kick user.
	 *
	 * @param playerID the player id
	 */
	public void kickUser(int playerID) {
		this.log.info("Kick player " + playerID);
		try {
			User current = userList.get(playerID);
			current.stopThreads();
			current.getUserInputThread().interrupt();
			current.getReader().close();
			current.getWriter().close();
			current.getClientSocket().close();
			userList.remove(playerID);
			Player player = lobby.getPlayers().get(playerID);
			if (player.getCurrentGameID() != 0) {
				Game game = lobby.getGames().get(player.getCurrentGameID());
				if (game.isGamerunning()) {
					this.gameLogicServer[game.getGameID()].endRunningGame(playerID);
				}
			}
			lobby.removePlayer(playerID);
			String[] args = { "" + playerID };
			sendMessageToLobby(Command.UPDATE_LOBBY, Option.REMOVE_PLAYER, args);
		} catch (Exception e) {

		}
	}

	/**
	 * Creates the game.
	 *
	 * @param playerID the player id
	 */
	public void createGame(int playerID) {
		if (lobby.playerIDExists(playerID) && lobby.playerInGame(playerID) == 0) {
			Game game = new Game(++highestGameID, lobby);
			game.addPlayer(playerID);
			lobby.addGame(game);
			sendMessageToLobby(Command.UPDATE_LOBBY, Option.ADD_GAME, game.toArray());
			sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, lobby.getPlayers().get(playerID).toArray());
		}
	}

	/**
	 * Join gamerequest.
	 *
	 * @param playerID the player id
	 * @param GameID the game id
	 */
	public void joinGameRequest(int playerID, int GameID) {
		if (lobby.playerIDExists(playerID) && lobby.playerInGame(playerID) == 0) {
			Game game = this.getGames().get(GameID);
			if (game.isGamerunning()==false) {
				boolean joined = game.addPlayer(playerID);
				if (joined) {
					sendMessageToLobby(Command.UPDATE_LOBBY, Option.ADD_GAME, game.toArray());
					sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, lobby.getPlayers().get(playerID).toArray());
					if (game.isGamerunning()) {
						gameLogicServer[GameID].newPlayerJoines(playerID);
						String[] playerNamefromGame = game.playerNamesToString();
						String[] arguments = { playerNamefromGame[0], playerNamefromGame[1], playerNamefromGame[2],
								game.getGameID() + "", 100 + "" };
						sendMessageToPlayer(playerID, Command.UPDATE_GAME, Option.START_GAME, arguments);
					}
				}
			}
		}
	}

	/**
	 * Leave gamerequest.
	 *
	 * @param playerID the player id
	 * @param GameID the game id
	 */
	public void leaveGameRequest(int playerID, int GameID) {
		if (lobby.playerIDExists(playerID) && lobby.playerInGame(playerID) != 0) {
			Game game = this.getGames().get(GameID);
			this.getPlayers().get(playerID).setCurrentGameID(0);
			boolean gamestillexists = game.removePlayer(playerID);
			if (gamestillexists) {
				sendMessageToLobby(Command.UPDATE_LOBBY, Option.ADD_GAME, game.toArray());
				sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, lobby.getPlayers().get(playerID).toArray());
			} else {
				this.getGames().remove(GameID);
				String[] arguments = { GameID + "" };
				sendMessageToLobby(Command.UPDATE_LOBBY, Option.REMOVE_GAME, arguments);
				sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, lobby.getPlayers().get(playerID).toArray());
			}
		}
	}

	/**
	 * Chnages GameFlag for the Player id with the PlayerID. Checks if all 3
	 * Players are ready, if so the Game Thread Starts
	 *
	 * @param playerID the player id
	 * @param GameID the game id
	 * @param bots the bots
	 * @param GameTime the game time
	 */
	public void setGameFlagReady(int playerID, int GameID, String bots, int GameTime) {
		if (lobby.playerIDExists(playerID) && lobby.playerInGame(playerID) != 0) {
			Game game = this.getGames().get(GameID);
			bots.trim();
			if (game.getPlayerLeader() == playerID && bots.equals("true")) {
				game.setBotsBoolean(true);
			}
			if (game.getPlayerLeader() == playerID && bots.equals("false")) {
				game.setBotsBoolean(false);
			}

			int playersetready = game.setFlagReady(playerID);

			boolean checktostart = false;
			if (playerID == game.getPlayerLeader() && playersetready == game.countPlayers() - 1
					&& game.isBotsBoolean()) {
				checktostart = true;
			}

			if (playerID == game.getPlayerLeader() && playersetready == 2) {
				checktostart = true;
			}

			if (checktostart) {
				int[] playerIDfromGame = game.playerIDsToArray();
				String[] playerNamefromGame = game.playerNamesToString();
				PlayerGameServer player1 = new PlayerGameServer(270, 200, playerIDfromGame[0]);
				PlayerGameServer player2 = new PlayerGameServer(150, 200, playerIDfromGame[1]);
				PlayerGameServer player3 = new PlayerGameServer(350, 200, playerIDfromGame[2]);

				gameLogicServer[GameID] = new GameLogicServer(player1, player2, player3, this, GameID, GameTime);
				game.setGamerunning(true);
				String[] arguments = { playerNamefromGame[0], playerNamefromGame[1], playerNamefromGame[2], GameID + "",
						GameTime + "" };
				this.sendMessageToGame(GameID, Command.UPDATE_GAME, Option.START_GAME, arguments);
			} else {
				sendMessageToLobby(Command.UPDATE_LOBBY, Option.ADD_GAME, game.toArray());
				sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, lobby.getPlayers().get(playerID).toArray());
			}
		}
	}

	/**
	 * Sets new Keyinputs for the Players in the gameLogicServer.
	 *
	 * @param keyinputs the keyinputs
	 * @param playerID the player id
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	public void updateGameLogicServer(String[] keyinputs, int playerID) throws IllegalArgumentsException {
		int gameID = lobby.playerInGame(playerID);
		gameLogicServer[gameID].updateKeyInputs(keyinputs, playerID);
	}

	/**
	 * Adds a new OldGame to the Hashtable and gets current Date.
	 *
	 * @param player1ID the player1 id
	 * @param player2ID the player2 id
	 * @param player3ID the player3 id
	 * @param player1goal the player1goal
	 * @param player2goal the player2goal
	 * @param player3goal the player3goal
	 */
	public void addGameResults(int player1ID, int player2ID, int player3ID, int player1goal, int player2goal,
			int player3goal) {
		String[] playername = { this.lobby.getPlayerNamefromID(player1ID), this.lobby.getPlayerNamefromID(player2ID),
				this.lobby.getPlayerNamefromID(player3ID) };
		int[] playergoals = { player1goal, player2goal, player3goal };
		Date date = new Date();
		String dates = date.toString();
		dates = dates.replaceAll("\\s+", "");
		this.lobby.addOldGame(playername, playergoals, dates);
	}

	/**
	 * Gets the all old games.
	 *
	 * @return the all old games
	 */
	public String[] getAllOldGames() {
		return this.lobby.getAllOldArrays();
	}

	/**
	 * Resets the Ready Flag to false and sends a Message to the Lobby to reset
	 * those Flags in the Game Lobby.
	 *
	 * @param id the id
	 */
	public void resetReadyFlag(int id) {
		Player player1 = this.getPlayers().get(id);
		player1.changereadyflag();
		sendMessageToLobby(Command.UPDATE_PLAYER, Option.PLAYER, player1.toArray());
	}

	/**
	 * Sets the game running.
	 *
	 * @param gameID the game id
	 * @param b the b
	 */
	public void setGameRunning(int gameID, boolean b) {
		Game game = this.getGames().get(gameID);
		game.setGamerunning(b);
	}

	/**
	 * Gets the lobby.
	 *
	 * @return the lobby
	 */
	public Lobby getLobby() {
		return this.lobby;
	}

}
