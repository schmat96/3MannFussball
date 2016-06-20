package logic.server;

import java.awt.Point;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * A class that manages the ball. U can update the Ball or check if the ball collides with a wall.
 * @author mathias
 *
 */

public class BallServer {
	
	/** The id. */
	private int id = 1;
	
	/** The location. */
	private Point location = new Point(400,300);
	
	/** The radius. */
	private int radius = 5;
	
	/** The speedvector. */
	private VectorServer speedvector = new VectorServer(-1,1);
	
	/** The speed. */
	private float speed = (float) 2;
	
	/** The nocollisionp1. */
	private int nocollisionp1 = 0;
	
	/** The nocollisionp2. */
	private int nocollisionp2 = 0;
	
	/** The nocollisionp3. */
	private int nocollisionp3 = 0;
	
	/** The walls. */
	private WallServer[] walls = new WallServer[6];
	
	/** The goals. */
	private GoalServer[] goals = new GoalServer[3];
	
	/**
	 * Instantiates a new ball server.
	 */
	public BallServer(){
		
	}
	
	/**
	 * Checks if there are any collisions between the ball, a player or a wall. Updates the ball location depending on the collisions.
	 *
	 * @param player1 the player1
	 * @param player2 the player2
	 * @param player3 the player3
	 * @return the int
	 */
	public int update(PlayerGameServer player1,PlayerGameServer player2, PlayerGameServer player3) {
		
		//used to know if the Ball is stuck. If the variable is higher then 2, the ball resets his orientation Vector and the player(s) will need to reset the Game with f6
		int countcollisions = 0;
		
		VectorServer rotatevector = new VectorServer();
		
		VectorServer playerballvector = new VectorServer();
		
		//Checks if the ball collides with a wall.
		
			for (int i=0;i<6;i++) {
				if (pointToLineDistance(walls[i].getLocation1(),walls[(i+1)%6].getLocation1())<radius) {
					if ((i-1)%2==0) {
					if (location.getX()>goals[1].getLocation1().getX() && location.getX()<goals[1].getLocation2().getX()) {return 2;}
					if (location.getX()<goals[0].getLocation1().getX() && location.getX()>goals[0].getLocation2().getX()) {return 1;}
					if (location.getX()>goals[2].getLocation1().getX() && location.getX()<goals[2].getLocation2().getX()) {return 3;}
					}

					rotatevector = walls[i].getOrientation();
					countcollisions++;
				}
			}
			
			//Checks if the ball collides with a player. 
			if (distance2p(player1.getLocation())<this.radius+player1.getRadius() && nocollisionp1 < 0) {
				playerballvector.setX(this.location.getX() - player1.getLocation().getX());
				playerballvector.setY(this.location.getY() - player1.getLocation().getY());
				playerballvector.rotateByRadian(-Math.PI/2);
				rotatevector = playerballvector;
				countcollisions++;
				if (player2.isKickkey()) {
					calcspeed(1);
				} 
				nocollisionp1 = 20;
			}
			
			nocollisionp1--;
			
			if (distance2p(player2.getLocation())<this.radius+player2.getRadius() && nocollisionp2 < 0) {
				playerballvector.setX(this.location.getX() - player2.getLocation().getX());
				playerballvector.setY(this.location.getY() - player2.getLocation().getY());
				playerballvector.rotateByRadian(-Math.PI/2);
				rotatevector = playerballvector;
				if (player2.isKickkey()) {
					calcspeed(1);
				} 
				countcollisions++;

				nocollisionp2 = 20;
			}
			
			nocollisionp2--;
			
			if (distance2p(player3.getLocation())<this.radius+player3.getRadius() && nocollisionp3 < 0) {
				playerballvector.setX(this.location.getX() - player3.getLocation().getX());
				playerballvector.setY(this.location.getY() - player3.getLocation().getY());
				playerballvector.rotateByRadian(-Math.PI/2);
				rotatevector = playerballvector;
				
				if (player3.isKickkey()) {
					calcspeed(1);
				}
				countcollisions++;

				nocollisionp3 = 20;
			}
			
			nocollisionp3--;
		
		if (countcollisions==0) {
			calcspeed(-1);
		}
		
		if (countcollisions==1) {			
				speedvector.rotate(rotatevector);
		}
		

		location.setLocation(location.getX()+speed*speedvector.getX(), location.getY()+speed*speedvector.getY());
		return 0;
	}
	
