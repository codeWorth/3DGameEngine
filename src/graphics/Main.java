package graphics;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

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
		
		GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Window myWindow = Window.getWindows()[0];
		DisplayMode oldDispMode = myDevice.getDisplayMode();
		
		Camera.CAM_WIDTH = oldDispMode.getWidth();
		Camera.CAM_HEIGHT = oldDispMode.getHeight();
		
		setTitle("3D Game");
		setUndecorated(true);
		
		if (myDevice.isFullScreenSupported()) {
			try {
				myDevice.setFullScreenWindow(myWindow);
			} finally {				
				myDevice.setFullScreenWindow(null);
			}
		}
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
		BufferCapabilities capabilities = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBufferCapabilities();
		
		if (capabilities.isPageFlipping()) {
			if (capabilities.isMultiBufferAvailable()) {
				this.createBufferStrategy(3);	
			} else {
				this.createBufferStrategy(2);
			}
		} else {
			this.createBufferStrategy(1);
		}
		
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
