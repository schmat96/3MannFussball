package server.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.Command;
import net.Option;
import net.ServerMessageHandler;
import models.Game;
import models.Player;
import server.GameServer;

// TODO: Auto-generated Javadoc
/**
 * A new instance of User is invoked for every Client that connects. The User
 * class will then start a new PingThread and a new UserInputThread.
 * 
 */
public class User {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(User.class);

	/** socket from the client that gets passed over to the ClientThread. */
	private Socket clientSocket;

	/**
	 * Reader for communication between server and client.
	 */
	private BufferedReader in;

	/**
	 * Writer for communication between server and client.
	 */
	private PrintWriter out;

	/** GameServer, used to execute commands requested from the client. */
	private GameServer server;

	/**
	 * Thread that sends pings.
	 */
	private ServerPingThread serverPingThread;

	/**
	 * Thread that receives messages from the Client.
	 */
	private UserInputThread userInputThread;

	/**
	 * Handles messages sent and received.
	 */
	private ServerMessageHandler msgHandler;

	/** handles the users player. */
	private Player player;

	/**
	 * Flag to set if the Server is diconnected.
	 */
	private boolean disconnected = false;

	/**
	 * Upon initialization, a new PingThread and a new UserInputThread are
	 * started.
	 *
	 * @param clientSocket            socket from the connection to the client
	 * @param server            GameServer class, to execute commands
	 * @param playerID the player id
	 */
	public User(Socket clientSocket, GameServer server, int playerID) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.player = new Player(playerID);
		msgHandler = new ServerMessageHandler(server, this);

		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}

		serverPingThread = new ServerPingThread(this);
		userInputThread = new UserInputThread(this);
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
	 * Gets the writer.
	 *
	 * @return the writer
	 */
	public PrintWriter getWriter() {
		return out;
	}

	/**
	 * Gets the socket from the client that gets passed over to the ClientThread.
	 *
	 * @return the socket from the client that gets passed over to the ClientThread
	 */
	public Socket getClientSocket() {
		return clientSocket;
	}

	/**
	 * Gets the game server.
	 *
	 * @return the game server
	 */
	public GameServer getGameServer() {
		return server;
	}

	/**
	 * Gets the server message handler.
	 *
	 * @return the server message handler
	 */
	public ServerMessageHandler getServerMessageHandler() {
		return msgHandler;
	}

	/**
	 * Allows ServerMessageHandler to log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		log.info(message);
	}

	/**
	 * Stops all Threads for this User (ServerPingThread and UserInputThread).
	 */
	public void stopThreads() {
		serverPingThread.stopThread();
		userInputThread.stopThread();
	}

	/**
	 * composes and sends message to client.
	 *
	 * @param command the command
	 * @param option the option
	 * @param arguments the arguments
	 */
	public synchronized void sendMessageToClient(Command command, Option option, String[] arguments) {
		String message = command.toString() + " " + option.toString();
		for (String argument : arguments) {
			message += " " + argument;
		}
		out.println(message);
	}

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Hashtable<Integer, Player> getPlayers() {
		return this.server.getPlayers();
	}

	/**
	 * Gets the games.
	 *
	 * @return the games
	 */
	public Hashtable<Integer, Game> getGames() {
		return this.server.getGames();
	}

	/**
	 * Removes the open pings.
	 */
	public void removeOpenPings() {
		if (disconnected) {
			setDisconnected(false);
			this.log("Client has reconnected");
		}
		serverPingThread.setCountopenpings(0);
	}

	/**
	 * Gets the thread that receives messages from the Client.
	 *
	 * @return the thread that receives messages from the Client
	 */
	public Thread getUserInputThread() {

		return this.userInputThread;
	}

	/**
	 * Checks if is flag to set if the Server is diconnected.
	 *
	 * @return the flag to set if the Server is diconnected
	 */
	public boolean isDisconnected() {
		return disconnected;
	}

	/**
	 * Sets the flag to set if the Server is diconnected.
	 *
	 * @param disconnected the new flag to set if the Server is diconnected
	 */
	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
		this.player.setDisconnected(disconnected);
	}

	/**
	 * Gets the handles the users player.
	 *
	 * @return the handles the users player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the handles the users player.
	 *
	 * @param player the new handles the users player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

}
