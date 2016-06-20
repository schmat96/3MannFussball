package server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * This Thread will ping the Client in regular intervals.
 *
 */
public class ServerPingThread extends Thread {
	/**
	 * The period in which the client should be pinged, in milliseconds.
	 */
	private static final long PING_PERIOD_MILLIS = 1000;

	/** The Constant DISCONNECT_AFTER_SECONDS. */
	private static final long DISCONNECT_AFTER_SECONDS = 60;

	/**
	 * Basic Logger.
	 */
	Logger log = LoggerFactory.getLogger(ServerPingThread.class);

	/**
	 * User object to get access to the GameServer and the client's Socket.
	 */
	private User user;

	/**
	 * Running flag to eventually stop the Thread.
	 */
	private volatile boolean running = true;

	/** Count the pings. */
	private int countopenpings = 0;

	/**
	 * The PingThread starts itself upon initialization.
	 * 
	 * @param user
	 *            User object to get access to the GameServer and the client's
	 *            Socket
	 */
	public ServerPingThread(User user) {
		this.user = user;

		start();
	}

	/**
	 * Sends a "Marco" ping with a random Integer in periodical intervals.
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
				user.sendMessageToClient(Command.PING, Option.MARCO, arguments);

				if (countopenpings > ((DISCONNECT_AFTER_SECONDS * 1000) / PING_PERIOD_MILLIS) / 10) {
					this.user.setDisconnected(true);
					this.user.log("Player " + this.user.getPlayer().getPlayerID() + " disconnected");
				}
				if (countopenpings > (DISCONNECT_AFTER_SECONDS * 1000) / PING_PERIOD_MILLIS) {
					this.user.getGameServer().kickUser(this.user.getPlayer().getPlayerID());
				}
				countopenpings++;

			}
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		running = false;
	}

	/**
	 * Gets the count the pings.
	 *
	 * @return the count the pings
	 */
	public int getCountopenpings() {
		return countopenpings;
	}

	/**
	 * Sets the count the pings.
	 *
	 * @param countopenpings the new count the pings
	 */
	public void setCountopenpings(int countopenpings) {
		this.countopenpings = countopenpings;
	}
}
