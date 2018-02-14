package graphics.shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import graphics.Camera;
import graphics.Drawable;
import graphics.Face;
import graphics.World;
import util.math.Matrix;
import util.math.Vector;

public class RectPrism implements Drawable {
	
	private class VertexLocation {
		public static final int ULF = 0;
		public static final int URF = 1;
		public static final int LLF = 2;
		public static final int LRF = 3;
		public static final int ULB = 4;
		public static final int URB = 5;
		public static final int LLB = 6;
		public static final int LRB = 7;
	}
	
	//private static int[] edgePathStart = {0, 1, 3, 2, 0, 1, 2, 3, 4, 5, 7, 6};
	//private static int[] edgePathEnds = {1, 3, 2, 0, 4, 5, 6, 7, 5, 7, 6, 4};

	public Vector[] vertices;
	public Vector[] resultVectors;
	private Vector[] pVectors;
	private Vector tempVector = new Vector(3);
	
	private static int[][] facePaths = {{0, 1, 3, 2}, {6, 7, 5, 4}, {0, 2, 6, 4}, {5, 7, 3, 1}, {4, 5, 1, 0}, {2, 3, 7, 6}};
	private Face[] faces = new Face[6];
	
	public double width, height, depth;
	public Vector position;
	
	/**
	 * Creates a rectangular with the given dimensions.
	 * The given coordinate is the center of the cube.
	 *  
	 * @param x X of center of cube
	 * @param y X of center of cube
	 * @param z X of center of cube
	 * @param dX Width
	 * @param dY Height
	 * @param dZ Depth
	 */
	public RectPrism(double x, double y, double z, double dX, double dY, double dZ) {
		this.position = new Vector(3, x, y, z);
		
		this.vertices = new Vector[8];
		
		/*
		this.vertices[VertexLocation.ULF] = new Vector(3, -dX/2, dY/2, -dZ/2);
		this.vertices[VertexLocation.URF] = new Vector(3, dX/2, dY/2, -dZ/2);
		this.vertices[VertexLocation.LLF] = new Vector(3, -dX/2, -dY/2, -dZ/2);
		this.vertices[VertexLocation.LRF] = new Vector(3, dX/2, -dY/2, -dZ/2);
		this.vertices[VertexLocation.ULB] = new Vector(3, -dX/2, dY/2, dZ/2);
		this.vertices[VertexLocation.URB] = new Vector(3, dX/2, dY/2, dZ/2);
		this.vertices[VertexLocation.LLB] = new Vector(3, -dX/2, -dY/2, dZ/2);
		this.vertices[VertexLocation.LRB] = new Vector(3, dX/2, -dY/2, dZ/2);
		*/ 
		
		this.vertices[VertexLocation.ULF] = new Vector(3, -dX, dY/2, -dZ/2);
		this.vertices[VertexLocation.URF] = new Vector(3, dX, dY/2, -dZ/2);
		this.vertices[VertexLocation.LLF] = new Vector(3, -dX/2, -dY/2, -dZ);
		this.vertices[VertexLocation.LRF] = new Vector(3, dX/2, -dY/2, -dZ);
		this.vertices[VertexLocation.ULB] = new Vector(3, -dX, dY/2, dZ/2);
		this.vertices[VertexLocation.URB] = new Vector(3, dX, dY/2, dZ/2);
		this.vertices[VertexLocation.LLB] = new Vector(3, -dX/2, -dY/2, dZ);
		this.vertices[VertexLocation.LRB] = new Vector(3, dX/2, -dY/2, dZ);
		
		
		this.width = dX;
		this.height = dY;
		this.depth = dZ;
			
		pVectors = new Vector[8];
		resultVectors = new Vector[8];
		for (int i = 0; i < 8; i++) {
			pVectors[i] = new Vector(2);
			resultVectors[i] = new Vector(3);
		}
		
		Vector[] vertexes = new Vector[4];
		for (int i = 0; i < faces.length; i++) {
			int j = 0;
			for (int vertex : facePaths[i]) {
				vertexes[j] = this.vertices[vertex];
				j++;
			}
			
			this.faces[i] = new Face(this.position, vertexes);
		}
	
	}
	
