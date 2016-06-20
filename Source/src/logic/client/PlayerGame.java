package logic.client;

import client.GameClient;

// TODO: Auto-generated Javadoc
/**
 * This class extends from Ball but has his own constructor. Main difference is the update methode.
 * @author mathias
 * @kickkey a boolean variable. The boolean changes whenever u press the kick Button on your keyboard.
 * @moveiny & @moveinx togheter they build a vector for the keyboard inputs. They are getting changed in the Window class whenever u hit/release a button.
 * @goals count the goals u received
 * @speed this variable is used to easely change the velocity of your player (f.E. powerups IF we implement them)
 * 
 */
public class PlayerGame extends Ball {
	
	/** The kickkey. */
	private boolean kickkey = true;
	
	/** The moveiny. */
	private int moveiny = 0;
	
	/** The moveinx. */
	private int moveinx = 0;
	
	/** The owngoal. */
	private Goal owngoal = new Goal();
	
	/** The goal. */
	private int goal = 0;
	
	/** The speed. */
	private int speed = 2;
	
	/** The playername. */
	private String playername;

	
	/**
	 * The constructor.
	 *
	 * @param x The x-coordinate. Could be calculated with the player id and will be implemented later cause tired.
	 * @param y The y-coordinate.
	 * @param id The ID. Every Player has his own ID.
	 * @param playername the playername
	 */
	public PlayerGame(int x, int y, int id, String playername){
		this.playername = playername;
		this.setId(id);
		this.getLocation().move(x, y);
		this.setRadius(15);
	}

	/**
	 * A not needed constructor with no parameter. Eclipse told me to add one so I did.
	 */
	public PlayerGame() {
		
	}
	
