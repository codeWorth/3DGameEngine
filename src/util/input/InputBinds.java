package util.input;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class InputBinds {

	public static Keybind forward = new Keybind(KeyEvent.VK_W);
	public static Keybind backward = new Keybind(KeyEvent.VK_S);
	public static Keybind left = new Keybind(KeyEvent.VK_A);
	public static Keybind right = new Keybind(KeyEvent.VK_D);
	public static Keybind jump = new Keybind(KeyEvent.VK_SPACE);
	public static Keybind sprint = new Keybind(KeyEvent.VK_SHIFT);
	
	public static MouseBind look = new MouseBind();
	
	public static void bind(JFrame bindTo) {
		bindTo.addKeyListener(InputBinds.forward);
		bindTo.addKeyListener(InputBinds.backward);
		bindTo.addKeyListener(InputBinds.left);
		bindTo.addKeyListener(InputBinds.right);
		bindTo.addKeyListener(InputBinds.jump);
		bindTo.addKeyListener(InputBinds.sprint);
		
		bindTo.addMouseMotionListener(look);
	}
	
}
