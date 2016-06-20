package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IllegalPortNumberException;
import net.ClientMessageHandler;
import net.Command;
import net.Option;
import models.Game;
import models.Lobby;
import models.Player;
import client.window.LobbyWindow;
import logic.client.GameLogic;
import logic.client.Window;

// TODO: Auto-generated Javadoc
/**
 * The Class GameClient.
 */
public class GameClient {

	/** The player id. */
	private int playerID;	

	/** The Socket of the client. */
	private Socket clientSocket;

	/** Client-side reader for Client-Server communication. */
	private BufferedReader in;
	
	/** Client-side writer for Client-Server communication. */
	private PrintWriter out;

	/** Client-side reader for user input. */
	private BufferedReader userIn;

	/** Logger for debug/info/error messages. */
	private Logger log;

	/**
	 * Message Handler.
	 */
	private ClientMessageHandler msgHandler;

	/**
	 * Thread that reads inputs from the Server.
	 */
	private ServerInputThread serverInputThread;

	/**
	 * Thread that pings the Server.
	 */
	private ClientPingThread clientPingThread;

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

	/**
	 * Flag to set if the Server is diconnected.
	 */
	private boolean disconnect = false;

	/** The hostname. */
	private String hostname;

	/** The port number. */
	private int portNumber;

	/**
	 * Used to draw the Lobby Interface on it.
	 */
	private LobbyWindow lobbywindow;

	/** The lobby. */
	private Lobby lobby;

	/** The gamewindow. */
	private Window gamewindow;

	/** The player. */
	private Player player;

	/** The gamelogic. */
	private GameLogic gamelogic;

	/**
	 * Constructor sets up the Client socket and starts a new ServerInputThread,
	 * a new ClientPingThread and a new (anonymous) ConsoleInputThread.
	 *
	 * @throws IllegalPortNumberException             If port number is outside of range stated above
	 * @deprecated 
	 */
	public GameClient() throws IllegalPortNumberException {
		// initializing the Objects
		log = LoggerFactory.getLogger(GameClient.class);
		msgHandler = new ClientMessageHandler(this);
		player = new Player(0);

		lobbywindow = new LobbyWindow(700, 500, this);
		lobby = new Lobby();

		String[] portandip = lobbywindow.getServerPortandIP();

		try {
			this.portNumber = Integer.parseInt(portandip[0]);
		} catch (NumberFormatException e) {
			log("Port needs to be an intger!");
		}

		this.hostname = portandip[1];

		// throw Exception if illegal port number is entered
		if (portNumber < 1024 || portNumber > 65536) {
			throw new IllegalPortNumberException(portNumber);
		}

		try {
			// initializing Socket connection to the server
			clientSocket = new Socket(hostname, portNumber);

			// initializing readers/writers
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			userIn = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException ex) {
			log.error("Error with connecting to Server/creating Reader/Writer:\n\t" + ex.getMessage());
		}

		/*
		 * start an anonymous console listener. it does not need to be stopped
		 * since it is the Thread that decides when to stop
		 */
		new ConsoleInputThread(this);
		/*
		 * start the threads that communicate with the server. they have to be
		 * stopped manually
		 */
		serverInputThread = new ServerInputThread(this);
		clientPingThread = new ClientPingThread(this);
		lobbywindow.getUsername();

		// out.write(Command.UPDATE_PLAYER + " " + Option.ID);
	}

	/**
	 * Instantiates a new game client.
	 *
	 * @param string the string
	 * @param string2 the string2
	 * @throws IllegalPortNumberException the illegal port number exception
	 */
	public GameClient(String string, String string2) throws IllegalPortNumberException {
		// initializing the Objects
		log = LoggerFactory.getLogger(GameClient.class);
		msgHandler = new ClientMessageHandler(this);
		player = new Player(0);

		lobbywindow = new LobbyWindow(700, 500, this);
		lobby = new Lobby();

		try {
			this.portNumber = Integer.parseInt(string2);
		} catch (NumberFormatException e) {
			log("Port needs to be an intger! Trying to get Port number from jPane.");
		}

		this.hostname = string;

		// throw Exception if illegal port number is entered
		if (portNumber < 1024 || portNumber > 65536) {
			throw new IllegalPortNumberException(portNumber);
		}

		try {
			// initializing Socket connection to the server
			clientSocket = new Socket(hostname, portNumber);

			// initializing readers/writers
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			userIn = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException ex) {
			log.error("Error with connecting to Server/creating Reader/Writer:\n\t" + ex.getMessage());
		}

		/*
		 * start an anonymous console listener. it does not need to be stopped
		 * since it is the Thread that decides when to stop
		 */
		new ConsoleInputThread(this);
		/*
		 * start the threads that communicate with the server. they have to be
		 * stopped manually
		 */
		serverInputThread = new ServerInputThread(this);
		clientPingThread = new ClientPingThread(this);
		lobbywindow.getUsername();
	}