	/**
	 * Calculates the speed depending on the input (if the input is below 0, the speed is being decreased).
	 *
	 * @param a the a
	 */
	private void calcspeed(int a) {	
		
	}
	
	/**
	 * Calculates the distance between a line and the Ball.
	 *
	 * @param A Location of the line beginning.
	 * @param B Location where the line ends.
	 * @return the double
	 */
	public double pointToLineDistance(Point A, Point B) {
    	double normalLength = Math.sqrt((B.getX()-A.getX())*(B.getX()-A.getX())+(B.getY()-A.getY())*(B.getY()-A.getY()));
		return Math.abs((location.getX()-A.getX())*(B.getY()-A.getY())-(location.getY()-A.getY())*(B.getX()-A.getX()))/normalLength;
  	}
	
	/**
	 * Calculates the distance between a point and the the loaction form the class itself.
	 *
	 * @param point1 the point1
	 * @return returns the distance.
	 */
	
	public double distance2p(Point point1){
		return Math.sqrt((point1.getX()-this.location.getX())*(point1.getX()-this.location.getX())+(point1.getY()-this.location.getY())*(point1.getY()-this.location.getY()));
	}
	
	
	/**
	 * Adds the walls to the class. Needed but only called once.
	 *
	 * @param walls the walls
	 * @param goals the goals
	 */
	public void addWalls(WallServer[] walls,GoalServer[] goals) {
		this.walls = walls;
		this.goals = goals;
	}
	
	/**
	 * Converts a double to a int.
	 *
	 * @param d the d
	 * @return the int
	 */
	public int doubleToInt(double d) {
		return (int) Math.toIntExact(Math.round(d));
	}

	/**
	 * Resets the Ball to the center.
	 */
	public void reset() {
		location.move(300,200);
		Random generator = new Random(); 
		int i = generator.nextInt(4);
		
		switch(i) {
		case(1):
			speedvector.setX(-1);
			speedvector.setY(-1);	
			break;
		case(2):
			speedvector.setX(1);
			speedvector.setY(1);	
			break;
		case(3):
			speedvector.setX(-1);
			speedvector.setY(-1);	
			break;
		case(4):
			speedvector.setX(1);
			speedvector.setY(-1);	
			break;
		default:
			speedvector.setX(1);
			speedvector.setY(-1);	
		}
		
	}
	
	/**
	 * Gets the xcoord.
	 *
	 * @return the xcoord
	 */
	public String getXcoord() {
		return ((int)(Math.round(location.getX())))+"";
	}

	/**
	 * Gets the ycoord.
	 *
	 * @return the ycoord
	 */
	public String getYcoord() {
		return ((int)(Math.round(location.getY()))) + "";
	}

	/**
	 * Check if outside.
	 *
	 * @return true, if successful
	 */
	public boolean checkIfOutside() {
		if (location.getX()<0) {
			return true;
		}
		if (location.getY()<0) {
			return true;
		}
		if (location.getY()>400) {
			return true;
		}
		if (location.getX()>600) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Gets the speedvector.
	 *
	 * @return the speedvector
	 */
	public VectorServer getSpeedvector() {
		return speedvector;
	}

	/**
	 * Sets the speedvector.
	 *
	 * @param speedvector the new speedvector
	 */
	public void setSpeedvector(VectorServer speedvector) {
		this.speedvector = speedvector;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
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
}
