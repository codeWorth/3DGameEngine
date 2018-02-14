package physics;

public interface Timeout {

	/**
	 * Returns the time until this object should be removed
	 * This should be at its max at initialization, and then decrease to 0
	 * 
	 * @return Time left in milliseconds
	 */
	public long timeLeft();
	
	/**
	 * One time tick, called 10 times every second
	 */
	public void timeTick();
	
}
