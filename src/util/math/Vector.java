package util.math;

/**
 * A Vector of N length. This is simply a Matrix
 * with dimensions [1, N]. The resize, flip, flatten, and squeeze functions
 * inherited from Matrix will do nothing, as a vector cannot be resized.
 * 
 * @author andrew
 *
 */
public class Vector extends Matrix {
	
	public static final Vector zeros3D = new Vector(3);

	/**
	 * Creates a Vector with the given length as a matrix
	 * with dimensions [1, length].
	 * If the wrong number of values is given an error is thrown.
	 * 
	 * @param length Length of the vector, not 0
	 * @param values Values to put into vector
	 */
	public Vector(int length, double... values) {
		super(1, length, values);
	}
	
	/**
	 * Creates an empty Vector with the given length as a matrix
	 * with dimensions [1, length].
	 * 
	 * @param length Length of the vector, not 0
	 */
	public Vector(int length) {
		super(1, length);
	}
	
	/**
	 * Creates a vector from a correctly sized matrix.
	 * 
	 * @param matrix A matrix with width 1
	 */
	public Vector(Matrix matrix) {
		this(matrix.height, matrix.matrix[0]);
	}
	
	@Override
	public void resize(int newWidth, int newHeight) {
		return;
	}
	
	@Override
	public void flip() {
		return;
	}
	
	@Override
	public void flatten() {
		return;
	}
	
	@Override
	public void squeeze() {
		return;
	}
	
	/**
	 * Quickly calculates the length of this Vector squared
	 * 
	 * @return Length of the vector squared
	 */
	public double length2() {
		double sum = 0;
		for (int n = 0; n < width*height; n++) {
			int j = n/this.width;
			int i = n - j*this.width;
			sum += matrix[i][j]*matrix[i][j];
		}
		return sum;
	}
	
	/**
	 * More expensive function that returns the actual length of the vector.
	 * Uses the square root function.
	 * 
	 * @return Length of the vector
	 */
	public double length() {
		return Math.sqrt(length2());
	}
	
	/**
	 * Dot product of this vector by another. 
	 * Throws an error if the vectors' lengths are
	 * not equal.
	 * 
	 * @param other Other vector
	 * @return A number, the dot product of this vector by other
	 */
	public double dot(Vector other) {
		double sum = 0;
		for (int n = 0; n < height; n++) {
			sum += this.matrix[0][n] * other.matrix[0][n];
		}
		return sum;
	}
	
	/**
	 * Sets the length of this vector to 1.
	 * As expensive as {@link #length()}
	 */
	public void normalize() {
		double length = length();
		this.divide(length);
	}
	
	
	/**
	 * Extremely fast function that checks if 2 line segments intersect using cross products.
	 * Only works in 2 dimensions.
	 * Assumes that Vectors are 2D. If given 3+ dimensional vectors,
	 * other dimensions are ignored.
	 * 
	 * @param v1 First segment, centered at (0, 0)
	 * @param v2 Second segment, centered at v2Offset
	 * @param v2Offset The origin of v2
	 * @return true if they intersect
	 */
	public static boolean intersects(Vector v1, Vector v2, Vector v2Offset) {
		
		//using cross products, which side of v2Offset is v1?
		double n1 = v1.matrix[0][0] * (v2.matrix[0][1] + v2Offset.matrix[0][1]) - (v2.matrix[0][0] + v2Offset.matrix[0][0]) * v1.matrix[0][1]; 
		double n2 = v1.matrix[0][0] * v2Offset.matrix[0][1] - v2Offset.matrix[0][0] * v1.matrix[0][1];
		
		double m1 = v2.matrix[0][0] * (-v2Offset.matrix[0][1]) - (-v2Offset.matrix[0][0]) * v2.matrix[0][1];
		double m2 = v2.matrix[0][0] * (v1.matrix[0][1] - v2Offset.matrix[0][1]) - (v1.matrix[0][0] - v2Offset.matrix[0][0]) * v2.matrix[0][1];
		
		double n1Sign = Math.signum(n1);
		double n2Sign = Math.signum(n2);
		double m1Sign = Math.signum(m1);
		double m2Sign = Math.signum(m2);
		
		return (n1Sign == -n2Sign && m1Sign == -m2Sign);
	}
	
