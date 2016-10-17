package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;

/**
 * Bullet class
 * @author Brandon Yue
 */
public class Bullet extends Entity {
	public int damage = 10;
	public Bullet(Texture sprite, Shape shape, float stats[]) {
		super(sprite, shape, stats);
	}
	public Bullet(String spriteFilename, Shape shape, float stats[]) {
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
		//should bullets be affected by gravity? Seems like a weird concept
	}
}