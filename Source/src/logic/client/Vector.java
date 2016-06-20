package logic.client;

// TODO: Auto-generated Javadoc
/**
 * Manages a Vector and is used to easy rotate 2 vectors.
 * @author mathias
 *
 */

public class Vector {
	
	/** The x. */
	private double x;
	
	/** The y. */
	private double y;
	
	/**
	 * Instantiates a new vector.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Vector(double x, double y) {
	this.x = x;
	this.y = y;
	}
	
	/**
	 * Instantiates a new vector.
	 */
	public Vector() {
		
		}
	
	/**
	 * 	
	 * Rotates 2 vectors: The input vector and the vector of the class.
	 *
	 * @param v the v
	 */
	public void rotate(Vector v) {
	     double angle = anglebetweenvector(v);
	    
	     rotateByRadian(angle*2);
 
	}
	
	/**
	 * calculates the angle between the the input vector and the vector from this class.
	 *
	 * @param v the v
	 * @return the double
	 */
	double anglebetweenvector(Vector v) {
		return Math.acos(scalar(v)/(absolute(this)*absolute(v)));
	}

	/**
	 * Rotates a vector by a radian. Works clockwise.
	 *
	 * @param radian the radian
	 */
	
	public void rotateByRadian(double radian) {
		 double oldrotationx = this.x;
	     double oldrotationy = this.y;  
	     this.x = (Math.cos(radian)*oldrotationx - Math.sin(radian)*oldrotationy);
	     this.y = (Math.sin(radian)*oldrotationx + Math.cos(radian)*oldrotationy);     
	}
	
	
	
	/**
	 * Resets the vector to 0/0.
	 */
	public void reset() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Calculates the absolute of a vector.
	 *
	 * @param v the vector
	 * @return returns the absolute
	 */
	
	public double absolute(Vector v) {
		return Math.sqrt(v.x*v.x+v.y*v.y);
  	}
	
	/**
	 * Calculates the scalar of the input vector and the vector from the class.
	 * @param b the Input vector
	 * @return Returns the scalar.
	 */
	public double scalar(Vector b) {
		return this.x*b.x+this.y*b.y;
  	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}
}
