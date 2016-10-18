package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

/**
 * Planet class
 * @author Arthur Bouvier
 */
public class Planet extends Entity {
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Planet(Texture sprite, Shape shape, float[] stats, int cx, int cy) {
		super(sprite, shape, stats, cx, cy);
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Planet(String spriteFilename, Shape shape, float[] stats, int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, stats, cx, cy);
	}
	
}
