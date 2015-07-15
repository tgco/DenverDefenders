package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Engine.Maze.ChildSprite;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Screens.ScreenAdapterManager;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

// Power up which shows the player the general direction to the closest kid in the maze.
public class MaskPowerUp extends PowerUp {

	@Override
	// Returns true if this power is done
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch) {
		// Find the closest child to the player
		PlayerSprite player = mazeScreen.getPlayerCharacter();
		Vector2 playerPos = new Vector2(player.getX(), player.getY());
		ChildSprite closest = null;
		float closestDist = Float.POSITIVE_INFINITY;
		for (ChildSprite cs : mazeScreen.getMazeChildren()) {
			if (!cs.getFollow() && playerPos.cpy().sub(cs.getX(), cs.getY()).len() < closestDist) {
				closest = cs;
				closestDist = playerPos.cpy().sub(cs.getX(), cs.getY()).len();
			}
		}
		
		// Return if no children left
		if (closest == null) {
			return true;
		}
		
		// If close enough, this power is done, return
		if (closestDist < mazeScreen.getMaze().getPixWidth()) {
			return true;
		}
		
		// Draw triangle above child if on screen, or around the player
		TextureRegion arrow = new TextureRegion( ScreenAdapterManager.getInstance().game.getAssetManager().get("PowerUps/mask/arrow.png", Texture.class) );
		if (onScreen(closest, mazeScreen.getCamera())) {
			// Triangle rotated and above
			batch.draw(arrow, closest.getX(), closest.getY() + closest.getHeight(), mazeScreen.getMaze().getPixWidth()/2, mazeScreen.getMaze().getPixHeight()/2, closest.getWidth(), closest.getHeight(), 1, 1, -135);
		} else {
			// Find the point relative to the player position (screen center) to draw arrow towards
			Vector2 delta = new Vector2(closest.getX(), closest.getY());
			delta.sub(playerPos);
			batch.draw(arrow, playerPos.x + mazeScreen.getMaze().getPixWidth()/2, playerPos.y + mazeScreen.getMaze().getPixHeight()/2, 0, 0, mazeScreen.getMaze().getPixWidth(), mazeScreen.getMaze().getPixHeight(), 1, 1, delta.angle() - 45);
		}
		
		return false;
	}
	
	// Determines if the sprite is on screen
	public boolean onScreen(ChildSprite cs, OrthographicCamera cam) {
		// get screen coords
		Vector3 screenCoords = cam.project(new Vector3(cs.getX(), cs.getY(), 0));
		if (screenCoords.x + cs.getWidth() > 0 && screenCoords.x < Gdx.graphics.getWidth()) {
			if (screenCoords.y + cs.getHeight() > 0 && screenCoords.y < Gdx.graphics.getHeight()) {
				return true;
			} else return false;
		} else return false;
	}

}
