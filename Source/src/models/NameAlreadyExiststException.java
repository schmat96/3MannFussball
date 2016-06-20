package models;

// TODO: Auto-generated Javadoc
/**
 * The Class NameAlreadyExiststException.
 */
public class NameAlreadyExiststException extends Exception {
	
	/** The name. */
	private String name;
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new name already existst exception.
	 *
	 * @param name the name
	 */
	public NameAlreadyExiststException(String name){
		this.name = name;
		this.message = "Username " + name + " already exists!";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return message;
	}
}
