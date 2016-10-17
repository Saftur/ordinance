package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

public class Player extends Ship {
	
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Player(Texture sprite, Shape shape, float stats[]) {
		super(sprite, shape, stats);
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Player(String spriteFilename, Shape shape, float stats[]) {
		super(spriteFilename, shape, stats);
	}
	
	
}
