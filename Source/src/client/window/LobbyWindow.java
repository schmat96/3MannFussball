package client.window;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.jsoup.Jsoup;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import client.GameClient;
import models.Game;
import models.Lobby;
import models.OldGame;
import models.Player;
import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * A class that manages the Lobby as java swing window. Adds all Buttons and
 * Panels with the KeyListener.
 * 
 * @author mathias
 *
 */
@SuppressWarnings("serial")
public class LobbyWindow extends Component implements KeyListener {

	/** the simulated framebuffer. */
	private BufferedImage _image;
	
	/** the java2D graphics context of above framebuffer. */
	private Graphics2D _imageGraphics;
	
	/** the Swing window this class manages. */
	private JFrame _jFrame;
	/**
	 * Handles all kind of Players in the Lobby.
	 */
	private JPanelPlayer _panelPlayer;

	/**
	 * Lists all Games.
	 */
	private JPanelGames _panelGames;

	/** The _panel old games. */
	private JPanelOldGames _panelOldGames;

	/**
	 * Handles a single Game if the user joined one.
	 */
	private JPanelGame _panelGame;

	/**
	 * Handles text input and the actual text feed.
	 */
	private JPanelChat _panelChat;

	/**
	 * A JButton. Handles send requests.
	 */
	private JButton _jButtonSend;

	/**
	 * JButton to show old Games.
	 */
	private JButton _jButtonShowOldGames;

	/**
	 * Handles add Game requests.
	 */
	private JButton _jButtonAddGame;

	/**
	 * Sets a Player to ready.
	 */
	private JButton _jSetReady;

	/** JButton for LeaveGame requests. */
	private JButton _jLeaveGame;

	/** The _j back to overview. */
	private JButton _jBackToOverview;

	/** The _j text field information game leader. */
	private JTextPane _jTextFieldInformationGameLeader;

	/**
	 * Used to send PlayerUpdate only to the users in the current Game.
	 */
	private int incurrentGameID;

	/**
	 * JOption Pane for the old games.
	 */
	private JOptionPane showAllOldGames;
	/**
	 * JOptionPane for Server Messages.
	 */
	private JOptionPane ServerMessagePane;

	/**
	 * Action Listener for the Buttons.
	 */
	private ActionListener addGameListener;

	/** The leave game listener. */
	private ActionListener leaveGameListener;

	/** The set game ready flag. */
	private ActionListener setGameReadyFlag;

	/** The show old games. */
	private ActionListener showOldGames;

	/**
	 * ActionListener for send requests.
	 */
	private ActionListener sendListener;

	/**
	 * Main Boarder for all panels.
	 */
	private Border border = BorderFactory.createLineBorder(Color.black);

	/**
	 * JLabel[] for the Game Panel. Only 3 are needed, its not possible to have
	 * more Player in a single Game.
	 */
	private JLabel[] _jLabelPlayerInGame = new JLabel[3];

	/**
	 * Needed to send ClientMessages.
	 */
	private GameClient user;

	/** The game leader. */
	private boolean gameLeader = false;

	/** The _j button fill empty places with bots. */
	private Checkbox _jButtonFillEmptyPlacesWithBots;

	/** The Constant GAME_TIME_MIN. */
	static final int GAME_TIME_MIN = 5;
	
	/** The Constant GAME_TIME_MAX. */
	static final int GAME_TIME_MAX = 600;
	
	/** The Constant GAME_TIME_INIT. */
	static final int GAME_TIME_INIT = 60;
	
	/** The Constant MIN_SIZE_NAME. */
	private static final int MIN_SIZE_NAME = 3;
	
	/** The Constant MAX_SIZE_NAME. */
	private static final int MAX_SIZE_NAME = 16;

	/** The _j slider game time. */
	private JSlider _jSliderGameTime;
	
	/** The old games. */
	private boolean oldGames = false;

	/**
	 * Creates a new instance of the ImageWindow class.
	 *
	 * @param width            the width of the paint (image) area
	 * @param height            the height of the paint (image) area
	 * @param user the user
	 */
	public LobbyWindow(int width, int height, GameClient user) {
		this.user = user;
		openWindow();
		setFocusable(true);
	}

