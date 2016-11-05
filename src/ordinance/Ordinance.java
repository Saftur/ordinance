package ordinance;

import java.io.IOException;

import com.ivan.xinput.*;
import com.ivan.xinput.enums.XInputAxis;
import com.ivan.xinput.enums.XInputButton;
import com.ivan.xinput.listener.SimpleXInputDeviceListener;
import com.ivan.xinput.listener.XInputDeviceListener;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ordinance.entity.Bullet;
import ordinance.entity.Enemy;
import ordinance.entity.Entity.Shape;
import ordinance.entity.Planet;
import ordinance.entity.Player;
import ordinance.entity.Ship;
import ordinance.entity.Weapon;

import static org.lwjgl.opengl.GL11.*;


/**
 * Ordinance game
 * @author Arthur Bouvier
 * @author Brandon Yue
 */
public class Ordinance {
	public static Ordinance app;
	private static final float STICK_DEADZONE = 0.2f;
	private static final float TRIGGER_PRESSED = 0.5f;
	
	static public int mouseX=0, mouseY=0;
	
	public Map map;
	public Ship player;
	private Controls controls;
	private Gamepad gamepad = null;
	//private XInputDevice gamepad = null;
	//private XInputComponents gamepadComps = null;
	//private Controller gamepad;
	
	public int width=1600, height=1200;
	public int mapWidth=1600, mapHeight=1200;
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
		
