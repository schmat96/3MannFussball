package util;

// TODO: Auto-generated Javadoc
/**
 * The Class IllegalArgumentsException.
 */
public class IllegalArgumentsException extends Exception {
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new illegal arguments exception.
	 *
	 * @param requestComponents the request components
	 * @param index the index
	 */
	public IllegalArgumentsException(String[] requestComponents, int index) {
		// Wrong or unnecessary argument
		if (index > 1 && index < requestComponents.length) {
			message = "Error: Unexpected argument \"" + requestComponents[index] + "\" for command \""
					+ requestComponents[0] + " " + requestComponents[1] + "\"!";
		}
		// Missing argument
		else if (index == -1) {
			message = "Error: Missing argument for command \"" + requestComponents[0] + " " + requestComponents[1]
					+ "\"!";
		} else {
			message = "Warning: Misuse of IllegalArgumentsException!";
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return message;
	}
}
