package graphics;

import util.math.Matrix;
import util.math.Vector;

public class Camera {
		
	/**
	 * In world coordinates
	 */
	public static Vector CAM_POSITION = new Vector(3, 0, 0, 0);
	
	/**
	 * In radians
	 */
	private static Vector kCAM_ROTATION = new Vector(3, 0, 0, 0);
	public static Vector CAM_ROTATION() {
		return kCAM_ROTATION;
	}
	public static void setRX(double rX) {
		kCAM_ROTATION.matrix[0][0] = rX;
		double rY = kCAM_ROTATION.matrix[0][1];
		double rZ = kCAM_ROTATION.matrix[0][2];
		
		ALL_ROTATION_MATRIX.matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[1][0] = -Math.cos(rX)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[1][1] = Math.cos(rX)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[0][2] = -Math.cos(rX) * Math.sin(rY);
		ALL_ROTATION_MATRIX.matrix[1][2] = Math.sin(rX);
		ALL_ROTATION_MATRIX.matrix[2][2] = Math.cos(rX)*Math.cos(rY);
	}
	public static void setRY(double rY) {
		kCAM_ROTATION.matrix[0][1] = rY;
		double rX = kCAM_ROTATION.matrix[0][0];
		double rZ = kCAM_ROTATION.matrix[0][2];
		
		ALL_ROTATION_MATRIX.matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[0][2] = -Math.cos(rX) * Math.sin(rY);
		ALL_ROTATION_MATRIX.matrix[2][2] = Math.cos(rX)*Math.cos(rY);
	}
	public static void setRZ(double rZ) {
		kCAM_ROTATION.matrix[0][2] = rZ;
		double rX = kCAM_ROTATION.matrix[0][0];
		double rY = kCAM_ROTATION.matrix[0][1];
		
		ALL_ROTATION_MATRIX.matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[1][0] = -Math.cos(rX)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[1][1] = Math.cos(rX)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
	}
	public static void setRXRY(double rX, double rY) {
		kCAM_ROTATION.matrix[0][0] = rX;
		kCAM_ROTATION.matrix[0][1] = rY;
		double rZ = kCAM_ROTATION.matrix[0][2];
		
		ALL_ROTATION_MATRIX.matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[1][0] = -Math.cos(rX)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[1][1] = Math.cos(rX)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[0][2] = -Math.cos(rX) * Math.sin(rY);
		ALL_ROTATION_MATRIX.matrix[1][2] = Math.sin(rX);
		ALL_ROTATION_MATRIX.matrix[2][2] = Math.cos(rX)*Math.cos(rY);
	}
	
	private static Matrix rX = new Matrix(3, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0);
	private static Matrix rY = new Matrix(3, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0);
	private static Matrix rZ = new Matrix(3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1);
	
	public static Matrix X_ROTATION_MATRIX(double radians) {
		rX.matrix[1][1] = Math.cos(radians);
		rX.matrix[2][1] = -Math.sin(radians);
		rX.matrix[1][2] = Math.sin(radians);
		rX.matrix[2][2] = Math.cos(radians);
		return rX;
	}
	public static Matrix Y_ROTATION_MATRIX(double radians) {
		rY.matrix[0][0] = Math.cos(radians);
		rY.matrix[2][0] = Math.sin(radians);
		rY.matrix[0][2] = -Math.sin(radians);
		rY.matrix[2][2] = Math.cos(radians);
		return rY;
	}
	public static Matrix Z_ROTATION_MATRIX(double radians) {
		rZ.matrix[0][0] = Math.cos(radians);
		rZ.matrix[1][0] = -Math.sin(radians);
		rZ.matrix[0][1] = Math.sin(radians);
		rZ.matrix[1][1] = Math.cos(radians);
		return rZ;
	}
	
	// NOTE TO SELF: this matrix is different than the normal one because the order of multiplication is
	// R_Y * R_Z * R_X. The important part is that R_Y is happening before R_Z and R_X.
	// This matters because otherwise looking up also tilts the screen (around the Z axis).
	public static Matrix ALL_ROTATION_MATRIX = new Matrix(3, 3);
	/*
	private static void ALL_ROTATION_MATRIX(double rX, double rY, double rZ) {
		ALL_ROTATION_MATRIX.matrix[0][0] = Math.cos(rY)*Math.cos(rZ) - Math.sin(rX)*Math.sin(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[1][0] = -Math.cos(rX)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[2][0] = Math.sin(rY)*Math.cos(rZ) + Math.sin(rX)*Math.cos(rY)*Math.sin(rZ);
		ALL_ROTATION_MATRIX.matrix[0][1] = Math.cos(rY)*Math.sin(rZ) + Math.sin(rX)*Math.sin(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[1][1] = Math.cos(rX)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[2][1] = Math.sin(rY)*Math.sin(rZ) - Math.sin(rX)*Math.cos(rY)*Math.cos(rZ);
		ALL_ROTATION_MATRIX.matrix[0][2] = -Math.cos(rX) * Math.sin(rY);
		ALL_ROTATION_MATRIX.matrix[1][2] = Math.sin(rX);
		ALL_ROTATION_MATRIX.matrix[2][2] = Math.cos(rX)*Math.cos(rY);
	} */
	
	/**
	 * Project a 3D vector onto the screen's 2d plane.
	 * Projection is put into given 2D vector.
	 * Camera distance is given by {@link #CAM_DISTANCE}
	 * 
	 * @param vector 3D to project
	 * @param vector2d 2D vector to be modified
	 */
	public static void projectVector(Matrix vector, Vector vector2d) {
		double t = CAM_DISTANCE / vector.matrix[0][2];
		double x = t * vector.matrix[0][0];
		double y = t * vector.matrix[0][1];
		
		vector2d.matrix[0][0] = x + CAM_WIDTH/2;
		vector2d.matrix[0][1] = -y + CAM_HEIGHT/2;
	}
	
	public static boolean inFrame(Vector vector) {
		return vector.matrix[0][0] > 0 && vector.matrix[0][0] < CAM_WIDTH && vector.matrix[0][1] > 0 && vector.matrix[0][1] < CAM_HEIGHT;
	}
	
	/**
	 * In screen coordinates
	 */
	public static int CAM_WIDTH = 1200;
	/**
	 * In screen coordinates
	 */
	public static int CAM_HEIGHT = 700;
	
	public static double FOV = 100.0 / 180.0 * Math.PI;
	public static double CAM_DISTANCE = Math.tan(FOV/2) * CAM_WIDTH/2;	

	
}
