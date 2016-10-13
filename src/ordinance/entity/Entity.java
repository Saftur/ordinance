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
	
	protected float x=0, y=0;
	protected float xspd=0, yspd=0;
	protected float rot=0;
	protected float lxspd=0, lyspd=0;
	protected float maxspd, spdinc, spddec;
	protected float density;
	
	
	/**
	 * Defines entity shape
	 * @author Arthur Bouvier
	 */
	public static enum Shape {
		RECT,
		CIRC;
		
		private int width = 0;
		private int height = 0;
		
		/**
		 * Empty constructor
		 */
		private Shape() {
			
		}
		
		/**
		 * Get shape width
		 * @return width
		 */
		public int getWidth() {
			return width;
		}
		
		/**
		 * Get shape height
		 * @return height
		 */
		public int getHeight() {
			return height;
		}
		
		
		/**
		 * Create new rectangle
		 * @param width	  new rect width
		 * @param height  new rect height
		 * @return		  new rect
		 */
		public static Shape newRect(int width, int height) {
			Shape newShape = Shape.RECT;
			newShape.width = width;
			newShape.height = height;
			return newShape;
		}
		
		/**
		 * Create new circle
		 * @param diameter	new circ diameter
		 * @return			new circ
		 */
		public static Shape newCirc(int diameter) {
			Shape newShape = Shape.CIRC;
			newShape.width = diameter;
			newShape.height = diameter;
			return newShape;
		}
	}
	
	/**
	 * Calculates xspd and yspd for a direction
	 * @author Arthur Bouvier
	 */
	private static class SpeedDir {
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
	public Entity(Texture sprite, Shape shape, float stats[]) {
		this.sprite = sprite;
		this.shape = shape;
		if (stats.length >= 3) {
			this.maxspd = stats[0]*60;
			this.spdinc = stats[1]*3600;
			this.spddec = stats[2]*3600;
			if (stats.length >= 4)
				this.density = stats[3];
			else this.density = 1f;
		}
	}
	
	/**
	 * Constructor taking sprite filename, shape, and stats
	 * @param spriteFilename  sprite filename
	 * @param shape			  entity shape
	 * @param stats			  entity stats
	 */
	public Entity(String spriteFilename, Shape shape, float stats[]) {
		this(Ordinance.loadTexture(spriteFilename), shape, stats);
	}
	
	
	/**
	 * Update entity
	 * @param delta delta time in ms
	 */
	public void update(int delta) {
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
		if (xspd>maxspd) xspd = maxspd;
		if (xspd<-maxspd) xspd = -maxspd;
		if (yspd>maxspd) yspd = maxspd;
		if (yspd<-maxspd) yspd = -maxspd;
		x+=sDelta*xspd;
		y+=sDelta*yspd;
		if (x<0) {
			x=0;
			if (xspd<0) {
				if (REBOUND) xspd=-xspd*.8f;
				else xspd=0;
			}
		}
		if (x+getWidth()>=map.width) {
			x=map.width-getWidth();
			if (xspd>0) {
				if (REBOUND) xspd=-xspd*.8f;
				else xspd=0;
			}
		}
		if (y<0) {
			y=0;
			if (yspd<0) {
				if (REBOUND) yspd=-yspd*.8f;
				else yspd=0;
			}
		}
		if (y+getHeight()>=map.height) {
			y=map.height-getHeight();
			if (yspd>0) {
				if (REBOUND) yspd=-yspd*.8f;
				else yspd=0;
			}
		}
		lxspd = xspd; lyspd = yspd;
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
	 * Accelerate in direction dir at scl speed
	 * @param scl	 speed scale
	 * @param dir	 direction
	 * @param delta	 delta time in ms
	 */
	public void accelDir(float scl, float dir, int delta) {
		SpeedDir.setSpdDir(scl, dir);
		accel(SpeedDir.getXspd(), SpeedDir.getYspd(), delta);
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

	/**
	 * Set containing map
	 * @param map  containing map
	 */
	public void setMap(Map map) {
		this.map = map;
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
	 * Get entity x position
	 * @return x position
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Get entity y position
	 * @return y position
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Get entity width
	 * @return sprite width
	 */
	public int getWidth() {
		return sprite.getTextureWidth();
	}
	
	/**
	 * Get entity height
	 * @return sprite height
	 */
	public int getHeight() {
		return sprite.getTextureHeight();
	}
	
	/**
	 * Get entity mass
	 * @return area*density
	 */
	public float getMass() {
		switch (shape) {
		case RECT:
			return (float)(shape.width*shape.height)*density*SCALE;
		case CIRC:
			return (float)(Math.PI*Math.pow(((float)shape.width/2), 2)*density*SCALE);
		default:
			return 0;
		}
	}
	
}
