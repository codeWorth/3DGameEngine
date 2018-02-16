package util.math;

public class Plane {

	public Vector a, b, c;
	
	/**
	 * Delta from point A to point B
	 */
	public Vector dB = new Vector(3);
	/**
	 * Delta from point A to point C
	 */
	public Vector dC = new Vector(3);
	
	public Vector normal = new Vector(3);
	
	/**
	 * Creates a plane in 3D space defined by these points.
	 * It is assumed that a, b, and c do not fall along the same
	 * line. If they do strange behavior may happen.
	 * Vectors a, b, and c will not be modified.
	 * 
	 * @param a Point A, a {@link Vector} of length 3
	 * @param b Point B, a {@link Vector} of length 3
	 * @param c Point C, a {@link Vector} of length 3
	 */
	public Plane(Vector a, Vector b, Vector c) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		this.dB.set(b);
		this.dC.set(c);
		this.dB.subtract(a);
		this.dC.subtract(a);
		
		this.normal = Vector.cross(dB, dC);
		this.normal.normalize();
	}
	
	/**
	 * Finds the point of intersection between a plane and a
	 * line in 3D space.
	 * 
	 * @param plane The place that the line will intersect
	 * @param a1 One point along line A
	 * @param a2 Second point along line A
	 * @return a 3D Vector if they intersect, null if not
	 */
	public static Vector intersection(Plane plane, Vector a1, Vector a2) {
		Vector dA = new Vector(a2.height, a2.matrix[0]);
		dA.subtract(a1);
		
		Vector cross = new Vector(plane.dC.height, Vector.cross(plane.dB, plane.dC).matrix[0]);
		double determinant = -dA.dot(cross);
		
		if (determinant == 0) {
			return null;
		}
		
		Vector dif = new Vector(a1.height, a1.matrix[0]);
		dif.subtract(plane.a);
		double numerator = cross.dot(dif);
		
		double t = numerator / determinant;
		dA.multiply(t);
		dA.add(a1);
		return dA;
		
	}
	
}
