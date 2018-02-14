package graphics;

import java.awt.Graphics2D;

import util.math.Vector;

public interface Drawable {

	/**
	 * Draws a visual representation of the object.
	 * Will use the Camera class to find
	 * Camera X, Y, Scale, and Rotation.
	 * <p>
	 * 
	 * @param ctx		context to draw the object on
	 */
	public void draw(Graphics2D ctx);
	
	/**
	 * Returns a boolean which will decide if the object is drawn or not
	 * This method should be lightweight, so err on the side of
	 * quick execute than sometimes draws the object unnecessarily, rather
	 * than complicated equations.
	 * 
	 * @return Whether or not the object is in the window
	 */
	public boolean inCameraWindow();
	
	/**
	 * Order this object wants to be drawn in, higher numbers mean it will
	 * be drawn first (so behind other objects)
	 * 
	 * @return A number deciding how soon in the tick an object is drawn
	 */
	public int order();
	
	/**
	 * Calculate whether the given raytrace collides with
	 * this drawable.
	 * 
	 * @param source Source vector for raytrace
	 * @param destination Destination vector for raytrace
	 * @return true if the ray collides, fale if not
	 */
	public boolean raytrace(Vector source, Vector destination);
	
}