	/**
	 * Creates a Window w/o the user.
	 */
	public LobbyWindow() {
		setFocusable(true);
	}

	/**
	 * Resizes the paint (image) area to the given size. Also resizes the window
	 * border around it.<br>
	 * The old paint area will be copied to the new paint area and clipped if
	 * necessary.
	 * 
	 * @param newWidth
	 *            the resized width of the paint area
	 * @param newHeight
	 *            the resized height of the paint area
	 */
	public void resizeImage(int newWidth, int newHeight) {
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		newImage.getGraphics().setColor(new Color(255, 255, 255, 255));
		((Graphics2D) (newImage.getGraphics())).fill(new Rectangle2D.Float(0, 0, newWidth, newHeight));
		newImage.getGraphics().drawImage(_image, 0, 0, null);
		_image = newImage;
		_imageGraphics = (Graphics2D) _image.getGraphics();
		_jFrame.pack();
	}

	/**
	 * Opens the window at the x,y location on screen with the given Windowname.
	 *
	 * @param windowName            the windowname
	 * @param x            x-position on screen
	 * @param y            y-position on screen
	 */
	public void openWindow(String windowName, int x, int y) {
		if (_jFrame != null) {
			_jFrame.dispose();
		}
		_jFrame = new JFrame(windowName);
		_jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (_jFrame != null) {
					_jFrame.dispose();
					_jFrame = null;
				}
			}
		});
		_jFrame.setLocation(x, y);
		_jFrame.add(this);
		_jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_jFrame.setVisible(true);
		_jFrame.setPreferredSize(new Dimension(800, 550));
		// _jFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		// _jFrame.setUndecorated(true);
		BorderLayout layout = new BorderLayout();
		_jFrame.setLayout(layout);
		_panelPlayer = new JPanelPlayer(this);
		_panelChat = new JPanelChat(this);
		_panelPlayer.setPreferredSize(new Dimension(200, 450));
		_jFrame.add(_panelPlayer, BorderLayout.LINE_START);
		_panelGames = new JPanelGames(user);
		_panelOldGames = new JPanelOldGames(this);
		_panelGame = new JPanelGame(user);

		_jTextFieldInformationGameLeader = new JTextPane();
		_jTextFieldInformationGameLeader.setEditable(false);
		_jTextFieldInformationGameLeader
				.setText("You are Game Leader. Set the arguments for the Game and Start it afterwards!");

		_panelGames.setPreferredSize(new Dimension(500, 200));
		_jFrame.add(new JScrollPane(_panelGames), BorderLayout.LINE_END);

		_panelChat.setPreferredSize(new Dimension(700, 250));
		_jFrame.add(_panelChat, BorderLayout.PAGE_END);

		_panelGame.setPreferredSize(new Dimension(400, 250));
		
		_panelOldGames.setPreferredSize(new Dimension(400, 250));

		addPanelsAndButtons();
		_jFrame.pack();
	}

	/**
	 * Opens the image window.
	 */
	public void openWindow() {
		openWindow("ImageWindow", 0, 0);
	}

	/**
	 * Closes the image window.
	 */
	public void closeWindow() {
		if (_jFrame == null)
			return;
		_jFrame.dispose();
		_jFrame = null;
	}

	/**
	 * Checks if is window open.
	 *
	 * @return true, if is window open
	 */
	public boolean isWindowOpen() {
		if (_jFrame == null)
			return false;
		return true;
	}

	/**
	 * Forces swing to redraw the current image.
	 */
	public void redraw() {
		if (_jFrame != null) {
			_jFrame.repaint();
			_jFrame.validate();
			_jFrame.revalidate();
			_jFrame.pack();
		}
	}

	/**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
	public int getImageWidth() {
		return _image.getWidth();
	}

	/**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
	public int getImageHeight() {
		return _image.getHeight();
	}

	/**
	 * Adds all Buttons and the Action Listener to the JFrame.
	 * Maybe add new Methods for the panels to keep it clean.
	 */
	private void addPanelsAndButtons() {

		for (int n = 0; n < 3; n++) {
			_jLabelPlayerInGame[n] = new JLabel("no Player yet connected");
			_jLabelPlayerInGame[n].setText("no Player yet connected");
			_jLabelPlayerInGame[n].setFont(new Font("Verdana", 1, 20));
			_jLabelPlayerInGame[n].setBackground(new Color(255, 255, 255));
			_jLabelPlayerInGame[n].setVisible(true);
			_panelGame.add(_jLabelPlayerInGame[n]);
		}

		_jButtonFillEmptyPlacesWithBots = new Checkbox("Fill empty Players with Bots", null, false);

		sendListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sendButtonPressed();
			}

		};

		/**
		 * Action Listener to Add a new Game.
		 */
		addGameListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				user.sendMessageToServer(Command.UPDATE_LOBBY, Option.CREATE_GAME, null);
			}
		};

		/**
		 * Action Listener to leave a current Game.
		 */
		leaveGameListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String[] arguments = { incurrentGameID + "" };
				user.sendMessageToServer(Command.UPDATE_GAME, Option.LEAVE_GAME, arguments);
			}
		};

		/**
		 * Action Listener to set a the GameReadyFlag to true or false.
		 */
		setGameReadyFlag = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String booleanBots = "false";
				String GameTime = GAME_TIME_INIT + "";
				if (_jButtonFillEmptyPlacesWithBots.getState()) {
					booleanBots = "true";
				}
				if (gameLeader) {
					GameTime = _jSliderGameTime.getValue() + "";
				}
				String[] arguments = { incurrentGameID + "", booleanBots, GameTime };
				user.sendMessageToServer(Command.UPDATE_GAME, Option.I_AM_READY, arguments);
			}
		};

		/**
		 * A ActionListener which makes for each OldGame a new JPane. #TODO
		 * Maybe make the JPane Scrollable and only show a single JPane.
		 */
		showOldGames = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String[] arguments = {};
				user.sendMessageToServer(Command.UPDATE_LOBBY, Option.GET_OLD_GAMES, arguments);
				setOldGames(true);
				refreshWindow();
			}
		};

		_jButtonShowOldGames = new JButton("Show Old Games");
		_jButtonShowOldGames.addActionListener(showOldGames);
		GridBagConstraints q = new GridBagConstraints();

		q.gridx = 10;
		q.gridy = 1;
		q.gridwidth = 1;
		_panelGames.add(_jButtonShowOldGames, q);

		_jButtonAddGame = new JButton("Add Game");
		_jButtonAddGame.addActionListener(addGameListener);
		_panelGames.add(_jButtonAddGame);

		_jButtonSend = new JButton("send");
		_jButtonSend.addActionListener(sendListener);
		_panelChat.add(_jButtonSend);

		_jSetReady = new JButton("Ready?");
		_jSetReady.addActionListener(setGameReadyFlag);
		_panelGame.add(_jSetReady);
		_jLeaveGame = new JButton("Leave Game?");
		_jLeaveGame.addActionListener(leaveGameListener);
		_panelGame.add(_jLeaveGame);

		_jSliderGameTime = new JSlider(JSlider.HORIZONTAL, GAME_TIME_MIN, GAME_TIME_MAX, GAME_TIME_INIT);
		_jSliderGameTime.setToolTipText("Set the Game Time. In seconds.");
		_jSliderGameTime.setMajorTickSpacing(30);
		_jSliderGameTime.setMinorTickSpacing(120);
		_jSliderGameTime.setPaintTicks(true);
		_jSliderGameTime.setPaintLabels(true);
		this.redraw();
	}

	/**
	 * Methode for using either Enter or press the send Button. Checks if its a
	 * private message or global. Sends the message from the jTextAres to the
	 * Server
	 */
	protected void sendButtonPressed() {
		String s = _panelChat.getTextInput();
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		if (s.length() > 4) {
			sendMessageToServer(s);
		} else {
			String[] words = s.split(" ");
			String[] arguments = new String[words.length + 1];
			arguments[0] = 0 + "";
			for (int i = 1; i < words.length + 1; i++) {
				arguments[i] = words[i - 1];
			}
			user.sendMessageToServer(Command.CHAT, Option.GLOBAL, arguments);
			_panelChat._jTextChat.setText("");
		}

	}

	/**
	 * Checks if its a pivate message or global and sends it to the Server.
	 * 
	 * @param s
	 *            contains the message.
	 */
	public void sendMessageToServer(String s) {
		if (s.substring(0, 4).equals("/say")) {
			String message = "";
			String[] words = s.split("\\s+");
			String[] arguments = new String[words.length - 1];
			int sendtoID = user.getLobby().getPlayerIDfromName(words[1]);
			if (sendtoID == -1) {
				this.addInformationText("Player name not found. Please check your input.");
			} else {
				arguments[0] = sendtoID + "";
				for (int i = 1; i < words.length - 1; i++) {
					arguments[i] = words[i + 1];
					message += arguments[i] + " ";
				}
				user.sendMessageToServer(Command.CHAT, Option.PRIVATE, arguments);
				this.addInformationText("To " + user.getLobby().getPlayerNamefromID(sendtoID) + ": " + message);
				_panelChat._jTextChat.setText("/say " + words[1]);
				_panelChat.repaint();
			}
		} else if (s.substring(0, 5).equals("/game")) {
			String[] words = s.split("\\s+");
			String[] arguments = words;
			int currentPlayerID = this.user.getPlayerID();
			int currentGameID = this.user.getLobby().playerInGame(currentPlayerID);
			if (currentGameID == 0) {
				this.addInformationText("Your are currently not in a game. /game cant be used.");
			} else {
				arguments[0] = 0 + "";
				user.sendMessageToServer(Command.CHAT, Option.PRIVATE, arguments);
				
				_panelChat.repaint();
			}
		} else {
			String[] words = s.split(" ");
			String[] arguments = new String[words.length + 1];
			arguments[0] = 0 + "";
			for (int i = 1; i < words.length + 1; i++) {
				arguments[i] = words[i - 1];
			}
			user.sendMessageToServer(Command.CHAT, Option.GLOBAL, arguments);
			_panelChat._jTextChat.setText("");
		}
	}

	/**
	 * Gets the server port and ip.
	 *
	 * @return the server port and ip
	 */
	public String[] getServerPortandIP() {
		int ServerPortInt = 0;

		String ServerPortString = "ServerPort:";
		String ServerIPString = "ServerIP:";

		JTextField ServerPort = new JTextField();

		JTextField ServerIP = new JTextField();
		Object[] message = { ServerPortString, ServerPort, ServerIPString, ServerIP };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);
		pane.createDialog(null, "Server Port and Server IP").setVisible(true);

		String[] returnArray = { ServerPort.getText(), ServerIP.getText() };
		return returnArray;
	}

	/**
	 * Asks the user with a Pop-Up for his Name.
	 *
	 * @return the username
	 */
	public void getUsername() {

		Object[] objects = { "Show your Custom Name", "Login", "Cancel" };

		String name = getCustomName();

		JPanel panel = new JPanel();
		JLabel infotext = new JLabel("Set your Name or use your custom name");
		panel.setLayout(new FlowLayout());
		panel.add(infotext);
		JTextField textField = new JTextField(MAX_SIZE_NAME);
		textField.setText(name);
		panel.add(textField);

		while (true) {
			int result = JOptionPane.showOptionDialog(null, panel, "Enter Username", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, objects, null);
			if (result == JOptionPane.NO_OPTION) {
				
				String a = textField.getText();
				if (a.length()<MIN_SIZE_NAME || a.length()>MAX_SIZE_NAME) {
					textField.setText(this.getCustomName());
					infotext.setText("Your name was shorter then "+MIN_SIZE_NAME+" or longer then "+MAX_SIZE_NAME+". Custom name has been set.");
				} else {
				String b = a.replaceAll("[^A-Za-z0-9]", "");
				if (a.equals(b)==false) {
					infotext.setText("U are only allowed to use Chars from the alphabate or numbers. Adjustments have been made.");
					textField.setText(b);
					

					
					if (this.checkNameCollision(b)) {
						textField.setText(this.getCustomName());
						infotext.setText("We removed special chars and checked if the name is already taken. It was! Custom name has been set.");
						
					} else {
						if (b.length()<MIN_SIZE_NAME || b.length()>MAX_SIZE_NAME) {
							infotext.setText("After removing special Chars your name was shorter then "+MIN_SIZE_NAME+" or longer then "+MAX_SIZE_NAME+". Custom name has been set.");
							textField.setText(this.getCustomName());
						} else {
							textField.setText(b);
						}
						
						
					}
				} else {
					if (this.checkNameCollision(a) == false) {
						name = a;
						break;
					} else {
						textField.setText(this.getCustomName());
						infotext.setText("Name already taken. Custom name has been set.");
					}
				}
				}
				
				
			} else if (result == JOptionPane.YES_OPTION) {
				textField.setText(this.getCustomName());
			} else if (result == JOptionPane.CLOSED_OPTION) {
				this.kickPlayer();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				this.kickPlayer();
			}
		}

		changeWindowName(name);
		String[] arguments = { name };
		user.sendMessageToServer(Command.UPDATE_PLAYER, Option.SET_NAME, arguments);
		String[] arguments1 = {};
		user.sendMessageToServer(Command.UPDATE_PLAYER, Option.ID, arguments1);
		user.sendMessageToServer(Command.UPDATE_LOBBY, Option.ALL, arguments1);
	}

	/**
	 * Kick player.
	 */
	public void kickPlayer() {
		String arguments[] = {};
		getUser().sendMessageToServer(Command.UPDATE_LOBBY, Option.REMOVE_PLAYER, arguments);
		addInformationText("U will be logged off and the window closes.");
		System.exit(0);

	}

	/**
	 * Gets the custom name.
	 *
	 * @return the custom name
	 */

	private String getCustomName() {
		String s;
		try {
			s = System.getProperty("user.name");
			s = s.replaceAll("\\s+", "");
			if (s.length()>MAX_SIZE_NAME || s.length()<MIN_SIZE_NAME) {
				s = "CustomNameYourComputerNameisTooShort";
			}
		} catch (SecurityException e) {
			s = "CustomNameSecurityProblem";
		} catch (NullPointerException e) {
			s = "CustomNameNoPropertyNameSet";
		} catch (IllegalArgumentException e) {
			s = "CustomName";
		}
		int i = 1;
		String name = s;
		while (true) {
			if (checkNameCollision(s)) {
				s = name + i;
				i++;
			} else {
				break;
			}
		}
		return s;
	}

	/**
	 * Checks if a Playername s already exists. Returns true if he does.
	 *
	 * @param s the s
	 * @return true if the name is taken, false if it is free
	 */
	private boolean checkNameCollision(String s) {
		String[] arguments1 = {};
		user.sendMessageToServer(Command.UPDATE_LOBBY, Option.ALL, arguments1);
		return user.getLobby().playerNameExists(s);
	}

	/**
	 * Changes the windowname.
	 *
	 * @param windowname the windowname
	 */
	public void changeWindowName(String windowname) {
		this._jFrame.setTitle("Hexagoal - Logged in as: " + windowname);
	}

	/**
	 * Adds the Game Panel to the Lobby and removes the Panel with all Games.
	 *
	 * @param currentGameID the current game id
	 */
	public void userJoinedGame(int currentGameID) {
		this.incurrentGameID = currentGameID;
		this._jFrame.remove(this._panelGames);
		this._jFrame.add(_panelGame);
		_panelGame.repaint();
		_panelGame.validate();
	}

	/**
	 * Removes the _panelGame from the jFrame but adds the _panelGames to the
	 * Frame.
	 */
	public void userleftGame() {
		this._jFrame.remove(this._panelGame);
		this._jFrame.add(_panelGames);
		_panelGames.repaint();
		_panelGames.validate();
	}

	/**
	 * Adds for each player in a game the Player Panel.
	 * 
	 * @param playerName
	 *            PlayerName
	 * @param ready
	 *            Readyflag
	 * @param deep
	 *            Which position the player has in the GameLobby.
	 */
	public void gamePanelAddPlayers(String playerName, boolean ready, int deep) {
		if (playerName.equals("")) {
			_jLabelPlayerInGame[deep].setText("no player yet connected");
			_jLabelPlayerInGame[deep].validate();
			_panelGame.validate();
			if (_jFrame != null) {
				_jFrame.add(_panelGame);
			}
			setFocusTraversalKeysEnabled(false);
		} else {
			if (deep == 0) {
				_jLabelPlayerInGame[deep].setText(playerName + " Game Leader");
			} else {
				_jLabelPlayerInGame[deep].setText(playerName + " " + ready);
			}
			_jLabelPlayerInGame[deep].validate();
			_panelGame.validate();
			if (_jFrame != null) {
				_jFrame.add(_panelGame);
			}
			setFocusTraversalKeysEnabled(false);
		}
		if (gameLeader == true) {

		} else {
			_panelGame.remove(_jButtonFillEmptyPlacesWithBots);
		}
	}

	/**
	 * Opens a JOption Pane to show a Old Game.
	 *
	 * @param text the text
	 */
	public void OldGamesPaneCall(String text) {
		showAllOldGames = new JOptionPane();
		JOptionPane.showMessageDialog(null, text, "Old Games", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Opens a JOption Pane for a ServerMessage.
	 *
	 * @param message the message
	 */
	public void ServerMessagePaneCall(String message) {
		ServerMessagePane = new JOptionPane();
		JOptionPane.showMessageDialog(null, message, "Server Message", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Key Listener for keyTyped. Mainly used for refreshing Window.
	 *
	 * @param e the e
	 */
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			System.out.println("key typed");
		}
	}

	/**
	 * Key Listener for keyPressed. Not used but needed cause class Implements
	 * KeyListener.
	 *
	 * @param e the e
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			redraw();
			_jFrame.pack();
		}

	}

	/**
	 * Key Listener for keyReleased. Not used but needed cause class Implements
	 * KeyListener.
	 *
	 * @param e the e
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds the specific jButtons and JSliders if the actual Player is game
	 * Leader.
	 */
	public void addPanelsAsGameLeader() {
		_panelGame.add(_jTextFieldInformationGameLeader);
		_panelGame.add(_jButtonFillEmptyPlacesWithBots);
		_panelGame.add(_jSliderGameTime);
		_jSetReady.setText("Start Game?");
		_jSetReady.setToolTipText(
				"Server will recheck if all players are ready! If u press this Button twice, your status will be reset to false.");
		_panelGame.validate();
	}

	/**
	 * Add and removes the specific jButton/jSliders to the panel if the actual
	 * Player is game Leader.
	 */
	public void addPanelsNotGameLeader() {
		_panelGame.remove(_jButtonFillEmptyPlacesWithBots);
		_panelGame.remove(_jTextFieldInformationGameLeader);
		_panelGame.remove(_jSliderGameTime);
		_jSetReady.setText("Ready?");
		_jSetReady.setToolTipText("Game will start as soon as Player Ready is ready.");
		_panelGame.validate();
	}

	/**
	 * Refreshes the Window with all Players and Games.
	 */
	public void refreshWindow() {
		
		_panelPlayer.resetLists();
		_panelGames.resetList();
		ArrayList<Player> players = new ArrayList<Player>(user.getLobby().getPlayers().values());
		for (Player player : players) {
			_panelPlayer.addJlabelPlayer(player.getPlayerID(), player.getPlayerName());
		}

		

		int currentPlayerID = this.user.getPlayerID();
		
		Player player = this.user.getLobby().getPlayers().get(currentPlayerID);
		
		if (currentPlayerID == 0) {
			return;
		}
		
		int playerGameID = player.getCurrentGameID();
		
		if (playerGameID != 0) {
			userJoinedGame(playerGameID);
			Game game = user.getLobby().getGame(playerGameID);

			if (game != null) {
				ArrayList<Integer> playerIDs = game.getPlayerIDs();
				int n = 0;
				for (Integer playerID : playerIDs) {
					Player playerInGame = user.getLobby().getPlayers().get(playerID);
					
					String playerName = playerInGame.getPlayerName();
					boolean playerReadyFlag = playerInGame.isReadyflag();
					
					gamePanelAddPlayers(playerName, playerReadyFlag, n);
					
					if (currentPlayerID == game.getPlayerLeader()) {
						gameLeader = true;
					} else {
						gameLeader = false;
					}
					
					n++;
				}
				
				if (n == 1) {
					gamePanelAddPlayers("", true, 1);
					gamePanelAddPlayers("", true, 2);
				}else if (n == 2) {
					gamePanelAddPlayers("", true, 2);
				}

				if (gameLeader) {
					addPanelsAsGameLeader();
				} else {
					addPanelsNotGameLeader();
				}
			}
		} else {
			userleftGame();
			if (isOldGames()) {
				this._jFrame.remove(_panelGames);
				this._jFrame.add(_panelOldGames);
				_panelOldGames.resetList();
				ArrayList<OldGame> oldgames = null;
				oldgames = new ArrayList<OldGame>(user.getLobby().getOldgames().values());
				for (OldGame game : oldgames) {
					_panelOldGames.addOldGame(game);
				}
			} else {
				this._jFrame.add(_panelGames);
				this._jFrame.remove(_panelOldGames);
				_panelGames.resetList();
				ArrayList<Game> games = null;
				games = new ArrayList<Game>(user.getLobby().getGames().values());
				for (Game game : games) {
					_panelGames.addJPanelGame(game);
				}
			}

		}

		redraw();
	}

	/**
	 * Adds Text to the ChatArea.
	 * 
	 * @param message
	 *            the actual message
	 * @param recipientID
	 *            if its a private message, this is the ID of the Player who
	 *            receives the message. If its global, -1 is trasmitted.
	 * @param senderID
	 *            The ID from where the Message comes.
	 */
	public void addText(String message, int recipientID, int senderID) {
		String senderName = this.user.getLobby().getPlayerNamefromID(senderID);
		if (recipientID == -1) {
			_panelChat.setTextShow("[global]<span style=\"color:#0B610B\">" + senderName + ":</span>"
					+ "<span style=\"color:#0040FF\">" + message + "</span>");
		} else if(recipientID == 0){
			_panelChat.setTextShow("<span style=\"color:#0B610B\">" + senderName + ":</span>"
					+ "<span style=\"color:#000000\">" + message + "</span>");
		}else{
			_panelChat.setTextShow("[whisper]<span style=\"color:#0B610B\">*" + senderName + "*:</span>"
					+ "<span style=\"color:#FFBF00\">" + message + "</span>");
		}
		_panelChat.setRollPosition();
	}

	/**
	 * For each OldGame a new jPane is created with the text inside it.
	 *
	 * @param text the text
	 */
	public void refreshOldGamesWindow(String text) {
		OldGamesPaneCall(text);
	}

	/**
	 * For each Server Message a JPane is created with the message inside it.
	 *
	 * @param message the message
	 */
	public void ServerMessage(String message) {
		ServerMessagePaneCall(message);
	}

	/**
	 * Checks if is old games.
	 *
	 * @return true, if is old games
	 */
	public boolean isOldGames() {
		return oldGames;
	}

	/**
	 * Sets the old games.
	 *
	 * @param oldGames the new old games
	 */
	public void setOldGames(boolean oldGames) {
		this.oldGames = oldGames;
	}

	/**
	 * Adds Information message to the _panelChat in the _jTextShow, mostly used
	 * for game results and unknown playerNames (/say).
	 *
	 * @param message            the message to be added.
	 */
	public void addInformationText(String message) {
		_panelChat.setTextShow("<span style=\"color:#FF0080\">" + message + "</span>");
		_panelChat.setRollPosition();
	}

	/**
	 * used to only show oldGames from a certain Player. Will delete all
	 * oldGames and readd them.
	 *
	 * @param playerName            from which the playerNames should be shown.
	 * @return the old games from player name
	 */
	public void getOldGamesFromPlayerName(String playerName) {
		ArrayList<OldGame> oldgames = null;
		oldgames = new ArrayList<OldGame>(user.getLobby().getOldgames().values());
		for (OldGame game : oldgames) {
			String[] playerNames = game.getPlayerNames();
			if (playerNames[0].equals(playerName) || playerNames[1].equals(playerName)
					|| playerNames[2].equals(playerName)) {
				_panelOldGames.addOldGame(game);
			}

		}
		this.redraw();

	}

	/**
	 * Returns the _panelPlayer.
	 *
	 * @return _panelPlayer
	 */
	public JPanelPlayer get_panelPlayer() {
		return _panelPlayer;
	}

	/**
	 * Sets _panelPlayer.
	 *
	 * @param _panelPlayer the new _panel player
	 */
	public void set_panelPlayer(JPanelPlayer _panelPlayer) {
		this._panelPlayer = _panelPlayer;
	}

	/**
	 * Gets the _panel games.
	 *
	 * @return the _panel games
	 */
	public JPanelGames get_panelGames() {
		return _panelGames;
	}

	/**
	 * Sets the _panel games.
	 *
	 * @param _panelGames the new _panel games
	 */
	public void set_panelGames(JPanelGames _panelGames) {
		this._panelGames = _panelGames;
	}

	/**
	 * Gets the _panel game.
	 *
	 * @return the _panel game
	 */
	public JPanelGame get_panelGame() {
		return _panelGame;
	}

	/**
	 * Sets the _panel game.
	 *
	 * @param _panelGame the new _panel game
	 */
	public void set_panelGame(JPanelGame _panelGame) {
		this._panelGame = _panelGame;
	}

	/**
	 * Gets the _panel chat.
	 *
	 * @return the _panel chat
	 */
	public JPanelChat get_panelChat() {
		return _panelChat;
	}

	/**
	 * Sets the _panel chat.
	 *
	 * @param _panelChat the new _panel chat
	 */
	public void set_panelChat(JPanelChat _panelChat) {
		this._panelChat = _panelChat;
	}

	/**
	 * Gets the jOption Pane for the old games.
	 *
	 * @return the jOption Pane for the old games
	 */
	public JOptionPane getShowAllOldGames() {
		return showAllOldGames;
	}

	/**
	 * Sets the jOption Pane for the old games.
	 *
	 * @param showAllOldGames the new jOption Pane for the old games
	 */
	public void setShowAllOldGames(JOptionPane showAllOldGames) {
		this.showAllOldGames = showAllOldGames;
	}

	/**
	 * Gets the server message pane.
	 *
	 * @return the server message pane
	 */
	public JOptionPane getServerMessagePane() {
		return ServerMessagePane;
	}

	/**
	 * Sets the server message pane.
	 *
	 * @param serverMessagePane the new server message pane
	 */
	public void setServerMessagePane(JOptionPane serverMessagePane) {
		ServerMessagePane = serverMessagePane;
	}

	/**
	 * Gets the needed to send ClientMessages.
	 *
	 * @return the needed to send ClientMessages
	 */
	public GameClient getUser() {
		return user;
	}

	/**
	 * Sets the needed to send ClientMessages.
	 *
	 * @param user the new needed to send ClientMessages
	 */
	public void setUser(GameClient user) {
		this.user = user;
	}

	/**
	 * Checks if is game leader.
	 *
	 * @return true, if is game leader
	 */
	public boolean isGameLeader() {
		return gameLeader;
	}

	/**
	 * Sets the game leader.
	 *
	 * @param gameLeader the new game leader
	 */
	public void setGameLeader(boolean gameLeader) {
		this.gameLeader = gameLeader;
	}

	/**
	 * Gets the _j button fill empty places with bots.
	 *
	 * @return the _j button fill empty places with bots
	 */
	public Checkbox get_jButtonFillEmptyPlacesWithBots() {
		return _jButtonFillEmptyPlacesWithBots;
	}

	/**
	 * Sets the _j button fill empty places with bots.
	 *
	 * @param _jButtonFillEmptyPlacesWithBots the new _j button fill empty places with bots
	 */
	public void set_jButtonFillEmptyPlacesWithBots(Checkbox _jButtonFillEmptyPlacesWithBots) {
		this._jButtonFillEmptyPlacesWithBots = _jButtonFillEmptyPlacesWithBots;
	}
}
