package logic.server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;


import models.Game;
import models.Player;
import net.Command;
import net.Option;
import server.GameServer;
import util.IllegalArgumentsException;

// TODO: Auto-generated Javadoc
/**
 * The Class GameLogicServer.
 *
 * @author mathias
 */

public class GameLogicServer extends Thread {

	/** The Constant UPDATE_PERIOD_MILLIS. */
	private static final long UPDATE_PERIOD_MILLIS = 20;
	
	/** The Constant SEND_COORD_PERIOD_MILLIS. */
	private static final long SEND_COORD_PERIOD_MILLIS = 100;
	
	/** The Constant PAUSE_TIME_MILLIS. */
	private static final long PAUSE_TIME_MILLIS = 1500;

	private static boolean firsttime = true;
	
	/** The end game time millis. */
	private long endGameTimeMillis = 6000;
	
	/** The running. */
	private boolean running = true;

	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The walls. */
	private WallServer[] walls = new WallServer[6];
	
	/** The goals. */
	private GoalServer[] goals = new GoalServer[3];
	
	/** The ball. */
	private BallServer ball = new BallServer();

	/** The Game id. */
	private int GameID;

	/** The pause. */
	private boolean pause = true;

	/**
	 * Checks if is pause.
	 *
	 * @return true, if is pause
	 */
	public boolean isPause() {
		return pause;
	}

	/**
	 * Sets the pause.
	 *
	 * @param pause the new pause
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/** The player1. */
	public PlayerGameServer player1 = new PlayerGameServer();
	
	/** The player2. */
	public PlayerGameServer player2 = new PlayerGameServer();
	
	/** The player3. */
	public PlayerGameServer player3 = new PlayerGameServer();
	
	/** The gameserver. */
	private GameServer gameserver;
	
	/** The player disconnected. */
	private boolean playerDisconnected;

	/**
	 * The Constructer for the Game. Implements the calculation for the walls
	 * and goals.
	 *
	 * @param player1 the player1
	 * @param player2 the player2
	 * @param player3 the player3
	 * @param gameServer the game server
	 * @param GameID the game id
	 * @param gameTime the game time
	 */

	public GameLogicServer(PlayerGameServer player1, PlayerGameServer player2, PlayerGameServer player3,
		GameServer gameServer, int GameID, int gameTime) {
		this.setGameserver(gameServer);
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
		this.GameID = GameID;
		this.endGameTimeMillis = gameTime * 1000;
		makewalls();
		ball.addWalls(walls, goals);
		player1.addWalls(walls, goals, goals[0]);
		player2.addWalls(walls, goals, goals[1]);
		player3.addWalls(walls, goals, goals[2]);
		player1.reset();
		player2.reset();
		player3.reset();
		ball.reset();
		start();
	}

	/**
	 * Constructs the walls in the game.
	 */
	public void makewalls() {
		float r = 200;
		for (int i = 0; i < 6; i++) {
			int x = (int) Math.round(300 + (r * Math.cos(2 * Math.PI * i / 6)));
			int y = (int) Math.round(200 + (r * Math.sin(2 * Math.PI * i / 6)));
			walls[i] = new WallServer(x, y, ((60 * i) + 120) % 360, 200, 10);
			if ((i - 1) % 2 == 0) {
				goals[(i - 1) / 2] = new GoalServer(x, y, ((60 * (i)) + 120) % 360, 50, 30, (i - 1) / 2);
			}
		}
	}

	/**
	 * Updates all Players and check if someone has gotten a goal.
	 */
	public void update() {
			
			if (pause) {
				
			} else {
				int goalplayer = ball.update(player1, player2, player3);
				calcGoals(goalplayer);
				if (goalplayer > 0) {
					reset();
				}

				if (player2.isKI()) {
					intelligentMoveDirection(player2);
				}

				if (player3.isKI()) {
					intelligentMoveDirection(player3);
				}

				player1.update(player2, player3);
				player2.update(player1, player3);
				player3.update(player1, player2);
				
				if (ball.checkIfOutside()) {
					this.reset();
				}
			}
		
	}

