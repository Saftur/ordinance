package ordinance.entity;

import org.newdawn.slick.opengl.Texture;

import ordinance.Map;
import ordinance.Ordinance;

public abstract class Entity {
	public Texture sprite;
	protected float x=0, y=0;
	protected float vx=0, vy=0;
	protected float maxVel, velInc, velDec;
	protected Map map;
	
	protected Entity() { }
	
	public Entity(Texture sprite, float stats[]) {
		this.sprite = sprite;
		if (stats.length >= 3) {
			this.maxVel = stats[0];
			this.velInc = stats[1];
			this.velDec = stats[2];
		}
	}
	
	public Entity(String spriteFilename, float stats[]) {
		this(Ordinance.loadTexture(spriteFilename), stats);
	}
	
	public void update(int delta) {
		if (vx>maxVel) vx = maxVel;
		if (vx<-maxVel) vx = -maxVel;
		if (vy>maxVel) vy = maxVel;
		if (vy<-maxVel) vy = -maxVel;
		x+=vx*delta;
		y+=vy*delta;
		if (x<0) {
			x=0;
			if (vx<0) vx=0;
		}
		if (x+getWidth()>=map.width) {
			x=map.width-getWidth();
			if (vx>0) vx=0;
		}
		if (y<0) {
			y=0;
			if (vy<0) vy=0;
		}
		if (y+getHeight()>=map.height) {
			y=map.height-getHeight();
			if (vy>0) vy=0;
		}
	}
	
	public void accel(float avx, float avy) {
		//System.out.println(avx+", "+avy);
		vx+=avx*velInc;
		vy+=avy*velInc;
		if (vx>maxVel) vx = maxVel;
		if (vx<-maxVel) vx = -maxVel;
		if (vy>maxVel) vy = maxVel;
		if (vy<-maxVel) vy = -maxVel;
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
