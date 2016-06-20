package net;

public enum Command {
	/**
	 * Server Options:
	 * <p>
	 * <ul>
	 * <li>MARCO</li>
	 * <li>POLO</li>
	 * </ul>
	 * 
	 * Client Options:
	 * <p>
	 * <ul>
	 * <li>MARCO</li>
	 * <li>POLO</li>
	 * </ul>
	 */
	PING("Ping"),

	/**
	 * Server Options:
	 * <p>
	 * <ul>
	 * <li>GLOBAL</li>
	 * <li>PRIVATE</li>
	 * </ul>
	 * 
	 * Client Options:
	 * <p>
	 * <ul>
	 * <li>GLOBAL</li>
	 * <li>PRIVATE</li>
	 * </ul>
	 */
	CHAT("Chat"),

	/**
	 * Server Options:
	 * <p>
	 * <ul>
	 * <li>ADD_GAME</li>
	 * <li>ADD_PLAYER</li>
	 * <li>REMOVE_GAME</li>
	 * <li>REMOVE_PLAYER</li>
	 * </ul>
	 * 
	 * Client Options:
	 * <p>
	 * <ul>
	 * <li>ALL</li>
	 * <li>GET_GAME</li>
	 * <li>GET_PLAYER</li>
	 * <li>LEAVE_LOBBY</li>
	 * </ul>
	 */
	UPDATE_LOBBY("UpdateLobby"),

	/**
	 * Server Options:
	 * <p>
	 * <ul>
	 * <li>PLAYER_JOINED</li>
	 * <li>PLAYER_LEFT</li>
	 * <li>PLAYER_READY</li>
	 * <li>START_GAME</li>
	 * <li>END_GAME</li>
	 * <li>CREATE_GAME</li>
	 * </ul>
	 * 
	 * Client Options:
	 * <p>
	 * <ul>
	 * <li>JOIN_GAME</li>
	 * <li>LEAVE_GAME</li>
	 * <li>I_AM_READY</li>
	 * </ul>
	 */
	UPDATE_GAME("UpdateGame"),

	/**
	 * Server Options:
	 * <p>
	 * <ul>
	 * <li>ID</li>
	 * <li>NAME</li>
	 * </ul>
	 * 
	 * Client Options:
	 * <p>
	 * <ul>
	 * <li>ID</li>
	 * <li>NAME</li>
	 * </ul>
	 */
	UPDATE_PLAYER("UpdatePlayer"),

	/**
	 * Server Options:
	 * <p>
	 * TBD
	 * 
	 * Client Options:
	 * <p>
	 * TBD
	 */
	ERROR("Error");

	private String value;

	Command(String stringRepresetation) {
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
	public static Command fromString(String stringRepresentation) {
		if (stringRepresentation != null) {
			for (Command command : Command.values()) {
				if (stringRepresentation.equalsIgnoreCase(command.value)) {
					return command;
				}
			}
		}
		return null;
	}
}