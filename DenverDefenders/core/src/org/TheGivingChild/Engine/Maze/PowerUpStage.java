package org.TheGivingChild.Engine.Maze;
import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.PowerUps.PowerUp;
import org.TheGivingChild.Engine.PowerUps.PowerUpEnum;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

// Stage for unlocked power ups shown/used as the maze screen HUD
// Author: Walter Schlosser
public class PowerUpStage extends Stage {
	private Table powerupButtons;
	private static final float BUTTON_SIZE = Gdx.graphics.getHeight()/6f;
	
	public PowerUpStage(TGC_Engine game, ScreenMaze mazeScreen) {
		powerupButtons = constructButtons(game, mazeScreen);
		// Add to stage
		this.addActor(powerupButtons);
	}
	
	// Constructs unlocked buttons for powerups and creates a vertical table
	public Table constructButtons(TGC_Engine game, final ScreenMaze mazeScreen) {
		Table verticalGroup = new Table();
		// Get list of unlocked powerups
		final Array<String> unlocked = game.data.getUnlockedPowerUps(ScreenMaze.mazeType);
		// Create a button for each
		for (int i = 0; i < unlocked.size; i++) {
			// Get the button images
			Texture buttonTex = game.getAssetManager().get("PowerUps/" + unlocked.get(i) + "/button.png", Texture.class);
			Texture buttonUsedTex = game.getAssetManager().get("PowerUps/" + unlocked.get(i)+ "/buttonUsed.png", Texture.class);
			// Create button and listener
			ButtonStyle bs = new ButtonStyle();
			bs.up = new TextureRegionDrawable( new TextureRegion( buttonTex ) );
			bs.disabled = new TextureRegionDrawable( new TextureRegion( buttonUsedTex) );
			Button toAdd = new Button(bs);
			toAdd.setSize(BUTTON_SIZE, BUTTON_SIZE);
			final String powerName = unlocked.get(i);
			toAdd.addListener(new MyChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
					// Add the powerup to the active ones in the maze
					PowerUp addMe = PowerUpEnum.valueOf(powerName.toUpperCase()).construct();
					mazeScreen.addPowerUp(addMe);
					// Change this button to disabled
					((Button) actor).setDisabled(true);
				}
			});
			verticalGroup.add(toAdd).size(BUTTON_SIZE, BUTTON_SIZE).pad(BUTTON_SIZE/4f);
			verticalGroup.row();
		}
		// Adjust group
		verticalGroup.setSize(BUTTON_SIZE*1.5f, Gdx.graphics.getHeight());
		verticalGroup.align(Align.top);
		verticalGroup.setPosition(Gdx.graphics.getWidth() - BUTTON_SIZE*3f/2f, 0);
		
		return verticalGroup;
	}
}
