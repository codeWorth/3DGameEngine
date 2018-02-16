package util;

public class FixedQueue {

	protected double[] values;
	int front = 0;
	int end = 0;
	boolean started = false;
	public int sum = 0;
	
	public FixedQueue(int length) {
		this.values = new double[length];
	}
	
	public void add(double value) {		
		if (end == front && started) {
			front++;
		}
		if (front >= values.length) {
			front = 0;
		}
		
		sum -= values[end];
		sum += value;
		values[end] = value;
		started = true;
		
		end++;
		if (end >= values.length) {
			end = 0;
		}
	}
	
	public double remove() {
		double val = values[front];
		
		front++;
		if (front >= values.length) {
			front = 0;
		}
		return val;
	}
	
	public double peek() {
		return values[front];
	}
	
}
