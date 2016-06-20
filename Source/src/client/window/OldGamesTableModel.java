package client.window;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import models.OldGame;

// TODO: Auto-generated Javadoc
/**
 * The Class OldGamesTableModel.
 */
public class OldGamesTableModel extends AbstractTableModel {
	
	/** The Constant COLUMN_NO. */
	private static final int COLUMN_NO = 0;
	
	/** The Constant COLUMN_DATE. */
	private static final int COLUMN_DATE = 1;
	
	/** The Constant COLUMN_PLAYERSnGOALS. */
	private static final int COLUMN_PLAYERSnGOALS = 2;
	
	/** The Constant COLUMN_COUNTGOALS. */
	private static final int COLUMN_COUNTGOALS = 3;

	/** The column names. */
	private String[] columnNames = { "No #", "Date", "Players: Goals", "Total Goals" };
	
	/** The list old games. */
	private List<OldGame> listOldGames;

	/**
	 * Instantiates a new old games table model.
	 *
	 * @param listOldGames the list old games
	 */
	public OldGamesTableModel(List<OldGame> listOldGames) {
		this.listOldGames = listOldGames;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return listOldGames.size() * 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int columnIndex) {
		if (listOldGames.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

	/**
	 * Gets the all count goals.
	 *
	 * @return the all count goals
	 */
	public int[] getAllCountGoals() {
		int[] returnInt = new int[this.getRowCount()];
		for (int j = 0; j < this.getRowCount(); j++) {
			Object obj = this.getValueAt(j, 3);
			if (obj != null) {
				returnInt[j] = (int) obj;
			} else {
				returnInt[j] = -1;
			}
		}
		return returnInt;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {

		OldGame oldgame;
		if (rowIndex % 3 == 0) {
			oldgame = listOldGames.get(rowIndex / 3);
		} else if (rowIndex - 1 % 3 == 0) {
			oldgame = listOldGames.get((rowIndex - 1) / 3);
		} else {
			oldgame = listOldGames.get((rowIndex - 2) / 3);
		}
		Object returnValue = null;

		switch (columnIndex) {
		case COLUMN_NO:
			if (rowIndex % 3 == 0) {
				returnValue = oldgame.getIndex();
			}
			break;
		case COLUMN_DATE:
			if (rowIndex % 3 == 0) {
				returnValue = oldgame.getDate();
			}
			break;
		case COLUMN_PLAYERSnGOALS:
			returnValue = oldgame.getPlayersAndGoals(rowIndex % 3);
			break;
		case COLUMN_COUNTGOALS:
			if (rowIndex % 3 == 0) {
				return oldgame.getCountGoals();
			}
			returnValue = null;
		default:
			returnValue = null;
		}

		return returnValue;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		OldGame oldgame = listOldGames.get(rowIndex);
		if (columnIndex == COLUMN_NO) {
			oldgame.setIndex((int) value);
		}
	}

}
