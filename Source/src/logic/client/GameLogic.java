package logic.client;

import client.GameClient;
import net.Command;
import net.Option;
import util.IllegalArgumentsException;

// TODO: Auto-generated Javadoc
/**
 * The Class GameLogic.
 *
 * @author mathias
 */




public class GameLogic {

	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The walls. */
	private Wall[] walls = new Wall[6];
	
	/** The goals. */
	private Goal[] goals = new Goal[3];
	
	/** The ball. */
	private Ball ball = new Ball();
	
	/** The Game id. */
	private int GameID;
	
	/** The gameclient. */
	private GameClient gameclient;

	/** The player1. */
	private PlayerGame player1 = new PlayerGame();
	
	/** The player2. */
	private PlayerGame player2 = new PlayerGame();
	
	/** The player3. */
	private PlayerGame player3 = new PlayerGame();
	
	/** The window. */
	private Window window;
	
	/**
	 * The Constructer for the Game. Implements the calculation for the walls and goals.
	 *
	 * @param window the window
	 * @param player1 the player1
	 * @param player2 the player2
	 * @param player3 the player3
	 * @param gameclient the gameclient
	 * @param GameID the game id
	 */
	
	public GameLogic(Window window, PlayerGame player1, PlayerGame player2, PlayerGame player3, GameClient gameclient, int GameID) {
		this.window = window;
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
		this.GameID = GameID;
		this.gameclient = gameclient;
		window.redraw();
		makeWalls();
		ball.addWalls(walls,goals);
		player1.addwalls(walls,goals, goals[0]);
		player2.addwalls(walls,goals, goals[1]);
		player3.addwalls(walls,goals, goals[2]);
		draw();
	}

	

	/**
	 * Draws everything.
	 */
	private void draw() {
		window.clearImage();
		walls[0].drawWall(window);
		walls[1].drawWall(window);
		walls[2].drawWall(window);
		walls[3].drawWall(window);
		walls[4].drawWall(window);
		walls[5].drawWall(window);
		goals[0].drawWall(window);
		goals[1].drawWall(window);
		goals[2].drawWall(window);
		ball.draw(window);
		player1.draw(window, this.gameclient);
		player2.draw(window, this.gameclient);
		player3.draw(window, this.gameclient);
		window.get_jPanelTimer().updateTime();
		window.redraw();
	}
	
	/**
	 * Constructs the walls in the game.
	 */
	public void makeWalls() {
		float r = 200;
		for (int i=0;i<6;i++) {
			int x = (int) Math.round(300+(r * Math.cos(2 * Math.PI * i / 6)));
			int y = (int) Math.round(200+(r * Math.sin(2 * Math.PI * i / 6)));
			walls[i] = new Wall(x, y,((60*i)+120)%360,200,10);
				if ((i-1)%2==0) {
				goals[(i-1)/2] = new Goal(x, y,((60*(i))+120)%360,50,30,(i-1)/2);
				}
			}	
		}



/**
 * Send player inputs.
 */
private void sendPlayerInputs() {
		String[] arguments = this.window.getKeyinputs();
		this.gameclient.sendMessageToServer(Command.UPDATE_GAME, Option.KEY_INPUTS, arguments);
		//gameclient.udpClient.sendUserInputsToServer(arguments);
	}


/**
 * Reset.
 */
/*
 * Resets everything to its default
 */
	public void reset() {
		ball.reset();
		player1.reset();
		player2.reset();
		player3.reset();
	}
		
	/**
	 * Start.
	 */
	public void start() {
		this.window.openWindow();
		sendPlayerInputs();
	}

	/**
	 * Sets the new coord.
	 *
	 * @param components the new new coord
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */

	public void setNewCoord(String[] components) throws IllegalArgumentsException {
		
		int[] arguments = new int[9];
		
		for (int n=0;n<9;n++) {
			try {
				arguments[n] = Integer.parseInt(components[n+2]);
			}  catch (NumberFormatException e) {
				
			}
			
		}
		player1.getLocation().setLocation(arguments[0], arguments[1]);
		player2.getLocation().setLocation(arguments[2], arguments[3]);
		player3.getLocation().setLocation(arguments[4], arguments[5]);
		ball.getLocation().setLocation(arguments[6], arguments[7]);
		draw();
		this.window.setNewEndTime(arguments[8]);
		
		
		sendPlayerInputs();
		
	}



	private void disconnectMessage(String[] components) {
		String text = "";
		for (int i = 2;i<components.length;i++) {
			text += components[i];
		}
		this.window.addTextToImage(text);
		
	}



	/**
	 * New score board.
	 *
	 * @param requestComponents the request components
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	public void newScoreBoard(String[] requestComponents) throws IllegalArgumentsException {
		int PlayerID;
		int PlayerGoals;
		try {
			PlayerID = Integer.parseInt(requestComponents[2]);
			PlayerGoals = Integer.parseInt(requestComponents[3]);
		}  catch (NumberFormatException e) {
			throw new IllegalArgumentsException(requestComponents, 2);
		}
		switch(PlayerID) {
		case(1):
			getPlayer1().setGoal(PlayerGoals);
			break;
		case(2):
			getPlayer2().setGoal(PlayerGoals);
			break;
		case(3):
			getPlayer3().setGoal(PlayerGoals);
			break;
		}
		
	}



	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}



	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}



	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}



	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}



	/**
	 * Gets the walls.
	 *
	 * @return the walls
	 */
	public Wall[] getWalls() {
		return walls;
	}



	/**
	 * Sets the walls.
	 *
	 * @param walls the new walls
	 */
	public void setWalls(Wall[] walls) {
		this.walls = walls;
	}



	/**
	 * Gets the goals.
	 *
	 * @return the goals
	 */
	public Goal[] getGoals() {
		return goals;
	}



	/**
	 * Sets the goals.
	 *
	 * @param goals the new goals
	 */
	public void setGoals(Goal[] goals) {
		this.goals = goals;
	}



	/**
	 * Gets the ball.
	 *
	 * @return the ball
	 */
	public Ball getBall() {
		return ball;
	}



	/**
	 * Sets the ball.
	 *
	 * @param ball the new ball
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}



	/**
	 * Gets the game id.
	 *
	 * @return the game id
	 */
	public int getGameID() {
		return GameID;
	}



	/**
	 * Sets the game id.
	 *
	 * @param gameID the new game id
	 */
	public void setGameID(int gameID) {
		GameID = gameID;
	}



	/**
	 * Gets the gameclient.
	 *
	 * @return the gameclient
	 */
	public GameClient getGameclient() {
		return gameclient;
	}



	/**
	 * Sets the gameclient.
	 *
	 * @param gameclient the new gameclient
	 */
	public void setGameclient(GameClient gameclient) {
		this.gameclient = gameclient;
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
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}



	/**
	 * Sets the window.
	 *
	 * @param window the new window
	 */
	public void setWindow(Window window) {
		this.window = window;
	}
}

