package client.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

// TODO: Auto-generated Javadoc
/**
 * Handles the JPanel for the Chat. Implements a JTextField for the Chat Inputs
 * aswell as a JTextPane to show the Text.
 * 
 * @author mathias
 *
 */
public class JPanelChat extends JPanel {
	
	/** The _j text chat. */
	public JTextField _jTextChat;
	
	/** The _j text chat show. */
	private JTextPane _jTextChatShow;
	
	/** The String text. */
	private String StringText = "<meta charset=\"utf-8\">";
	
	/** The border. */
	private Border border;
	
	/** The window. */
	private LobbyWindow window;

	/**
	 * Constructor for JPanelChat. This class implements how the Labels in this
	 * Panel should be set.
	 *
	 * @param window
	 *            the window
	 */
	JPanelChat(LobbyWindow window) {
		this.window = window;
		border = BorderFactory.createLineBorder(Color.black);

		this._jTextChat = new JTextField();
		this._jTextChatShow = new JTextPane();
		_jTextChatShow.setContentType("text/html");
		this.setLayout(new FlowLayout());
		_jTextChatShow.setEditable(false);
		_jTextChatShow.setPreferredSize(new Dimension(700, 180));

		this.add(new JScrollPane(_jTextChatShow));

		/*
		 * Key Listener to make Chat Inputs with Enter possible.
		 */
		KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					window.sendButtonPressed();
				}

			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};
		_jTextChat.addKeyListener(keyListener);

		_jTextChat.setBorder(border);
		_jTextChat.setPreferredSize(new Dimension(600, 40));
		this.add(_jTextChat);

	}

	/**
	 * Sets new Text to Chat Panel.
	 *
	 * @param insidetext
	 *            the new text show
	 */

	public void setTextShow(String insidetext) {
		StringText += insidetext + "<br>";
		this._jTextChatShow.setText(StringText);
	}

	/**
	 * Sets Caret Position. Used to be always scrolled down.
	 */
	public void setRollPosition() {
		this._jTextChatShow.setCaretPosition(this._jTextChatShow.getDocument().getLength());
	}

	/**
	 * Gets the text show.
	 *
	 * @return the text show
	 */
	public String getTextShow() {
		return this._jTextChatShow.getText();
	}

	/**
	 * Gets the text input.
	 *
	 * @return the text input
	 */
	public String getTextInput() {
		return this._jTextChat.getText();
	}

	/**
	 * Adds the say argument.
	 *
	 * @param s
	 *            the s
	 */
	public void addSayArgument(String s) {
		this._jTextChat.setText("/say " + s + " ");
	}
}
