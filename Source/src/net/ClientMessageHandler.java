package net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.GameClient;
import logic.client.GameLogic;
import logic.client.PlayerGame;
import logic.client.Window;
import models.Game;
import models.Player;
import util.IllegalArgumentsException;

// TODO: Auto-generated Javadoc
/**
 * TODO to implement by Andrea
 * 
 * see also MessageHandler.
 */
public class ClientMessageHandler extends MessageHandler {

	/** Logger for debug/info/error messages. */
	private Logger log;

	/**
	 * GameClient object to display messages in console, etc. no methods yet
	 * implemented. feel free to call non-existing methods
	 */
	private GameClient client;

	/**
	 * Instantiates a new client message handler.
	 *
	 * @param client the client
	 */
	public ClientMessageHandler(GameClient client) {
		log = LoggerFactory.getLogger(ClientMessageHandler.class);
		this.client = client;
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
		if (client.getLogPingMessages()) {
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
			this.client.sendMessageToServer(Command.PING, Option.POLO, arguments);
			break;
		case POLO:
			try {
				pingID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.removeOpenPings();
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
			throw new IllegalOptionException(requestComponents);
		}

		String message = "";
		int senderID;
		switch (option) {
		case GLOBAL:
			try {
				senderID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			for (int i = 3; i < requestComponents.length; i++) {
				message += " " + requestComponents[i];
			}
			this.client.getLobbywindow().addText(message, -1, senderID);
			break;
		case PRIVATE:
			try {
				senderID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			for (int i = 3; i < requestComponents.length; i++) {
				message += " " + requestComponents[i];
			}
			this.client.getLobbywindow().addText(message, this.client.getPlayer().getPlayerID(), senderID);
			this.client.getLobbywindow().refreshWindow();
			break;
		case INFORMATION:
			for (int i = 2; i < requestComponents.length; i++) {
				message += " " + requestComponents[i];
			}

			this.client.getLobbywindow().addInformationText(message);
			if (this.client.getGamewindow().isWindowOpen()) {
				this.client.getGamewindow().addTextToImage(message);
			}
			this.client.getLobbywindow().refreshWindow();
			break;
		case SERVER_MESSAGE:
			for (int i = 2; i < requestComponents.length; i++) {
				message += " " + requestComponents[i];
			}
			this.client.getLobbywindow().ServerMessage(message);
			this.client.getLobbywindow().refreshWindow();
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
			throw new IllegalOptionException(requestComponents);
		}

		int gameID;
		int playerID;
		String playerName;
		int errorIndex = 0;

		switch (option) {
		case ADD_GAME:
			try {
				gameID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			Game game = new Game(gameID, this.client.getLobby());
			try {
				for (int i = 3; i < 6; i++) {
					errorIndex = i;
					playerID = Integer.parseInt(requestComponents[i]);
					game.addPlayer(playerID);
				}
				int gameIsRunning = Integer.parseInt(requestComponents[6]);
				if (gameIsRunning==1) {
					game.setGamerunning(true);
				} else {
					game.setGamerunning(false);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, errorIndex);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.getLobby().addGame(game);
			this.client.getLobbywindow().refreshWindow();

			break;
		case ADD_PLAYER:
			try {
				playerID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			try {
				playerName = requestComponents[3];
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.addPlayerToLobby(new Player(playerID, playerName));
			this.client.getLobbywindow().refreshWindow();

			break;
		case REMOVE_GAME:
			int GameID;
			try {
				GameID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.getLobby().removeGame(GameID);
			this.client.getLobbywindow().refreshWindow();
			break;
		case REMOVE_PLAYER:
			int PlayerID;
			try {
				PlayerID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.getLobby().removePlayer(PlayerID);
			this.client.getLobbywindow().refreshWindow();
			break;
		case GET_OLD_GAMES:
			client.clearOldGame();
			client.addOldGame(requestComponents);
			this.client.getLobbywindow().refreshWindow();
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
			throw new IllegalOptionException(requestComponents);
		}

		int gameID;
		int playerID;
		switch (option) {
		case PLAYER_JOINED:
			try {
				gameID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			try {
				playerID = Integer.parseInt(requestComponents[3]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 3);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.addPlayerToGame(playerID, gameID);
			this.client.getLobbywindow().refreshWindow();
			break;
		case PLAYER_LEFT:
			// TODO Remove Player from game
			break;
		case PLAYER_READY:
			try {
				playerID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			Player player = this.client.getLobby().getPlayers().get(playerID);
			player.changereadyflag();
			this.client.getLobbywindow().refreshWindow();
			break;
		case START_GAME:
			int GameID;
			int gameTime;
			try {
				GameID = Integer.parseInt(requestComponents[5]);
				gameTime = Integer.parseInt(requestComponents[6]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			final PlayerGame player1 = new PlayerGame(270, 200, 1, requestComponents[2]);
			final PlayerGame player2 = new PlayerGame(150, 200, 2, requestComponents[3]);
			final PlayerGame player3 = new PlayerGame(350, 200, 3, requestComponents[4]);
			this.client.setGamewindow(new Window(600, 400, player1, player2, player3, gameTime * 1000));
			this.client.setGamelogic(
					new GameLogic(this.client.getGamewindow(), player1, player2, player3, this.client, GameID));
			client.getGamelogic().start();
			break;
		case END_GAME:
			int playerDisconnect;
			try {
				playerDisconnect = Integer.parseInt(requestComponents[3]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			client.getGamelogic().getWindow().closeWindow();
			if (playerDisconnect != 0) {
				this.client.getLobbywindow()
						.addInformationText("Player " + this.client.getLobby().getPlayerNamefromID(playerDisconnect)
								+ " has diconnected. Game has been terminated.");
				this.client.getLobbywindow().refreshWindow();
			}
			break;
		case PLAYER_UPDATE_COORD:
			client.getGamelogic().setNewCoord(requestComponents);
			break;
		case SCOREBOARD:
			client.getGamelogic().newScoreBoard(requestComponents);
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
			throw new IllegalOptionException(requestComponents);
		}

		int playerID;
		String playerName;
		int currentGameID;
		switch (option) {
		case ID:
			try {
				playerID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			this.client.getPlayer().setPlayerID(playerID);
			this.client.setPlayerID(playerID);
			this.client.addPlayerToLobby(this.client.getPlayer());
			this.client.getLobbywindow().refreshWindow();
			break;
		case PLAYER:
			try {
				playerID = Integer.parseInt(requestComponents[2]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 2);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			try {
				playerName = requestComponents[3];
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			try {
				currentGameID = Integer.parseInt(requestComponents[4]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 4);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			int readyflag;
			try {
				readyflag = Integer.parseInt(requestComponents[5]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(requestComponents, 4);
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentsException(requestComponents, -1);
			}
			if (this.client.getLobby().playerIDExists(playerID) == false) {
				this.client.addPlayerToLobby(new Player(playerID, playerName));
			}

			this.client.updatePlayer(playerID, playerName, currentGameID, readyflag);
			this.client.getLobbywindow().refreshWindow();
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
		log.error(e.getMessage());
	}

}
