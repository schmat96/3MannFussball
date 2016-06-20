package net;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Game;
import models.Player;
import server.GameServer;
import server.user.User;
import util.IllegalArgumentsException;

// TODO: Auto-generated Javadoc
/**
 * see also MessageHandler.
 */
public class ServerMessageHandler extends MessageHandler {
	
	/** Logger for debug/info/error messages. */
	private Logger log;
	
	/** GameServer object to send messages to other clients, log, update lobby etc. */
	private GameServer server;
	
	/** User object that holds information about the user. */
	private User user;

	/**
	 * Instantiates a new server message handler.
	 *
	 * @param server the server
	 * @param clientThread the client thread
	 */
	public ServerMessageHandler(GameServer server, User clientThread) {
		log = LoggerFactory.getLogger(ServerMessageHandler.class);
		this.server = server;
		this.user = clientThread;
	}

	/*
	 * ----------------------------------------------- Block of functions to
	 * handle incoming Messages
	 */

	/**
	 * Parses a incoming request and calls corresponding function.
	 *
	 * @param request            Unprocessed request String
	 */
	public void processMessage(String request) {
		Command command;
		String[] requestComponents;
		try {
			requestComponents = request.split("\\s+");
			command = Command.fromString(requestComponents[0]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
			return;
		}
		/*
		 * Iterate through known commands and call respective functions
		 */
		switch (command) {
		case PING:
			try {
				this.ping(requestComponents);
			} catch (Exception e) {
				this.handleError(e);
			}
			break;
		case CHAT:
			try {
				this.chat(requestComponents);
			} catch (Exception e) {
				this.handleError(e);
			}
			break;
		case UPDATE_LOBBY:
			try {
				this.updateLobby(requestComponents);
			} catch (Exception e) {
				this.handleError(e);
			}
			break;
		case UPDATE_GAME:
			try {
				this.updateGame(requestComponents);
			} catch (Exception e) {
				this.handleError(e);
			}
			break;
		case UPDATE_PLAYER:
			try {
				this.updatePlayer(requestComponents);
			} catch (Exception e) {
				this.handleError(e);
			}
			break;
		case ERROR:

			break;
		default:
			handleError(new IllegalCommandException(requestComponents));
			break;
		}
	}

	/**
	 * Handles a incoming Ping request.
	 *
	 * @param requestComponents            Complete request string split up by spaces
	 * @throws IllegalOptionException the illegal option exception
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	private void ping(String[] requestComponents) throws IllegalOptionException, IllegalArgumentsException {
		/*
		 * log the ping message if should log
		 */
		if (server.getLogPingMessages()) {
			logRawMessage(requestComponents);
		}

		Option option = null;
		try {
			option = Option.fromString(requestComponents[1]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
		}

		int pingID;
		switch (option) {
		case MARCO:
			try {
				pingID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			String[] arguments = { pingID + "" };
			this.user.sendMessageToClient(Command.PING, Option.POLO, arguments);
			break;
		case POLO:
			try {
				pingID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.user.removeOpenPings();
			break;
		default:
			throw new IllegalOptionException(requestComponents);
		}

	}

	/**
	 * Handles a incoming Chat request.
	 *
	 * @param requestComponents            Complete request string split up by spaces
	 * @throws IllegalOptionException the illegal option exception
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	private void chat(String[] requestComponents) throws IllegalOptionException, IllegalArgumentsException {
		logRawMessage(requestComponents);

		Option option = null;
		try {
			option = Option.fromString(requestComponents[1]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
		}

		String[] arguments = new String[requestComponents.length - 2];
		int senderID;
		int recipientID;
		switch (option) {
		case GLOBAL:
			senderID = this.user.getPlayer().getPlayerID();
			arguments[0] = senderID + "";
			for (int i = 3; i < requestComponents.length; i++) {
				arguments[i - 2] = "" + requestComponents[i];
			}
			this.server.sendMessageToLobby(Command.CHAT, Option.GLOBAL, arguments);
			break;
		case PRIVATE:
			try {
				recipientID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			
			if (recipientID>0) {
				senderID = this.user.getPlayer().getPlayerID();
				arguments[0] = senderID + "";
				for (int i = 3; i < requestComponents.length; i++) {
					arguments[i - 2] = "" + requestComponents[i];
				}
				senderID = this.user.getPlayer().getPlayerID();
				this.server.sendMessageToPlayer(recipientID, Command.CHAT, Option.PRIVATE, arguments);
			} else {
				
				senderID = this.user.getPlayer().getPlayerID();
				int gameID = this.user.getGameServer().getLobby().playerInGame(senderID);
				arguments[0] = senderID + "";
				for (int i = 3; i < requestComponents.length; i++) {
					arguments[i - 2] = "" + requestComponents[i];
				}
				senderID = this.user.getPlayer().getPlayerID();
				this.server.sendMessageToGame(gameID, Command.CHAT, Option.PRIVATE, arguments);
			}
			break;
		default:
			throw new IllegalOptionException(requestComponents);
		}
	}

	/**
	 * Handles a incoming UpdateLobby request.
	 *
	 * @param requestComponents            Complete request string split up by spaces
	 * @throws IllegalOptionException the illegal option exception
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	private void updateLobby(String[] requestComponents) throws IllegalOptionException, IllegalArgumentsException {
		logRawMessage(requestComponents);

		Option option = null;
		try {
			option = Option.fromString(requestComponents[1]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
		}

		switch (option) {
		case ALL:
			ArrayList<Player> players = new ArrayList<Player>(this.user.getPlayers().values());
			for (Player player : players) {
				// Send all players to client
				this.user.sendMessageToClient(Command.UPDATE_LOBBY, Option.ADD_PLAYER, player.toArray());
			}

			ArrayList<Game> games = new ArrayList<Game>(this.user.getGames().values());
			for (Game game : games) {
				// Send all games to client
				this.user.sendMessageToClient(Command.UPDATE_LOBBY, Option.ADD_GAME, game.toArray());
			}
			break;
		case GET_GAME:
			break;
		case GET_PLAYER:
			break;
		case LEAVE_LOBBY:
			this.user.getGameServer().kickUser(this.user.getPlayer().getPlayerID());
			break;
		case CREATE_GAME:
			this.user.getGameServer().createGame(this.user.getPlayer().getPlayerID());
			break;
		case GET_OLD_GAMES:
			String[] arguments = this.user.getGameServer().getAllOldGames();
			this.user.sendMessageToClient(Command.UPDATE_LOBBY, Option.GET_OLD_GAMES, arguments);
			break;
		case REMOVE_PLAYER:
			this.user.getGameServer().kickUser(this.user.getPlayer().getPlayerID());
			break;
		default:
			throw new IllegalOptionException(requestComponents);
		}
	}

	/**
	 * Handles a incoming UpdateGame request.
	 *
	 * @param requestComponents            Complete request string split up by spaces
	 * @throws IllegalOptionException the illegal option exception
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	private void updateGame(String[] requestComponents) throws IllegalOptionException, IllegalArgumentsException {
		logRawMessage(requestComponents);

		Option option = null;
		try {
			option = Option.fromString(requestComponents[1]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
		}
		int gameID;
		switch (option) {
		case JOIN_GAME:
			try {
				gameID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.user.getGameServer().joinGameRequest(this.user.getPlayer().getPlayerID(), gameID);
			break;
		case LEAVE_GAME:
			try {
				gameID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.user.getGameServer().leaveGameRequest(this.user.getPlayer().getPlayerID(), gameID);
			break;
		case I_AM_READY:
			int gameTime;
			try {
				gameID = Integer.parseInt(requestComponents[2]);
				gameTime = Integer.parseInt(requestComponents[4]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.user.getGameServer().setGameFlagReady(this.user.getPlayer().getPlayerID(), gameID,
					requestComponents[3], gameTime);
			break;
		case KEY_INPUTS:
			String[] arguments = new String[4];
			try {
				for (int n = 0; n < 4; n++) {
					arguments[n] = requestComponents[n + 2];
				}
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.user.getGameServer().updateGameLogicServer(arguments, this.user.getPlayer().getPlayerID());
			break;
		default:
			throw new IllegalOptionException(requestComponents);
		}
	}

	/**
	 * Handles a incoming UpdatePlayer request.
	 *
	 * @param requestComponents            Complete request string split up by spaces
	 * @throws IllegalOptionException the illegal option exception
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	private void updatePlayer(String[] requestComponents) throws IllegalOptionException, IllegalArgumentsException {
		logRawMessage(requestComponents);

		Option option = null;
		try {
			option = Option.fromString(requestComponents[1]);
		} catch (IndexOutOfBoundsException e) {
			this.handleError(e);
		}
		String name;
		switch (option) {
		case ID:
			String[] argumentsID = { this.user.getPlayer().getPlayerID() + "" };
			this.user.sendMessageToClient(Command.UPDATE_PLAYER, Option.ID, argumentsID);
			break;
		case SET_NAME:
			try {
				name = requestComponents[2];
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			if (this.server.nameIsAvailable(name)) {
				this.user.getPlayer().setPlayerName(name);
				for (int i = 0; i < this.server.getUserList().size(); i++) {
					User user = this.server.getUserList().get(i);
					user.sendMessageToClient(Command.UPDATE_PLAYER, Option.PLAYER, this.user.getPlayer().toArray());
				}
			} else {
				String[] arguments = { name };
				this.user.sendMessageToClient(Command.ERROR, Option.USERNAME_TAKEN, arguments);
			}
			break;
		default:
			throw new IllegalOptionException(requestComponents);
		}

	}

	/**
	 * This method sends a message to the logger from the GameClient.
	 *
	 * @param message the message
	 */
	private void logRawMessage(String message) {
		log.info("IN: " + message);
	}

	/**
	 * This method calls logRawMessage(String message) with a rebuild String
	 * from the split up words.
	 * 
	 * @param messageComponents
	 *            Words from a String
	 */
	private void logRawMessage(String[] messageComponents) {
		// rebuild the message that has been split up
		String message = "";
		for (String str : messageComponents) {
			message = message + str + " ";
		}
		// remove the last whitespace that is created with the upper for loop
		message = message.trim();

		// send the message to the logger in the GameClient class
		logRawMessage(message);
	}

	/**
	 * Handle error.
	 *
	 * @param e the e
	 */
	public void handleError(Exception e) {
		this.user.log(e.getMessage());
	}

}
