package graphics;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;

import util.input.InputBinds;

public class Main extends JFrame {
			
	private static final long serialVersionUID = -6315093079179712285L;

	public Main() {
		initUI();
		InputBinds.bind(this);
	}

	private void initUI() {
		
		try {
			World.robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Couldn't move mouse");
			e.printStackTrace();
			return;
		}
		World.robot.mouseMove(Camera.CAM_WIDTH/2, Camera.CAM_HEIGHT/2);
		
		setTitle("3D Game");
		setUndecorated(true);
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
		Camera.CAM_WIDTH = screenSize.width;
		Camera.CAM_HEIGHT = screenSize.height;
		
		this.createBufferStrategy(1);
        do {
            Surface.bs = this.getBufferStrategy();
        } while (Surface.bs == null);
		
		add(new Surface());
		pack();
		
		setSize(Camera.CAM_WIDTH, Camera.CAM_HEIGHT);
		
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Main ex = new Main();
				ex.setVisible(true);
			}
		});
		
	}
}
