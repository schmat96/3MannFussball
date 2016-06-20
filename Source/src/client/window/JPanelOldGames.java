package client.window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import models.OldGame;
import net.Command;
import net.Option;

// TODO: Auto-generated Javadoc
/**
 * The Class JPanelOldGames.
 */
public class JPanelOldGames extends JPanel {

	/** The Show certain player. */
	private boolean ShowCertainPlayer = true;

	/** The list old games. */
	private List<OldGame> listOldGames;

	/** The _j back to overview. */
	private JButton _jBackToOverview;

	/** The _j high score. */
	private JButton _jHighScore;

	/** The _j show only games with certain player. */
	private JButton _jShowOnlyGamesWithCertainPlayer;

	/** The lobbywindow. */
	private LobbyWindow lobbywindow;

	/**
	 * Instantiates a new j panel old games.
	 *
	 * @param lobbywindow the lobbywindow
	 */
	public JPanelOldGames(LobbyWindow lobbywindow) {
		this.lobbywindow = lobbywindow;
		
		listOldGames = new ArrayList<>();

		OldGamesTableModel tableModel = new OldGamesTableModel(listOldGames);
		JTable table = new JTable((TableModel) tableModel);
		table.setAutoCreateRowSorter(false);
		table.setAutoscrolls(true);

		_jBackToOverview = new JButton("back");
		_jBackToOverview.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				lobbywindow.setOldGames(false);
				String arguments[] = {};
				lobbywindow.getUser().sendMessageToServer(Command.UPDATE_LOBBY, Option.ALL, arguments);
				lobbywindow.redraw();
			}

		});

		_jHighScore = new JButton("Show HighScore");
		_jHighScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] highScore = tableModel.getAllCountGoals();
				int highscore = 0;
				for (int i = 0; i < highScore.length; i++) {
					if (highScore[i] > highscore) {
						highscore = highScore[i];
						table.clearSelection();
						table.addRowSelectionInterval(i, i);
					} else if (highScore[i] == highscore) {
						table.addRowSelectionInterval(i, i);
					}
				}

			}

		});

		_jShowOnlyGamesWithCertainPlayer = new JButton("Show only Games from Certain Player");
		_jShowOnlyGamesWithCertainPlayer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (ShowCertainPlayer) {
					ShowCertainPlayer = false;
					_jShowOnlyGamesWithCertainPlayer.setText("Show All Games");
					int i = table.getSelectedRow();
					if (i < 0) {
						lobbywindow.addInformationText("U need to select a row to show the games from a certain Player");
					} else {
						table.clearSelection();
						String playerNameandGoals = (String) table.getValueAt(i, 2);
						String playerName = playerNameandGoals.split("\\s+")[0];
						resetList();
						lobbywindow.getOldGamesFromPlayerName(playerName);
					}
				} else {
					ShowCertainPlayer = true;
					_jShowOnlyGamesWithCertainPlayer.setText("Show only Games from Certain Player");
					lobbywindow.refreshWindow();
				}
			}

		});

		this.add(_jBackToOverview);
		this.add(_jHighScore);
		this.add(_jShowOnlyGamesWithCertainPlayer);
		JScrollPane JScrollPaneTable = new JScrollPane(table);
		JScrollPaneTable.setPreferredSize(new Dimension(450,230));
		this.add(JScrollPaneTable);
		
		this.setLayout(new FlowLayout());

	}

	/**
	 * Adds the old game.
	 *
	 * @param oldgame the oldgame
	 */
	public void addOldGame(OldGame oldgame) {
		listOldGames.add(oldgame);
	}

	/**
	 * Reset list.
	 */
	public void resetList() {
		listOldGames.clear();
	}
}
