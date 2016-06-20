package logic.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

	// TODO: Auto-generated Javadoc
/**
	 * The Class JPanelTimer.
	 */
	@SuppressWarnings("serial")
	public class JPanelTimer extends JPanel {
	
		/** The string timer. */
		private String stringTimer = null;
		
		/** The buff. */
		BufferedImage buff = null;
		
		/** The player_font. */
		Font player_font = new Font("Century Gothic",1,25);
		
		/** The current time. */
		public long currentTime;
		
		/** The game duration. */
		public long gameDuration;
		
		/** The end time. */
		public long endTime;
		
		
		
		/** The timer info. */
		private String timerInfo = "Game duration:";
		
		/** The display time. */
		public String displayTime;
		
		/**
		 * Instantiates a new j panel timer.
		 *
		 * @param gameTime the game time
		 */
		JPanelTimer(int gameTime){
			gameDuration = gameTime;
			setPreferredSize(new Dimension(200,80));
		}
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
			super.paintComponent(g);
		   g.drawImage(buff, 0, 0,this);
		   g.setFont(player_font);
		   g.drawString(timerInfo, 10, 20);
		   g.drawString(stringTimer+"", 10, 60);
	   }
	
	/**
	 * Update time.
	 */
	public void updateTime() {
		endTime = Math.round(endTime/1000);
		int min = (int) endTime/60;
		long sec = endTime%60;
		stringTimer = min+"min "+sec+"sec";
	}



	/**
	 * Sets the end time.
	 *
	 * @param i the new end time
	 */
	public void setEndTime(int i) {
		if (i <= 0) {
			this.endTime = i;
			this.timerInfo =  "Pause! Restart in:";
		} else {
			this.endTime = i;
			this.timerInfo =  "Game duration:";
		}
		
	}

}