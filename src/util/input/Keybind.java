package util.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import util.Action;

public class Keybind implements KeyListener {

	public boolean state = false;
	public int key = 0;
	private ArrayList<Action> downActions = new ArrayList<Action>();
	private ArrayList<Action> upActions = new ArrayList<Action>();
	
	public Keybind(int key) {
		this.key = key;
	}
	
	
	public void addDownAction(Action action) {
		this.downActions.add(action);
	}
	
	public void addUpAction(Action action) {
		this.upActions.add(action);
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == this.key) {
			this.state = true;
			
			for (Action act : this.downActions) {
				act.execute();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == this.key) {
			this.state = false;
			
			for (Action act: this.upActions) {
				act.execute();
			}
		}
	}
	
}
