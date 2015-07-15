package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Zooms the camera out to increase the original viewport size for a set time
// Also doubles the players speed
public class CapePowerUp extends PowerUp {
	// Zoom states
	private enum ZoomState { WAIT, OUT, PAUSED, IN; }
	private ZoomState zoomState = ZoomState.WAIT;
	// Timer in sec to stay zoomed out
	private float timer = 10f;
	// Initial size
	private float initialViewX;
	private float initialViewY;
	// Factor to zoom out to (factor*initialViewDimension)
	private float zoomFactor = 2;
	
	@Override
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch) {
		switch (zoomState) {
		case WAIT:
			// Speed up
			mazeScreen.getPlayerCharacter().setSpeed(2 * mazeScreen.getPlayerCharacter().getSpeed());
			// Get initial coords to move on
			initialViewX = mazeScreen.getCamera().viewportWidth;
			initialViewY = mazeScreen.getCamera().viewportHeight;
			zoomState = ZoomState.OUT;
			break;
		case OUT:
			// Zoom out, returns true when done
			if ( zoomOut(mazeScreen.getCamera()) ) zoomState = ZoomState.PAUSED;
			break;
		case PAUSED:
			// Decrement timer
			timer -= Gdx.graphics.getDeltaTime();
			if (timer <= 0) zoomState = ZoomState.IN;
			break;
		case IN:
			// Zoom in, true if done, last stage for this power
			if ( zoomIn(mazeScreen.getCamera()) ) {
				mazeScreen.getPlayerCharacter().setSpeed(0.5f * mazeScreen.getPlayerCharacter().getSpeed());
				return true;
			}
			break;
		}
		
		return false;
	}
	
	// Zooms out the cameras viewport with linear interpolation.
	// Returns true when done
	private boolean zoomOut(OrthographicCamera cam) {
		// Linearly interpolate to the new viewport size
		float deltaX = zoomFactor * initialViewX - cam.viewportWidth;
		float deltaY = zoomFactor * initialViewY - cam.viewportHeight;
		// Snap and finish if close enough to new view size
		// Test deltaX since it has the larger distance to travel in landscape
		if (deltaX < 5f) {
			cam.viewportWidth = zoomFactor * initialViewX;
			cam.viewportHeight = zoomFactor * initialViewY;
			// Zoom done
			return true;
		}
				
		// Otherwise interpolate
		cam.viewportWidth += deltaX * 2*Gdx.graphics.getDeltaTime();
		cam.viewportHeight += deltaY * 2*Gdx.graphics.getDeltaTime();
		
		return false;
	}
	
	// Zooms in the camera's viewport with linear interpolation
	// Return true when done
	private boolean zoomIn(OrthographicCamera cam) {
		// Linear interpolation
		float deltaX = cam.viewportWidth - initialViewX;
		float deltaY = cam.viewportHeight - initialViewY;
		// Snap and finish if close enough
		// Testing on x since it has the larger distance to go in landscape
		if (deltaX < 5f) {
			cam.viewportWidth = initialViewX;
			cam.viewportHeight = initialViewY;
			// Zoom done
			return true;
		}
		
		// Otherwise interpolate
		// 2 for testing speed purposes
		cam.viewportWidth -= deltaX * 2*Gdx.graphics.getDeltaTime();
		cam.viewportHeight -= deltaY * 2*Gdx.graphics.getDeltaTime();
		
		return false;
	}

}
