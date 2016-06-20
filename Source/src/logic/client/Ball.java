package logic.client;

import java.awt.Point;

// TODO: Auto-generated Javadoc
/**
 * A class that manages the ball. U can update the Ball or check if the ball collides with a wall.
 * @author mathias
 *
 */

public class Ball {
	
	/** The id. */
	private int id = 1;
	
	/** The location. */
	private Point location = new Point(400,300);
	
	/** The radius. */
	private int radius = 5;
	
	/** The speedvector. */
	private Vector speedvector = new Vector(-1,1);
	
	/** The speed. */
	private float speed = (float) 2;
	
	/** The nocollisionp1. */
	private int nocollisionp1 = 0;
	
	/** The nocollisionp2. */
	private int nocollisionp2 = 0;
	
	/** The nocollisionp3. */
	private int nocollisionp3 = 0;
	
	/** The walls. */
	private Wall[] walls = new Wall[6];
	
	/** The goals. */
	private Goal[] goals = new Goal[3];
	
	/**
	 * Instantiates a new ball.
	 */
	public Ball(){
		
	}
	
	/**
	 * Draws the ball with the current location, radius and the fill Color in RGB.
	 * @param window This variable is needed for knowing the window, where the ball should be drown.
	 */
	public void draw(Window window) {
		window.drawOval((int) location.getX(), (int) location.getY(), radius, radius, 100, 100, 100);
	}
	
	/**
	 * Checks if there are any collisions between the ball, a player or a wall. Updates the ball location depending on the collisions.
	 *
	 * @param player1 the player1
	 * @param player2 the player2
	 * @param player3 the player3
	 * @return the int
	 */
	public int update(PlayerGame player1,PlayerGame player2, PlayerGame player3) {
		
		//used to know if the Ball is stuck. If the variable is higher then 2, the ball resets his orientation Vector and the player(s) will need to reset the Game with f6
		int countcollisions = 0;
		
		Vector rotatevector = new Vector();
		
		Vector playerballvector = new Vector();
		
		//Checks if the ball collides with a wall.
		
			for (int i=0;i<6;i++) {
				if (pointToLineDistance(walls[i].getLocation1(),walls[(i+1)%6].getLocation1())<radius) {
					if ((i-1)%2==0) {
					if (location.getX()>goals[1].getLocation1().getX() && location.getX()<goals[1].getLocation2().getX())
						return 2;
					if (location.getX()<goals[0].getLocation1().getX() && location.getX()>goals[0].getLocation2().getX())
						return 1;
					if (location.getX()>goals[2].getLocation1().getX() && location.getX()<goals[2].getLocation2().getX())
						return 3;
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
				nocollisionp1 = 20;
			}
			
			nocollisionp1--;
			
			if (distance2p(player2.getLocation())<this.radius+player2.getRadius() && nocollisionp2 < 0) {
				playerballvector.setX(this.location.getX() - player2.getLocation().getX());
				playerballvector.setY(this.location.getY() - player2.getLocation().getY());
				playerballvector.rotateByRadian(-Math.PI/2);
				rotatevector = playerballvector;
				countcollisions++;

				nocollisionp2 = 20;
			}
			
			nocollisionp2--;
			
			if (distance2p(player3.getLocation())<this.radius+player3.getRadius() && nocollisionp3 < 0) {
				playerballvector.setX(this.location.getX() - player3.getLocation().getX());
				playerballvector.setY(this.location.getY() - player3.getLocation().getY());
				playerballvector.rotateByRadian(-Math.PI/2);
				rotatevector = playerballvector;
				
				countcollisions++;

				nocollisionp3 = 20;
			}
			
			nocollisionp3--;

		if (countcollisions==1) {			
				speedvector.rotate(rotatevector);
		}
		
		if (countcollisions>1) {
			speedvector.reset();
			System.out.println("Ball sitzt fest. Es wird empfohlen das Spiel mit f6 zu reseten.");
		}
		

		location.setLocation(location.getX()+speed*speedvector.getX(), location.getY()+speed*speedvector.getY());
		return 0;
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
	public void addWalls(Wall[] walls,Goal[] goals) {
		this.walls = walls;
		this.goals = goals;
	}
	
	/**
	 * Converts a double to a int.
	 *
	 * @param d the d
	 * @return the int
	 */
	public int doubletoint(double d) {
		return (int) Math.toIntExact(Math.round(d));
	}

	/**
	 * Resets the Ball to the center.
	 */
	public void reset() {
		location.move(300,200);
		speedvector.setX(1);
		speedvector.setY(-1);
		
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
	public Vector getSpeedvector() {
		return speedvector;
	}

	/**
	 * Sets the speedvector.
	 *
	 * @param speedvector the new speedvector
	 */
	public void setSpeedVector(Vector speedvector) {
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
}
