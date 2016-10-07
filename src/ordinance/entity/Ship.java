package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

public class Ship extends Entity {

	
	public Ship(Texture sprite, float stats[]) {
		super(sprite, stats);
	}
	
	public Ship(String spriteFilename, float stats[]) {
		super(spriteFilename, stats);
	}
}
