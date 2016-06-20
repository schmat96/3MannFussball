package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * This Thread listens for console inputs.
 * 
 * @author Maximilian Reber
 *
 */
public class ConsoleInputThread extends Thread {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(ConsoleInputThread.class);

	/** GameClient object to access methods;. */
	private GameClient client;

	/**
	 * Console input.
	 */
	private BufferedReader userIn;

	/**
	 * Thread is started upon initialization.�
	 * 
	 * So far this class only handles two possible commands: stop: calls the
	 * exit method in the client send: sends the message out to the server
	 *
	 * @param client
	 *            the client
	 */
	public ConsoleInputThread(GameClient client) {
		this.client = client;


		start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		userIn = new BufferedReader(new InputStreamReader(System.in));

		String inLine;

		try {
			while ((inLine = userIn.readLine()) != null) {
				if (inLine.equals("stop")) {
					client.exitClient();
					break;
				} else if (inLine.startsWith("send ")) {
					// cut off the "send"
					String message = inLine.substring(4).trim();
					// send the message to the server
					client.sendRawMessageToServer(message);
				} else if (inLine.equals("pinglog on")) {
					client.setLogPingMessages(true);
				} else if (inLine.equals("pinglog off")) {
					client.setLogPingMessages(false);
				}
			}
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
	}
}
