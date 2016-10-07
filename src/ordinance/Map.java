package ordinance;

import java.util.ArrayList;
import java.util.List;

import ordinance.entity.Entity;
import ordinance.entity.Ship;

public class Map {
	public int width, height;
	public List<Entity> ents;
	private Ship player;
	
	public Map(int width, int height, Ship player) {
		this.width = width;
		this.height = height;
		ents = new ArrayList<Entity>();
		this.player = player;
		addEntity(player);
	}
	
	public Entity addEntity(Entity newEnt) {
		ents.add(newEnt);
		newEnt.setMap(this);
		return newEnt;
	}
	
	public void update(int delta) {
		for (Entity ent : ents) {
			ent.update(delta);
		}
	}
	
	public void render() {
		for (Entity ent : ents) {
			if (ent != player) {
				Ordinance.app.drawSprite(ent.sprite, ent.getX(), ent.getY());
			}
		}
		Ordinance.app.drawSprite(player.sprite, player.getX(), player.getY());
	}
}
