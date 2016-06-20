package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

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
public class ServerWindow extends Component implements KeyListener {

	/** the simulated framebuffer. */
	private BufferedImage _image;
	
	/** the java2D graphics context of above framebuffer. */
	private Graphics2D _imageGraphics;
	
	/** the Swing window this class manages. */
	private JFrame _jFrame;

	/**
	 * A JButton. Handles send requests.
	 */
	private JButton _jButtonStop;

	/** The _j button send. */
	private JButton _jButtonSend;

	/** The J label portand ip. */
	private JLabel JLabelPortandIP;

	/** The panel. */
	private JPanel panel;

	/** The _j text chat. */
	public JTextField _jTextChat;

	/** The stop server listener. */
	private ActionListener stopServerListener;

	/** The send server listener. */
	private ActionListener sendServerListener;

	/**
	 * Main Boarder for all panels.
	 */
	private Border border = BorderFactory.createLineBorder(Color.black);

	/**
	 * Needed to send ClientMessages.
	 */
	public GameServer gameServer;

	/**
	 * Creates a new instance of the ImageWindow class.
	 *
	 * @param width            the width of the paint (image) area
	 * @param height            the height of the paint (image) area
	 * @param server the server
	 */
	public ServerWindow(int width, int height, GameServer server) {
		this.gameServer = server;
		setFocusable(true);
	}

	/**
	 * Creates a Window w/o the server.
	 */
	public ServerWindow() {
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
		_jFrame.setPreferredSize(new Dimension(200, 200));

		BorderLayout layout = new BorderLayout();
		_jFrame.setLayout(layout);

		panel = new JPanel();
		this._jFrame.add(this.panel);

		_jButtonStop = new JButton("Stop Server");
		_jButtonStop.setText("Stop Server");
		_jButtonStop.setToolTipText("Closes all Threads and shuts down the server");

		_jButtonSend = new JButton("Send Message");
		_jButtonSend.setText("Send Message");
		_jButtonSend.setToolTipText("Sends a message to all users on the Server");

		JLabelPortandIP = new JLabel();
		String IP = "";
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.gameServer.log.error("Was not able to get IP Adress: " + e);
		}
		String PortAndIP = "Server: " + IP + ":" + this.gameServer.getPortNumber();
		_jFrame.setTitle(PortAndIP);
		JLabelPortandIP.setText(PortAndIP);
		JLabelPortandIP.validate();

		stopServerListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gameServer.stopServer();
				String[] arguments = { "Server", "is", "shutting", "down" };
				gameServer.sendMessageToLobby(Command.CHAT, Option.SERVER_MESSAGE, arguments);
			}
		};
		_jButtonStop.addActionListener(stopServerListener);
		_jButtonStop.setPreferredSize(new Dimension(100, 50));

		sendServerListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				sendButtonpressed();
			}
		};
		_jButtonSend.addActionListener(sendServerListener);
		_jButtonSend.setPreferredSize(new Dimension(100, 50));

		this._jTextChat = new JTextField();

		KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					sendButtonpressed();

				}

			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};
		_jTextChat.addKeyListener(keyListener);

		_jTextChat.setBorder(border);

		_jTextChat.setPreferredSize(new Dimension(200, 40));

		panel.add(JLabelPortandIP);
		panel.add(_jButtonStop);
		panel.add(_jTextChat, BorderLayout.CENTER);
		panel.add(_jButtonSend);

		_jFrame.pack();
	}

	/**
	 * Send buttonpressed.
	 */
	protected void sendButtonpressed() {
		String s = _jTextChat.getText();
		String[] arguments = s.split("\\s+");
		gameServer.sendMessageToLobby(Command.CHAT, Option.SERVER_MESSAGE, arguments);
	}

	/**
	 * Opens the image window.
	 */
	public void openWindow() {
		openWindow("", 0, 0);
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
		}
	}

	/**
	 * Pauses this Thread for the number of milliseconds.
	 *
	 * @param milliseconds            the pause length
	 */
	public void pause(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * Gets the port number.
	 *
	 * @return the port number
	 */
	public int getPortNumber() {
		String eingabe;
		eingabe = JOptionPane.showInputDialog(null, "Enter your preferred Port Number", "get port number",
				JOptionPane.PLAIN_MESSAGE);

		int portNumber = 0;
		try {
			portNumber = Integer.parseInt(eingabe);
		} catch (NumberFormatException e) {
			portNumber = -1;
		}
		return portNumber;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			redraw();
			_jFrame.pack();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			redraw();
			_jFrame.pack();
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
