package logic.client;

import java.awt.Point;

// TODO: Auto-generated Javadoc
/**
 * This class is used to store the Wall corners and the rotation of them. 
 * @author mathias
 *
 */
public class Wall {
	
	/** The id. */
	private int id;
	
	/** The location1. */
	private Point location1;
	
	/** The length. */
	private int length;
	
	/** The width. */
	private int width;
	
	/** The orientation. */
	private Vector orientation;
	
	/**
	 * Instantiates a new wall.
	 */
	public Wall() {
	
	}
	
	/**
	 * Constructor. 
	 * @param locationx x-coordinate where the Wall starts
	 * @param locationy y-coordinate
	 * @param rotationdegree the wall is getting rotated in the constructor by this number. Works clockwise. Starting point is always the same but endpoint can change.
	 * @param l length of the wall
	 * @param w width of the wall
	 */
	public Wall(int locationx, int locationy, int rotationdegree,int l,int w) {
		this.length = l;
		this.width = w;
		location1 = new Point(locationx,locationy);
		orientation = new Vector(Math.cos(rotationdegree * Math.PI / 180), Math.sin(rotationdegree * Math.PI / 180));
	}
	
	/**
	 * Draws the wall.
	 *
	 * @param window the window
	 */
	public void drawWall(Window window) {
		for (int i=0;i<length;i++) {
		window.setPixel(doubletoint(location1.getX()+i*orientation.getX()), doubletoint(location1.getY()+i*orientation.getY()), 255, 0, 0);
		}
	}
	
	/**
	 * Used to round or change a double into an int.
	 *
	 * @param d the d
	 * @return the int
	 */
	public int doubletoint(double d) {
		return (int) Math.toIntExact(Math.round(d));
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Gets the location1.
	 *
	 * @return the location1
	 */
	public Point getLocation1() {
		return location1;
	}

	/**
	 * Sets the location1.
	 *
	 * @param location1 the new location1
	 */
	public void setLocation1(Point location1) {
		this.location1 = location1;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
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
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public Vector getOrientation() {
		return orientation;
	}

	/**
	 * Sets the orientation.
	 *
	 * @param orientation the new orientation
	 */
	public void setOrientation(Vector orientation) {
		this.orientation = orientation;
	}
	
	

}
