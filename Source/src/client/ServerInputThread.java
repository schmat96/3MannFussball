package client;

import java.io.BufferedReader;
import java.io.IOException;

import net.ClientMessageHandler;

// TODO: Auto-generated Javadoc
/**
 * This Thread listens for messages coming from the Server.
 *
 * @author Maximilian Reber
 */
public class ServerInputThread extends Thread {
	
	/** GameClient object to access its methods. */
	private GameClient client;

	/** Reader that reads messages from the Server. */
	private BufferedReader in;

	/**
	 * Message Handler.
	 */
	private ClientMessageHandler msgHandler;

	/** The running. */
	private volatile boolean running = true;

	/**
	 * ServerInputThread starts itself upon initialization.
	 * 
	 * @param client
	 *            GameClient object
	 */
	public ServerInputThread(GameClient client) {
		this.client = client;
		this.in = client.getReader();
		this.msgHandler = client.getClientMessageHandler();

		start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		String inLine;

		try {
			while ((inLine = in.readLine()) != null) {
				if (!running) {
					break;
				}
				msgHandler.processMessage(inLine);
			}
		} catch (IOException ex) {
			client.log(ex.getMessage());
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		running = false;
	}
}
