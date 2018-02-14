package graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import physics.Timeout;
import util.math.Vector;

public class FrameCounter implements Timeout, Drawable {

	private int ticks = 0;
	private int FPS = 0;
	private String displayFPS = "37";
	
	@Override
	public long timeLeft() {
		return 1;
	}

	@Override
	public void timeTick() {
		ticks++;
		
		if (ticks == 10) {
			ticks = 0;
			this.displayFPS = "FPS: " + String.valueOf(FPS);
			this.FPS = 0;
		}
	}

	@Override
	public void draw(Graphics2D ctx) {
		this.FPS++;
		
		ctx.setPaint(Color.WHITE);
		ctx.drawString(this.displayFPS, 40, 40); 
	}

	@Override
	public boolean inCameraWindow() {
		return true;
	}

	@Override
	public int order() {
		return -1;
	}

	@Override
	public boolean raytrace(Vector source, Vector destination) {
		return false;
	}

}
