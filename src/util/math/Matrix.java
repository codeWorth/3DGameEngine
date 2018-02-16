package util.math;

public class Matrix {
		
	public int width;
	public int height;
	
	/**
	 * Values stored within this matrix. First index is x position, second index is y position.
	 */
	public double[][] matrix;
	
	/**
	 * Makes a square matrix from the given values, if possible.
	 * Throws an error if a square matrix cannot be made.
	 * 
	 * @param values Numbers to put into the matrix, going right first.
	 */
	public Matrix(double... values) {
		
		this((int)Math.sqrt(values.length), (int)Math.sqrt(values.length), values);
		
	}
	
	/**
	 * Make a matrix with the given width and height, and put the given
	 * numbers into it. Throws an error if the wrong number of values is given
	 * for the matrix size.
	 * 
	 * @param width Wanted width, greater than 0.
	 * @param height Wanted height, greater than 0.
	 * @param values Numbers to be inserted, going right first. For example, if given values = 1, 2, 3, 4 for a 2 by 2 matrix, 
	 * matrix would look like <pre>
	 * 	1	2
	 * 	3	4 </pre>
	 */
	public Matrix(int width, int height, double... values) {
		
		if (values.length != width*height) {
			throw new IllegalArgumentException();
		}
		
		this.width = width;
		this.height = height;
		this.matrix = new double[this.width][this.height];
		
		for (int n = 0; n < values.length; n++) {
			int j = n/this.width;
			int i = n - j*this.width;
			matrix[i][j] = values[n];
		}
		
	}
	
	/**
	 * Make an empty array with given width and height.
	 * 
	 * @param width Wanted width, greater than 0
	 * @param height Wanted height, greater than 0
	 */
	public Matrix(int width, int height) {
		
		this.width = width;
		this.height = height;
		this.matrix = new double[this.width][this.height];
		
	}
	
	/**
	 * Set this matrix's values to another matrix's values.
	 * The matricies will remain independent after this operation.
	 * Will resize this matrix if neccisary
	 * 
	 * @param otherMatrix The other matrix
	 */
	public void set(Matrix otherMatrix) {
		
		this.width = otherMatrix.width;
		this.height = otherMatrix.height;
		this.matrix = new double[width][height];
		for (int n = 0; n < width*height; n++) {
			int i = n/this.height;
			int j = n - i*this.height;
			matrix[i][j] = otherMatrix.matrix[i][j];
		}
		
	}
	
	/**
	 * Add a scalar value to each element of this Matrix.
	 * 
	 * @param value The scalar value
	 */
	public void add(double value) {
		
		for (int n = 0; n < this.width*this.height; n++) {
			int i = n/this.height;
			int j = n - i*this.height;
			matrix[i][j] += value;
		}
		
	}
	
	/**
	 * Multiply each element of this Matrix by a scalar value.
	 * 
	 * @param value The scalar value
	 */
	public void multiply(double value) {
		
		for (int n = 0; n < this.width*this.height; n++) {
			int i = n/this.height;
			int j = n - i*this.height;
			matrix[i][j] *= value;
		}
		
	}
	
	/**
	 * Divide each element of this Matrix by a scalar value.
	 * 
	 * @param value The scalar value
	 */
	public void divide(double value) {
		
		for (int n = 0; n < this.width*this.height; n++) {
			int i = n/this.height;
			int j = n - i*this.height;
			matrix[i][j] /= value;
		}
		
	}
	
	/**
	 * Adds values in this matrix to values from another.
	 * If matricies are different sizes, adds as if they are the same size
	 * as the smallest one. Modifies this matrix, not other one.
	 * 
	 * @param other The other matrix to add to
	 */
	public void add(Matrix other) {
		
		int width = Math.min(this.width, other.width);
		int height = Math.min(this.height, other.height);
		for (int n = 0; n < width*height; n++) {
			int i = n/height;
			int j = n - i*height;
			matrix[i][j] += other.matrix[i][j];
		}
		
	}
	
	/**
	 * Subtracts values in this matrix from values from another.
	 * If matricies are different sizes, subtracts as if they are the same size
	 * as the smallest one. Modifies this matrix, not other one.
	 * 
	 * @param other The other matrix to subtract from this one
	 */
	public void subtract(Matrix other) {
		
		int width = Math.min(this.width, other.width);
		int height = Math.min(this.height, other.height);
		for (int n = 0; n < width*height; n++) {
			int i = n/height;
			int j = n - i*height;
			matrix[i][j] -= other.matrix[i][j];
		}
		
	}
	
	/**
	 * Multiplies values from this matrix to values from another.
	 * If matricies are different sizes, multiplies as if they are the same size
	 * as the smallest one. Modifies this matrix, not other one.
	 * 
	 * @param other The other matrix to multiply by
	 */
	public void multiply(Matrix other) {
		
		int width = Math.min(this.width, other.width);
		int height = Math.min(this.height, other.height);
		for (int n = 0; n < width*height; n++) {
			int i = n/height;
			int j = n - i*height;
			matrix[i][j] *= other.matrix[i][j];
		}
		
	}
	
