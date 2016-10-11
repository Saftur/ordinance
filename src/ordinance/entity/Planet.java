package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

/**
 * Planet class
 * @author Arthur Bouvier
 * @version %I%, %G%
 */
public class Planet extends Entity {
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Planet(Texture sprite, Shape shape, float[] stats) {
		super(sprite, shape, stats);
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Planet(String spriteFilename, Shape shape, float[] stats) {
		super(spriteFilename, shape, stats);
	}
	
}
