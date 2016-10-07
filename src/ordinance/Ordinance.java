package ordinance;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ordinance.entity.Ship;

import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

public class Ordinance {
	public static Ordinance app;
	
	public Map map;
	
	public int width=800, height=600;
	public int mapWidth=800, mapHeight=600;
	//public float x=width/2, y=height/2;
	//public float rotation = 0;
	public int mx=0, my=0;
	private long lastFrameTime;
	private long lastFPSTime;
	private int fps;
	
	private Ship player;
	//private DisplayMode windowDisplayMode;
	//private Texture texture;
	private Controller gamepad;
	
	public void run() {
		init();
		loop();
	}
	
	private void init() {
		try {
			//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			//System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "true");
			setDisplayMode(width, height, false);
			//windowDisplayMode = Display.getDisplayMode();
			//Display.setDisplayMode(new DisplayMode(width, height));
			//Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try {
			Controllers.create();
			int numGamepads = Controllers.getControllerCount();
			System.out.println("Number of gamepads = "+numGamepads);
			for (int i=0; i<numGamepads; i++) {
				Controller c = Controllers.getController(i);
				System.out.println(i+" = "+c.getName());
				int numButtons = c.getButtonCount();
				if (numButtons > 0) System.out.println("Buttons:");
				for (int b=0; b<numButtons; b++) {
					System.out.println("  "+b+" = "+c.getButtonName(b));
				}
				int numAxes = c.getAxisCount();
				if (numAxes > 0) System.out.println("Axes:");
				for (int a=0; a<numAxes; a++) {
					System.out.println("  "+a+" = "+c.getAxisName(a));
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glEnable(GL_TEXTURE_2D);
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0,0,width,height);
		glMatrixMode(GL_MODELVIEW);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		float playerStats[] = {.5f, 1f, 0.2f};
		player = new Ship("ship", playerStats);
		map = new Map(mapWidth, mapHeight, player);
		map.addEntity(player);
	}
	
	private void loop() {
		int delta;
		getDelta();
		lastFPSTime = getTime();
		while (!Display.isCloseRequested()) {
			delta = getDelta();
			
			update(delta);
			render(delta);
			updateFPS();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	private void update(int delta) {
		pollInput(delta);
		/*rotation += 0.15f * delta;
		if (x<0) x=0;
		if (x+texture.getTextureWidth()>=width) x=width-1-texture.getTextureWidth();
		if (y<0) y=0;
		if (y+texture.getTextureHeight()>=height) y=height-1-texture.getTextureHeight();*/
		map.update(delta);
	}
	
	private void pollInput(int delta) {
		mx = Mouse.getX();
		my = height-Mouse.getY()-1;
		//System.out.println(mx+", "+my);
		
		/*if (Mouse.isButtonDown(0)) {
			//System.out.println("Mouse down: "+mx+", "+my);
			x=mx-texture.getTextureWidth()/2;
			y=my-texture.getTextureHeight()/2;
		}*/
		
		//if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
		//if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
		//if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
		//if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) player.accel(-(float)delta/1000, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) player.accel((float)delta/1000, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) player.accel(0, -(float)delta/1000);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) player.accel(0, (float)delta/1000);
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				switch (Keyboard.getEventKey()) {
				/*case Keyboard.KEY_F:
					DisplayMode desktop = Display.getDesktopDisplayMode();
					if (isFullscreen())
						try {
							Display.setDisplayMode(windowDisplayMode);
						} catch (LWJGLException e) {
							e.printStackTrace();
						}
					else
						setDisplayMode(desktop.getWidth(), desktop.getHeight(), true);
					break;*/
				case Keyboard.KEY_ESCAPE:
					System.exit(0);
				}
			}
		}
	}
	
	private void render(int delta) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		/*glColor3f(0.5f, 0.5f, 1.0f);
		
		glPushMatrix(); {
			glTranslatef(x, y, 0);
			glRotatef(rotation, 0f, 0f, 1f);
			glTranslatef(-x, -y, 0);
			glBegin(GL_QUADS); {
				glVertex2f(x-50, y-50);
				glVertex2f(x+50, y-50);
				glVertex2f(x+50, y+50);
				glVertex2f(x-50, y+50);
			} glEnd();
		} glPopMatrix();*/
		/*Color.white.bind();
		texture.bind();
		
		glBegin(GL_QUADS); {
			glTexCoord2f(0,0);
			glVertex2f(x,y);
			glTexCoord2f(1,0);
			glVertex2f(x+texture.getTextureWidth(),y);
			glTexCoord2f(1,1);
			glVertex2f(x+texture.getTextureWidth(),y+texture.getTextureHeight());
			glTexCoord2f(0,1);
			glVertex2f(x,y+texture.getTextureHeight());
		} glEnd();*/
		
		map.render();
	}
	
	public void drawSprite(Texture sprite, float x, float y) {
		glBegin(GL_QUADS); {
			glTexCoord2f(0,0);
			glVertex2f(x,y);
			glTexCoord2f(1,0);
			glVertex2f(x+sprite.getTextureWidth(),y);
			glTexCoord2f(1,1);
			glVertex2f(x+sprite.getTextureWidth(),y+sprite.getTextureHeight());
			glTexCoord2f(0,1);
			glVertex2f(x,y+sprite.getTextureHeight());
		} glEnd();
	}
	
	private void updateFPS() {
		if (getTime() - lastFPSTime > 1000) {
			Display.setTitle("FPS: "+fps);
			fps = 0;
			lastFPSTime+=1000;
		}
		fps++;
	}
	
	public void setDisplayMode(int width, int height, boolean fullscreen) {
		if (Display.getDisplayMode().getWidth() == width &&
			Display.getDisplayMode().getHeight() == height &&
			Display.isFullscreen() == fullscreen) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i=0; i<modes.length; i++) {
					DisplayMode current = modes[i];
					
					if (current.isFullscreenCapable() &&
						current.getWidth() == width &&
						current.getHeight() == height) {
						if (targetDisplayMode == null || current.getFrequency() >= freq) {
							if (targetDisplayMode == null || current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}
						if (current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() &&
							current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
				return;
			}
			
			Display.setDisplayMode(targetDisplayMode);
			//Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fs="+fullscreen + e);
		}
	}
	
	public long getTime() {
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	
	private int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastFrameTime);
		lastFrameTime = time;
		
		return delta;
	}
	
	public boolean isFullscreen() {
		return Display.isFullscreen();
	}
	
	
	public static Texture loadTexture(String filename) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/"+filename+".png"));
			
			System.out.println("Texture "+texture.getTextureID()+" "+filename+": "+texture.getTextureWidth()+","+texture.getTextureHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	
	public static void main(String[] args) {
		(app = new Ordinance()).run();
	}
}
