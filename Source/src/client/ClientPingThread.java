package client;

import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * This Thread will ping the Server in regular intervals.
 *
 * @author Maximilian Reber
 */
public class ClientPingThread extends Thread {
	/**
	 * The period in which the client should be pinged, in milliseconds.
	 */
	private static final long PING_PERIOD_MILLIS = 10000;

	/**
	 * GameClient object to access its methods.
	 */
	private GameClient client;

	/**
	 * Running flag to eventually stop the Thread.
	 */
	private volatile boolean running = true;

	/** The countopenpings. */
	private int countopenpings = 0;

	/**
	 * Gets the countopenpings.
	 *
	 * @return the countopenpings
	 */
	public int getCountopenpings() {
		return countopenpings;
	}

	/**
	 * Sets the countopenpings.
	 *
	 * @param countopenpings the new countopenpings
	 */
	public void setCountopenpings(int countopenpings) {
		this.countopenpings = countopenpings;
	}

	/**
	 * Instantiates a new client ping thread.
	 *
	 * @param client the client
	 */
	public ClientPingThread(GameClient client) {
		this.client = client;

		start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		long startingTime = System.currentTimeMillis();
		long timePassed = 0;

		while (running) {
			timePassed = System.currentTimeMillis() - startingTime;

			if (timePassed > PING_PERIOD_MILLIS) {
				startingTime = System.currentTimeMillis();
				timePassed = 0;

				// a random positive integer (0 < Math.random < 1)
				int randomInteger = (int) (Math.random() * Integer.MAX_VALUE);

				// random integer is sent as a "Marco" Ping
				String[] arguments = { randomInteger + "" };
				client.sendMessageToServer(Command.PING, Option.MARCO, arguments);

				if (countopenpings >= 3) {
					this.client.setDisconnect(true);
					this.client.log("Server disconnected");
					this.client.getGamewindow().addTextToImage("u have disconnected");
					this.client.getLobbywindow().addInformationText("u have disconnected");
				} else {
					countopenpings++;
				}

			}
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		running = false;
	}
}
