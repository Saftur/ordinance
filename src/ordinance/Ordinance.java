package ordinance;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
//import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ordinance.entity.Entity.Shape;
import ordinance.entity.Planet;
import ordinance.entity.Player;
import ordinance.entity.Ship;
import ordinance.screen.Screen;

import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

/**
 * Ordinance game
 * @author Arthur Bouvier
 * @author Brandon Yue
 */
public class Ordinance {
	public static Ordinance app;
	
	public Screen screen;
	public Map map;
	private Ship player;
	//private Controller gamepad;
	
	public int width=1600, height=1200;
	public int mapWidth=1600, mapHeight=1200;
	public int mouseX=0, mouseY=0;
	private long lastFrameTime;
	private long nextFPSTime;
	private int fpsCount;
	private int delta;
	
	/**
	 * Initiate the game
	 */
	private void init() {
		try {
			setDisplayMode(width, height, false);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		/*try {
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
		}*/
		
		glEnable(GL_TEXTURE_2D);
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0,0,width,height);
		//glMatrixMode(GL_MODELVIEW);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		float playerStats[] = {6f, .4f, .3f, 100};
		float planetStats[] = {6f, .4f, .3f};
		Shape playerShape = Shape.newCirc(64);
		player = new Player("ship", "../player", playerShape, playerStats, 32, 32);
		Planet planet = new Planet("planet1", playerShape, planetStats, 32, 32);
		//planet.moveTo(width/2-32, height/2-32);
		System.out.println(player.getMass());
		map = new Map(mapWidth, mapHeight, player);
		player.moveTo(0, 0);
		map.addEntity(planet);
		//map.addEntity(player);
	}
	
	/**
	 * Main game loop
	 */
	private void loop() {
		getDelta();
		nextFPSTime = getTime();
		while (!Display.isCloseRequested()) {
			delta = getDelta();
			//System.out.println(delta);
			
			update();
			render();
			updateFPS();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	/**
	 * Update the world
	 */
	private void update() {
		pollInput();
		//player.accelDir(1, 45, delta);
		map.update(delta);
	}
	
	/**
	 * Poll input from devices
	 */
	private void pollInput() {
		mouseX = Mouse.getX();
		mouseY = height-Mouse.getY()-1;

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) player.accel(-1, 0, delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) player.accel(1, 0, delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) player.accel(0, -1, delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) player.accel(0, 1, delta);
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_R: //ease of testing, can be taken out later
					Display.destroy();
					init();
					break;
				case Keyboard.KEY_ESCAPE:
					System.exit(0);
				}
			}
		}
	}
	
	/**
	 * Render the world
	 */
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		map.render();
	}
	
	
	/**
	 * Check FPS and updates title
	 */
	private void updateFPS() {
		if (getTime() - nextFPSTime > 1000) {
			Display.setTitle("FPS: "+fpsCount);
			fpsCount = 0;
			nextFPSTime+=1000;
		}
		fpsCount++;
	}
	
	
	/**
	 * Get current program time
	 * @return current time in ms
	 */
	public long getTime() {
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	
	/**
	 * Get delta time since last call
	 * @return delta time in ms
	 */
	private int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastFrameTime);
		lastFrameTime = time;
		
		return delta;
	}
	
	
	/**
	 * Set width/height
	 * @param width	  new width
	 * @param height  new height
	 */
	public static void setDisplayMode(int width, int height) {
		setDisplayMode(width, height, Display.isFullscreen());
	}
	
	/**
	 * Set width/height and fullscreen or not
	 * @param width		  new width
	 * @param height	  new height
	 * @param fullscreen  fullscreen or not
	 */
	public static void setDisplayMode(int width, int height, boolean fullscreen) {
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
	
	/**
	 * Loads a PNG file and returns resulting texture
	 * @param filename	name of png file
	 * @return			new texture
	 */
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

	/**
	 * Draw a sprite at specified coordinates
	 * @param sprite  texture to be rendered
	 * @param x		  x coordinate
	 * @param y		  y coordinate
	 */
	public static void renderSprite(Texture sprite, float x, float y) {
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
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
	
	/**
	 * Main function
	 * @param args	command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Ordinance V0.0.8");
		app = new Ordinance();
		app.init();
		app.loop();
	}
}
