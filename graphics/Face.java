package graphics;

import util.math.Plane;
import util.math.Vector;

public class Face {
		
	public Vector[] projectedPoints;
	public Plane plane;
	public Vector center;
	
	private int[][] trianglePaths;
	private Vector tempVector = new Vector(3);
	private Vector position;
	
	/**
	 * Creates a face with the given parameters. projectedPoints should be modified to
	 * draw this face in the correct position
	 * 
	 * @param position A vector which will be reference to determine this face's position in the world
	 * @param points Points relative to the shape's center
	 * @param projectedPoints Projected 2D points which will be referenced to draw this face
	 * @param trianglePaths
	 */
	public Face(Vector position, Vector[] points, Vector[] projectedPoints, int[][] trianglePaths) {
		if (points.length < 3) {
			System.out.println("Not enough points given to form a Face");
			throw new IllegalArgumentException();
		}
		
		this.position = position;
		this.center = Face.center(points);
		this.projectedPoints = projectedPoints;
		this.plane = new Plane(points[0], points[1], points[2]);
		this.trianglePaths = trianglePaths;
	}

	/**
	 * Draws this face, moving over by the
	 * translate vector. The translate vector will be modified.
	 * 
	 * @param ctx The Graphics2D context to draw on
	 * @param translate Amount to shift by
	 */
	public void draw(int r, int g, int b) {
		
		double time = System.nanoTime();		
		
		Face.triangleFill(projectedPoints, trianglePaths, r, g, b);
				
		World.writeTime += System.nanoTime() - time;

	}
	
	/**
	 * Calculate whether this face should be visible
	 * using back face culling
	 * 
	 * @return true if the face is looking towards the camera
	 */
	public boolean isFacingCamera() {
		tempVector.set(Camera.CAM_POSITION);
		tempVector.subtract(this.position);
		tempVector.subtract(this.center);
		
		double dot = plane.normal.dot(tempVector);
		return dot > 0;
	}
	
	/**
	 * Get light level based on angle relative to source
	 * 
	 * @param source A vector in space which is the light source
	 * @return a number from -1 to 1 representing how intense the light should be
	 */
	public double getLightLevel(Vector source) {
		tempVector.set(source);
		tempVector.subtract(this.position);
		tempVector.subtract(this.center);
				
		double dot = plane.normal.dot(tempVector);
		double distance = tempVector.length();
		//double multFactor = Math.pow(1.1, distance * -0.003);
		return dot / distance; //* multFactor;
	}
	
	public static Vector center(Vector... points) {
		if (points.length == 0) {
			throw new IllegalArgumentException();
		}
		
		Vector center = new Vector(points[0].height);
		
		double[] averages = new double[center.height];
		
		for (Vector vec : points) {
			for (int i = 0; i < averages.length; i++) {
				averages[i] += vec.matrix[0][i];
			}
		}
		
		for (int i = 0; i < averages.length; i++) {
			averages[i] /= points.length;
			center.matrix[0][i] = averages[i];
		}
		
		return center;
	}
	
	public static void triangleFill(Vector[] projectedPoints, int[][] paths, int r, int g, int b) {
		for (int[] path : paths) {
			Vector highestPoint = projectedPoints[path[0]]; // lowest y
			Vector middlePoint = projectedPoints[path[1]]; // middle y
			Vector lowestPoint = projectedPoints[path[2]]; // largest y
			
			if (highestPoint.y() > lowestPoint.y()) {
				Vector hold = highestPoint;
				highestPoint = lowestPoint;
				lowestPoint = hold;
			}
			if (middlePoint.y() > lowestPoint.y()) {
				Vector hold = middlePoint;
				middlePoint = lowestPoint;
				lowestPoint = hold;
			}
			if (highestPoint.y() > middlePoint.y()) {
				Vector hold = middlePoint;
				middlePoint = highestPoint;
				highestPoint = hold;
			}
			
			double dYMH = middlePoint.y() - highestPoint.y();
			double dYLM = lowestPoint.y() - middlePoint.y();
			double dYLH = lowestPoint.y() - highestPoint.y();
			
			double dXMH = middlePoint.x() - highestPoint.x();
			double dXLM = lowestPoint.x() - middlePoint.x();
			double dXLH = lowestPoint.x() - highestPoint.x();
			
			for (int j = 0; j < dYMH; j++) { // this goes from top (highestPoint) down (middlePoint)
				int y = j + (int)highestPoint.y();
				
				int x1 = (int)(dXMH*(j / dYMH) + highestPoint.x());
				int x2 = (int)(dXLH*(j / dYLH) + highestPoint.x());
				if (x1 > x2) {
					int hold = x1;
					x1 = x2;
					x2 = hold;
				}
				
				for (int x = x1; x < x2; x++) {
					Surface.setColor(x, y, r, g, b);
				}
			}
			
			for (int j = 0; j > -dYLM; j--) { // this goes from bottom (lowestPoint) up (middlePoint)
				int y = j + (int)lowestPoint.y();
				
				int x1 = (int)(dXLM*(j / dYLM) + lowestPoint.x());
				int x2 = (int)(dXLH*(j / dYLH) + lowestPoint.x());
				if (x1 > x2) {
					int hold = x1;
					x1 = x2;
					x2 = hold;
				}
				
				for (int x = x1; x < x2; x++) {
					Surface.setColor(x, y, r, g, b);
				}
			}
		}
	}

}
