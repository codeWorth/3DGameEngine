package graphics.shapes;

import graphics.Camera;
import graphics.Drawable;
import graphics.Surface;
import util.math.Matrix;
import util.math.Vector;

public class Point implements Drawable {
	
	public Vector position;
	private Matrix _position =  new Vector(3);
	private Matrix result = new Vector(3);
	private Vector projection = new Vector(2);
	private int radius = 15;
	
	public Point(double x, double y, double z) {
		position = new Vector(3, x, y, z);
	}

	@Override
	public void draw() {
		Camera.projectVector(result, projection);
		
		int x = (int) projection.matrix[0][0];
		int y = (int) projection.matrix[0][1];
		
		for (int i = x - radius; i < x + radius; i++) {
			int height = (int) Math.sqrt(radius*radius - (i - x)*(i - x));
			for (int j = y - height; j < y + height; j++) {
				Surface.setColor(i, j, 255, 255, 0);
			}
		}
	}

	@Override
	public boolean inCameraWindow() {
		_position.set(position);
		_position.subtract(Camera.CAM_POSITION);
		
		double[][] r = Camera.CAM_ROTATION().matrix;
		double rX = r[0][0];
		double rY = r[0][1];
		double rZ = r[0][2];
		Matrix.product(_position, Camera.ALL_ROTATION_MATRIX(rX, rY, rZ), result);
		
		return result.matrix[0][2] >= 0;
	}

	@Override
	public int order() {
		return (int)result.matrix[0][2];//return 0;
	}

	@Override
	public boolean raytrace(Vector source, Vector destination) {
		return false;
	}

}
