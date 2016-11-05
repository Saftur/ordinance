package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

public class Enemy extends Ship {
	public float dmg;
	public int enemyType;
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Enemy(Texture sprite, Shape shape, int width, int height, float stats[], int cx, int cy) {
		super(sprite, shape, width, height, stats, cx, cy);
		if (stats != null && stats.length > 5) {
			dmg = stats[4];
			enemyType = (int)stats[5];
		} else {
			dmg = 0;
			enemyType = 0;
		}
		enableGravity(false);
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
	
	public boolean update(int delta){
		boolean result = super.update(delta);
		pointTo(Ordinance.app.player);
		if (enemyType == 0) {
			if (distanceTo(Ordinance.app.player) < 500)
				shoot();
		} else {
			accelDir(1, rot, delta);
		}
		if(hp <= 0)
			return false;
		return result;
	}
	
}
