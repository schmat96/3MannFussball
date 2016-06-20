package client.window;

import java.awt.GridLayout;

import javax.swing.JPanel;

import client.GameClient;

// TODO: Auto-generated Javadoc
/**
 * The Class JPanelGame.
 */
public class JPanelGame extends JPanel {


	/** The user. */
	private GameClient user;

	/**
	 * Handles the Players current Game and shows all Player in the Game aswell
	 * as the ReadyFlag of all Players.
	 *
	 * @param user the user
	 */
	public JPanelGame(GameClient user) {
		this.user = user;
		this.setLayout(new GridLayout(0, 1));
	}

}
