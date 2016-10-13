package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;

/**
 * Ship class
 * @author Arthur Bouvier
 */
public class Ship extends Entity {
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Ship(Texture sprite, Shape shape, float stats[]) {
		super(sprite, shape, stats);
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Ship(String spriteFilename, Shape shape, float stats[]) {
		super(spriteFilename, shape, stats);
	}
	
	
	public void update(int delta) {
		super.update(delta);
		
		for (Entity ent : map.ents) {
			if (ent != this) {
				if (ent instanceof Planet) {
					float dist = distanceTo(ent)/20;
					//System.out.println(dist);
					float force = (float)(Map.GRAVITY*getMass()*ent.getMass()/Math.pow(dist, 2));
					System.out.println(force);
					if (force > .0000001) {
						float accel = force/getMass()*1000000000*10;
						//System.out.println(accel);
						accelDir(accel/spdinc, angleTo(ent)+rot, delta);
					}
				}
			}
		}
	}
}
