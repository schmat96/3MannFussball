package logic.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

// TODO: Auto-generated Javadoc
/**
 * A class that manages a java swing window, allowing
 * for easy manipulation of a framebuffer as well
 * as mouse and keyboard polling.
 * @author matthias
 *
 */
@SuppressWarnings("serial")
public class Window extends Component implements KeyListener {
	

	/** The firstime. */
	private boolean firstime = true;
	
	/** the simulated framebuffer. */
	private BufferedImage _image;
	
	/** the java2D graphics context of above framebuffer. */
	private Graphics2D  _imageGraphics;
	
	/** the Swing window this class manages. */
	private JFrame _jFrame;
	
	//private JLabel _jLabelPlayeField;
	
	/** The _j panel scoreboard. */
	private JPanelScoreboard _jPanelScoreboard;
	
	/** The _j panel timer. */
	private JPanelTimer _jPanelTimer;
	
	/** The _j panel banner. */
	private JPanel _jPanelBanner;
	
	/** The _j slider game time. */
	private JProgressBar _jSliderGameTime;
	
	/** listener class for mouse actions. */
	
	private long timer;
	
	/** The keyinputs. */
	private String[] keyinputs = {0+"",0+"",0+"",0+""};
	
	/** The player1. */
	private PlayerGame player1 = new PlayerGame();
	
	/** The player2. */
	private PlayerGame player2 = new PlayerGame();
	
	/** The player3. */
	private PlayerGame player3 = new PlayerGame();
	
	
	

	
	
	/**
	 * Creates a new instance of the ImageWindow class.
	 *
	 * @param width the width of the paint (image) area
	 * @param height the height of the paint (image) area
	 * @param currentPlayerr the current playerr
	 * @param enemy1 the enemy1
	 * @param enemy2 the enemy2
	 * @param gameTime the game time
	 */
	public Window(int width, int height, PlayerGame currentPlayerr, PlayerGame enemy1, PlayerGame enemy2, int gameTime) {
		_image= new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		_imageGraphics=(Graphics2D)_image.getGraphics();
	     setFocusable(true);
	     _jPanelTimer = new JPanelTimer(gameTime);
	     _jPanelBanner = new JPanel();
	     _jPanelBanner.setSize(new Dimension(width, 50));
	     _jSliderGameTime = new JProgressBar(JProgressBar.VERTICAL, 0, gameTime);
	     _jSliderGameTime.setSize(10, height);
	     _jSliderGameTime.setPreferredSize(new Dimension(50,height));
	     _jSliderGameTime.validate();
	     _jSliderGameTime.repaint();
	     _jPanelBanner.add(_jSliderGameTime);
	     
	     this.player1 = currentPlayerr;
	     this.player2 = enemy1;
	     this.player3 = enemy2;
	     addKeyListener(this);
	   
	}

