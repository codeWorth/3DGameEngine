package physics;

import physics.hitboxes.Hitbox;
import util.math.Vector;

public abstract class PhysicsObject {
	
	protected Vector acceleration = new Vector(3);
	
	/** 
	 * Cached velocity, updated after every tick
	 */
	private Vector _velocity = new Vector(3);
	/**
	 * Cached position, updated after every tick
	 */
	private Vector _position = new Vector(3);
	
	/**
	 * Real velocity
	 */
	protected Vector velocity = new Vector(3);
	/**
	 * Real position
	 */
	protected Vector position = new Vector(3);
	
	public double mass;
	public Hitbox hitbox;

	public Vector velocity() {
		return _velocity;
	}
	public Vector position() {
		return _position;
	}
	
	protected abstract void ticks(double dT);
	/**
	 * Update the internal data of this object, called every physics tick.
	 */
	public void update(double dT)
	{	
		acceleration.multiply(dT/mass);
		velocity.add(acceleration);
		position.addMult(_velocity, dT);
		
		acceleration.zero();
		
		_velocity.set(velocity);
		_position.set(position);
		
		ticks(dT);
	}
	
	/**
	 * Adds a force to the object for this tick
	 * 
	 * @param force The force to apply
	 */
	public void applyForce(Vector force)
	{
		acceleration.add(force);
	}
	
	/**
	 * Adds the given velocity to this object's velocity.
	 * Change only takes effect at beginning of next tick.
	 * 
	 * @param velocity Velocity to apply
	 */
	public void applyVelocity(Vector velocity) {
		this.velocity.add(velocity);
	}
	
	/**
	 * Sets this object's velocity to the given velocity.
	 * Change only takes effect at beginning of next tick.
	 * 
	 * @param velocity Velocity to set
	 */
	public void setVelocity(Vector velocity) {
		this.velocity.set(velocity);
	}
	
}
