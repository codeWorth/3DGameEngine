package physics.hitboxes;

import util.math.Vector;

public abstract class Hitbox {

	public Vector ownerPosition;
	public abstract boolean touching(Hitbox other);
	
}
