package net;

// TODO: Auto-generated Javadoc
/**
 * The Class IllegalOptionException.
 */
public class IllegalOptionException extends Exception{
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new illegal option exception.
	 *
	 * @param requestComponents the request components
	 */
	public IllegalOptionException(String[] requestComponents) {
		if (requestComponents.length >=2){
			message = "Error: Unknown Option \""+requestComponents[1]+"\" for command \""+requestComponents[0]+"\"!";
		}else{
			message = "Error: Option Missing!";
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return message;
	}
}
