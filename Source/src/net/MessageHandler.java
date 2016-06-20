package net;

// TODO: Auto-generated Javadoc
/**
 * Abstract class for the implementation of ServerMessageHandler and
 * ClientMessageHandler. If you have any questions, please feel free to contact
 * the author via WhatsApp.
 * 
 * @author Maximilian Reber
 *
 */
public abstract class MessageHandler {

	/**
	 * Abstract of the incoming message processing method. It's implementation
	 * will be different for Server/Client.
	 * 
	 * @param request
	 *            Message that is getting processed.
	 */
	public abstract void processMessage(String request);

}
