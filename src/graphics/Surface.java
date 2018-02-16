package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import physics.Physics;

public class Surface extends JPanel implements Runnable {
	private static final long serialVersionUID = 7864031487758888868L;

	public static final int colorRange = 255; //8 bits per color
	public static final int bitsPerColor = 8;
	public static final int rShiftBits = 2 * bitsPerColor; //Red channel is first 8 bits
	public static final int gShiftBits = 1 * bitsPerColor; //Green channel is next 8 bits
	public static final int bShiftBits = 0 * bitsPerColor; //Blue channel is last 8 bits
	public static final int aShiftBits = 3 * bitsPerColor;
	public static final int totalBits = rShiftBits + bShiftBits + gShiftBits + aShiftBits;
	private static final int FPS = 60;
	private static final int DELAY = 1000/FPS;
	
	public static BufferStrategy bs;
	public static int[] raster;
    public static BufferedImage img;
    private static Graphics g;
	
	/**
	 * Sets a pixel to the given color, if that pixel exists.
	 * Upper left corner is (0, 0)
	 * 
	 * @param x X value, from 0 to this window's width
	 * @param y Y value, from 0 to this window's height
	 * @param r Red value, from 0 to 255
	 * @param g Green value, from 0 to 255
	 * @param b Blue value, from 0 to 255
	 */
	public static void setColor(int x, int y, int r, int g, int b) {
		if (x >= 0 && x < Camera.CAM_WIDTH && y >= 0 && y < Camera.CAM_HEIGHT) {
			int rgb = 0;
			rgb = rgb | r << rShiftBits;
			rgb = rgb | g << gShiftBits;
			rgb = rgb | b << bShiftBits;
			rgb = rgb | 255 << aShiftBits;
			
			int location = y * Camera.CAM_WIDTH + x;
			raster[location] = rgb;
		}
	}
	
	private Thread t;
	
	public Surface() {
		super();
		
		this.setDoubleBuffered(false);
		raster = new int[Camera.CAM_WIDTH*Camera.CAM_HEIGHT];

        ColorModel colorModel = ColorModel.getRGBdefault();
        DataBufferInt buffer = new DataBufferInt(raster, raster.length);
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(Camera.CAM_WIDTH, Camera.CAM_HEIGHT);
        WritableRaster wrRaster = Raster.createWritableRaster(sampleModel, buffer, null);
        
        img = new BufferedImage(colorModel, wrRaster, false, null);
		
		World.initialize();
		
		setBackground(Color.black);
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
        	
			World.FPS++;	
			World.graphicsUpdate();
			
        	double time = System.nanoTime();
			
			g = bs.getDrawGraphics();     
			g.setColor(Color.WHITE);
			g.drawImage(img, 0, 0, null);
			g.dispose();
			g = null;
			bs.show();
			
			World.totalDrawTime += System.nanoTime() - time;
			
			int clear = 0;
			clear |= 0 << rShiftBits;
			clear |= 0 << gShiftBits;
			clear |= 0 << bShiftBits;
			clear |= 255 << aShiftBits;
			for (int i = 0; i < raster.length; i++) {
				raster[i] = clear;
			 }

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
