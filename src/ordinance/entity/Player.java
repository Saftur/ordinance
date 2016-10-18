package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

public class Player extends Ship {
	protected Texture norm_sprite;
	protected Texture inv_sprite;
	
	public int invper, invuln;
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Player(Texture sprite, Texture invSprite, Shape shape, int width, int height, float stats[], int cx, int cy) {
		super(sprite, shape, width, height, stats, cx, cy);
		if (stats != null && stats.length > 4) {
			invper = (int)stats[4];
		} else {
			invper = 0;
		}
		invuln = 0;
		norm_sprite = sprite;
		inv_sprite = invSprite;
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Player(String spriteFilename, String invSpriteFilename, Shape shape, int width, int height, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename),
			 Ordinance.loadTexture(invSpriteFilename),
			 shape, width, height, stats, cx, cy);
	}
	
	
	public void takeDamage(float dmg, boolean use_invuln) {
		if (use_invuln) {
			if (invuln >= invper)
				takeDamage(dmg);
			else if (invuln == 0) {
				takeDamage(dmg);
				invuln = invper+2;
				sprite = norm_sprite;
			}
		} else {
			takeDamage(dmg);
			invuln = invper;
			sprite = norm_sprite;
		}
	}
	
	public boolean update(int delta) {
		boolean result = super.update(delta);
		if (invuln > 0) {
			invuln--;
		}
		return result;
	}
	
	public void render() {
		if (invuln != 0) {
			if ((Math.floor((invuln-1)/10))%2 == 0)
				sprite = norm_sprite;
			else
				sprite = inv_sprite;
		} else sprite = norm_sprite;
		super.render();
	}
	
}
