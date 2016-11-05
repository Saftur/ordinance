package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;
import ordinance.Ordinance;

/**
 * Super class for all entities in game
 * @author Arthur Bouvier
 */
public abstract class Entity {
	protected static final boolean REBOUND = false;
	protected static final boolean DECREASE = false;
	protected static final int SCALE = 16;
	
	public Texture sprite;
	public Shape shape;
	protected Map map;
	
	protected int width, height;
	public float x=0, y=0;
	protected int cx=0, cy=0;
	protected float xspd=0, yspd=0;
	public float rot=0;
	protected float lxspd=0, lyspd=0;
	protected float maxspd, spdinc, spddec;
	protected int life=0;
	protected boolean gravity=false;
	public float density=1f;
	
	
	/**
	 * Defines entity shape
	 * @author Arthur Bouvier
	 */
	public static enum Shape {
		RECT,
		CIRC;
	}
	
	/**
	 * Calculates xspd and yspd for a direction
	 * @author Arthur Bouvier
	 */
	public static class SpeedDir {
		private static float xspd=0, yspd=0;
		
		/**
		 * Calculate xspd and yspd
		 * @param spd  directional speed
		 * @param dir  direction
		 */
		public static void setSpdDir(float spd, float dir) {
			int dirquad = 0;
			while (dir >= 90) {
				dir -= 90;
				dirquad++;
			}
			if (dir == 0) {
				switch (dirquad) {
				case 0: xspd = spd; yspd = 0; break;
				case 1: xspd = 0; yspd = spd; break;
				case 2: xspd = -spd; yspd = 0; break;
				case 3: xspd = 0; yspd = -spd; break;
				}
			} else {
				xspd = (float)(Math.cos(dir*Math.PI/180)*spd);
				yspd = (float)(Math.sin(dir*Math.PI/180)*spd);
				float oldXspd = xspd;
				switch (dirquad) {
				case 0: break;
				case 1: xspd = -yspd; yspd = oldXspd; break; //cxspd,cyspd = -cyspd,cxspd; break;
				case 2: xspd = -xspd; yspd = -yspd; break; //cxspd,cyspd = -cxspd,-cyspd; break;
				case 3: xspd = yspd; yspd = -oldXspd; break; //cxspd,cyspd = cyspd,-cxspd; break;
				}
			}
		}
		
		/**
		 * Get xspd
		 * @return xspd
		 */
		public static float getXspd() {
			float xspd = SpeedDir.xspd;
			//SpeedDir.xspd = 0;
			return xspd;
		}
		
