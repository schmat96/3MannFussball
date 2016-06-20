package util;

// TODO: Auto-generated Javadoc
/**
 * The Class IllegalPortNumberException.
 */
public class IllegalPortNumberException extends Exception {
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new illegal port number exception.
	 *
	 * @param portNumber the port number
	 */
	public IllegalPortNumberException(int portNumber) {
		if (portNumber < 1024) {
			message = "Error: Port number must be greater than 1024! (Port numbers below 1024 are reserved)";
		} else if (portNumber > 65536) {
			message = "Error: Port number must be smaller than 65536!";
		} else {
			message = "Warning: Misuse of IllegalPortNumberException!";
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return message;
	}
}
