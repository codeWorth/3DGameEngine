package physics;

import graphics.World;

public class Physics implements Runnable {

	public static Physics instance;
	
	public static final int resolution = 5;
	public static final double TIME_ADJUSTMENT = 10000000;
	public static final long nanoDelay = 100000;
		
	private Thread t;
	
    public Physics() {    	
        this.t = new Thread(this);
        this.t.start();
    }
	
	@Override
    public void run() {

        double beforeTime, timeDiff;

        beforeTime = System.nanoTime();

        while (true) {
            timeDiff = (System.nanoTime() - beforeTime) / TIME_ADJUSTMENT;
            
            World.physicsUpdate(timeDiff); //TIME_ADJUSTMENT because I don't wanna fix all the other constants
            beforeTime = System.nanoTime();
            
            try {
                Thread.sleep(0, 50000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }            
        }
    }
	
	public static void start() {
		Physics.instance = new Physics();
	}
	
}
