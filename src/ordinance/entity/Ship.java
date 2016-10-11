package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

/**
 * Ship class
 * @author Arthur Bouvier
 * @version %I%, %G%
 */
public class Ship extends Entity {

	/**
	 * Constructor taking sprite texture and stats
	 * @param sprite  entity texture
	 * @param stats	  entity stats
	 */
	public Ship(Texture sprite, float stats[]) {
		super(sprite, stats);
	}

	/**
	 * Constructor taking sprite filename and stats
	 * @param spriteFilename  sprite filename
	 * @param stats			  entity stats
	 */
	public Ship(String spriteFilename, float stats[]) {
		super(spriteFilename, stats);
	}
}
