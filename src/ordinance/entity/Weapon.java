package ordinance.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import ordinance.Ordinance;

public class Weapon extends Entity {
	protected List<Bullet> bullets;
	public Bullet bullet;
	public Ship owner;
	
	public int lastShot;
	public int cooldown;
	float shtspd=0, shtdel=0, shtnum=0;
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Weapon(Texture sprite, Shape shape, Bullet bullet, float stats[], int cx, int cy) {
		super(sprite, shape, null, cx, cy);
		
		this.bullet = bullet;
		bullet.weapon = this;
		
		if (stats != null) {
			shtspd = stats[0];
			shtdel = stats[1];
			shtnum = stats[2];
		}
		cooldown = 0;
		lastShot = -((int)shtdel);
		
		bullets = new ArrayList<Bullet>();
	}

	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Weapon(String spriteFilename, Shape shape, Bullet bullet, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, bullet, stats, cx, cy);
	}
	
	
	public boolean update() {
		updatePos();
		if (cooldown > 0)
			cooldown--;
		return true;
	}
	
	public void updatePos() {
		x = owner.x; y = owner.y;
		rot = owner.rot;
	}
	
	public void shoot() {
		if (cooldown == 0) {
			Bullet newBullet = bullet.copy();
			map.addEntity(newBullet, x, y);
			while (newBullet.distanceTo(this)+newBullet.getEdge(0) < getEdge(0))
				newBullet.moveDir(0.1f, rot);
			newBullet.shoot(shtspd, rot);
			bullets.add(newBullet);
			if (bullets.size() > shtnum) {
				map.removeEntity(bullets.get(0));
				bullets.remove(0);
			}
			cooldown = (int)shtdel;
		}
	}
	
	public void removeBullet(Entity bullet) {
		bullets.remove(bullet);
	}
	
}
