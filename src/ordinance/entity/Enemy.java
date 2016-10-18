package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

public class Enemy extends Ship {
	public float dmg;
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Enemy(Texture sprite, Shape shape, int width, int height, float stats[], int cx, int cy) {
		super(sprite, shape, width, height, stats, cx, cy);
		if (stats != null) {
			dmg = stats[4];
		} else {
			dmg = 0;
		}
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Enemy(String spriteFilename, Shape shape, int width, int height, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, width, height, stats, cx, cy);
	}
	
}
