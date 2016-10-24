package ordinance;

public class Controls {
	public final int LEFT;
	public final int RIGHT;
	public final int UP;
	public final int DOWN;
	public final int RESTART;
	public final int GRAVITY;
	public final int QUIT;
	
	public Controls(final int keys[]) throws IllegalArgumentException {
		if (keys.length == 7) {
			LEFT = keys[0];
			RIGHT = keys[1];
			UP = keys[2];
			DOWN = keys[3];
			RESTART = keys[4];
			GRAVITY = keys[5];
			QUIT = keys[6];
		} else throw new IllegalArgumentException();
	}
	
}
