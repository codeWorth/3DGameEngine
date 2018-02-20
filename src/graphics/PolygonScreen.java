package graphics;

import java.awt.Graphics;
import java.util.ArrayList;

import util.math.Vector;

public class PolygonScreen {
	
	private static final int startSize = 200;
	
	private ArrayList<Vector[]> polygons = new ArrayList<>(startSize);
	private ArrayList<int[]> drawColor = new ArrayList<>(startSize);
	private ArrayList<int[]> polygonBoundingBox = new ArrayList<>(startSize);
	private ArrayList<Integer> polyOrder = new ArrayList<>(startSize);
	
	private int width, height, edges;
	private int[] raster;
	
	public PolygonScreen(int width, int height, int[] raster) {
		this.width = width;
		this.height = height;
		this.raster = raster;
	}
	
	private void clear() {
		int clear = 0;
		clear |= 0 << Surface.rShiftBits;
		clear |= 0 << Surface.gShiftBits;
		clear |= 0 << Surface.bShiftBits;
		clear |= 255 << Surface.aShiftBits;

		for (int i = 0; i < raster.length; i++) {
			raster[i] = clear;
		}
		
		polygons.clear();
		drawColor.clear();
		polygonBoundingBox.clear();
		polyOrder.clear();
		
		edges = 0;
	}
	
	public void write(int x, int y, int r, int g, int b) {
		int rgb = 0;
		rgb = rgb | r << Surface.rShiftBits;
		rgb = rgb | g << Surface.gShiftBits;
		rgb = rgb | b << Surface.bShiftBits;
		rgb = rgb | 255 << Surface.aShiftBits;
		
		int location = y * width + x;
		raster[location] = rgb;
	}
	
	public void addPolygon(Vector[] vertices, int[] color, int zOrder) {
		edges += vertices.length;
		
		int minY = 10000, maxY = -1;
		
		for (Vector vertex : vertices) {
			int y = (int)vertex.y();
			
			if (y < minY) {
				minY = y;
			} else if (y > maxY) {
				maxY = y;
			}
		}
		
		int[] dimensions = {minY, maxY};
		
		polygons.add(vertices);
		drawColor.add(color);
		polygonBoundingBox.add(dimensions);
		polyOrder.add(zOrder);
	}
	
	public void draw(Graphics g) {		
		boolean[] inPolygon = new boolean[polygons.size()];
		quicksortPolygons(0, inPolygon.length - 1);
		
		int interceptCount = 0;
		int[][] intercepts = new int[edges][3]; // 0 = x, 1 = polygon index
		
		for (int y = 0; y < height; y++) {
			
			interceptCount = 0;

			for (int n = 0; n < inPolygon.length; n++) {
				Vector[] polygon = polygons.get(n);
				int[] dimensions = polygonBoundingBox.get(n);
				int minY = dimensions[0];
				int maxY = dimensions[1];
				
				if (minY < y && maxY > y) {
					
					for (int j1 = 0; j1 < polygon.length; j1++) {
						int j2 = j1 - 1;
						if (j1 == 0) {
							j2 = polygon.length;
						}
						
						double x1 = polygon[j1].x();
						double y1 = polygon[j1].y();
						double dX = polygon[j2].x() - x1;
						double dY = polygon[j2].y() - y1;
						
						// y = dY * t + y1
						// t = (y - y1) / dY
						
						if (dY != 0)
						{
							double t = (y - y1) / dY;
							if (t >= 0 && t < 1) {
								double interceptX = dX * t + x1;
								
								intercepts[interceptCount][0] = (int)interceptX;
								intercepts[interceptCount][1] = n;
								interceptCount++;
							}
						
						}
						
					}
					
				}
				
				
			}
			
			double time = System.nanoTime();
			quicksort(intercepts, 0, intercepts.length - 1);
			World.sortTime += System.nanoTime() - time;
			
			for (int i = 0; i < inPolygon.length; i++) {
				inPolygon[i] = false;
			}
			
			int prevX = intercepts[0][0];
			int polyIndex = intercepts[0][1];
			inPolygon[polyIndex] = !inPolygon[polyIndex];
			
			for (int i = 1; i < intercepts.length - 1; i++) {
				int endX = intercepts[i][0];
				polyIndex = intercepts[i][1];
				inPolygon[polyIndex] = !inPolygon[polyIndex];
				
				int n = inPolygon.length - 1;
				while (n >= 0 && !inPolygon[n]) {
					n--;
				}
				if (n >= 0) {
					int[] clr = drawColor.get(n);
					for (int x = prevX; x < endX; x++) {
						this.write(x, y, clr[0], clr[1], clr[2]);
					}
				}
				
				prevX = endX;
			}
			
		}
		
		clear();
	}
	
	private void quicksortPolygons(int low, int high) {
		int i = low;
		int j = high;
		int pivot = polyOrder.get(low + (high-low)/2);

		while (i <= j) {
			while (polyOrder.get(i) < pivot) {
				i++;
			}

			while (polyOrder.get(j) > pivot) {
				j--;
			}

			if (i <= j) {
				
				Vector[] polygonA = polygons.get(i);
				int[] colorA = drawColor.get(i);
				int[] boxA = polygonBoundingBox.get(i);
				int zA = polyOrder.get(i);
				
				polygons.set(i, polygons.get(j));
				drawColor.set(i, drawColor.get(j));
				polygonBoundingBox.set(i, polygonBoundingBox.get(j));
				polyOrder.set(i, polyOrder.get(j));
				
				polygons.set(j, polygonA);
				drawColor.set(j, colorA);
				polygonBoundingBox.set(j, boxA);
				polyOrder.set(j, zA);

				i++;
				j--;
			}
		}

		if (low < j) {
			quicksortPolygons(low, j);
		}
		if (i < high) {
			quicksortPolygons(i, high);
		}
	}

	private void quicksort(int[][] intercepts, int low, int high) {
		int i = low;
		int j = high;
		int pivot = intercepts[low + (high-low)/2][0];

		while (i <= j) {
			while (intercepts[i][0] < pivot) {
				i++;
			}

			while (intercepts[j][0] > pivot) {
				j--;
			}

			if (i <= j) {
				swap(intercepts, i, j);
				i++;
				j--;
			}
		}

		if (low < j) {
			quicksort(intercepts, low, j);
		}
		if (i < high) {
			quicksort(intercepts, i, high);
		}
	}
	
	private void swap(int[][] array, int i1, int i2) {
		int[] dah = array[i1];
		array[i1] = array[i2];
		array[i2] = dah;
	}
	
}