	/**
	 * Checks for any player disconnected.
	 */
	private void hasAnyPlayerDisconnected() {
		this.playerDisconnected = true;
		Game game = this.getGameserver().getGames().get(this.GameID);
		Hashtable<Integer, Player> players = this.getGameserver().getPlayers();
		ArrayList<Integer> keys = game.getPlayerIDs();
		for (int k : keys) {
			if ((players.get(k) != null) && players.get(k).isDisconnected()) {
				this.playerDisconnected = false;
			}
		}
	}

	/**
	 * Intelligent move direction.
	 *
	 * @param player the player
	 */
	private void intelligentMoveDirection(PlayerGameServer player) {
		Point midgoal = getMidOf2Points(player.getOwngoal().getLocation1(), player.getOwngoal().getLocation2());
		Point midFromGoalToBall = getMidOf2Points(midgoal, this.ball.getLocation());

		if (player.getLocation().getX() - midFromGoalToBall.getX() > 0) {
			player.setMoveinx(-1);
		}

		if (player.getLocation().getX() - midFromGoalToBall.getX() < 0) {
			player.setMoveinx(1);
		}

		if (player.getLocation().getY() - midFromGoalToBall.getY() < 0) {
			player.setMoveiny(1);
		}

		if (player.getLocation().getY() - midFromGoalToBall.getY() > 0) {
			player.setMoveiny(-1);
		}

	}

	/**
	 * Gets the mid of2 points.
	 *
	 * @param point1 the point1
	 * @param point2 the point2
	 * @return the mid of2 points
	 */
	private Point getMidOf2Points(Point point1, Point point2) {
		Point pointReturn = new Point();
		double newX = (point1.getX() + point2.getX()) / 2;
		double newY = (point1.getY() + point2.getY()) / 2;
		pointReturn.setLocation(doubleToInt(newX), doubleToInt(newY));
		return pointReturn;
	}

	/**
	 * Doubletoint.
	 *
	 * @param d the d
	 * @return the int
	 */
	private int doubleToInt(double d) {
		return (int) Math.toIntExact(Math.round(d));
	}

	/**
	 * This class calculates the goals a player received.
	 *
	 * @param goalplayer the goalplayer
	 */
	private void calcGoals(int goalplayer) {
		if (goalplayer == 1) {
			this.setPause(true);
			player1.setGoal(player1.getGoal()+1);
			String[] arguments = { 1 + "", player1.getGoal() + "" };
			this.getGameserver().sendMessageToGame(GameID, Command.UPDATE_GAME, Option.SCOREBOARD, arguments);
		}
		if (goalplayer == 2) {
			this.setPause(true);
			player2.setGoal(player2.getGoal()+1);
			String[] arguments = { 2 + "", player2.getGoal() + "" };
			this.getGameserver().sendMessageToGame(GameID, Command.UPDATE_GAME, Option.SCOREBOARD, arguments);
		}
		if (goalplayer == 3) {
			this.setPause(true);
			player3.setGoal(player3.getGoal()+1);
			String[] arguments = { 3 + "", player3.getGoal() + "" };
			this.getGameserver().sendMessageToGame(GameID, Command.UPDATE_GAME, Option.SCOREBOARD, arguments);
		}

	}

