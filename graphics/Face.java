package graphics;

import java.awt.Graphics2D;
import java.awt.Polygon;

import util.math.Matrix;
import util.math.Plane;
import util.math.Vector;

public class Face {
	
	public Vector[] points;
	public Vector center;
	private Vector[] cameraPoints;
	private Vector[] projectedPoints;
	private Vector tempVector = new Vector(3);
		
	public Plane plane;
	private Vector position;
	private Vector lightSource = new Vector(3);
	
	/**
	 * Create face with given position and offset points.
	 * 
	 * @param position A vector which will be stored as a reference
	 * @param center The center of the face
	 * @param points The points that are offset from position
	 */
	public Face(Vector position, Vector... points) {
		if (points.length < 3) {
			System.out.println("Not enough points given to Face");
			throw new IllegalArgumentException();
		}
		
		this.position = position;
		
		this.points = new Vector[points.length];
		this.cameraPoints = new Vector[points.length];
		this.projectedPoints = new Vector[points.length];
		
		double averageX = 0;
		double averageY = 0;
		double averageZ = 0;
		
		int i = 0;
		for (Vector vec : points) {
			this.points[i] = vec;
			averageX += vec.matrix[0][0];
			averageY += vec.matrix[0][1];
			averageZ += vec.matrix[0][2];
			
			i++;
		}
		
		averageX /= points.length;
		averageY /= points.length;
		averageZ /= points.length;
		this.center = new Vector(3, averageX, averageY, averageZ);
		
		this.plane = new Plane(points[0], points[1], points[2]);
		
		for (int j = 0; j < points.length; j++) {
			cameraPoints[j] = new Vector(3);
			projectedPoints[j] = new Vector(2);
		}
	}

	/**
	 * Draws this face, moving over by the
	 * translate vector. The translate vector will be modified.
	 * 
	 * @param ctx The Graphics2D context to draw on
	 * @param translate Amount to shift by
	 */
	public void draw(Graphics2D ctx, Vector translate) {
				
		double[][] r = Camera.CAM_ROTATION().matrix;
		double rX = r[0][0];
		double rY = r[0][1];
		double rZ = r[0][2];
		Matrix rotMatrix = Camera.ALL_ROTATION_MATRIX(rX, rY, rZ);
		
		Polygon shape = new Polygon();
		for (int i = 0; i < points.length; i++) {
			tempVector.set(translate);
			tempVector.add(points[i]);
			Matrix.productTranslate(tempVector, rotMatrix, Camera.CAM_POSITION, cameraPoints[i]);
			Camera.projectVector(cameraPoints[i], projectedPoints[i]);
			shape.addPoint((int)projectedPoints[i].matrix[0][0], (int)projectedPoints[i].matrix[0][1]);
		}
		ctx.fill(shape);
		
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
		lightSource.set(source);
		lightSource.subtract(this.position);
		tempVector.subtract(this.center);
				
		double dot = plane.normal.dot(lightSource);
		double distance = lightSource.length();
		double multFactor = Math.pow(1.1, distance * -0.003);
		return dot / distance / plane.normal.length() * multFactor;
	}

}