		/**
		 * Get yspd
		 * @return yspd
		 */
		public static float getYspd() {
			float yspd = SpeedDir.yspd;
			//SpeedDir.yspd = 0;
			return yspd;
		}
	}
	
	
	/**
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Entity(Texture sprite, Shape shape, int width, int height, float stats[], int cx, int cy) {
		this.sprite = sprite;
		this.shape = shape;
		this.width = width;
		this.height = height;
		if (stats != null && stats.length > 2) {
			this.maxspd = stats[0]*60;
			this.spdinc = stats[1]*3600;
			this.spddec = stats[2]*3600;
		}
		this.cx = cx;
		this.cy = cy;
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Entity(String spriteFilename, Shape shape, int width, int height, float stats[], int cx, int cy) {
		this(Ordinance.loadTexture(spriteFilename), shape, width, height, stats, cx, cy);
	}
	
	
	/**
	 * Update entity
	 * @param delta delta time in ms
	 */
	public boolean update(int delta) {
		float sDelta = (float)delta/1000;
		if (DECREASE) {
			if (xspd == lxspd && xspd != 0) {
				if (xspd > 0)
					xspd = (xspd < sDelta*spddec) ? 0 : xspd-sDelta*spddec;
				else if (xspd < 0)
					xspd = (-xspd < sDelta*spddec) ? 0 : xspd+sDelta*spddec;
			}
			if (yspd == lyspd && yspd != 0) {
				if (yspd > 0)
					yspd = (yspd < sDelta*spddec) ? 0 : yspd-sDelta*spddec;
				else if (yspd < 0)
					yspd = (-yspd < sDelta*spddec) ? 0 : yspd+sDelta*spddec;
			}
		}
		//if (xspd>maxspd) xspd = maxspd;
		//if (xspd<-maxspd) xspd = -maxspd;
		//if (yspd>maxspd) yspd = maxspd;
		//if (yspd<-maxspd) yspd = -maxspd;
		
		float spd = (float)Math.sqrt(xspd*xspd+yspd*yspd);
		if (spd > maxspd) {
			float dir = angleTo(x+xspd, y+yspd)+rot;
			SpeedDir.setSpdDir(maxspd, dir);
			xspd = SpeedDir.getXspd();
			yspd = SpeedDir.getYspd();
		}
		
		x+=sDelta*xspd;
		y+=sDelta*yspd;
		lxspd = xspd; lyspd = yspd;
		
		if (gravity) {
			if (this instanceof Bullet) System.out.println("Bullet gravity");
			for (Entity ent : map.ents) {
				if (ent != this) {
					if (ent instanceof Planet) {
						float dist = distanceTo(ent)/20;
						//System.out.println(dist);
						float force = (float)(Map.GRAVITY*getMass()*ent.getMass()/Math.pow(dist, 2));
						//System.out.println(force);
						if (force > .00000001) {
							float accel = force/getMass()*1000000000*15;
							//System.out.println(accel);
							accelDir(accel/spdinc, angleTo(ent)+rot, delta);
						}
					}
				}
			}
		}
		
		if (life > 0) {
			life--;
			if (life <= 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Move to x/y point
	 * @param x	 x coordinate
	 * @param y	 y coordinate
	 */
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveDir(float spd, float dir) {
		if (map != null) {
			SpeedDir.setSpdDir(spd, dir);
			x+=SpeedDir.getXspd();
			y+=SpeedDir.getYspd();
		}
	}
	
	/**
	 * Accelerate in sclx/scly direction
	 * @param sclx	 x speed scale
	 * @param scly	 y speed scale
	 * @param delta	 delta time in ms
	 */
	public void accel(float sclx, float scly, int delta) {
		xspd+=(float)delta/1000*sclx*spdinc;
		yspd+=(float)delta/1000*scly*spdinc;
	}

	/**
	 * Accelerate in sclx/scly direction
	 * @param sclx	   x speed scale
	 * @param scly	   y speed scale
	 * @param maxXSpd  max xspd
	 * @param maxYSpd  max yspd
	 * @param delta	   delta time in ms
	 */
	public void accel(float sclx, float scly, float maxXSpd, float maxYSpd, int delta) {
		float dxspd = (float)delta/1000*sclx*spdinc;
		float dyspd = (float)delta/1000*scly*spdinc;
		maxXSpd *= maxspd;
		maxYSpd *= maxspd;
		if (dxspd < 0) {
			dxspd = -dxspd;
			maxXSpd = -maxXSpd;
		}
		if (dyspd < 0) {
			dyspd = -dyspd;
			maxYSpd = -maxYSpd;
		}
		
		if (xspd < maxXSpd) {
			xspd+=dxspd;
		}
		if (xspd > maxXSpd) {
			xspd-=dxspd;
			if (xspd < maxXSpd)
				xspd = maxXSpd;
		}
		if (yspd < maxYSpd) {
			yspd+=dyspd;
		}
		if (yspd > maxYSpd) {
			yspd-=dyspd;
			if (yspd < maxYSpd)
				yspd = maxYSpd;
		}
	}
	
	/**
	 * Accelerate in direction dir at scl speed
	 * @param scl	 speed scale
	 * @param dir	 direction
	 * @param delta	 delta time in ms
	 */
	public void accelDir(float scl, float dir, int delta) {
		SpeedDir.setSpdDir(scl, dir);
		accel(SpeedDir.getXspd(), SpeedDir.getYspd(), delta);
	}
	
	public void accelDir(float scl, float dir, float maxspd, int delta) {
		/*float spd = (float)Math.sqrt(xspd*xspd+yspd*yspd);
		float dspd = (float)delta/1000*scl*spdinc;
		maxspd *= this.maxspd;
		if (dspd < 0) {
			dspd = -dspd;
			maxspd = -maxspd;
		}

		if (spd < maxspd) {
			spd+=dspd;
		}
		if (spd > maxspd) {
			spd-=dspd;
			if (spd < maxspd)
				spd = maxspd;
		}
		
		SpeedDir.setSpdDir(spd, dir);
		xspd = SpeedDir.getXspd();
		yspd = SpeedDir.getYspd();*/
		maxspd *= this.maxspd;
		float spd = (float)Math.sqrt(xspd*xspd+yspd*yspd);
		float dspd = (float)delta/1000*scl*spdinc;
		//System.out.println(xspd+", "+yspd);
		if (spd < maxspd)
			accelDir(scl, dir, delta);
		//System.out.println(xspd+", "+yspd);
		dir = angleTo(x+xspd, y+yspd)+rot;
		//System.out.println(dir);
		spd = (float)Math.sqrt(xspd*xspd+yspd*yspd);
		if (spd > maxspd)
			spd -= dspd;
		SpeedDir.setSpdDir(spd, dir);
		xspd = SpeedDir.getXspd();
		yspd = SpeedDir.getYspd();
		//System.out.println(xspd+", "+yspd);
		//System.out.println();
	}
	
	/**
	 * Point to x, y
	 * @param x	 x coordinate
	 * @param y	 y coordinate
	 */
	public void pointTo(float x, float y) {
		rot += angleTo(x, y);
		while (rot >= 360) rot -= 360;
		while (rot < 0) rot += 360;
		
	}
	
	/**
	 * Point to other entity
	 * @param other	 other entity
	 */
	public void pointTo(Entity other) {
		rot += angleTo(other);
		while (rot >= 360) rot -= 360;
		while (rot < 0) rot += 360;
	}
	
	public boolean mapEdges() {
		if (this instanceof Ship) {
			Ship self = (Ship)this;
			if (x-cx<0) {
				x=cx;
				if (xspd<0) {
					if (REBOUND) xspd=-xspd*.8f;
					else xspd=0;
				}
				if (self.weapons.size() > 0)
					self.weapons.get(self.wpn).updatePos();
				return true;
			}
			if (x-cx+getWidth()>map.width) {
				x=map.width-getWidth()+cx;
				if (xspd>0) {
					if (REBOUND) xspd=-xspd*.8f;
					else xspd=0;
				}
				if (self.weapons.size() > 0)
					self.weapons.get(self.wpn).updatePos();
				return true;
			}
			if (y-cy<0) {
				y=cy;
				if (yspd<0) {
					if (REBOUND) yspd=-yspd*.8f;
					else yspd=0;
				}
				if (self.weapons.size() > 0)
					self.weapons.get(self.wpn).updatePos();
				return true;
			}
			if (y-cy+getHeight()>map.height) {
				y=map.height-getHeight()+cy;
				if (yspd>0) {
					if (REBOUND) yspd=-yspd*.8f;
					else yspd=0;
				}
				if (self.weapons.size() > 0)
					self.weapons.get(self.wpn).updatePos();
				return true;
			}
		} else if (this instanceof Bullet) {
			if (x+getLongestSize() < 0 || x-getLongestSize() >= map.width || y+getLongestSize() < 0 || y-getLongestSize() >= map.height) {
				map.removeEntity(this);
				//return true;
			}
		}
		return false;
	}
	
	/**
	 * Set containing map
	 * @param map  containing map
	 */
	public void setMap(Map map) {
		this.map = map;
	}
	
	public void enableGravity(boolean enable) {
		gravity = enable;
	}
	
	public void toggleGravity() {
		enableGravity(!gravity);
	}
	
	public void render() {
		Ordinance.renderSprite(sprite, x, y, cx, cy, rot, getWidth(), getHeight());
	}
	
	
	/**
	 * Get distance to entity other
	 * @param other	 other entity
	 * @return		 distance
	 */
	public float distanceTo(Entity other) {
		if (map != null && map == other.map) {
			double dx = Math.abs(other.x-x), dy = Math.abs(other.y-y);
			return (float)Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
		} else return -1;
	}
	
	/**
	 * Get angle to x, y
	 * @param x	 x coordinate
	 * @param y	 y coordinate
	 * @return	 rotation
	 */
	public float angleTo(float x, float y) {
		if (map != null) {
			float dx = x-this.x, dy = y-this.y;
			float rot = (float) ((dx != 0) ? (Math.atan(Math.abs(dy)/Math.abs(dx))*180/Math.PI) : 90);
			if (dx < 0 && dy < 0) rot = rot+180;
			if (dx > 0 && dy < 0) rot = -rot;
			if (dx < 0 && dy > 0) rot = 180-rot;
			if (dx < 0 && dy == 0) rot = 180;
			if (dy < 0 && dx == 0) rot = 270;
			while (rot >= 360) rot -= 360;
			while (rot < 0) rot += 360;
			float dr = rot-this.rot;
			return dr;
		} else return 0;
	}
	
	/**
	 * Get angle to other entity
	 * @param other	 other entity
	 * @return		 rotation
	 */
	public float angleTo(Entity other) {
		if (map != null && map == other.map) {
			return angleTo(other.x, other.y);
		} else return 0;
	}
	
	/**
	 * Get entity width
	 * @return sprite width
	 */
	public int getWidth() {
		return width;
		//return sprite.getTextureWidth();
	}
	
	/**
	 * Get entity height
	 * @return sprite height
	 */
	public int getHeight() {
		return height;
		//return sprite.getTextureHeight();
	}
	
	public int getEdge(int e) {
		switch (e) {
		case 0:
			return getWidth()-cx;
		case 1:
			return getHeight()-cy;
		case 2:
			return cx;
		case 3:
			return cy;
		default:
			return 0;
		}
	}
	
	/**
	 * Gets the longest size needed to test collisions
	 * @return longest size
	 */
	public float getLongestSize() {
		return (float)(Math.sqrt(Math.pow(getWidth(), 2)+Math.pow(getHeight(), 2))/2);
	}
	
	/**
	 * Get entity mass
	 * @return area*density
	 */
	public float getMass() {
		switch (shape) {
		case RECT:
			return (float)(width*height)*density*SCALE;
		case CIRC:
			return (float)(Math.PI*Math.pow(((float)width/2), 2)*density*SCALE);
		default:
			return 0;
		}
	}
	
	public boolean collide(Entity other) {
		if (this.shape == Shape.CIRC) {
			if (other.shape == Shape.CIRC)
				return distanceTo(other)-(getEdge(0)+other.getEdge(0)) < 0;
			else if (other.shape == Shape.RECT)
				return collideCircRect(this, other);
		} else if (this.shape == Shape.RECT) {
			if (other.shape == Shape.CIRC)
				return collideCircRect(other, this);
		}
		return false;
	}
	
	
	public static boolean collideCircRect(Entity circ, Entity rect) {
		float d1=0, d2=0;
		float angle = rect.angleTo(circ);
		while (angle < 0) angle += 360;
		float dist = rect.distanceTo(circ);
		
		int quad = 0;
		while (angle >= 90) {
			angle -= 90;
			quad++;
		}
		if (angle == 0) {
			switch (quad) {
			case 0: d1 = dist; d2 = 0; break;
			case 1: d1 = 0; d2 = dist; break;
			case 2: d1 = -dist; d2 = 0; break;
			case 3: d1 = 0; d2 = -dist; break;
			}
		} else {
			d1 = (float)(Math.cos(angle*Math.PI/180)*dist);
			d2 = (float)(Math.sin(angle*Math.PI/180)*dist);
			float oldD1 = d1;
			switch (quad) {
			case 0: break;
			case 1: d1 = -d2; d2 = oldD1; break;
			case 2: d1 = -d1; d2 = -d2; break;
			case 3: d1 = d2; d2 = -oldD1; break;
			}
		}
		
		float e1 = rect.getEdge(0), e2 = rect.getEdge(1);
		float e3 = rect.getEdge(2), e4 = rect.getEdge(3);
		float ec = circ.getEdge(0);
		if (d1 <= e1 && d1 >= -e3) {
			if (d2 > e2) return d2-(ec+e2) < 0;
			else if (d2 < -e4) return -d2-(ec+e4) < 0;
			else return true;
		} else if (d2 <= e2 && d2 >= -e4) {
			if (d1 > e1) return d1-(ec+e1) < 0;
			else if (d1 < -e3) return -d1-(ec+e3) < 0;
			else return true;
		} else {
			float a=0, b=0;
			if (d1 > e1) a = d1-e1;
			else if (d1 < -e3) a = -d1-e3;
			if (d2 > e2) b = d2-e2;
			else if (d2 < -e4) b = -d2-e4;
			return Math.sqrt(Math.pow(a,2)+Math.pow(b,2))-ec < 0;
		}
	}
	
}
