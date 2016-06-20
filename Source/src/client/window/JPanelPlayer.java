package client.window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * Class for JPanelPlayer. Extend JPanel. Implements the Layout for the JPanel
 * ans sets preferred Size. Adds JLabel to the Panel.
 * 
 * @author mathias
 *
 */
public class JPanelPlayer extends JPanel {

	/** Button to update the Lobby. */
	private JButton _jUpdateLobbyButton;

	/** The _j log out. */
	private JButton _jLogOut;

	/** The list player. */
	private JList listPlayer;
	
	/** The model. */
	private DefaultListModel<String> model;
	
	/** The window. */
	private LobbyWindow window;

	/**
	 * Handles the JPanel for the Players.
	 *
	 * @param window the window
	 */
	public JPanelPlayer(LobbyWindow window) {
		this.window = window;
		model = new DefaultListModel<String>();
		listPlayer = new JList(model);
		listPlayer.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listPlayer.setLayoutOrientation(JList.VERTICAL);
		listPlayer.setVisibleRowCount(-1);
		listPlayer.setFocusable(false);
		ListSelectionListener listListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = listPlayer.getSelectedIndex();
				String s = model.get(i);
				window.get_panelChat().addSayArgument(s);
			}
		};

		listPlayer.addListSelectionListener(listListener);
		JScrollPane listScroller = new JScrollPane(listPlayer);
		listScroller.setPreferredSize(new Dimension(170, 200));
		this.add(listScroller);
		_jUpdateLobbyButton = new JButton("Update Lobby");
		_jUpdateLobbyButton.setToolTipText("Updates everything");
		_jUpdateLobbyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String arguments[] = {};
				window.getUser().sendMessageToServer(Command.UPDATE_LOBBY, Option.ALL, arguments);
				window.redraw();
			}
		});

		_jLogOut = new JButton("Log out");
		_jLogOut.setToolTipText("By pressing u log out and the Window will automatically close.");
		_jLogOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				window.kickPlayer();
			}
		});

		this.add(_jUpdateLobbyButton);
		this.add(_jLogOut);
		this.setLayout(new FlowLayout());
	}

	/**
	 * Adds a jLabel to the _panelPlayer only if it does not exist yet.
	 * 
	 * @param id
	 *            The ID of the Player
	 * @param insidetext
	 *            Most likely the Name of the Player.
	 */
	public void addJlabelPlayer(int id, String insidetext) {
		if (insidetext != null) {
			if (playerAlreadyinList(insidetext)) {
				model.addElement(insidetext);
				listPlayer.validate();
			}
		}
	}

	/**
	 * Checks if a given name is already in the list.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	private boolean playerAlreadyinList(String name) {
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equalsIgnoreCase(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes the jLabel with name form the model.
	 *
	 * @param name the name
	 */
	public void removeJlabelPlayer(String name) {
		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equalsIgnoreCase(name)) {
				model.remove(i);
				listPlayer.validate();
			}
		}
	}

	/**
	 * Deletes all Items in the model-list.
	 */
	public void resetLists() {
		model.clear();
	}
}
