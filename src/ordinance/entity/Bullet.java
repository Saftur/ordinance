package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

/**
 * Bullet class
 * @author Brandon Yue
 */
public class Bullet extends Entity {
	public Weapon weapon;
	
	public float dmg;
	public float cxspd, cyspd;
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Bullet(Texture sprite, Shape shape, int width, int height, float stats[], int cx, int cy) {
		super(sprite, shape, width, height, null, cx, cy);
		if (stats != null) {
			dmg = stats[0];
			life = (int)stats[1];
		} else {
			dmg = 0;
		}
		//enableGravity(true);
	}

	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Bullet(String spriteFilename, Shape shape, int width, int height, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, width, height, stats, cx, cy);
	}
	

	/**
	 * Update entity
	 * @param delta delta time in ms
	 */
	public boolean update(int delta) {
		//super.update(delta);
		x += cxspd; y += cyspd;
		return super.update(delta);
	}
	
	public Bullet shoot(float spd, float dir) {
		rot = dir;
		SpeedDir.setSpdDir(spd, dir);
		cxspd = SpeedDir.getXspd();
		cyspd = SpeedDir.getYspd();
		xspd = 0;
		yspd = 0;
		return this;
	}
	
	
	/**
	 * Check if bullet comes from a player
	 * @return bullet from player or not
	 */
	public boolean fromPlayer() {
		if (weapon != null && weapon.owner != null)
			return weapon.owner instanceof Player;
		else return false;
	}
	
	public Bullet copy() {
		float stats[] = {dmg, life};
		Bullet newBullet = new Bullet(sprite, shape, width, height, stats, cx, cy);
		newBullet.weapon = weapon;
		return newBullet;
	}
	
}