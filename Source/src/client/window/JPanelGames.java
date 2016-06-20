package client.window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import client.GameClient;
import models.Game;
import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * The Class JPanelGames.
 */
public class JPanelGames extends JPanel {

	/** The jpanel games. */
	private Hashtable<Integer, JLabel> jpanelGames;

	/** The user. */
	private GameClient user;

	/** The list games. */
	private JList listGames;

	/** The _j button join game. */
	private JButton _jButtonJoinGame;

	/** The _j button change name. */
	private JButton _jButtonChangeName;

	/** The model games text. */
	private DefaultListModel<String> modelGamesText;

	/** The model games. */
	private Hashtable<Integer, Game> modelGames;

	/**
	 * Handles the JPanel for all Games. This is only displayed, if the Player
	 * is currently not in a game. All Games will be displayed, even if they are
	 * full.
	 *
	 * @param user the user
	 */
	public JPanelGames(GameClient user) {
		this.user = user;
		modelGames = new Hashtable<Integer, Game>();
		modelGamesText = new DefaultListModel<String>();
		listGames = new JList(modelGamesText);
		listGames.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listGames.setLayoutOrientation(JList.VERTICAL);
		listGames.setVisibleRowCount(-1);
		listGames.setFocusable(false);
		listGames.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					joinGameRequest(index);
				}
			}
		});

		JScrollPane listScroller = new JScrollPane(listGames);
		listScroller.setPreferredSize(new Dimension(200, 200));
		this.add(listGames);
		_jButtonJoinGame = new JButton("Join Game");
		_jButtonJoinGame.setToolTipText("Join the selected Game");
		_jButtonJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedGame = listGames.getSelectedIndex();
				joinGameRequest(selectedGame);

			}

		});
		this.add(_jButtonJoinGame);

		_jButtonChangeName = new JButton("Change Name");
		_jButtonChangeName.setToolTipText("Change your name!");
		_jButtonChangeName.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				user.getLobbywindow().getUsername();
			}

		});
		this.add(_jButtonChangeName);
		this.setLayout(new FlowLayout());
	}

	/**
	 * Join game request.
	 *
	 * @param selectedGame the selected game
	 */
	private void joinGameRequest(int selectedGame) {
		if (selectedGame >= 0) {
			int selectedGameID = getGameIDfromString(modelGamesText.get(selectedGame));
			String arguments[] = { selectedGameID + "" };
			user.sendMessageToServer(Command.UPDATE_GAME, Option.JOIN_GAME, arguments);
		}
	}

	/**
	 * Gets the game id from string.
	 *
	 * @param s the s
	 * @return the game id from string
	 */
	private int getGameIDfromString(String s) {
		s.trim();
		String[] a = s.split("\\s+");
		try {
			return Integer.parseInt(a[1]);
		} catch (Exception e) {

		}
		return 0;

	}

	/**
	 * Adds a new Game with the Game ID and the playercount.
	 * 
	 * @param id
	 *            ID of the Game
	 * @param playercount
	 *            current Playercount in the Game.
	 */
	public void addJPanelGame(int id, int playercount) {
		String ids = id + "";

		modelGamesText.addElement(ids);
		listGames.validate();

		this.add(listGames);

	}

	/**
	 * Reset list.
	 */
	public void resetList() {
		this.modelGames.clear();
		this.modelGamesText.clear();
	}

	/**
	 * Adds the j panel game.
	 *
	 * @param game the game
	 */
	public void addJPanelGame(Game game) {
		modelGames.put(game.getGameID(), game);
		modelGamesText.addElement(game.getShowText());
		listGames.validate();
		this.add(listGames);
	}

	/**
	 * Gets the jpanel games.
	 *
	 * @return the jpanel games
	 */
	public Hashtable<Integer, JLabel> getJpanelGames() {
		return jpanelGames;
	}

	/**
	 * Sets the jpanel games.
	 *
	 * @param jpanelGames the jpanel games
	 */
	public void setJpanelGames(Hashtable<Integer, JLabel> jpanelGames) {
		this.jpanelGames = jpanelGames;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public GameClient getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(GameClient user) {
		this.user = user;
	}

	/**
	 * Gets the model games.
	 *
	 * @return the model games
	 */
	public Hashtable<Integer, Game> getModelGames() {
		return modelGames;
	}

	/**
	 * Sets the model games.
	 *
	 * @param modelGames the model games
	 */
	public void setModelGames(Hashtable<Integer, Game> modelGames) {
		this.modelGames = modelGames;
	}

}
