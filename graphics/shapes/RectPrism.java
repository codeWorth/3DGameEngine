package graphics.shapes;

import java.awt.Color;

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
	private boolean inFrame = false;
	private boolean needVectors = true;
	
	public float color = 0.5f;
	
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
		
		this.vertices[VertexLocation.ULF] = new Vector(3, -dX/2, dY/2, -dZ/2);
		this.vertices[VertexLocation.URF] = new Vector(3, dX/2, dY/2, -dZ/2);
		this.vertices[VertexLocation.LLF] = new Vector(3, -dX/2, -dY/2, -dZ/2);
		this.vertices[VertexLocation.LRF] = new Vector(3, dX/2, -dY/2, -dZ/2);
		this.vertices[VertexLocation.ULB] = new Vector(3, -dX/2, dY/2, dZ/2);
		this.vertices[VertexLocation.URB] = new Vector(3, dX/2, dY/2, dZ/2);
		this.vertices[VertexLocation.LLB] = new Vector(3, -dX/2, -dY/2, dZ/2);
		this.vertices[VertexLocation.LRB] = new Vector(3, dX/2, -dY/2, dZ/2);
		
		this.width = dX;
		this.height = dY;
		this.depth = dZ;
			
		pVectors = new Vector[8];
		resultVectors = new Vector[8];
		for (int i = 0; i < 8; i++) {
			pVectors[i] = new Vector(2);
			resultVectors[i] = new Vector(3);
		}
		
		int[][] triPaths = {{0, 1, 2}, {0, 3, 2}};
		for (int i = 0; i < faces.length; i++) {
						
			Vector[] vertexes = new Vector[4];
			Vector[] pVectorReferences = new Vector[4];
			
			int j = 0;
			for (int vertex : facePaths[i]) {
				vertexes[j] = this.vertices[vertex].clone();
				pVectorReferences[j] = this.pVectors[vertex];
				j++;
			}
			
			this.faces[i] = new Face(this.position, vertexes, pVectorReferences, triPaths);
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
	
	/**
	 * Gets the projected 2D vectors, and puts it into
	 * this.pVectors. Returns whether this rectangular prism
	 * is within the frame. Also sets the inFrame boolean to true or false.
	 * Also sets needVectors to false, because the pVectors have been found.
	 * 
	 */
	private void getPVectors() {
		
		needVectors = false;
		double time = System.nanoTime();
		
		double[][] r = Camera.CAM_ROTATION().matrix;
		double rX = r[0][0];
		double rY = r[0][1];
		double rZ = r[0][2];
		Matrix rotMatrix = Camera.ALL_ROTATION_MATRIX(rX, rY, rZ);
						
		this.inFrame = false;
		
		for (int i = 0; i < 8; i++) {
			tempVector.set(this.vertices[i]);
			tempVector.add(this.position);
			Matrix.productTranslate(tempVector, rotMatrix, Camera.CAM_POSITION, resultVectors[i]);
			Camera.projectVector(resultVectors[i], pVectors[i]);
			
			if (!this.inFrame && resultVectors[i].matrix[0][2] > 0 && Camera.inFrame(pVectors[i])) {
				this.inFrame = true;
			}
		}
		
		World.mathTime += System.nanoTime() - time;
	}

	@Override
	public void draw() {
				
		if (needVectors) {
			getPVectors();
		}
		
		for (Face face : faces) {
			float lightLevel = (float)face.getLightLevel(World.LIGHT_SOURCE);
			lightLevel *= 0.5;
			lightLevel += 0.5;
			if (lightLevel < 0.1) {
				lightLevel = 0.1f;
			}
			
			Color color = Color.getHSBColor(this.color, 1f, lightLevel);
			if (face.isFacingCamera()) {
				face.draw(color.getRed(), color.getGreen(), color.getBlue());
			}
		}

		needVectors = true;
		
	}
	
	@Override
	public boolean inCameraWindow() {
		if (needVectors) {
			getPVectors();
		}
		if (!inFrame) {
			needVectors = true;
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
