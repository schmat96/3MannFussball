package net;

// TODO: Auto-generated Javadoc
/**
 * The Class IllegalCommandException.
 */
public class IllegalCommandException extends Exception {
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new illegal command exception.
	 *
	 * @param requestComponents the request components
	 */
	public IllegalCommandException(String[] requestComponents) {
		if(requestComponents.length > 0){
			message = "Error: Unknown command \""+requestComponents[0]+"\"!";
		}else{
			message = "Warning: Misuse of IllegalCommandException!";
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return message;
	}
}
