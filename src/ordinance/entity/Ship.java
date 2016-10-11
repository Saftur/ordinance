package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

/**
 * Ship class
 * @author Arthur Bouvier
 * @version %I%, %G%
 */
public class Ship extends Entity {
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Ship(Texture sprite, Shape shape, float stats[]) {
		super(sprite, shape, stats);
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Ship(String spriteFilename, Shape shape, float stats[]) {
		super(spriteFilename, shape, stats);
	}
	
}
