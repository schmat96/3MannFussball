package logic.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



// TODO: Auto-generated Javadoc
/**
 * The Class JPanelScoreboard.
 */
@SuppressWarnings("serial")
public class JPanelScoreboard extends JPanel {
	
	/** The player1goals. */
	private int player1goals = 0;
	
	/** The player2goals. */
	private int player2goals = 0;
	
	/** The player3goals. */
	private int player3goals = 0;
	
	/** The buff. */
	private BufferedImage buff = null;
	
	/** The player_font. */
	private Font player_font = new Font("Century Gothic",1,25);
	
	/** The player1. */
	private PlayerGame player1;
	
	/** The player2. */
	private PlayerGame player2;
	
	/** The player3. */
	private PlayerGame player3;
	
	/**
	 * Instantiates a new j panel scoreboard.
	 *
	 * @param currentPlayer the current player
	 * @param enemy1 the enemy1
	 * @param enemy2 the enemy2
	 */
	JPanelScoreboard(PlayerGame currentPlayer, PlayerGame enemy1, PlayerGame enemy2){
		setPreferredSize(new Dimension(200,400));
		this.player1 = currentPlayer;
		this.player2 = enemy1;
		this.player3 = enemy2;
	}



/* (non-Javadoc)
 * @see javax.swing.JComponent#paint(java.awt.Graphics)
 */
public void paint(Graphics g) {
	super.paintComponent(g);
	try {
		buff = ImageIO.read(new File("images/Scoreboard.jpg"));
	} catch (IOException e) {
	}
	   g.drawImage(buff, 0, 0,this);
	   g.setFont(player_font);
	   g.setColor(new Color(255,0,0));
	   g.drawString(player1.getPlayername(), 10, 150);
	   g.drawString(Integer.toString(player1.getGoal()), 10, 200);
	   g.setColor(new Color(0,255,0));
	   g.drawString(player2.getPlayername(), 10, 250);
	   g.drawString(Integer.toString(player2.getGoal()), 10, 300);
	   g.setColor(new Color(0,0,255));
	   g.drawString(player3.getPlayername(), 10, 350);
	   g.drawString(Integer.toString(player3.getGoal()), 10, 400);
   }



/**
 * Gets the player1goals.
 *
 * @return the player1goals
 */
public int getPlayer1goals() {
	return player1goals;
}



/**
 * Sets the player1goals.
 *
 * @param player1goals the new player1goals
 */
public void setPlayer1goals(int player1goals) {
	this.player1goals = player1goals;
}



/**
 * Gets the player2goals.
 *
 * @return the player2goals
 */
public int getPlayer2goals() {
	return player2goals;
}



/**
 * Sets the player2goals.
 *
 * @param player2goals the new player2goals
 */
public void setPlayer2goals(int player2goals) {
	this.player2goals = player2goals;
}



/**
 * Gets the player3goals.
 *
 * @return the player3goals
 */
public int getPlayer3goals() {
	return player3goals;
}



/**
 * Sets the player3goals.
 *
 * @param player3goals the new player3goals
 */
public void setPlayer3goals(int player3goals) {
	this.player3goals = player3goals;
}



/**
 * Gets the buff.
 *
 * @return the buff
 */
public BufferedImage getBuff() {
	return buff;
}



/**
 * Sets the buff.
 *
 * @param buff the new buff
 */
public void setBuff(BufferedImage buff) {
	this.buff = buff;
}



/**
 * Gets the player_font.
 *
 * @return the player_font
 */
public Font getPlayer_font() {
	return player_font;
}



/**
 * Sets the player_font.
 *
 * @param player_font the new player_font
 */
public void setPlayer_font(Font player_font) {
	this.player_font = player_font;
}



/**
 * Gets the player1.
 *
 * @return the player1
 */
public PlayerGame getPlayer1() {
	return player1;
}



/**
 * Sets the player1.
 *
 * @param player1 the new player1
 */
public void setPlayer1(PlayerGame player1) {
	this.player1 = player1;
}



/**
 * Gets the player2.
 *
 * @return the player2
 */
public PlayerGame getPlayer2() {
	return player2;
}



/**
 * Sets the player2.
 *
 * @param player2 the new player2
 */
public void setPlayer2(PlayerGame player2) {
	this.player2 = player2;
}



/**
 * Gets the player3.
 *
 * @return the player3
 */
public PlayerGame getPlayer3() {
	return player3;
}



/**
 * Sets the player3.
 *
 * @param player3 the new player3
 */
public void setPlayer3(PlayerGame player3) {
	this.player3 = player3;
}
}