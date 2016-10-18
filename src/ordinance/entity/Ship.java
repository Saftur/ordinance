package ordinance.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;
import ordinance.Ordinance;

/**
 * Ship class
 * @author Arthur Bouvier
 */
public class Ship extends Entity {
	public List<Weapon> weapons;
	
	public float hp;
	public int wpn;
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Ship(Texture sprite, Shape shape, float stats[], int cx, int cy) {
		super(sprite, shape, stats, cx, cy);
		
		if (stats != null && stats.length > 3) {
			hp = stats[3];
		} else {
			hp = 10;
		}
		enableGravity(true);
		
		weapons = new ArrayList<Weapon>();
		wpn = 0;
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Ship(String spriteFilename, Shape shape, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, stats, cx, cy);
	}
	

	/**
	 * Update entity
	 * @param delta delta time in ms
	 */
	public boolean update(int delta) {
		return super.update(delta);
	}
	
	public void moveTo(float x, float y) {
		super.moveTo(x, y);
		if (weapons.size() > 0)
			weapons.get(wpn).updatePos();
	}
	
	public void moveDir(float spd, float dir) {
		super.moveDir(spd, dir);
		if (weapons.size() > 0)
			weapons.get(wpn).updatePos();
	}
	
	public void pointTo(float x, float y) {
		super.pointTo(x, y);
		if (weapons.size() > 0)
			weapons.get(wpn).updatePos();
	}
	
	public void pointTo(Entity other) {
		super.pointTo(other);
		if (weapons.size() > 0)
			weapons.get(wpn).updatePos();
	}
	
	/**
	 * Give a weapon to the ship
	 * @param weapon  new weapon
	 * @return		  new weapon
	 */
	public Weapon getWeapon(Weapon weapon) {
		weapons.add(weapon);
		weapon.owner = this;
		weapon.map = map;
		return weapon;
	}

	public void switchWeapon(int i) {
		wpn += i;
		while (wpn < 0) wpn += weapons.size();
		while (wpn >= weapons.size()) wpn -= weapons.size();
	}
	
	public void setMap(Map map) {
		super.setMap(map);
		for (Weapon w : weapons)
			w.setMap(map);
	}
	
	public void shoot() {
		if (weapons.size() > 0)
			weapons.get(wpn).shoot();
	}
	
	public void takeDamage(float dmg) {
		hp-=dmg;
	}
	
}