	/**
	 * Divides values from this matrix to values from another.
	 * If matricies are different sizes, divides as if they are the same size
	 * as the smallest one. Modifies this matrix, not other one.
	 * 
	 * @param other The other matrix to divide by
	 */
	public void divide(Matrix other) {
		
		int width = Math.min(this.width, other.width);
		int height = Math.min(this.height, other.height);
		for (int n = 0; n < width*height; n++) {
			int i = n/height;
			int j = n - i*height;
			matrix[i][j] /= other.matrix[i][j];
		}
		
	}
	
	/**
	 * Multiplies a matrix by a scalar, then adds that matrix to this one.
	 * Does not modify the passed in vector.
	 * 
	 * @param other Matrix that will be added
	 * @param scalar Scalar to multiply vector by
	 */
	public void addMult(Matrix other, double scalar) {
		
		int width = Math.min(this.width, other.width);
		int height = Math.min(this.height, other.height);
		for (int n = 0; n < width*height; n++) {
			int i = n/height;
			int j = n - i*height;
			matrix[i][j] += other.matrix[i][j] * scalar;
		}
		
	}
	
	/**
	 * Cross multiplies first by other.
	 * Puts the result into the result matrix.
	 * Assumes that result is the correct size.
	 * Order matters.
	 * 
	 * @param first First matrix to multiply
	 * @param other Second matrix to multiply
	 * @param result Matrix that result will be sent to. Matrix's current values will be overridden
	 */
	public static void product(Matrix first, Matrix other, Matrix result) {
		if (first.height != other.width) {
			throw new IllegalArgumentException();
		}
		
		for (int n = 0; n < first.width*other.height; n++) {
			int i = n/other.height;
			int j = n - i*other.height;
			
			double sum = 0;
			for (int k = 0; k < first.height; k++) {
				sum += first.matrix[i][k] * other.matrix[k][j];
			}
			result.matrix[i][j] = sum;
		}
	}
	
	/**
	 * Cross multiplies first by other.
	 * Order matters.
	 * 
	 * @param first First matrix the multiply
	 * @param other Other matrix to multiply
	 * @return A new matrix that is the cross product of first and other
	 */
	public static Matrix product(Matrix first, Matrix other) {
		Matrix newMatrix = new Matrix(first.width, other.height);
		product(first, other, newMatrix);
		return newMatrix;
	}
	
	/**
	 * Adds translate vector to first, then 
	 * cross multiplies by other. Order matters.
	 * Doesn't modify first, other, or translate.
	 * 
	 * @param first First matrix the multiply
	 * @param other Other matrix the multiply
	 * @param translate Translation vector
	 * @param result Matrix that result will be sent to. Matrix's current values will be overridden
	 */
	public static void productTranslate(Matrix first, Matrix other, Vector translate, Matrix result) {
		if (first.height != other.width) {
			throw new IllegalArgumentException();
		}
		
		for (int n = 0; n < first.width*other.height; n++) {
			int i = n/other.height;
			int j = n - i*other.height;
			
			double sum = 0;
			for (int k = 0; k < first.height; k++) {
				sum += (first.matrix[i][k] - translate.matrix[0][k]) * other.matrix[k][j];
			}
			result.matrix[i][j] = sum;
		}
	}
	
	/**
	 * Switches this matrix's width and height
	 */
	public void flip() {
		
		double[][] newMatrix = new double[height][width];
		for (int n = 0; n < this.width*this.height; n++) {
			int j = n/this.width;
			int i = n - j*this.width;
			
			newMatrix[j][i] = matrix[i][j];
		}
		
		this.width = this.height;
		this.height = this.matrix.length;
		this.matrix = newMatrix;
	}
	
	/**
	 * Makes this matrix 1 tall
	 */
	public void flatten() {
		resize(this.width * this.height, 1);
	}
	
	/**
	 * Makes this matrix 1 wide
	 */
	public void squeeze() {
		resize(1, this.width * this.height);
	}
	
	/**
	 * Changes the shape of this matrix, moving the values inside as neccisary.
	 * Total matrix capacity must remain unchanged.
	 * 
	 * @param width New width.
	 * @param height New height
	 */
	public void resize(int newWidth, int newHeight) {
		
		if (newWidth*newHeight != this.width*this.height) {
			throw new IllegalArgumentException();
		}
		
		double[][] newMatrix = new double[newWidth][newHeight];
		for (int n = 0; n < this.width*this.height; n++) {
			int j = n/this.width;
			int i = n - j*this.width;
			
			int nJ = n/newWidth;
			int nI = n - nJ*newWidth;
			
			newMatrix[nI][nJ] = matrix[i][j];
		}
		
		this.width = newWidth;
		this.height = newHeight;
		this.matrix = newMatrix;
		
	}
	
	/**
	 * Set all all items to 0
	 */
	public void zero() {
		for (int n = 0; n < width*height; n++) {
			int j = n/this.width;
			int i = n - j*this.width;
			matrix[i][j] = 0;
		}
	}
	
	/**
	 * Print a nicely formatted version of this Matrix
	 */
	public void print() {
				
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println("");
		}
		
		System.out.println("-----------");
		
	}

}
