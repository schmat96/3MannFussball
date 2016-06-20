package logic.server;

import java.awt.Point;

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
public class PlayerGameServer extends BallServer {
	
	/** The kickkey. */
	private boolean kickkey = true;
	
	/** The moveiny. */
	private int moveiny = 0;
	
	/** The moveinx. */
	private int moveinx = 0;
	
	/** The owngoal. */
	private GoalServer owngoal = new GoalServer();
	
	/** The goal. */
	private int goal = 0;
	
	/** The speed. */
	private int speed = 2;
	
	/** The ki. */
	private boolean KI = false;

	
	/**
	 * The constructor.
	 *
	 * @param x The x-coordinate. Could be calculated with the player id and will be implemented later cause tired.
	 * @param y The y-coordinate.
	 * @param id The ID. Every Player has his own ID.
	 */
	public PlayerGameServer(int x, int y, int id){
		this.setId(id);
		if (id==0) {
			KI=true;
		}
		this.getLocation().move(x, y);
		this.setRadius(15);
	}

	/**
	 * A not needed constructor with no parameter. Eclipse told me to add one so I did.
	 */
	public PlayerGameServer() {
		
	}
	
	/**
	 * Checks if the Player collides with an wall or an enemy. If the Player does, he gets Restrictions in his moving sequence (makes it impossible to move into other Players, outside of the Map
	 *
	 * @param enemy1 one of the other players.
	 * @param enemy2 the enemy2
	 */
	public void update(PlayerGameServer enemy1, PlayerGameServer enemy2) {
		
		VectorServer playerenemy1vector = new VectorServer();
		VectorServer playerenemy2vector = new VectorServer();
		VectorServer playerwall1vector = new VectorServer();
			
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
		
		this.setRadius(countDistancebetweenOwnGoal());
								
					
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
	 * Count distancebetween own goal.
	 *
	 * @return the int
	 */
	private int countDistancebetweenOwnGoal() {
		double i = pointToLineDistance(owngoal.getLocation1(),owngoal.getLocation2());
		int a = this.doubleToInt(i/5);
		if (a < 5) {
			return 5;
		}
		if (a > 20) {
			return 20;
		}
		return a;
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
	public boolean isDirectionPossible(VectorServer a, VectorServer b, VectorServer c) {
		VectorServer directionkey = new VectorServer();
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
	public void addWalls(WallServer[] walls,GoalServer[] goals, GoalServer owngoal) {
		this.setWalls(walls);
		this.setGoals(goals);
		this.owngoal = owngoal;
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
		double newX = (point1.getX() + point2.getX())/2;
		double newY = (point1.getY() + point2.getY())/2;
		pointReturn.setLocation(doubleToInt(newX), doubleToInt(newY));
		return pointReturn;
	}
	
	/* (non-Javadoc)
	 * @see logic.server.BallServer#reset()
	 */
	public void reset() {
		Point mid = new Point();
		mid.move(300,200);
		this.setLocation(getMidOf2Points(getMidOf2Points(this.owngoal.getLocation1(), this.owngoal.getLocation2()), mid));
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
	public GoalServer getOwngoal() {
		return owngoal;
	}

	/**
	 * Sets the owngoal.
	 *
	 * @param owngoal the new owngoal
	 */
	public void setOwngoal(GoalServer owngoal) {
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
	 * @see logic.server.BallServer#getSpeed()
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
	 * Checks if is ki.
	 *
	 * @return true, if is ki
	 */
	public boolean isKI() {
		return KI;
	}

	/**
	 * Sets the ki.
	 *
	 * @param kI the new ki
	 */
	public void setKI(boolean kI) {
		KI = kI;
	}
}
