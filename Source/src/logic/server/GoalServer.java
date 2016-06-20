package logic.server;

import java.awt.Point;

// TODO: Auto-generated Javadoc
/**
 * This class Goal extends from Wall and has his own constructor. 
 * @author mathias
 */
public class GoalServer extends WallServer {
	
	/**
	 * This Variable makes life easier since u dont have to calculate it all the time again.
	 */
	private Point location2;
	
	/**
	 * Instantiates a new goal server.
	 *
	 * @param locationx the locationx
	 * @param locationy the locationy
	 * @param rotationdegree the rotationdegree
	 * @param l the l
	 * @param w the w
	 * @param id the id
	 */
	/*
	 * The Constructor. Only difference between the super class is the calculation for location2. Therefore super() could be used.
	 */
	public GoalServer(int locationx, int locationy, int rotationdegree,int l,int w, int id) {
		this.setLength(l);
		this.setWidth(w);
		this.setId(id);
		setOrientation(new VectorServer(Math.cos(rotationdegree * Math.PI / 180), Math.sin(rotationdegree * Math.PI / 180)));
		setLocation1(new Point((int) (locationx+getOrientation().getX()*75),(int) (locationy+getOrientation().getY()*75)));
		location2 = new Point((int) (locationx+getOrientation().getX()*125),(int) (locationy+getOrientation().getY()*125));

	}
	
	/**
	 * Instantiates a new goal server.
	 */
	public GoalServer() {
		
	}

	/**
	 * Gets the this Variable makes life easier since u dont have to calculate it all the time again.
	 *
	 * @return the this Variable makes life easier since u dont have to calculate it all the time again
	 */
	public Point getLocation2() {
		return location2;
	}

	/**
	 * Sets the this Variable makes life easier since u dont have to calculate it all the time again.
	 *
	 * @param location2 the new this Variable makes life easier since u dont have to calculate it all the time again
	 */
	public void setLocation2(Point location2) {
		this.location2 = location2;
	}
}
