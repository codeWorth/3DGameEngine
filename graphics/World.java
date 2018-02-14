package graphics;

import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import graphics.shapes.Point;
import graphics.shapes.RectPrism;
import physics.PhysicsObject;
import physics.Timeout;
import util.math.Vector;

public class World {

	public static Vector LIGHT_SOURCE = new Vector(3);
	public static RectPrism MOVEY_BOI = new RectPrism(-300, 300, 800, 600, 600, 600);
	
	public static Graphics2D ctx;
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
		add(new RectPrism(800, 1200, -300, 600, 800, 600));
		add(new RectPrism(100, -600, 1600, 400, 300, 700));
		add(new Point(0, 0, 0));
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
		Camera.graphicsTick();
		
		int size = visuals.size();
		
		for (int i = 1; i < size; i++) {
			int j = i;
			while (j > 0 && visuals.get(j).order() > visuals.get(j - 1).order()) {
				swap(visuals, j, j - 1);
				j--;
			}
		}
		
		for (int i = 0; i < size; i++) {
			Drawable draw = visuals.get(i);
			if ( draw.inCameraWindow() ) {
				draw.draw(ctx);
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
	
	private static void swap(ArrayList<Drawable> array, int index1, int index2) {
		Drawable obj = array.get(index1);
		array.set(index1, array.get(index2));
		array.set(index2, obj);
	}
	
}