	/**
	 * Resizes the paint (image) area to the given size. Also resizes the
	 * window border around it.<br>The old paint area will be copied to the
	 * new paint area and clipped if necessary.
	 * @param newWidth the resized width of the paint area
	 * @param newHeight the resized height of the paint area
	 */
	public void resizeImage(int newWidth,int newHeight) {
		BufferedImage newImage = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_ARGB);
		newImage.getGraphics().setColor(new Color(255,255,255,255));
		((Graphics2D)(newImage.getGraphics())).fill(new Rectangle2D.Float(0, 0,newWidth,newHeight));
		newImage.getGraphics().drawImage(_image, 0,0,null);
		_image=newImage;
		_imageGraphics=(Graphics2D)_image.getGraphics();
		//this._jLabelPlayeField.add(_image);
		_jFrame.pack();
	}
	/**
	 * Resets the image to the color white.
	 */
	public void clearImage() {
		 fillImage(255, 255, 255);
	}
	/**
	 * Fills the image with the color values given.
	 * @param red the red part of the color 0-255
	 * @param green the green part of the color 0-255
	 * @param blue the blue part of the color 0-255
	 */
	public void fillImage(int red,int green,int blue){
		 Color color = new Color(red, green, blue, 255);
		  _imageGraphics.setColor(color);
		  _imageGraphics.fill(new Rectangle2D.Float(0, 0, _image.getWidth(),_image.getHeight()));
	}

	/**
	 * Sets a pixel to the given color value. 
	 * The x and y coordinates are horizontal and vertical positions in the image.<br>
	 * The (0,0) pixel is located at the top left corner of the image, whereas
	 * the (image_width-1,image_height-1) pixel is located at the bottom right corner.
	 * @param x the x position of the pixel
	 * @param y the y position of the pixel
	 * @param red the red part of the color 0-255
	 * @param green the green part of the color 0-255
	 * @param blue the blue part of the color 0-255
	 */
	public void setPixel(int x,int y,int red,int green,int blue) {
		try {
			_image.setRGB(x, y,  new Color(red, green, blue).getRGB());
		} catch (ArrayIndexOutOfBoundsException e) {
			//someone painted outside the window: do nothing
		}
	}
	
	/**
	 * Draws a oval with x and y coordinates, his radius and the color.
	 * @param x location x
	 * @param y location y
	 * @param width radius width
	 * @param height radius height
	 * @param red R'GB
	 * @param green RG'B
	 * @param blue RGB'
	 */
	public void drawOval(int x,int y, int width, int height, int red,int green,int blue) {
		try {
			Color c = new Color(red,green,blue);
			_imageGraphics.setColor(c);
			_imageGraphics.fillOval(x-width, y-width, width*2, height*2);
		} catch (ArrayIndexOutOfBoundsException e) {
			//someone painted outside the window: do nothing
		}
	}
	
	/**
	 * Opens the window at the x,y location on screen with the given
	 * Windowname.
	 *
	 * @param windowName the windowname
	 * @param x x-position on screen
	 * @param y y-position on screen
	 */
	public void openWindow(String windowName, int x, int y) {
		if (_jFrame != null) {
			_jFrame.dispose();
		}
		_jFrame = new JFrame(windowName);
		_jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (_jFrame!=null) {
					_jFrame.dispose();
					_jFrame=null;
				}
			}
		});
		
		_jFrame.setLocation(x, y);
		_jFrame.add(this);
		FlowLayout layout = new FlowLayout();
		_jFrame.setLayout(layout);
		//_jFrame.add(_jPanelPlayeField);
		_jPanelScoreboard = new JPanelScoreboard(this.player1, this.player2, this.player3);
		//_jPanelTimer = new JPanelTimer();
		_jFrame.add(this._jPanelBanner);
		_jFrame.add(_jPanelTimer);
		_jFrame.add(_jPanelScoreboard);
		
		_jFrame.pack();
		_jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_jFrame.setVisible(true);
		
		
	}
	/**
	 * Opens the image window.
	 */
	public void openWindow() {
		openWindow("ImageWindow",0,0);
	}
	/**
	 * Closes the image window.
	 */
	public void closeWindow() {
		this.firstime = true;
		if (_jFrame==null) return;
		_jFrame.dispose();
		_jFrame=null;
	}
	
	/**
	 * Checks if is window open.
	 *
	 * @return true, if is window open
	 */
	public boolean isWindowOpen() {
		if (_jFrame==null) return false;
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
	 * @param milliseconds the pause length
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
	//functions needed by java from this component 
	
	/*
	 * needed by java to paint this window (should not be
	 * called directly)
	 * (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint (Graphics g) {
		g.drawImage(_image,0,0,null);	
	}
	/*
	 * needed by java to place this window correctly
	 * (non-Javadoc)
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return new Dimension(_image.getWidth(),_image.getHeight());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Sets the Keyboard Input.
	 *
	 * @param e the e
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_D) 
			keyinputs[3] = 1+"";
        else if(e.getKeyCode()== KeyEvent.VK_A)
        	keyinputs[2] = 1+"";
        else if(e.getKeyCode()== KeyEvent.VK_S)
        	keyinputs[1] = 1+"";
        else if(e.getKeyCode()== KeyEvent.VK_W)
        	keyinputs[0] = 1+"";
        else if(e.getKeyCode()== KeyEvent.VK_SPACE)
            player1.setKickkey(true);
		
	}

	/**
	 * Resets the input keys of the Player.
	 *
	 * @param e the e
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_D) 
			keyinputs[3] = 0+"";
        else if(e.getKeyCode()== KeyEvent.VK_A)
        	keyinputs[2] = 0+"";
        else if(e.getKeyCode()== KeyEvent.VK_S)
        	keyinputs[1] = 0+"";
        else if(e.getKeyCode()== KeyEvent.VK_W)
        	keyinputs[0] = 0+"";
        else if(e.getKeyCode()== KeyEvent.VK_SPACE)
            player1.setKickkey(false);
	}

	/**
	 * Gets the _j panel scoreboard.
	 *
	 * @return the _j panel scoreboard
	 */
	public JPanelScoreboard get_jPanelScoreboard() {
		return _jPanelScoreboard;
	}

	/**
	 * Sets the _j panel scoreboard.
	 *
	 * @param _jPanelScoreboard the new _j panel scoreboard
	 */
	public void set_jPanelScoreboard(JPanelScoreboard _jPanelScoreboard) {
		this._jPanelScoreboard = _jPanelScoreboard;
	}

	/**
	 * Gets the _j panel timer.
	 *
	 * @return the _j panel timer
	 */
	public JPanelTimer get_jPanelTimer() {
		return _jPanelTimer;
	}

	/**
	 * Sets the _j panel timer.
	 *
	 * @param _jPanelTimer the new _j panel timer
	 */
	public void set_jPanelTimer(JPanelTimer _jPanelTimer) {
		this._jPanelTimer = _jPanelTimer;
	}

	/**
	 * Gets the listener class for mouse actions.
	 *
	 * @return the listener class for mouse actions
	 */
	public long getTimer() {
		return timer;
	}

	/**
	 * Sets the listener class for mouse actions.
	 *
	 * @param timer the new listener class for mouse actions
	 */
	public void setTimer(long timer) {
		this.timer = timer;
	}

	/**
	 * Gets the keyinputs.
	 *
	 * @return the keyinputs
	 */
	public String[] getKeyinputs() {
		return keyinputs;
	}

	/**
	 * Sets the keyinputs.
	 *
	 * @param keyinputs the new keyinputs
	 */
	public void setKeyinputs(String[] keyinputs) {
		this.keyinputs = keyinputs;
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

	/**
	 * Sets the new end time.
	 *
	 * @param i the new new end time
	 */
	public void setNewEndTime(int i) {
		this.get_jPanelTimer().setEndTime(i);
		if (i>0) {
			this._jSliderGameTime.setValue(i);
			firstime = false;
		} else {
			if (firstime) {
				addTextToImage("Time to wait till Game starts:" +i);
			} else {
				addTextToImage("GOAAAAAAAL!" +i);	
			}
			
		}
		
		
	}
	
	/**
	 * Adds the text to image.
	 *
	 * @param text the text
	 */
	public void addTextToImage(String text) {
		Font player_font = new Font("Century Gothic",1,20);
		Graphics g = this._image.getGraphics();
		g.setPaintMode();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, 700, 50);
		g.setColor(new Color(255,255,255));
		g.setFont(player_font);
		g.drawString(text, 10, 30);
	}
	
	
}