	/**
	 * Draws the ball with the current location, radius and the fill Color in RGB.
	 *
	 * @param window This variable is needed for knowing the window, where the ball should be drown.
	 * @param gameclient the gameclient
	 */
	public void draw(Window window, GameClient gameclient) {
		
		this.setRadius(countDistancebetweenOwnGoal());
		
		
		
		switch(getId()) {
		case(1): 
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius(), getRadius(), 255, 0, 0);
			break;
		case(2): 
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius(), getRadius(), 0, 255, 0);
			break;
		case(3): 
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius(), getRadius(), 0, 0, 255);
			break;
		case(0): 
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius(), getRadius(), 255, 0, 0);
			break;
		default:
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius(), getRadius(), 80*getId(), 80*getId(), 0);
			break;
		}
		
		int currentPlayerID = gameclient.getPlayerID();
		if (gameclient.getLobby().getPlayerNamefromID(currentPlayerID).equals(this.playername)) {
			window.drawOval((int) getLocation().getX(), (int) getLocation().getY(), getRadius()/2, getRadius()/2, 150, 150, 150);
		}
		
		
	}
	
	/**
	 * Count distancebetween own goal.
	 *
	 * @return the int
	 */
	private int countDistancebetweenOwnGoal() {
		double i = pointToLineDistance(owngoal.getLocation1(),owngoal.getLocation2());
		int a = this.doubletoint(i/5);
		if (a < 5) {
			return 5;
		}
		if (a > 20) {
			return 20;
		}
		return a;
	}
	
	/**
	 * Checks if the Player collides with an wall or an enemy. If the Player does, he gets Restrictions in his moving sequence (makes it impossible to move into other Players, outside of the Map
	 *
	 * @param enemy1 one of the other players.
	 * @param enemy2 the enemy2
	 */
	public void update(PlayerGame enemy1, PlayerGame enemy2) {
		
		Vector playerenemy1vector = new Vector();
		Vector playerenemy2vector = new Vector();
		Vector playerwall1vector = new Vector();
			
		/**
		 * Check if the Player collides with a wall. If so a Vector is created which is used for the movement restrictions.
		 * Further tests have shown that the Player cant stuck in a corner, therefore only 1 vector is needed and its not needed to count the collisions.
		 */
		for (int i=0;i<6;i++) {
			if (pointToLineDistance(getWalls()[i].getLocation1(),getWalls()[(i+1)%6].getLocation1())<getRadius()) {
					playerwall1vector.setX(getWalls()[i].getOrientation().getX());
					playerwall1vector.setY(getWalls()[i].getOrientation().getY());
					playerwall1vector.rotateByRadian(Math.PI/2);				
				}
			}
								
					
		/**
		 * Checks if the Player collides with one of the enemies. If so a vector is created and movement restrictions are made.
		 */
		if (distance2p(enemy1.getLocation())<this.getRadius()+enemy1.getRadius()) {
			playerenemy1vector.setX(this.getLocation().getX() - enemy1.getLocation().getX());
			playerenemy1vector.setY(this.getLocation().getY() - enemy1.getLocation().getY());
		} else {
			playerenemy1vector = null;
		}
		
		if (distance2p(enemy2.getLocation())<this.getRadius()+enemy2.getRadius()) {
			playerenemy2vector.setX(this.getLocation().getX() - enemy2.getLocation().getX());
			playerenemy2vector.setY(this.getLocation().getY() - enemy2.getLocation().getY());
		} else {
			playerenemy2vector = null;
		}
		
		if (isDirectionPossible(playerenemy1vector,playerenemy2vector,playerwall1vector)) {
				this.getLocation().translate(moveinx*speed, moveiny*speed);
		}
	}
	
	/**
	 * Its possible to be surrended by 3 colliding Items (1 Wall and 2 Players) therefore its needed to check
	 * if all Vectors are either zero or the current Movement direction can be made.
	 * If its not possible to move in the direction the Player is smashing his buttons, this methode returns false
	 * and the Player cant move at all till he changes his keyboard inputs.
	 *
	 * @param a The diffrent vector for restrictions.
	 * @param b the b
	 * @param c the c
	 * @return Returns if the current movement direction can be made.
	 */
	public boolean isDirectionPossible(Vector a, Vector b, Vector c) {
		Vector directionkey = new Vector();
		directionkey.setX(this.moveinx);
		directionkey.setY(this.moveiny);
		
		
		if (a != null) {
			if (directionkey.anglebetweenvector(a)>Math.PI/2)
				return false;
		}
		
		if (b != null) {
			if (directionkey.anglebetweenvector(b)>Math.PI/2)
				return false;
		}
		
		if (c != null) {
			if (directionkey.anglebetweenvector(c)>Math.PI/2)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Adds the walls to the class. Needed but only called once.
	 *
	 * @param walls the walls
	 * @param goals the goals
	 * @param owngoal the owngoal
	 */
	public void addwalls(Wall[] walls,Goal[] goals, Goal owngoal) {
		this.setWalls(walls);
		this.setGoals(goals);
		this.owngoal = owngoal;
	}
	
	/**
	 * Resets the Player to his old Positions. Should only be made with the ID but as mentioned before,
	 * this will be implemented later.
	 */
	public void reset() {
		switch(this.getId()) {
		case 1: getLocation().move(300, 300);
			break;
		case 2: getLocation().move(250, 150);
			break;
		}
		
		
	}

	/**
	 * Checks if is kickkey.
	 *
	 * @return true, if is kickkey
	 */
	public boolean isKickkey() {
		return kickkey;
	}

	/**
	 * Sets the kickkey.
	 *
	 * @param kickkey the new kickkey
	 */
	public void setKickkey(boolean kickkey) {
		this.kickkey = kickkey;
	}

	/**
	 * Gets the moveiny.
	 *
	 * @return the moveiny
	 */
	public int getMoveiny() {
		return moveiny;
	}

	/**
	 * Sets the moveiny.
	 *
	 * @param moveiny the new moveiny
	 */
	public void setMoveiny(int moveiny) {
		this.moveiny = moveiny;
	}

	/**
	 * Gets the moveinx.
	 *
	 * @return the moveinx
	 */
	public int getMoveinx() {
		return moveinx;
	}

	/**
	 * Sets the moveinx.
	 *
	 * @param moveinx the new moveinx
	 */
	public void setMoveinx(int moveinx) {
		this.moveinx = moveinx;
	}

	/**
	 * Gets the owngoal.
	 *
	 * @return the owngoal
	 */
	public Goal getOwngoal() {
		return owngoal;
	}

	/**
	 * Sets the owngoal.
	 *
	 * @param owngoal the new owngoal
	 */
	public void setOwngoal(Goal owngoal) {
		this.owngoal = owngoal;
	}

	/**
	 * Gets the goal.
	 *
	 * @return the goal
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * Sets the goal.
	 *
	 * @param goal the new goal
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

	/* (non-Javadoc)
	 * @see logic.client.Ball#getSpeed()
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Gets the playername.
	 *
	 * @return the playername
	 */
	public String getPlayername() {
		return playername;
	}

	/**
	 * Sets the playername.
	 *
	 * @param playername the new playername
	 */
	public void setPlayername(String playername) {
		this.playername = playername;
	}
	
}