	/**
	 * Cross multiply vector A and vector B.
	 * Does not modify either Vector.
	 * Order matters. The vectors must have the same length
	 * 
	 * @param a First vector.
	 * @param b Second vector.
	 * @return A new vector, the cross product of a and b
	 */
	public static Vector cross(Vector a, Vector b) {
		Vector newVec = new Vector(a.height);
		cross(a, b, newVec);
		return newVec;
	}
	
	/**
	 * Cross multiply 3D vectors A and vector B.
	 * Does not modify either Vector.
	 * Order matters. The vectors must have the same length.
	 * Puts the answer into result.
	 * 
	 * @param a First 3D vector.
	 * @param b Second 3D vector.
	 * @param result Where the answer is stored
	 */
	public static void cross(Vector a, Vector b, Vector result) {
		if (a.height != b.height || a.height != 3) {
			throw new IllegalArgumentException();
		}
		
		result.matrix[0][0] = a.matrix[0][1] * b.matrix[0][2] - a.matrix[0][2] * b.matrix[0][1];
		result.matrix[0][1] = a.matrix[0][2] * b.matrix[0][0] - a.matrix[0][0] * b.matrix[0][2];
		result.matrix[0][2] = a.matrix[0][0] * b.matrix[0][1] - a.matrix[0][1] * b.matrix[0][0];
	}
	
	/**
	 * Extremely fast function that checks if 2 line segments intersect using cross products.
	 * Only works in 2 dimensions.
	 * Assumes that Vectors are 2D. If given 3+ dimensional vectors,
	 * other dimensions are ignored.
	 * 
	 * @param v1X First segment X, centered at (0,0)
	 * @param v1Y First segment Y, centered at (0,0)
	 * @param v2X Second segment X, centered at v2Offset
	 * @param v2Y Second segment Y, centered at v2Offset
	 * @param v2OffsetX Origin X of second segment
	 * @param v2OffsetY Origin Y of second segment
	 * @return true if they intersect, not including edges
	 */
	public static boolean intersects(double v1X, double v1Y, double v2X, double v2Y, double v2OffsetX, double v2OffsetY) {
		
		//using cross products, which side of v2Offset is v1?
		double n1 = v1X * (v2Y + v2OffsetY) - (v2X + v2OffsetX) * v1Y; 
		double n2 = v1X * v2OffsetY - v2OffsetX * v1Y;
		
		double m1 = v2X * (-v2OffsetY) - (-v2OffsetX) * v2Y;
		double m2 = v2X * (v1Y - v2OffsetY) - (v1X - v2OffsetX) * v2Y;
		
		double n1Sign = Math.signum(n1);
		double n2Sign = Math.signum(n2);
		double m1Sign = Math.signum(m1);
		double m2Sign = Math.signum(m2);
		
		return (m1Sign != 0 && n1Sign != 0 && n1Sign == -n2Sign && m1Sign == -m2Sign);
	}
	
	/**
	 * Returns the x value of this vector, if it exists.
	 * This will throw an ArrayOutOfBoundsException
	 * if less than matrix 1 height.
	 * 
	 * @return The X coordinate value
	 */
	public double x() {
		return matrix[0][0];
	}
	
	/**
	 * Returns the y value of this vector, if it exists.
	 * This will throw an ArrayOutOfBoundsException
	 * if less than matrix 2 height.
	 * 
	 * @return The Y coordinate value
	 */
	public double y() {
		return matrix[0][1];
	}
	
	/**
	 * Returns the z value of this vector, if it exists.
	 * This will throw an ArrayOutOfBoundsException
	 * if less than matrix 3 height.
	 * 
	 * @return The Z coordinate value
	 */
	public double z() {
		return matrix[0][2];
	}
	
	/**
	 * Create a mutable independent copy of this vector.
	 */
	public Vector clone() {
		return new Vector(this.height, this.matrix[0]);
	}
	
	/**
	 * Fast inverse square root to find length of this vector over 1 (thanks Quake!)
	 */
	public double inverseLength() {
		return invSqrt(this.length2());
	}

	public static double invSqrt(double x) {
		double xhalf = 0.5d * x;
	    long i = Double.doubleToLongBits(x);
	    i = 0x5fe6ec85e7de30daL - (i >> 1);
	    x = Double.longBitsToDouble(i);
	    x *= (1.5d - xhalf * x * x);
	    return x;
	}
	
}
