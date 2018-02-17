package graphics;

import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import graphics.shapes.RectPrism;
import physics.PhysicsObject;
import physics.Timeout;
import util.math.Vector;

public class World {

	public static long FPS = 0;
	public static long writeTime = 0;
	public static long sortTime = 0;
	public static long mathTime = 0;
	public static long totalDrawTime = 0;
	
	public static double t= 0;
	
	public static Vector LIGHT_SOURCE = new Vector(3, 0, 0, 200);
	public static RectPrism MOVEY_BOI = new RectPrism(0, 600, 1500, 600, 600, 600);
	
	public static Robot robot;
		
	public static Timer timer;
	public static int timerDelay = 100;
	
	private static ArrayList<Drawable> visuals = new ArrayList<Drawable>();
	private static ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>(); 
	private static ArrayList<Timeout> timeouts = new ArrayList<Timeout>();
	
	private static ArrayList<PhysicsObject> objectsToRemove = new ArrayList<PhysicsObject>();
	private static ArrayList<Drawable> visualsToRemove = new ArrayList<Drawable>();
	private static ArrayList<Timeout> timeoutsToRemove = new ArrayList<Timeout>();
	
	public static void initialize() {	
				
		add(MOVEY_BOI);
		
		add(new Timeout() {
			int ticks = 0;
			
			@Override
			public long timeLeft() {
				return 1;
			}

			@Override
			public void timeTick() {
				ticks++;
				if (ticks == 5) {
					ticks = 0;
					
					double total = (sortTime + mathTime + totalDrawTime + writeTime);
					
					System.out.println("Sort: " + String.valueOf(sortTime / total * 100) + "%");
					System.out.println("Math: " + String.valueOf(mathTime / total * 100) + "%");
					System.out.println("Total Draw: " + String.valueOf(totalDrawTime / total * 100) + "%");
					System.out.println("Write: " + String.valueOf(writeTime / total * 100) + "%");
					
					mathTime = 0;
					sortTime = 0;
					totalDrawTime = 0;
					writeTime = 0;
					
				}
			}
		});
		
		int dudes = 10;
		
		for (int i = -dudes; i < dudes; i++) {
			for (int j = -dudes; j < dudes; j++) {
				RectPrism boi = new RectPrism(i * 400 + 200, -400, j * 300 + 200, 200, 300, 200);
				boi.color = (float)((i + dudes) * (j + dudes) / (dudes*dudes*4.0));
				add(boi);
			}
		}

		add(new Character());
		add(new FrameCounter());
		
		timer = new Timer(timerDelay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				int size = timeouts.size();
				for (int i = size - 1; i >= 0; i--) {
					Timeout out = timeouts.get(i);
					out.timeTick();
					
					if (out.timeLeft() <= 0) {
						remove(out);
					}
				}
				
				size = timeoutsToRemove.size();
				for (int i = size - 1; i >= 0; i--) {
					Object toRemoveObj = timeoutsToRemove.get(i);
					timeouts.remove(toRemoveObj);
					timeoutsToRemove.remove(i);
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
	
	public static void graphicsUpdate() {
		int size = visuals.size();
		
		t += 0.1;
		
		MOVEY_BOI.rotateX(0.05);
		MOVEY_BOI.rotateY(0.05);
		MOVEY_BOI.translate(3, -10*Math.cos(t), 3);
		
		//insertionSort(visuals, 0, size);
				
		for (int i = 0; i < size; i++) {
			Drawable draw = visuals.get(i);
			if ( draw.inCameraWindow() ) {
				draw.draw();
			}
		}
				
		size = visualsToRemove.size();
		for (int i = size - 1; i >= 0; i--) {
			Object toRemoveObj = visualsToRemove.get(i);
			visuals.remove(toRemoveObj);
			visualsToRemove.remove(i);
		}	
	}
	
	public static void physicsUpdate(double dT) {			
		int size = objects.size();
		
		for (int i = 0; i < size; i++) {
			PhysicsObject obj1 = objects.get(i);
			obj1.update(dT);
			
			for (int j = i + 1; j < size; j++) {
				collide(obj1, objects.get(j));
			}
		}
		
		size = objectsToRemove.size();
		for (int i = size - 1; i >= 0; i--) {
			Object toRemoveObj = objectsToRemove.get(i);
			objects.remove(toRemoveObj);
			objectsToRemove.remove(i);
		}
	}
	
	private static void collide(PhysicsObject obj1, PhysicsObject obj2) {
		/*if (obj1.shouldCollideWithObject(obj2)) {
			obj1.collideWithObject(obj2);
		}
		
		if (obj2.shouldCollideWithObject(obj1)) {
			obj2.collideWithObject(obj1);
		}*/
	}
	
	public static void remove(Object obj) {
		if (obj instanceof PhysicsObject) {
			objectsToRemove.add((PhysicsObject) obj);
		}
		if (obj instanceof Drawable) {
			visualsToRemove.add((Drawable) obj);
		}
		if (obj instanceof Timeout) {
			timeoutsToRemove.add((Timeout) obj);
		}
	}
	
	public static void add(Object obj) {
		if (obj instanceof PhysicsObject) {
			objects.add((PhysicsObject)obj);
		}
		if (obj instanceof Drawable) {
			visuals.add((Drawable)obj);
		}
		if (obj instanceof Timeout) {
			timeouts.add((Timeout)obj);
		}
	}
	
	private static void insertionSort(ArrayList<Drawable> array, int lower, int upper) {
		
		for (int i = lower + 1; i < upper; i++) {
			
			int j = i;
			while (j > 0 && array.get(j).order() > array.get(j - 1).order()) {
				swap(array, j, j - 1);
				j--;
			}
			
		}
		
	}

	
	private static void swap(ArrayList<Drawable> array, int index1, int index2) {
		Drawable obj = array.get(index1);
		array.set(index1, array.get(index2));
		array.set(index2, obj);
	}
	
}
