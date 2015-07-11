package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.PowerUps.PowerUpEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

// Displays an unlocked power and its description
public class ScreenUnlock extends ScreenAdapter {
	private TGC_Engine game;
	private Table unlockTable;
	
	public static String powerUpName;
	
	public ScreenUnlock() {
		this.game = ScreenAdapterManager.getInstance().game;
	}
	
	// Builds the table with the name and description of the power up
	public Table buildTable(String powerName) {
		// Create string labels
		String unlockMessage = "You unlocked the " + powerName + "!  Tap it to continue!";
		String powerDescription = PowerUpEnum.valueOf(powerName.toUpperCase()).description();
		
		// Create labels and button
		LabelStyle ls = new LabelStyle();
		ls.font = new BitmapFont();
		Label unlockMessageLabel = new Label(unlockMessage, ls);
		unlockMessageLabel.setColor(1, 1, 1, 1);
		// Font scale set
		unlockMessageLabel.setFontScale(ScreenAdapterManager.getInstance().game.getGlobalFontScale());
		unlockMessageLabel.setWrap(true);
		unlockMessageLabel.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		unlockMessageLabel.setAlignment(Align.center, Align.center);
		
		Label powerDescriptionLabel = new Label(powerDescription, ls);
		powerDescriptionLabel.setColor(1, 1, 1, 1);
		powerDescriptionLabel.setFontScale(ScreenAdapterManager.getInstance().game.getGlobalFontScale());
		powerDescriptionLabel.setWrap(true);
		powerDescriptionLabel.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		powerDescriptionLabel.setAlignment(Align.center, Align.center);
		
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable( new TextureRegion(game.getAssetManager().get("PowerUps/" + powerUpName + "/button.png", Texture.class) ));
		Button nextButton = new Button(bs);
		nextButton.setSize(Gdx.graphics.getHeight()/4, Gdx.graphics.getHeight()/4);
		nextButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				ScreenTransition unlockToMain = new ScreenTransition(ScreenAdapterEnums.UNLOCK, ScreenAdapterEnums.MAIN);
				game.setScreen(unlockToMain);
			}
		});
		
		// Make table
		Table t = new Table();
		t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		t.align(Align.center);
		t.setPosition(0, 0);
		t.add(unlockMessageLabel).width(Gdx.graphics.getWidth()/2);
		t.row();
		t.add(nextButton).size(Gdx.graphics.getHeight()/4);
		t.row();
		t.add(powerDescriptionLabel).width(Gdx.graphics.getWidth()/2);
		
		return t;
	}
	
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().backgroundImage();
	}
	
	@Override
	public void show() {
		// Rebuild table with powerup
		unlockTable = buildTable(powerUpName);
		// Add the table to the stage
		game.getStage().addActor(unlockTable);
	}
	
	@Override
	public void hide() {
		// Remove the table from the ui stage
		unlockTable.remove();
	}
	
	// Load the assets for the unlocked power
	public static void requestAssets(AssetManager manager) {
		// Load the button image
		manager.load("PowerUps/" + powerUpName + "/button.png", Texture.class);
	}

}
