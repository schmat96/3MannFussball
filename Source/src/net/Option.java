package net;

public enum Option {
	// ----------------------------------------------------Ping Options
	/**
	 * Option for Command PING<br>
	 * Server Arguments:
	 * <ol>
	 * <li>Random int</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>Random int</li>
	 * </ol>
	 */
	MARCO("Marco"),
	/**
	 * Option for Command PING<br>
	 * Server Arguments:
	 * <ol>
	 * <li>int from Marco Request</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>int from Marco Request</li>
	 * </ol>
	 */
	POLO("Polo"),
	// ----------------------------------------------------Chat Options
	/**
	 * Option for Command CHAT<br>
	 * Server Arguments:
	 * <ol>
	 * <li>SenderID</li>
	 * <li>Chat Message</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>Chat Message</li>
	 * </ol>
	 */
	GLOBAL("Global"),
	/**
	 * Option for Command CHAT<br>
	 * Server Arguments:
	 * <ol>
	 * <li>SenderID</li>
	 * <li>Chat Message</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>RecieverID</li>
	 * <li>Chat Message</li>
	 * </ol>
	 */
	PRIVATE("Private"),
	/**
	 * Option for Command CHAT<br>
	 * Client Arguments:
	 * <ol>
	 * <li>Message</li>
	 * </ol>
	 */
	INFORMATION("Information"),
	/**
	 * Option for Command CHAT<br>
	 * Client Arguments:
	 * <ol>
	 * <li>Message</li>
	 * </ol>
	 */
	SERVER_MESSAGE("ServerMessage"),
	// ----------------------------------------------------UpdateLobby Options
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * <li>First PlayerID</li>
	 * <li>Second PlayerID</li>
	 * <li>Third PlayerID</li>
	 * </ol>
	 */
	ADD_GAME("AddGame"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Server Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * <li>Player Name</li>
	 * <li>Current GameID</li>
	 * </ol>
	 */
	ADD_PLAYER("AddPlayer"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 */
	REMOVE_GAME("RemoveGame"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Server Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	REMOVE_PLAYER("RemovePlayer"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Client Arguments: -
	 */
	// TODO: Evaluate if needed
	ALL("All"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Client Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 */
	GET_GAME("GetGame"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Client Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	GET_PLAYER("GetPlayer"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Client Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 */
	LEAVE_LOBBY("LeaveLobby"),
	/**
	 * Option for Command UPDATE_LOBBY<br>
	 * Client Arguments: -
	 */
	CREATE_GAME("CreateGame"),
	// ----------------------------------------------------UpdateGame Options
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	PLAYER_JOINED("PlayerJoined"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	PLAYER_LEFT("PlayerLeft"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * <li>PlayerID</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	PLAYER_READY("PlayerReady"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 */
	START_GAME("StartGame"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 * Client Arguments
	 * <ol>
	 * <li>GameID</li>
	 * <li>Player Disconnect (0 if the game ended regularly</li>
	 * </ol>
	 */
	END_GAME("EndGame"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>PLayerName1</li>
	 * <li>PLayerName2</li>
	 * <li>PLayerName3</li>
	 * <li>Player1Goals</li>
	 * <li>Player2Goals</li>
	 * <li>Player3Goals</li>
	 * <li>Date</li>
	 * </ol>
	 */
	GET_OLD_GAMES("GetOldGames"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Server Arguments:
	 * <ol>
	 * <li>Player1 x-coord</li>
	 * <li>Player1 y-coord</li>
	 * <li>Player2 x-coord</li>
	 * <li>Player2 y-coord</li>
	 * <li>Player3 x-coord</li>
	 * <li>Player3 y-coord</li>
	 * <li>Ball x-coord</li>
	 * <li>Ball y-coord</li>
	 * <li>Time left ms</li>
	 * </ol>
	 */
	PLAYER_UPDATE_COORD("PlayerUpdateCoord"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Client Arguments:
	 * <ol>
	 * <li>GameID</li>
	 * </ol>
	 */
	JOIN_GAME("JoinGame"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Client Arguments: -
	 */
	LEAVE_GAME("LeaveGame"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Client Arguments: -
	 */
	I_AM_READY("IAmReady"),

	/**
	 * Option for Command UPDATE_GAME<br>
	 * Client Arguments:
	 * <ol>
	 * <li>Key W</li>
	 * <li>Key S</li>
	 * <li>Key A</li>
	 * <li>Key D</li>
	 * </ol>
	 */
	KEY_INPUTS("KeyInputs"),
	/**
	 * Option for Command UPDATE_GAME<br>
	 * Client Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * <li>Number of Goals</li>
	 * </ol>
	 */
	SCOREBOARD("ScoreBoard"),
	// ----------------------------------------------------UpdatePlayer Options
	/**
	 * Option for Command UPDATE_PLAYER<br>
	 * Server Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * </ol>
	 * Client Arguments: -
	 */
	ID("ID"),
	/**
	 * Option for Command UPDATE_PLAYER<br>
	 * Client Arguments:
	 * <ol>
	 * <li>Name</li>
	 * </ol>
	 */
	SET_NAME("SetName"),
	/**
	 * Option for Command UPDATE_PLAYER<br>
	 * Server Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * <li>Name</li>
	 * <li>CurrentGameID</li>
	 * </ol>
	 * Client Arguments:
	 * <ol>
	 * <li>PlayerID</li>
	 * </ol>
	 */
	PLAYER("Player"),
	// ----------------------------------------------------Error Options
	USERNAME_TAKEN("UsernameTaken"), PROTOCOL_UNKNOWN_COMMAND("UnknownCommand"), PROTOCOL_UNEXPECTED_OPTION(
			"UnexpectedOption"), PROTOCOL_UNEXPECTED_ARGUMENT(
					"UnexpectedArgument"), PROTOCOL_MISSING_ARGUMENT("MissingArgument");

	private String value;

	Option(String stringRepresetation) {
		this.value = stringRepresetation;
	}

	/**
	 * @return string value of command
	 */
	public String toString() {
		return this.value;
	}

	/**
	 * Get command with the same value as stringRepresentation. Case insensitive
	 * 
	 * @param stringRepresentation
	 *            string to compare command value to
	 * @return command for stringRepresentation
	 */
	public static Option fromString(String stringRepresentation) {
		if (stringRepresentation != null) {
			for (Option command : Option.values()) {
				if (stringRepresentation.equalsIgnoreCase(command.value)) {
					return command;
				}
			}
		}
		return null;
	}
}