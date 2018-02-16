package util.math;

public class Quaternion extends Matrix {
	
	public Vector translation, rotation;
	
	public double x() {
		return translation.matrix[0][0];
	}
	public double y() {
		return translation.matrix[0][1];
	}
	public double z() {
		return translation.matrix[0][2];
	}
	
	public double rX() {
		return rotation.matrix[0][0];
	}
	public double rY() {
		return rotation.matrix[0][1];
	}
	public double rZ() {
		return rotation.matrix[0][2];
	}
	
	/**
	 * Creates a Quaternion with the given translation and rotation.
	 * Angles in radians.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @param rX Rotation around X axis
	 * @param rY Rotation around Y axis
	 * @param rZ Rotation around Z axis
	 */
	public Quaternion(double x, double y, double z, double rX, double rY, double rZ) {
		this(new Vector(3, x, y, z), new Vector(4, rX, rY, rZ, 1));
	}
	
	/**
	 * Creates a quaternion with references to the given Vectors.
	 * As the vector values change, so does this quaternion.
	 * 
	 * @param translation
	 * @param rotation
	 */
	public Quaternion(Vector translation, Vector rotation) {
		super(4, 4);
		this.rotation = rotation;
		this.translation = translation;
		recompute();
	}
	
	public void recompute() {
		double rX = this.rotation.matrix[0][0];
		double rY = this.rotation.matrix[0][1];
		double rZ = this.rotation.matrix[0][2];
		
		matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		matrix[1][0] = -Math.cos(rX)*Math.sin(rZ);
		matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		matrix[1][1] = Math.cos(rX)*Math.cos(rZ);
		matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
		matrix[0][2] = -Math.cos(rX) * Math.sin(rY);
		matrix[1][2] = Math.sin(rX);
		matrix[2][2] = Math.cos(rX)*Math.cos(rY);
		
		matrix[3][0] = this.translation.matrix[0][0];
		matrix[3][1] = this.translation.matrix[0][1];
		matrix[3][2] = this.translation.matrix[0][2];
	}

}
