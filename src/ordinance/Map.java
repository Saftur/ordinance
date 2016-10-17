package ordinance;

import java.util.ArrayList;
import java.util.List;

import ordinance.entity.Entity;
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
		
		/*boolean collide;
		do {
			collide = false;
			i=0;
			while (i < ents.size()) {
				Entity ent1 = ents.get(i);
				int e=i+1;
				while (e < ents.size()) {
					Entity ent2 = ents.get(e);
					if (ent1.distanceTo(ent2)-ent1.getLongestSize()-ent2.getLongestSize() < 0) {
						
					}
				}
			}
		} while (collide);*/
		
		return 0;
	}
	
	/**
	 * Render the map and entities
	 */
	public void render() {
		for (Entity ent : ents) {
			if (ent != player) {
				Ordinance.renderSprite(ent.sprite, ent.getX(), ent.getY());
			}
		}
		Ordinance.renderSprite(player.sprite, player.getX(), player.getY());
	}

	/**
	 * Adds entity to the map
	 * @param newEnt  ent to add
	 * @return		  the added entity
	 */
	public Entity addEntity(Entity newEnt) {
		ents.add(newEnt);
		newEnt.setMap(this);
		return newEnt;
	}

	/**
	 * Removes entity from the map
	 * @param remEnt  ent to remove
	 * @return		  the removed entity
	 */
	public Entity removeEntity(Entity remEnt) {
		ents.remove(remEnt);
		remEnt.setMap(null);
		return remEnt;
	}
	
}
