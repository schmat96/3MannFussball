package server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerOptionsThread.
 */
public class ServerOptionsThread extends Thread {
	
	/** The server. */
	GameServer server;

	/** The log. */
	private Logger log;

	/**
	 * Instantiates a new server options thread.
	 *
	 * @param server the server
	 */
	public ServerOptionsThread(GameServer server) {
		log = LoggerFactory.getLogger(ServerOptionsThread.class);
		this.server = server;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		/*
		 * string and scanner to receive and store input from the console
		 */
		String line;
		Scanner scan = new Scanner(System.in);
		while ((line = scan.nextLine()) != null) {
			/*
			 * keyword "stop" stops the server keyword "pinglog on" turns on
			 * ping logging keyword "pinglog off" turns off ping logging
			 * 
			 * TODO other keywords, if needed
			 */
			if (line.equalsIgnoreCase("stop")) {
				server.stopServer();

				try {
					/*
					 * connect to itself to break the while loop after setting
					 * the running flag to false. socket is not needed any
					 * longer, so it gets closed right after
					 */
					Socket sock = new Socket("localhost", server.getPortNumber());
					sock.close();
				} catch (UnknownHostException e) {
					log.error(e.getMessage());
				} catch (IOException e) {
					log.error(e.getMessage());
				}

				break;
			} else if (line.equalsIgnoreCase("pinglog on")) {
				server.setLogPingMessages(true);
			} else if (line.equalsIgnoreCase("pinglog off")) {
				server.setLogPingMessages(false);
			}
		}
		scan.close();
	}
}
