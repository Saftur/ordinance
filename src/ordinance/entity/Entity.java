package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;
import ordinance.Ordinance;

public abstract class Entity {
	protected static final boolean rebound = false;
	protected static final boolean decrease = false;
	
	public Texture sprite;
	protected float x=0, y=0;
	protected float xspd=0, yspd=0;
	protected float lxspd=0, lyspd=0;
	protected float maxspd, spdinc, spddec;
	protected Map map;
	
	protected Entity() { }
	
	public Entity(Texture sprite, float stats[]) {
		this.sprite = sprite;
		if (stats.length >= 3) {
			this.maxspd = stats[0]*60;
			this.spdinc = stats[1]*3600;
			this.spddec = stats[2]*3600;
		}
	}
	
	public Entity(String spriteFilename, float stats[]) {
		this(Ordinance.loadTexture(spriteFilename), stats);
	}
	
	public void update(int delta) {
		float sDelta = (float)delta/1000;
		if (decrease) {
			//System.out.println(xspd+", "+yspd);
			//System.out.println(lxspd+", "+lyspd);
			//System.out.println();
			if (xspd == lxspd && xspd != 0) {
				//System.out.println(xspd+"=="+lxspd);
				if (xspd > 0)
					xspd = (xspd < sDelta*spddec) ? 0 : xspd-sDelta*spddec;
				else if (xspd < 0)
					xspd = (-xspd < sDelta*spddec) ? 0 : xspd+sDelta*spddec;
			}
			if (yspd == lyspd && yspd != 0) {
				//System.out.println(yspd+"=="+lyspd);
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
				if (rebound) xspd=-xspd*.8f;
				else xspd=0;
			}
		}
		if (x+getWidth()>=map.width) {
			x=map.width-getWidth();
			if (xspd>0) {
				if (rebound) xspd=-xspd*.8f;
				else xspd=0;
			}
		}
		if (y<0) {
			y=0;
			if (yspd<0) {
				if (rebound) yspd=-yspd*.8f;
				else yspd=0;
			}
		}
		if (y+getHeight()>=map.height) {
			y=map.height-getHeight();
			if (yspd>0) {
				if (rebound) yspd=-yspd*.8f;
				else yspd=0;
			}
		}
		lxspd = xspd; lyspd = yspd;
	}
	
	public void accel(float sclx, float scly, int delta) {
		//System.out.println(avx+", "+avy);
		xspd+=(float)delta/1000*sclx*spdinc;
		yspd+=(float)delta/1000*scly*spdinc;
		//System.out.println((float)delta/1000*sclx*spdinc);
		//if (vx>maxVel) vx = maxVel;
		//if (vx<-maxVel) vx = -maxVel;
		//if (vy>maxVel) vy = maxVel;
		//if (vy<-maxVel) vy = -maxVel;
		//System.out.println(xspd+", "+yspd);
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getWidth() {
		return sprite.getTextureWidth();
	}
	
	public int getHeight() {
		return sprite.getTextureHeight();
	}
}
