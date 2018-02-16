package graphics;

import physics.Timeout;

public class FrameCounter implements Timeout {

	private int ticks = 0;
	private String displayFPS = "...";
	
	@Override
	public long timeLeft() {
		return 1;
	}

	@Override
	public void timeTick() {
		ticks++;
		
		if (ticks == 10) {
			ticks = 0;
			this.displayFPS = "FPS: " + String.valueOf(World.FPS);
			System.out.println(this.displayFPS); 
			World.FPS = 0;
		}
	}

}
