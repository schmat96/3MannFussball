package logic.client;

import java.awt.Point;

// TODO: Auto-generated Javadoc
/**
 * This class Goal extends from Wall and has his own constructor. 
 * @author mathias
 */
public class Goal extends Wall {
	
	/**
	 * This Variable makes life easier since u dont have to calculate it all the time again.
	 */
	private Point location2;
	
	/**
	 * Instantiates a new goal.
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
	public Goal(int locationx, int locationy, int rotationdegree,int l,int w, int id) {
		this.setLength(l);
		this.setWidth(w);
		this.setID(id);
		setOrientation(new Vector(Math.cos(rotationdegree * Math.PI / 180), Math.sin(rotationdegree * Math.PI / 180)));
		setLocation1(new Point((int) (locationx+getOrientation().getX()*75),(int) (locationy+getOrientation().getY()*75)));
		setLocation2(new Point((int) (locationx+getOrientation().getX()*125),(int) (locationy+getOrientation().getY()*125)));

	}
	
	/**
	 * Instantiates a new goal.
	 */
	public Goal() {
		
	}

	/**
	 * Draws the Goal and the black dots in the edges.
	 *
	 * @param window the window
	 */
	public void drawWall(Window window) {
		
		for (int j=0;j<getWidth();j=j+2) {
			for (int i=0;i<getLength();i=i+2) {
				
				switch(getID()+1) {
				case(1): 
					window.setPixel(doubletoint(getLocation1().getX()+i*getOrientation().getX()+j*getOrientation().getY()), doubletoint(getLocation1().getY()+i*getOrientation().getY()-j*getOrientation().getX()), 255, 0, 0);
					break;
				case(2): 
					window.setPixel(doubletoint(getLocation1().getX()+i*getOrientation().getX()+j*getOrientation().getY()), doubletoint(getLocation1().getY()+i*getOrientation().getY()-j*getOrientation().getX()), 0, 255, 0);
					break;
				case(3): 
					window.setPixel(doubletoint(getLocation1().getX()+i*getOrientation().getX()+j*getOrientation().getY()), doubletoint(getLocation1().getY()+i*getOrientation().getY()-j*getOrientation().getX()), 0, 0, 255);
					break;
				default:
					window.setPixel(doubletoint(getLocation1().getX()+i*getOrientation().getX()+j*getOrientation().getY()), doubletoint(getLocation1().getY()+i*getOrientation().getY()-j*getOrientation().getX()), 80*(this.getID()+1), 80*(this.getID()+1), 0);
					break;
				}
				
			}
		}
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
