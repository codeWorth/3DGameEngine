package util;

public abstract class Action {

	public Object obj;
	
	public Action(Object obj) {
		this.obj = obj;
	}
	
	public abstract void execute(); 
	
}
