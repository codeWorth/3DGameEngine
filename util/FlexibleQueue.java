package util;

public class FlexibleQueue extends FixedQueue {
	
	public int length = 0;
	
	public FlexibleQueue(int length) {
		super(length);
	}
	
	public FlexibleQueue() {
		super(10);
	}
	
	@Override
	public void add(double value) {		
		length++;
		if (length >= values.length) {
			double[] replacement = new double[values.length * 2];
			for (int i = 0; i < values.length; i++) {
				replacement[i] = values[(i + front) % values.length];
			}
			this.values = replacement;
			this.end = this.length - 1;
			this.front = 0;
		}
		
		super.add(value);
	}
	
	@Override
	public double remove() {
		length--;
		
		return super.remove();
	}
	
}