		if (XInputDevice14.isAvailable()) {
			System.out.println("XInput 1.4 is available on this platform.");
			
			try {
				XInputDevice14[] devices = XInputDevice14.getAllDevices();
				gamepad = new Gamepad(devices[0]);
				XInputDeviceListener listener = new SimpleXInputDeviceListener() {
					@Override
					public void connected() {
						System.out.println("Connected");
					}
					@Override
					public void disconnected() {
						System.out.println("Disconnected");
					}
					@Override
					public void buttonChanged(final XInputButton button, final boolean pressed) {
						//System.out.println("Button");
					}
				};
				gamepad.addListener(listener);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (gamepad.isConnected())
				System.out.println("Controller connected");
		} else if (XInputDevice.isAvailable()) {
			System.out.println("XInput 1.3 is available on this platform.");
			
			try {
				XInputDevice[] devices = XInputDevice.getAllDevices();
				gamepad = new Gamepad(devices[0]);
				XInputDeviceListener listener = new SimpleXInputDeviceListener() {
					@Override
					public void connected() {
						System.out.println("Connected");
					}
					@Override
					public void disconnected() {
						System.out.println("Disconnected");
					}
					@Override
					public void buttonChanged(final XInputButton button, final boolean pressed) {
						//System.out.println("Button");
					}
				};
				gamepad.addListener(listener);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (gamepad.isConnected())
				System.out.println("Controller connected");
		}
		
		final int keys[] =
			{Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_W, Keyboard.KEY_S,
			 Keyboard.KEY_R, Keyboard.KEY_T, Keyboard.KEY_ESCAPE};
		try {
			controls = new Controls(keys);
		} catch (IllegalArgumentException e) {}
		
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
		
		//					   {maxspd, spdinc, spddec,    hp}
		float playerStats[]  = {    6f,    .4f,    .3f,   100,  60};
		float planetStats[]  = {    6f,    .4f,    .3f};
		float enemy1Stats[]  = {    5f,    .4f,    .3f,   100,   1,   1};
		float enemy2Stats[]  = {    0f,     0f,     0f,   100,   1,   0};
		
		float bulletStats[]  = {   10f,   120f};
		//					   {shtspd,  shdel, shtnum}
		float weapon1Stats[] = {   60f,    60f,   100f};
		float weapon2Stats[] = {   20f,    30f,   100f};
		player = new Player("ship", "player", Shape.CIRC, 64, 64, playerStats, 32, 32);
		player.enableGravity(false);
		Planet planet  = new Planet("planet1", Shape.CIRC, 64, 64, planetStats, 32, 32);
		Bullet bullet  = new Bullet("laser",   Shape.RECT, 32, 16, bulletStats, 16, 8);
		Weapon weapon1 = new Weapon("bullet",  Shape.RECT, 32, 16, bullet, weapon1Stats, -32, 8);
		Weapon weapon2 = new Weapon("bullet",  Shape.RECT, 32, 16, bullet.copy(), weapon2Stats, -32, 8);
		Enemy enemy1   =  new Enemy("player",  Shape.CIRC, 64, 64, enemy1Stats, 32, 32);
		Enemy enemy2   =  new Enemy("player",  Shape.CIRC, 64, 64, enemy2Stats, 32, 32);
		player.getWeapon(weapon1);
		enemy2.getWeapon(weapon2);
		//planet.moveTo(width/2-32, height/2-32);
		map = new Map(mapWidth, mapHeight, player);
		player.moveTo(32, 32);
		map.addEntity(planet);
		map.addEntity(enemy1, mapWidth-32, mapHeight-32);
		map.addEntity(enemy2, mapWidth-32, 32);
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
		//System.out.println(player.rot);
		System.out.println(player.hp);
		//player.accelDir(1, 45, delta);
		map.update(delta);
		if(player.hp <= 0){
			Display.destroy();
			init();
		}
	}
	
	/**
	 * Poll input from devices
	 */
	private void pollInput() {
		if (gamepad.isConnected()) {
			if (gamepad.poll()) {
				float posLX = gamepad.axes.get(XInputAxis.LEFT_THUMBSTICK_X);
				float posLY = gamepad.axes.get(XInputAxis.LEFT_THUMBSTICK_Y);
				float posRX = gamepad.axes.get(XInputAxis.RIGHT_THUMBSTICK_X);
				float posRY = gamepad.axes.get(XInputAxis.RIGHT_THUMBSTICK_Y);
				//float posLT = gamepad.axes.get(XInputAxis.LEFT_TRIGGER);
				float posRT = gamepad.axes.get(XInputAxis.RIGHT_TRIGGER);
				if (Math.abs(posLX) > STICK_DEADZONE || Math.abs(posLY) > STICK_DEADZONE) {
					//System.out.println(posLX + ", " + posLY);
					//player.accel(posLX, -posLY, Math.abs(posLX), Math.abs(posLY), delta);
					float scl = (float)Math.sqrt(posLX*posLX+posLY*posLY);
					float dir = player.angleTo(player.x+posLX, player.y-posLY)+player.rot;
					player.accelDir(scl, dir, scl, delta);
				}
				if (Math.abs(posRX) > STICK_DEADZONE || Math.abs(posRY) > STICK_DEADZONE) {
					//System.out.println(posRX + ", " + posRY);
					player.pointTo(player.x+posRX, player.y-posRY);
				}
				if (posRT > TRIGGER_PRESSED) {
					player.shoot();
				}
				
				if (gamepad.buttonsDelta.isPressed(XInputButton.LEFT_THUMBSTICK)) {
					Display.destroy();
					init();
				}
				if (gamepad.buttonsDelta.isPressed(XInputButton.BACK)) {
					player.toggleGravity();
				}
				if (gamepad.buttonsDelta.isPressed(XInputButton.START)) {
					System.exit(0);
				}
			}
		} else {
			mouseX = Mouse.getX();
			mouseY = height-Mouse.getY()-1;
			player.pointTo(Ordinance.mouseX, Ordinance.mouseY);
			if (Keyboard.isKeyDown(controls.LEFT)) player.accel(-1, 0, delta);
			if (Keyboard.isKeyDown(controls.RIGHT)) player.accel(1, 0, delta);
			if (Keyboard.isKeyDown(controls.UP)) player.accel(0, -1, delta);
			if (Keyboard.isKeyDown(controls.DOWN)) player.accel(0, 1, delta);
			if (Mouse.isButtonDown(0)) player.shoot();
			
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					final int key = Keyboard.getEventKey();
					if (key == controls.RESTART) {
						Display.destroy();
						init();
					} else if (key == controls.GRAVITY) {
						player.toggleGravity();
					} else if (key == controls.QUIT)
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
	
	public static void renderSprite(Texture sprite, float x, float y, float rot, float cx, float cy) {
		renderSprite(sprite, x, y, cx, cy, rot, sprite.getTextureWidth(), sprite.getTextureHeight());
	}
	
	/**
	 * Draw a sprite at specified coordinates
	 * @param sprite  texture to be rendered
	 * @param x		  x coordinate
	 * @param y		  y coordinate
	 * @param width	  sprite width
	 * @param height  sprite height
	 */
	public static void renderSprite(Texture sprite, float x, float y, float cx, float cy, float rot, float width, float height) {
		rot *= Math.PI/180;
		//System.out.println(rot);
		float sx = (float)(x-cx*Math.cos(rot)), sy = (float)(y-cx*Math.sin(rot));
		//float slope;
		float x1 = x-cx, x2 = x-cx+width, x3 = x-cx+width, x4 = x-cx;
		float y1 = y-cy, y2 = y-cy, y3 = y-cy+height, y4 = y-cy+height;
		if (rot == 180) {
			x1 = -x1; y1 = -y1;
			x2 = -x2; y2 = -y2;
			x3 = -x3; y3 = -y3;
			x4 = -x4; y4 = -y4;
		} else if (rot != 0) {
			//slope = (float)(-1/Math.tan(rot));
			x1 = (float)(sx+cy*Math.sin(rot));
			y1 = (float)(sy-cy*Math.cos(rot));
			x2 = (float)(x1+width*Math.cos(rot));
			y2 = (float)(y1+width*Math.sin(rot));
			x3 = (float)(x2-height*Math.sin(rot));
			y3 = (float)(y2+height*Math.cos(rot));
			x4 = (float)(x3-width*Math.cos(rot));
			y4 = (float)(y3-width*Math.sin(rot));
		}
		
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
		glBegin(GL_QUADS); {
			glTexCoord2f(0,0);
			glVertex2f(x1,y1);
			glTexCoord2f(1,0);
			glVertex2f(x2,y2);
			glTexCoord2f(1,1);
			glVertex2f(x3,y3);
			glTexCoord2f(0,1);
			glVertex2f(x4,y4);
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
