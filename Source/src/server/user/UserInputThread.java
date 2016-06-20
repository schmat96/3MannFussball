package server.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ServerMessageHandler;

// TODO: Auto-generated Javadoc
/**
 * This Thread waits for Requests from the user (Client).
 */
public class UserInputThread extends Thread {
	/**
	 * Basic Logger.
	 */
	Logger log = LoggerFactory.getLogger(UserInputThread.class);

	/**
	 * User object to get access to the GameServer and the client's Socket.
	 */
	private User user;

	/**
	 * Client's Socket class to access the I/O streams.
	 */
	private Socket clientSocket;

	/**
	 * Reader for communication between server and client.
	 */
	private BufferedReader in;

	/**
	 * Object responsible for message handling.
	 */
	private ServerMessageHandler msgHandler;

	/**
	 * Running Flag to eventually stop the Thread.
	 */
	private volatile boolean running = true;

	/**
	 * UserInputThread starts itself upon initialization.
	 *
	 * @param user the user
	 */
	public UserInputThread(User user) {
		this.user = user;
		this.clientSocket = user.getClientSocket();
		this.in = user.getReader();
		this.msgHandler = user.getServerMessageHandler();

		start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			// used to store read messages
			String line;

			// receive messages while the UserInputThread is running
			while ((line = in.readLine()) != null) {
				if (!running) {
					break;
				}

				// message is fully handled by a message handler
				msgHandler.processMessage(line);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		running = false;
	}
}
