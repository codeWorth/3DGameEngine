package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import physics.Physics;

public class Surface extends JPanel implements Runnable {

	private Thread t;
	
	private final int FPS = 60;
	private final int DELAY = 1000/FPS;
	
	public Surface() {
		super();
		
		World.initialize();
		
		setBackground(Color.black);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    World.ctx = (Graphics2D) g;
	    World.graphicsUpdate();
	}
	
	@Override
    public void addNotify() {
        super.addNotify();

        this.t = new Thread(this);
        this.t.start();   
        
        Physics.start();
    }
	
	@Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }
	
}
