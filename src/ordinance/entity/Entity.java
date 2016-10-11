package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;
import ordinance.Ordinance;

/**
 * Super class for all entities in game
 * @author Arthur Bouvier
 * @version %I%, %G%
 */
public abstract class Entity {
	protected static final boolean REBOUND = false;
	protected static final boolean DECREASE = false;
	
	public Texture sprite;
	public Shape shape;
	protected Map map;
	
	protected float x=0, y=0;
	protected float xspd=0, yspd=0;
	protected float lxspd=0, lyspd=0;
	protected float maxspd, spdinc, spddec;
	protected float density;
	
	
	/**
	 * Defines entity shape
	 * @author Arthur Bouvier
	 * @version %I%, %G%
	 */
	public enum Shape {
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
	 * Constructor taking sprite texture, shape, and stats
	 * @param sprite  entity texture
	 * @param shape	  entity shape
	 * @param stats	  entity stats
	 */
	public Entity(Texture sprite, Shape shape, float stats[]) {
		this.sprite = sprite;
		this.shape = shape;
		if (stats.length >= 4) {
			this.maxspd = stats[0]*60;
			this.spdinc = stats[1]*3600;
			this.spddec = stats[2]*3600;
			this.density = stats[3];
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
	 * Accelerate in sclx/scly direction
	 * @param sclx	 scale to accelerate horizontally by
	 * @param scly	 scale to accelerate vertically by
	 * @param delta	 delta time in ms
	 */
	public void accel(float sclx, float scly, int delta) {
		xspd+=(float)delta/1000*sclx*spdinc;
		yspd+=(float)delta/1000*scly*spdinc;
	}
	
	
	/**
	 * Set containing map
	 * @param map  containing map
	 */
	public void setMap(Map map) {
		this.map = map;
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
			return (float)(shape.width*shape.height)*density;
		case CIRC:
			return (float)(Math.PI*Math.pow(((float)shape.width/2), 2)*density);
		default:
			return 0;
		}
	}
	
}
