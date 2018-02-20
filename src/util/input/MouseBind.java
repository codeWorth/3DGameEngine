package util.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import graphics.Camera;
import graphics.World;
import util.Action;

public class MouseBind implements MouseMotionListener {
	
	public static final double SENSITIVITY = 0.0023;
	
	private double centerX = Camera.CAM_WIDTH/2;
	private double centerY = Camera.CAM_HEIGHT/2;
	
	public double mouseX = 0;
	public double mouseY = 0;
	private double lastMX = centerX;
	private double lastMY = centerY;
	private double tolerance = 20;
	private ArrayList<Action> moveActions = new ArrayList<Action>();
	
	//public double angle = -Math.PI/2;
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	public void addMoveAction(Action action) {
		this.moveActions.add(action);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		double mX = e.getX();
		double mY = e.getY();
		this.mouseX += SENSITIVITY * (mX - lastMX);
		this.mouseY += SENSITIVITY * (mY - lastMY);
		lastMX = mX;
		lastMY = mY;
		
		if (this.mouseX < -Math.PI*2 || this.mouseX > Math.PI*2) {
			this.mouseX = 0;
		}
		if (this.mouseY < -Math.PI/2) {
			this.mouseY = -Math.PI/2;
		}
		if (this.mouseY > Math.PI/2) {
			this.mouseY = Math.PI/2;
		}
		
		Camera.setRXRY(-this.mouseY, -this.mouseX);
		
		if (mX > Camera.CAM_WIDTH - tolerance || mX < tolerance || mY > Camera.CAM_HEIGHT - tolerance || mY < tolerance+50) {
			World.robot.mouseMove(Camera.CAM_WIDTH/2, Camera.CAM_HEIGHT/2);
			lastMX = centerX;
			lastMY = centerY;
		}
				
		//this.angle = Math.atan2(mouseY, mouseX);
		
		for (Action act : moveActions) {
			act.execute();
		}
	}
	
	/**
	 * Length squared, high performace
	 * @return The length as a double
	 */
	public double length2() {
		return mouseX*mouseX + mouseY*mouseY;
	}
	
}
