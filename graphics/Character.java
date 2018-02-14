package graphics;

import physics.PhysicsObject;
import util.Action;
import util.input.InputBinds;
import util.math.Vector;

public class Character extends PhysicsObject {
	
	public double FRICTION = 0.96;
	public double MAX_MOVE_SPEED = 4;
	public double MAX_JUMP_SPEED = 6;
	
	public boolean inAir = false;
	
	public Character() {
		this.mass = 1;
		Camera.CAM_POSITION = this.position;
		
		InputBinds.jump.addDownAction(new Action(this) {
			@Override
			public void execute() {
				Character character = (Character)this.obj;
				if (!character.inAir) {
					character.velocity.matrix[0][1] = MAX_JUMP_SPEED;
					character.inAir = true;
				}
			}
		});
	}

	@Override
	protected void ticks(double dT) {
		double theta = Camera.CAM_ROTATION().matrix[0][1];
		double nDX = velocity.x() * Math.cos(theta) - velocity.z() * Math.sin(theta);
		double nDZ = velocity.x() * Math.sin(theta) + velocity.z() * Math.cos(theta);
		
		this.position.matrix[0][0] += nDX;
		this.position.matrix[0][2] += nDZ;
		this.position.matrix[0][1] += velocity.y();
		
		if (this.velocity.x() * this.velocity.x() + this.velocity.z() * this.velocity.z() < 1) {
			this.velocity.matrix[0][0] = 0;
			this.velocity.matrix[0][2] = 0;
		} else {
			this.velocity.matrix[0][0] *= FRICTION;
			this.velocity.matrix[0][2] *= FRICTION;
		}
		
		if (this.position.y() + this.velocity.y() > 0) {
			this.applyForce(new Vector(3, 0, -0.3, 0));
		} else {
			this.velocity.matrix[0][1] = 0;
			this.position.matrix[0][1] = 0;
			this.inAir = false;
		}
		
		if (InputBinds.forward.state) {
			this.velocity.matrix[0][2] = MAX_MOVE_SPEED * (InputBinds.sprint.state ? 1.5 : 1);

		} else if (InputBinds.backward.state) {
			this.velocity.matrix[0][2] = -MAX_MOVE_SPEED * (InputBinds.sprint.state ? 1.5 : 1);
		}
		if (InputBinds.left.state) {
			this.velocity.matrix[0][0] = -MAX_MOVE_SPEED * (InputBinds.sprint.state ? 1.5 : 1);

		} else if (InputBinds.right.state) {
			this.velocity.matrix[0][0] = MAX_MOVE_SPEED * (InputBinds.sprint.state ? 1.5 : 1);
		}
	}
	
}