	/**
	 * Sets the playermoves.
	 *
	 * @param playerinputs the playerinputs
	 * @param player the player
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	public void setPlayermoves(String[] playerinputs, PlayerGameServer player) throws IllegalArgumentsException {
		player.setMoveiny(0);
		player.setMoveinx(0);
		int[] playerinputsint = new int[4];
		for (int n = 0; n < 4; n++) {
			try {
				playerinputsint[n] = Integer.parseInt(playerinputs[n]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentsException(playerinputs, 2);
			}
		}
		if (playerinputsint[0] == 1) {
			player.setMoveiny(-1);
		}
		if (playerinputsint[1] == 1) {
			player.setMoveiny(1);
		}
		if (playerinputsint[2] == 1) {
			player.setMoveinx(-1);
		}
		if (playerinputsint[3] == 1) {
			player.setMoveinx(1);
		}
	}

	/**
	 * Reset.
	 */
	/*
	 * Resets everything to its default
	 */
	public void reset() {
		pause = true;
		ball.reset();
		player1.reset();
		player2.reset();
		player3.reset();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		long startingTime = System.currentTimeMillis();
		long timePassed = 0;

		long startingTimeUpdate = System.currentTimeMillis();
		long timepassedupdate = 0;

		long EndTime = System.currentTimeMillis();
		long endtimepassed = 0;
		
		long pauseTime = System.currentTimeMillis();
		long timePaused = 0;

		while (running) {
			hasAnyPlayerDisconnected();
			if (this.playerDisconnected) {
				if (pause) {
					timePaused = System.currentTimeMillis() - pauseTime;
					if (timePaused > PAUSE_TIME_MILLIS) {
						this.setPause(false);
						EndTime+=PAUSE_TIME_MILLIS;
					}
				} else {
					pauseTime = System.currentTimeMillis();
				}
					endtimepassed = System.currentTimeMillis() - EndTime;
					if (endtimepassed > endGameTimeMillis) {
						startingTimeUpdate = System.currentTimeMillis();
						timepassedupdate = 0;
						endRunningGame(0);
					}


					timePassed = System.currentTimeMillis() - startingTime;
					if (timePassed > SEND_COORD_PERIOD_MILLIS) {
						startingTime = System.currentTimeMillis();
						timePassed = 0;
						String[] coordinates = getCoordinatesofPlayer();
						String[] arguments = new String[9];
						for (int i=0;i<8;i++) {
							arguments[i]=coordinates[i];
						}
						if (pause) {
							long s =  timePaused - PAUSE_TIME_MILLIS;
							arguments[8] = s+"";
						} else {
							long s = endGameTimeMillis - endtimepassed;
							arguments[8] = s+"";
						}
						this.getGameserver().sendMessageToGame(GameID, Command.UPDATE_GAME, Option.PLAYER_UPDATE_COORD, arguments);
					}
					
						timepassedupdate = System.currentTimeMillis() - startingTimeUpdate;
						if (timepassedupdate > UPDATE_PERIOD_MILLIS) {
							startingTimeUpdate = System.currentTimeMillis();
							timepassedupdate = 0;
							update();
						}
					
			} else {
				if (firsttime) {
					String[] arguments5 = {"A","Player", "has", "diconnected"};
					this.getGameserver().sendMessageToGame(GameID, Command.CHAT, Option.INFORMATION, arguments5);
					firsttime = false;
				}
				
			}
		}
	}

	/**
	 * End running game.
	 *
	 * @param i the i
	 */
	public void endRunningGame(int i) {
		String[] arguments = {0+"", i+""};
		this.getGameserver().sendMessageToGame(GameID, Command.UPDATE_GAME, Option.END_GAME, arguments);
		if (i==0) {
			String playerWinnerMessage = getWinnerName();
			String[] argumentsWinner = playerWinnerMessage.split("\\s+");
			this.getGameserver().sendMessageToGame(GameID, Command.CHAT, Option.INFORMATION, argumentsWinner);
		}
		this.getGameserver().setGameRunning(this.GameID, false);
		this.getGameserver().addGameResults(this.player1.getId(), this.player2.getId(), this.player3.getId(), this.player1.getGoal(), this.player2.getGoal(), this.player3.getGoal());
		if (this.player1.getId() != 0) {
			this.getGameserver().resetReadyFlag(this.player1.getId());
		}
		if (this.player2.getId() != 0) {
			this.getGameserver().resetReadyFlag(this.player2.getId());
		}
		if (this.player3.getId() != 0) {
			this.getGameserver().resetReadyFlag(this.player3.getId());
		}
		running = false;
	}

	/**
	 * Gets the winner name.
	 *
	 * @return the winner name
	 */
	private String getWinnerName() {
		int a = player1.getGoal();
		int b = player2.getGoal();
		int c = player3.getGoal();
		String NameA = this.getGameserver().getLobby().getPlayerNamefromID(player1.getId());
		String NameB = this.getGameserver().getLobby().getPlayerNamefromID(player2.getId());
		String NameC = this.getGameserver().getLobby().getPlayerNamefromID(player3.getId());
		if (a==b && b==c) {
			return "All players scored the same score. Everyone is a winner!";
		} else {
			if (a==b) {
			return "Player " + NameA + " and Player " + NameB + " have scored the same goal count. Both are winners!";
			}
			if (a==c) {
			return "Player " + NameA + " and Player " + NameC + " have scored the same goal count. Both are winners!";
			}
			if (b==c) {
			return "Player " + NameB + " and Player " + NameC + " have scored the same goal count. Both are winners!";
			}
			if (a < b && a < c) {
				return NameA + " has won the Game!";
			}
			if (b < a && b < c) {
				return NameB + " has won the Game!";
			}
			if (c < a && c < b) {
				return NameC + " has won the Game!";
			}
		}
		
		return "I was not able to get the winner...";
	}

