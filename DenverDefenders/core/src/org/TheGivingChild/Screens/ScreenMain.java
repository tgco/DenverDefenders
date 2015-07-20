package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.UI.CurtainScreenTransition;
import org.TheGivingChild.Screens.UI.UIScreenAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * This class creates the main screen that is first seen when the game is started. It allows
 * for navigation between all screens that we have created. It has a table of buttons on
 * the bottom of the screen that allow you to navigate to the play screen, how to play
 * screen, and options screen.
 * @author ctokunag
 */
class ScreenMain extends UIScreenAdapter {
	private float buttonHeight;
	private Table mainScreenTable, labelTable;
	private Skin skin;
	private TGC_Engine game;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", 
												"Button_MainScreen_Play", 
												"ButtonPressed_MainScreen_HowToPlay", 
												"Button_MainScreen_HowToPlay",
												"ButtonPressed_MainScreen_Options", 
												"Button_MainScreen_Options"
												};
	
	public ScreenMain() {
		// batch is needed for screen transition
		batch = new SpriteBatch();
		game = ScreenAdapterManager.getInstance().game;
		background = game.getAssetManager().get("ColdMountain.png", Texture.class);
		skin = new Skin();
		mainScreenTable = createMainScreenTable();
		labelTable = createLabel();
	}

	private Table createMainScreenTable() {
		//create a table for the buttons
		Table table = new Table();
		//adds the proper textures to skin from the asset manager
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		//variable to keep track of button height for table positioning
		int widthDivider = (buttonAtlasNamesArray.length/2);
		//iterate over button pack names in order to check
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += 2 ){
			ButtonStyle bs = new ButtonStyle();
			bs.down = skin.getDrawable(buttonAtlasNamesArray[i]);
			bs.up = skin.getDrawable(buttonAtlasNamesArray[i+1]);
			Button b = new Button(bs);
			b.setSize(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
			table.add(b).size(Gdx.graphics.getWidth()/widthDivider/2, Gdx.graphics.getHeight()/3/2).pad((Gdx.graphics.getWidth()/200)*(buttonAtlasNamesArray.length/2));
			buttonHeight = b.getHeight();
			final int j = i;
			//button to transition to different screens.
			b.addListener(new MyChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
					// Create a transition
					CurtainScreenTransition mainToOther = new CurtainScreenTransition(ScreenAdapterEnums.MAIN, ScreenAdapterEnums.values()[j/2]); // INTRODUCES DEPENDENCY ON ORDER OF THE ENUM
					game.setScreen(mainToOther);
				}
			});
		}
		table.setPosition(Gdx.graphics.getWidth()/2, buttonHeight/2);
		return table;
	}
	
	public Table createLabel() {
		Table table = new Table();
		skin.add("labelBack",game.getAssetManager().get("SemiTransparentBG.png"));
		LabelStyle ls = new LabelStyle();
		ls.font = game.getBitmapFontButton();
		ls.background = skin.getDrawable("labelBack");
		Label gameName = new Label("Denver Defenders", ls);
		gameName.setFontScale(game.getGlobalFontScale()*3f);
		gameName.setColor(Color.WHITE);
		table.add(gameName).top();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		return table;
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}

	@Override
	public void show() {
		// Add buttons when screen is shown
		game.getStage().addActor(labelTable);
		game.getStage().addActor(mainScreenTable);
	}

	@Override
	public void hide() {
		// Hide buttons on screen switch
		mainScreenTable.remove();
		labelTable.remove();
	}
	
	@Override
	public void dispose() {
		skin.dispose();
		batch.dispose();
	}

	public static void requestAssets(AssetManager manager) {
		
	}
}
