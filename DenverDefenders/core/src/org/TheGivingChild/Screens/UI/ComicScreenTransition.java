package org.TheGivingChild.Screens.UI;

import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

// Gray scales out, moves to other screen, gray scales in
public class ComicScreenTransition extends ScreenTransition {
	// Camera to zoom, move between screens
	private OrthographicCamera cam;
	// Draw a sheet of gray over everything to avoid writing a grayscale shader
	private Sprite grayScale;
	// Alpha for drawing the grayScale with varying transparency;
	private float alpha;
	// Batch to draw gray scale with
	private SpriteBatch batch;
	// Coordinates of where the camera should end
	private Vector3 camTarget;
	
	// Multiplier for cam zoom dimensions each frame
	private static final float ZOOM_AMOUNT = 1.002f;

	public ComicScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn, Direction dir) {
		super(screenOut, screenIn);
		// Init cam to default settings
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(new Vector3(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f,0));
		grayScale = new Sprite(manager.get("SemiTransparentBG.png", Texture.class));
		grayScale.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// start with no gray scale
		alpha = 0;
		batch = new SpriteBatch();
		// Set cam target based on direction to move (always moves down, can set left or right)
		switch (dir) {
		case LEFT:
			camTarget = new Vector3(-Gdx.graphics.getWidth()/2f, -Gdx.graphics.getHeight()/2f, 0);
			break;
		case RIGHT: default:
			camTarget = new Vector3(3f/2f * Gdx.graphics.getWidth(), -Gdx.graphics.getHeight()/2f, 0);
			break;
		}
	}
	
	@Override
	public void render(float delta) {
		switch (state) {
		case DRAW_IN:
			// gray scale out the current screen
			if (grayScaleOut(screenOut))
				state = TransitionState.ASSET_REQUEST;
			break;
		case MESSAGE_SET:
			// no messages or table, just move on (shouldn't reach this state anyway)
			state = TransitionState.ASSET_REQUEST;
			break;
		case ASSET_REQUEST:
			// request the assets and move on
			requestAssets();
			state = TransitionState.WAIT_FOR_LOAD;
			break;
		case WAIT_FOR_LOAD:
			// completes loading and then moves camera
			if (loadAndMove(ScreenAdapterManager.getInstance().getScreenFromEnum(screenOut), ScreenAdapterManager.getInstance().getScreenFromEnum(screenIn)))
				state = TransitionState.DRAW_OUT;
			break;
		case DRAW_OUT:
			// gray scale in the new screen, zoom in
			if (grayScaleIn(screenIn)) {
				//reset cams
				( (UIScreenAdapter) ScreenAdapterManager.getInstance().getScreenFromEnum(screenOut) ).getSpriteBatch().setProjectionMatrix(cam.combined);
				( (UIScreenAdapter) ScreenAdapterManager.getInstance().getScreenFromEnum(screenIn) ).getSpriteBatch().setProjectionMatrix(cam.combined);
				ScreenAdapterManager.getInstance().show(screenIn);
			}
			break;
		}
	}
	
	// Draws the screen with increasing gray scale
	// true if done;
	public boolean grayScaleOut(ScreenAdapterEnums screen) {
		// zoom out
		cam.viewportWidth *= ZOOM_AMOUNT;
		cam.viewportHeight *= ZOOM_AMOUNT;
		cam.update();
		((UIScreenAdapter) ScreenAdapterManager.getInstance().getScreenFromEnum(screen) ).getSpriteBatch().setProjectionMatrix(cam.combined);
		// Render the screen
		ScreenAdapterManager.getInstance().getScreenFromEnum(screen).render(Gdx.graphics.getDeltaTime());
		// Find new alpha
		alpha += 2.5 * Gdx.graphics.getDeltaTime();
		// Clamp 0 to 1
		float clampedAlpha = Math.max(0, Math.min(1, alpha));
		// Draw grayscale
		batch.begin();
		grayScale.draw(batch, clampedAlpha);
		batch.end();
		
		// Wait till 2 to stall on the leaving screen for a bit
		if (alpha >= 1.5) {
			alpha = 1;
			return true;
		}
		else return false;
	}
	
	// Loads requests in manager, moves cam from screen out to in once done
	// true if done
	public boolean loadAndMove(ScreenAdapter screenOut, ScreenAdapter screenIn) {
		// cast
		UIScreenAdapter screenA = (UIScreenAdapter) screenOut;
		UIScreenAdapter screenB = (UIScreenAdapter) screenIn;
		
		if (manager.update()) {
			// find new camera coordinates
			cam.position.interpolate(camTarget, 2*Gdx.graphics.getDeltaTime(), Interpolation.swingOut);
			//draw screen A with current cam position
			cam.update();
			screenA.getSpriteBatch().setProjectionMatrix(cam.combined);
			screenA.render(Gdx.graphics.getDeltaTime());
			//draw screen B with translation
			Vector3 temp = cam.position.cpy();
			Vector3 delta = cam.position.cpy().sub(camTarget);
			cam.position.set(Gdx.graphics.getWidth()/2f + delta.x, Gdx.graphics.getHeight()/2f + delta.y, 0);
			cam.update();
			screenB.getSpriteBatch().setProjectionMatrix(cam.combined);
			screenB.render(Gdx.graphics.getDeltaTime());
			// gray scale
			batch.begin();
			grayScale.draw(batch, 1);
			batch.end();
			// determine if done
			cam.position.set(temp);
			if (delta.len() < 5f) {
				// snap to targ by camera reset
				cam.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0);
				screenB.getSpriteBatch().setProjectionMatrix(cam.combined);
				screenA.getSpriteBatch().setProjectionMatrix(cam.combined);
				return true;
			}
			return false;
		} else {
			// Draw first screen still
			screenOut.render(Gdx.graphics.getDeltaTime());
			// Draw grayscale
			batch.begin();
			grayScale.draw(batch, alpha);
			batch.end();
			return false;
		}
	}
	
	// Draws the screen with decreasing gray scale
	// true if done
	public boolean grayScaleIn(ScreenAdapterEnums screen) {
		cam.viewportWidth /= ZOOM_AMOUNT;
		cam.viewportHeight /= ZOOM_AMOUNT;
		cam.update();
		((UIScreenAdapter) ScreenAdapterManager.getInstance().getScreenFromEnum(screen) ).getSpriteBatch().setProjectionMatrix(cam.combined);
		// Render the screen
		ScreenAdapterManager.getInstance().getScreenFromEnum(screen).render(Gdx.graphics.getDeltaTime());
		// Find new alpha
		alpha -= 2.5 * Gdx.graphics.getDeltaTime();
		// Clamp 0 to 1
		float clampedAlpha = Math.max(0, Math.min(1, alpha));
		// Draw grayscale
		batch.begin();
		grayScale.draw(batch, clampedAlpha);
		batch.end();

		if (alpha <= -0.5) {
			cam.viewportWidth = Gdx.graphics.getWidth();
			cam.viewportHeight = Gdx.graphics.getHeight();
			cam.update();
			return true;
		}
		else return false;
	}
	
	// Requests asset loading from manager
	public void requestAssets() {
		// Draw to avoid white flash
		ScreenAdapterManager.getInstance().getScreenFromEnum(screenOut).render(Gdx.graphics.getDeltaTime());
		batch.begin();
		grayScale.draw(batch, 1);
		batch.end();
		screenIn.requestAssets(manager);
	}

}
