package ordinance;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;

import static org.lwjgl.opengl.GL11.*;

public class Ordinance {
	public static Ordinance app;
	
	public int width=800, height=600;
	public float x=width/2, y=height/2;
	public float rotation = 0;
	public int mx=0, my=0;
	private long lastFrameTime;
	private long lastFPSTime;
	private int fps;
	
	public void run() {
		init();
		loop();
	}
	
	private void init() {
		try {
			setDisplayMode(width, height, true);
			//Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, 0, height, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		getDelta();
		lastFPSTime = getTime();
	}
	
	private void loop() {
		int delta;
		while (!Display.isCloseRequested()) {
			delta = getDelta();
			
			update(delta);
			draw(delta);
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	private void update(int delta) {
		rotation += 0.15f * delta;
		pollInput(delta);
		if (x<0) x=0;
		if (x>=width) x=width-1;
		if (y<0) y=0;
		if (y>=height) y=height-1;
		updateFPS();
	}
	
	private void draw(int delta) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(0.5f, 0.5f, 1.0f);
		
		glPushMatrix(); {
			glTranslatef(x, y, 0);
			glRotatef(rotation, 0f, 0f, 1f);
			glTranslatef(-x, -y, 0);
			glBegin(GL11.GL_QUADS); {
				glVertex2f(x-50, y-50);
				glVertex2f(x+50, y-50);
				glVertex2f(x+50, y+50);
				glVertex2f(x-50, y+50);
			} glEnd();
		} glPopMatrix();
	}
	
	private void pollInput(int delta) {
		mx = Mouse.getX();
		my = Mouse.getY();
		
		if (Mouse.isButtonDown(0))
			System.out.println("Mouse down: "+mx+", "+my);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			System.out.println("Space key is down");
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y += 0.35f * delta;
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				switch(Keyboard.getEventKey()) {
				case Keyboard.KEY_A:
					System.out.println("A key pressed");
					break;
				case Keyboard.KEY_S:
					System.out.println("S key pressed");
					break;
				case Keyboard.KEY_D:
					System.out.println("D key pressed");
					break;
				}
			} else {
				switch(Keyboard.getEventKey()) {
				case Keyboard.KEY_A:
					System.out.println("A key released");
					break;
				case Keyboard.KEY_S:
					System.out.println("S key released");
					break;
				case Keyboard.KEY_D:
					System.out.println("D key released");
					break;
				}
			}
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
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fs="+fullscreen + e);
		}
	}
	
	
	public static void main(String[] args) {
		(app = new Ordinance()).run();
	}
}
