package ordinance;

import java.util.ArrayList;
import java.util.List;

import ordinance.entity.Bullet;
import ordinance.entity.Enemy;
import ordinance.entity.Entity;
import ordinance.entity.Planet;
import ordinance.entity.Player;
import ordinance.entity.Ship;

/**
 * Holds information about the map and contains entities
 * @author Arthur Bouvier
 */
public class Map {
	public static final double GRAVITY = .00000000006674;
	
	public int width, height;
	public List<Entity> ents;
	private Ship player;
	
	/**
	 * Constructor taking width, height, and player object
	 * @param width	  map width
	 * @param height  map height
	 * @param player  player object
	 */
	public Map(int width, int height, Ship player) {
		this.width = width;
		this.height = height;
		ents = new ArrayList<Entity>();
		this.player = player;
		addEntity(player);
	}
	
	
	/**
	 * Update the map and entities
	 * @param delta	 delta time in ms
	 */
	public int update(int delta) {
		int i=0;
		while (i < ents.size()) {
			Entity ent = ents.get(i);
			if (!(ent instanceof Player) && !ent.update(delta))
				removeEntity(ent);
			else i++;
		}
		if (!player.update(delta)) return 1;
		
		boolean collide;
		do {
			collide = false;
			i=0;
			while (i < ents.size()) {
				Entity ent1 = ents.get(i);
				int e=i+1;
				while (e < ents.size()) {
					Entity ent2 = ents.get(e);
					if (ent1.distanceTo(ent2)-ent1.getLongestSize()-ent2.getLongestSize() < 0) {
						if (ent1 instanceof Bullet) {
							if (ent2 instanceof Ship && (ent2 instanceof Player == !((Bullet)ent1).fromPlayer())) {
								if (ent1.collide(ent2)) {
									((Ship)ent2).takeDamage(((Bullet)ent1).dmg);
									removeEntity(ent1);
								}
							} else if (ent2 instanceof Planet) {
								if (ent1.collide(ent2)) {
									removeEntity(ent1);
								}
							}
						} else if (ent1 instanceof Ship) {
							if (ent2 instanceof Bullet) {
								if (ent1 instanceof Player == !((Bullet)ent2).fromPlayer()) {
									if (ent1.collide(ent2)) {
										((Ship)ent1).takeDamage(((Bullet)ent2).dmg);
										removeEntity(ent2);
									}
								}
							} else if (ent2 instanceof Ship) {
								if (ent1.collide(ent2)) {
									float rot1 = ent2.angleTo(ent1)+ent2.rot;
									float rot2 = rot1 >= 180 ? rot1-180 : rot1+180;
									while (ent1.collide(ent2)) {
										ent1.moveDir(.1f, rot1);
										ent2.moveDir(.1f, rot2);
									}
									if (ent1 instanceof Player && ent2 instanceof Enemy)
										((Player)ent1).takeDamage(((Enemy)ent2).dmg, true);
								}
							} else if (ent2 instanceof Planet) {
								if (ent1.collide(ent2)) {
									//System.out.println("Collide");
									//return 1;
								}
							}
						} else if (ent1 instanceof Planet) {
							if (ent2 instanceof Bullet) {
								if (ent1.collide(ent2)) {
									removeEntity(ent2);
								}
							} else if (ent2 instanceof Ship) {
								if (ent1.collide(ent2)) {
									//System.out.println("Collide");
									//return 1;
								}
							}
						}
					}
					e++;
				}
				if (!ent1.mapEdges())
					i++;
			}
		} while (collide);
		
		return 0;
	}
	
	/**
	 * Render the map and entities
	 */
	public void render() {
		for (Entity ent : ents) {
			if (ent != player) {
				ent.render();
				//Ordinance.renderSprite(ent.sprite, ent.getX(), ent.getY());
			}
		}
		player.render();
		//Ordinance.renderSprite(player.sprite, player.getX(), player.getY());
	}

	/**
	 * Adds entity to the map
	 * @param newEnt  ent to add
	 * @return		  the added entity
	 */
	public Entity addEntity(Entity newEnt, float x, float y) {
		ents.add(newEnt);
		newEnt.setMap(this);
		if (x < 0) x = (width/2);
		if (y < 0) y = (height/2);
		newEnt.moveTo(x, y);
		return newEnt;
	}
	
	public Entity addEntity(Entity newEnt) {
		return addEntity(newEnt, -1, -1);
	}

	/**
	 * Removes entity from the map
	 * @param remEnt  ent to remove
	 * @return		  the removed entity
	 */
	public Entity removeEntity(Entity remEnt) {
		ents.remove(remEnt);
		remEnt.setMap(null);
		if (remEnt instanceof Bullet) {
			((Bullet)remEnt).weapon.removeBullet(remEnt);
		}
		return remEnt;
	}
	
}