	/**
	 * Allows other classes to log.
	 *
	 * @param message            message to be logged
	 */
	public void log(String message) {
		log.info(message);
	}

	/**
	 * composes and sends message to server.
	 *
	 * @param command the command
	 * @param option the option
	 * @param arguments the arguments
	 */
	public synchronized void sendMessageToServer(Command command, Option option, String[] arguments) {
		String message = command.toString() + " " + option.toString();
		if (arguments != null) {
			for (String argument : arguments) {
				message += " " + argument;
			}
			out.println(message);
		} else {
			out.println(message);
		}
	}

	/**
	 * Only to be called from ConsoleInputThread for sending raw message from
	 * console.<br>
	 * Use <b>sendMessageToServer(Command command, Option option, String[]
	 * arguments)</b> instead
	 *
	 * @param message the message
	 */
	public synchronized void sendRawMessageToServer(String message) {
		out.println(message);
	}

	/**
	 * Gets the client message handler.
	 *
	 * @return the client message handler
	 */
	public ClientMessageHandler getClientMessageHandler() {
		return msgHandler;
	}

	/**
	 * Gets the reader.
	 *
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return in;
	}

	/**
	 * closes everything and exits.
	 */
	public void exitClient() {
		try {
			serverInputThread.stopThread();
			clientPingThread.stopThread();

			in.close();
			out.close();
			userIn.close();
			clientSocket.close();
		} catch (IOException e) {
			log.error(e.getMessage());
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
	 * Update player.
	 *
	 * @param playerID the player id
	 * @param newName the new name
	 * @param currentGameID the current game id
	 * @param readyflag the readyflag
	 */
	public void updatePlayer(int playerID, String newName, int currentGameID, int readyflag) {
		if (this.lobby.playerIDExists(playerID)) {
			this.lobby.updatePlayer(playerID, newName, currentGameID, readyflag);
			this.getPlayer().setCurrentGameID(currentGameID);
			this.getPlayer().setPlayerID(playerID);
			this.getPlayer().setPlayerName(newName);
			if (readyflag == 1) {
				this.getPlayer().setReadyflag(true);
			} else {
				this.getPlayer().setReadyflag(false);
			}
		} else {
			String[] arguments = { playerID + "" };
			this.sendMessageToServer(Command.UPDATE_LOBBY, Option.GET_PLAYER, arguments);
		}
	}

	/**
	 * Adds the player to lobby.
	 *
	 * @param player the player
	 */
	public void addPlayerToLobby(Player player) {
		this.lobby.addPlayer(player);

	}

	/**
	 * Adds the game to lobby with players.
	 *
	 * @param gameID the game id
	 * @param playerIDs the player i ds
	 */
	public void addGameToLobbyWithPlayers(int gameID, ArrayList<Integer> playerIDs) {
		// Check if any playerID is unknown if that is the case update lobby
		Game game = new Game(gameID, this.lobby);
		boolean missingPlayer = false;
		for (Integer playerID : playerIDs) {
			if (this.lobby.playerIDExists(playerID.intValue())) {
				game.addPlayer(playerID.intValue());
			} else {
				// request the missing Player
				missingPlayer = true;
				String[] arguments = { playerID.toString() };
				this.sendMessageToServer(Command.UPDATE_LOBBY, Option.GET_PLAYER, arguments);
			}
		}
		if (missingPlayer) {
			// Request this same game again hoping that either the missing
			// player is now in clients lobby
			// or that they have been removed from the game on the server
			String[] argumentsGame = { gameID + "" };
			sendMessageToServer(Command.UPDATE_LOBBY, Option.GET_GAME, argumentsGame);
		} else {
			this.lobby.addGame(game);
		}
	}

	/**
	 * Clear lobby all.
	 */
	public void clearLobbyAll() {
		// TODO Auto-generated method stub
		this.lobby.clearAll();
	}

	/**
	 * Removes the open pings.
	 */
	public void removeOpenPings() {
		if (disconnect) {
			disconnect = false;
			this.log("Server has reconnected");
		}
		clientPingThread.setCountopenpings(0);

	}

	/**
	 * Adds the player to game.
	 *
	 * @param playerID the player id
	 * @param gameID the game id
	 * @return true, if successful
	 */
	public boolean addPlayerToGame(int playerID, int gameID) {
		boolean a = this.lobby.getGames().get(gameID).addPlayer(playerID);
		return a;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerID() {
		return this.playerID;
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
	 * Adds the old game.
	 *
	 * @param requestComponents the request components
	 */
	public void addOldGame(String[] requestComponents) {
		for (int n = 0; n < (requestComponents.length - 1) / 7; n++) {
			String[] playername = { requestComponents[(n * 7) + 2], requestComponents[(n * 7) + 3],
					requestComponents[(n * 7) + 4] };
			int player1goal = Integer.parseInt(requestComponents[(n * 7) + 5]);
			int player2goal = Integer.parseInt(requestComponents[(n * 7) + 6]);
			int player3goal = Integer.parseInt(requestComponents[(n * 7) + 7]);
			int[] playergoals = { player1goal, player2goal, player3goal };
			this.lobby.addOldGame(playername, playergoals, requestComponents[(n * 7) + 8]);
		}
	}

	/**
	 * Clear old game.
	 */
	public void clearOldGame() {
		this.lobby.getOldgames().clear();
		this.lobby.setNumberOfOldGames(0);
	}
	
	/**
	 * Checks if is flag to set if the Server is diconnected.
	 *
	 * @return the flag to set if the Server is diconnected
	 */
	public boolean isDisconnect() {
		return disconnect;
	}

	/**
	 * Sets the flag to set if the Server is diconnected.
	 *
	 * @param disconnect the new flag to set if the Server is diconnected
	 */
	public void setDisconnect(boolean disconnect) {
		this.disconnect = disconnect;
	}

	/**
	 * Gets the hostname.
	 *
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the hostname.
	 *
	 * @param hostname the new hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Gets the port number.
	 *
	 * @return the port number
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * Sets the port number.
	 *
	 * @param portNumber the new port number
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * Gets the used to draw the Lobby Interface on it.
	 *
	 * @return the used to draw the Lobby Interface on it
	 */
	public LobbyWindow getLobbywindow() {
		return lobbywindow;
	}

	/**
	 * Sets the used to draw the Lobby Interface on it.
	 *
	 * @param lobbywindow the new used to draw the Lobby Interface on it
	 */
	public void setLobbywindow(LobbyWindow lobbywindow) {
		this.lobbywindow = lobbywindow;
	}

	/**
	 * Gets the lobby.
	 *
	 * @return the lobby
	 */
	public Lobby getLobby() {
		return lobby;
	}

	/**
	 * Sets the lobby.
	 *
	 * @param lobby the new lobby
	 */
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	/**
	 * Gets the gamewindow.
	 *
	 * @return the gamewindow
	 */
	public Window getGamewindow() {
		return gamewindow;
	}

	/**
	 * Sets the gamewindow.
	 *
	 * @param gamewindow the new gamewindow
	 */
	public void setGamewindow(Window gamewindow) {
		this.gamewindow = gamewindow;
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 *
	 * @param player the new player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the gamelogic.
	 *
	 * @return the gamelogic
	 */
	public GameLogic getGamelogic() {
		return gamelogic;
	}

	/**
	 * Sets the gamelogic.
	 *
	 * @param gamelogic the new gamelogic
	 */
	public void setGamelogic(GameLogic gamelogic) {
		this.gamelogic = gamelogic;
	}
}
