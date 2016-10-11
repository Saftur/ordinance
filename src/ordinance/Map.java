package ordinance;

import java.util.ArrayList;
import java.util.List;

import ordinance.entity.Entity;
import ordinance.entity.Ship;

/**
 * Holds information about the map and contains entities
 * @author Arthur Bouvier
 * @version %I%, %G%
 */
public class Map {
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
	public void update(int delta) {
		for (Entity ent : ents) {
			ent.update(delta);
		}
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
}