	/**
	 * Update key inputs.
	 *
	 * @param keyinputs the keyinputs
	 * @param i the i
	 * @throws IllegalArgumentsException the illegal arguments exception
	 */
	public void updateKeyInputs(String[] keyinputs, int i) throws IllegalArgumentsException {
		if (this.player1.getId() == i) {
			setPlayermoves(keyinputs, player1);
		}
		if (this.player2.getId() == i) {
			setPlayermoves(keyinputs, player2);
		}
		if (this.player3.getId() == i) {
			setPlayermoves(keyinputs, player3);
		}

	}

	/**
	 * Gets the coordinatesof player.
	 *
	 * @return the coordinatesof player
	 */
	public String[] getCoordinatesofPlayer() {
		String[] coordinates = new String[8];
		coordinates[0] = player1.getXcoord();
		coordinates[1] = player1.getYcoord();
		coordinates[2] = player2.getXcoord();
		coordinates[3] = player2.getYcoord();
		coordinates[4] = player3.getXcoord();
		coordinates[5] = player3.getYcoord();
		coordinates[6] = ball.getXcoord();
		coordinates[7] = ball.getYcoord();
		return coordinates;
	}

	/**
	 * New player joines.
	 *
	 * @param playerID the player id
	 */
	public void newPlayerJoines(int playerID) {
		if (player2.isKI()) {
			player2.setId(playerID);
			player2.setKI(false);;
		} else {
			player3.setId(playerID);
			player3.setKI(false);
		}
		
	}

	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets the running.
	 *
	 * @param running the new running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Gets the gameserver.
	 *
	 * @return the gameserver
	 */
	public GameServer getGameserver() {
		return gameserver;
	}

	/**
	 * Sets the gameserver.
	 *
	 * @param gameserver the new gameserver
	 */
	public void setGameserver(GameServer gameserver) {
		this.gameserver = gameserver;
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
	public WallServer[] getWalls() {
		return walls;
	}

	/**
	 * Sets the walls.
	 *
	 * @param walls the new walls
	 */
	public void setWalls(WallServer[] walls) {
		this.walls = walls;
	}

	/**
	 * Gets the goals.
	 *
	 * @return the goals
	 */
	public GoalServer[] getGoals() {
		return goals;
	}

	/**
	 * Sets the goals.
	 *
	 * @param goals the new goals
	 */
	public void setGoals(GoalServer[] goals) {
		this.goals = goals;
	}

	/**
	 * Gets the ball.
	 *
	 * @return the ball
	 */
	public BallServer getBall() {
		return ball;
	}

	/**
	 * Sets the ball.
	 *
	 * @param ball the new ball
	 */
	public void setBall(BallServer ball) {
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
	 * Gets the player1.
	 *
	 * @return the player1
	 */
	public PlayerGameServer getPlayer1() {
		return player1;
	}

	/**
	 * Sets the player1.
	 *
	 * @param player1 the new player1
	 */
	public void setPlayer1(PlayerGameServer player1) {
		this.player1 = player1;
	}

	/**
	 * Gets the player2.
	 *
	 * @return the player2
	 */
	public PlayerGameServer getPlayer2() {
		return player2;
	}

	/**
	 * Sets the player2.
	 *
	 * @param player2 the new player2
	 */
	public void setPlayer2(PlayerGameServer player2) {
		this.player2 = player2;
	}

	/**
	 * Gets the player3.
	 *
	 * @return the player3
	 */
	public PlayerGameServer getPlayer3() {
		return player3;
	}

	/**
	 * Sets the player3.
	 *
	 * @param player3 the new player3
	 */
	public void setPlayer3(PlayerGameServer player3) {
		this.player3 = player3;
	}

}
