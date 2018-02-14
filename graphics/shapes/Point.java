package graphics.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import graphics.Camera;
import graphics.Drawable;
import util.math.Matrix;
import util.math.Vector;

public class Point implements Drawable {
	
	public Vector position;
	private Matrix _position =  new Vector(3);
	private Matrix result = new Vector(3);
	private Vector projection = new Vector(2);
	private double radius = 15;
	
	public Point(double x, double y, double z) {
		position = new Vector(3, x, y, z);
	}

	@Override
	public void draw(Graphics2D ctx) {
		Camera.projectVector(result, projection);
		
		double x = projection.matrix[0][0];
		double y = projection.matrix[0][1];
		
		ctx.setPaint(Color.YELLOW);
		Ellipse2D.Double outline = new Ellipse2D.Double(x - radius, y - radius, radius*2, radius*2);
		ctx.fill(outline);
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