	/**
	 * Creates a rectangular prism with the given verticies
	 * 
	 * @param ULF Vertex in the Upper Front Left corner
	 * @param LRB Vertex in the Lower Back Right corner
	 */
	public RectPrism(Vector ULF, Vector LRB) {
		this(ULF.matrix[0][0], 
				ULF.matrix[0][1], 
				ULF.matrix[0][2], 
				LRB.matrix[0][0] - ULF.matrix[0][0], 
				ULF.matrix[0][1] - LRB.matrix[0][1],
				LRB.matrix[0][2] - ULF.matrix[0][2]);
	}

	@Override
	public void draw(Graphics2D ctx) {
				
		for (Face face : faces) {
			float lightLevel = (float)face.getLightLevel(World.LIGHT_SOURCE);
			lightLevel *= 0.5;
			lightLevel += 0.5;
			if (lightLevel < 0.1) {
				lightLevel = 0.1f;
			}
			
			ctx.setPaint(Color.getHSBColor(0.65f, 1f, lightLevel));
			if (face.isFacingCamera()) {
				this.tempVector.set(this.position);
				face.draw(ctx, this.tempVector);
			}
		}

	}

	@Override
	public boolean inCameraWindow() {		
		double[][] r = Camera.CAM_ROTATION().matrix;
		double rX = r[0][0];
		double rY = r[0][1];
		double rZ = r[0][2];
		Matrix rotMatrix = Camera.ALL_ROTATION_MATRIX(rX, rY, rZ);
						
		boolean inFrame = false;
		
		for (int i = 0; i < 8; i++) {
			tempVector.set(this.vertices[i]);
			tempVector.add(this.position);
			Matrix.productTranslate(tempVector, rotMatrix, Camera.CAM_POSITION, resultVectors[i]);
			Camera.projectVector(resultVectors[i], pVectors[i]);
			
			if (!inFrame && resultVectors[i].matrix[0][2] > 0 && Camera.inFrame(pVectors[i])) {
				inFrame = true;
			}
		}
		
		return inFrame;
	}

	@Override
	public int order() {
		return (int)resultVectors[0].matrix[0][2];
	}

	@Override
	public boolean raytrace(Vector source, Vector destination) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void translate(double dX, double dY, double dZ) {
		this.position.matrix[0][0] += dX;
		this.position.matrix[0][0] += dY;
		this.position.matrix[0][0] += dZ;
	}
	
	public void rotateX(double angle) {
		Matrix rot = Camera.X_ROTATION_MATRIX(angle);
		for (Vector vertex : vertices) {
			vertex.set(Matrix.product(vertex, rot));
		}
		for (Face face : faces) {
			face.plane.normal.set(Matrix.product(face.plane.normal, rot));
			face.center.set(Matrix.product(face.center, rot));
		}
	}
	
	public void rotateY(double angle) {
		Matrix rot = Camera.Y_ROTATION_MATRIX(angle);
		for (Vector vertex : vertices) {
			vertex.set(Matrix.product(vertex, rot));
		}
		for (Face face : faces) {
			face.plane.normal.set(Matrix.product(face.plane.normal, rot));
			face.center.set(Matrix.product(face.center, rot));
		}
	}
	
	public void rotateZ(double angle) {
		Matrix rot = Camera.Z_ROTATION_MATRIX(angle);
		for (Vector vertex : vertices) {
			vertex.set(Matrix.product(vertex, rot));
		}
		for (Face face : faces) {
			face.plane.normal.set(Matrix.product(face.plane.normal, rot));
			face.center.set(Matrix.product(face.center, rot));
		}
	}

}
