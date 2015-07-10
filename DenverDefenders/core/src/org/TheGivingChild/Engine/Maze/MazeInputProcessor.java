package org.TheGivingChild.Engine.Maze;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenMaze;
import org.TheGivingChild.Screens.ScreenTransition;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

// Maps inputs to operations on the maze screen
public class MazeInputProcessor implements InputProcessor {
	/** Vector to store the last touch of the user */
	private Vector2 lastTouch;
	private ScreenMaze mazeScreen;
	private TGC_Engine game;
	
	// Distance a drag must be to be detected
	private final float DRAG_TOLERANCE = 30f;
	
	public MazeInputProcessor(ScreenMaze mazeScreen, TGC_Engine game) {
		this.mazeScreen = mazeScreen;
		this.game = game;
		lastTouch = new Vector2();
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return true;
	}
	
	/**Override the back button to show the main menu for Android*/
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK || keyCode == Keys.E){
			ScreenTransition mazeToMain = new ScreenTransition(ScreenAdapterEnums.MAZE, ScreenAdapterEnums.MAIN);
			game.setScreen(mazeToMain);
	    }
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 * Gets where a user first touched down on their device and 
	 * saves its position as a vector.
	 * @param screenX x coordinate of users down touch
	 * @param screenY y coordinate of users down touch
	 * @param pointer not used
	 * @param button not used
	 */

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Get vector of touch down to calculate change in movement during swipe
		lastTouch = new Vector2(screenX, screenY);
		return true;
	}

	/**
	 * Gets where a user touches up from their device and saves
	 * its position as a vector. Uses the touch up and the previous recorded
	 * touch to determine what direction the swipe was in and sets the
	 * players movement to the corresponding direction.
	 * @param screenX x coordinate of users up touch
	 * @param screenY y coordinate of users up touch
	 * @param pointer not used
	 * @param button not used
	 */

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//vectors contain the new position where the swipe ended
		Vector2 delta = new Vector2(screenX, screenY);
		//calculate the difference between the begin and end point
		delta.sub(lastTouch);
		// Ignore if the drag was short
		if (delta.len() < DRAG_TOLERANCE) return false;
		
		//if the magnitude of x is greater than the y, then move the sprite in the horizontal direction
		if (Math.abs(delta.x) > Math.abs(delta.y)) {
			//if the change was positive, move right, else move left
			if(delta.x > 0)  {
				mazeScreen.setTargetDirection(Direction.RIGHT);
			}
			if(delta.x < 0) {
				mazeScreen.setTargetDirection(Direction.LEFT);
			}

		}
		//otherwise y>=x so move vertically 
		else {
			// Y screen axis points down
			if(delta.y > 0)	{
				mazeScreen.setTargetDirection(Direction.DOWN);
			}
			if(delta.y < 0) {
				mazeScreen.setTargetDirection(Direction.UP);
			}
		}
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return true;
	}


}
