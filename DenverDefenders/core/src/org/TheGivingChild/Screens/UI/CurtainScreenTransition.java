package org.TheGivingChild.Screens.UI;

import java.util.Random;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

// This class takes a reference to two screen enums and transfers between them.
// Asks the incoming screen for which assets to load and completes the loading before
// setting the new screen.
// Author: Walter Schlosser
public class CurtainScreenTransition extends ScreenTransition {
	// Two curtain textures to close/open
	private Array<TextureRegion> transitionTextures;
	// The coordinate of the transition textures
	private float transitionTextureX;
	private SpriteBatch batch;
	// Time for curtains to open/close
	private float transitionTime;
	private Random rand;
	// Possible facts to show
	private static final String[] FACTS = {"The number of children living in poverty has increased 85 percent since 2000.\n--Colorado Coalition for the Homeless",
			  "The key characteristics of the 1/3 of children who end up making it in life have high self-esteem, hope (future sense of self), good social skills, positive peer influence, self-confidence and independence.\n--Heart and Hand",
			  "Heart and Hand provides hot, nutritious meals to kids along with academic support and enrichment activities!",
			  "Many people don't know this but Heart and Hand is a disguise for Hero Headquarters...And Hero Headquarters needs your Superhero powers! Are you ready to help?!"};
	private Table factTable;
	private Button nextButton;
	
	// If true, calls init on the screen about to be shown
	// REFACTOR: this is a workaround to call init on the maze screen by casting since init is not in the base class.  Could be better.
	private boolean initCall;
	
	// Constructor that builds a transition with a random fact
	public CurtainScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn) {
		super(screenOut, screenIn);
		transitionTextures = new Array<TextureRegion>();
		batch = new SpriteBatch();
		// Init the position of the transition textures to 0
		transitionTextureX = 0;
		transitionTime = 0.3f;
		rand = new Random();
		// Get references to the transition textures from an asset manager
		fillTransitionTextures(transitionTextures, ScreenAdapterManager.getInstance().game.getAssetManager());
		factTable = buildFactTable(ScreenAdapterManager.getInstance().game.getAssetManager(), FACTS[rand.nextInt(FACTS.length)]);
		initCall = false;
	}
	
	// Constructor that builds a transition with given text
	public CurtainScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn, String text) {
		super(screenOut, screenIn);
		transitionTextures = new Array<TextureRegion>();
		batch = new SpriteBatch();
		// Init the position of the transition textures to 0
		transitionTextureX = 0;
		transitionTime = 0.3f;
		rand = new Random();
		// Get references to the transition textures from an asset manager
		fillTransitionTextures(transitionTextures, ScreenAdapterManager.getInstance().game.getAssetManager());
		factTable = buildFactTable(ScreenAdapterManager.getInstance().game.getAssetManager(), text);
		initCall = false;
	}
	
	// Constructor that builds a transition with a random fact and calls init on screenIn
	public CurtainScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn, boolean initCall) {
		super(screenOut, screenIn);
		this.initCall = initCall;
		transitionTextures = new Array<TextureRegion>();
		batch = new SpriteBatch();
		// Init the position of the transition textures to 0
		transitionTextureX = 0;
		transitionTime = 0.3f;
		rand = new Random();
		// Get references to the transition textures from an asset manager
		fillTransitionTextures(transitionTextures, ScreenAdapterManager.getInstance().game.getAssetManager());
		factTable = buildFactTable(ScreenAdapterManager.getInstance().game.getAssetManager(), FACTS[rand.nextInt(FACTS.length)]);
	}
	
	public void fillTransitionTextures(Array<TextureRegion> transitionTextures, AssetManager manager) {
		// Retrieve atlas, which must be loaded on initial app creation
		TextureAtlas screenTransitionAtlas = manager.get("Packs/ScreenTransitions.pack");
		for(AtlasRegion texture : screenTransitionAtlas.getRegions()){
			transitionTextures.add(texture);
		}
	}
	
	// Builds a table for the fact label shown on closed transition
	public Table buildFactTable(AssetManager manager, String text) {
		// Overall container
		final Table container = new Table();
		container.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		container.setPosition(0,0);
		
		// Build the next button
		Table buttonContainer = new Table();
		Skin skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/Buttons.pack"));
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("Button_Next");
		style.down = skin.getDrawable("ButtonPressed_Next");
		style.disabled = new TextureRegionDrawable(new TextureRegion(manager.get("loadingButton.png", Texture.class)));
		nextButton = new Button(style);
		nextButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				state = TransitionState.DRAW_OUT;
				container.remove();
			}
		});
		buttonContainer.add(nextButton).center().bottom();
		buttonContainer.setPosition(Gdx.graphics.getWidth()/2, buttonContainer.getHeight());
		
		// Build the fact table
		Table factContainer = new Table();
		LabelStyle ls = new LabelStyle();
		ls.font = new BitmapFont();
		Label textLabel = new Label(text, ls);
		textLabel.setColor(1, 1, 1, 1);
		// Font scale set
		textLabel.setFontScale(ScreenAdapterManager.getInstance().game.getGlobalFontScale());
		textLabel.setWrap(true);
		textLabel.setAlignment(Align.center, Align.center);
		factContainer.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		factContainer.add(textLabel).width(Gdx.graphics.getWidth()/2);
		
		// Add to container
		container.row();
		container.add(buttonContainer);
		container.row();
		container.add(factContainer).align(Align.center);
		
		// Disable next button, enabled when load is done
		nextButton.setDisabled(true);
		
		return container;
	}
	
	@Override
	public void render(float delta) {
		// Reference to width/height of screen
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		// Reference to the textures to draw
		TextureRegion left = transitionTextures.get(0);
		TextureRegion right = transitionTextures.get(1);
		
		// Update movement of transition textures
		switch (state) {
		case DRAW_IN:
			ScreenAdapterManager.getInstance().getScreenFromEnum(screenOut).render(delta);
			if (close(delta) == true) state = TransitionState.MESSAGE_SET;
			break;
		case MESSAGE_SET:
			addTables(ScreenAdapterManager.getInstance().game);
			state = TransitionState.ASSET_REQUEST;
			break;
		case ASSET_REQUEST:
			requestAssets();
			state = TransitionState.WAIT_FOR_LOAD;
		case WAIT_FOR_LOAD:
			processRequests();
			break;
		case DRAW_OUT:
			ScreenAdapterManager.getInstance().getScreenFromEnum(screenIn).render(delta);
			if (open(delta) == true) ScreenAdapterManager.getInstance().show(screenIn);
			break;
		}
		
		// Draw transition textures
		batch.begin();
		batch.draw(left, transitionTextureX - width/2, 0, width/2, height);
		batch.draw(right, width - transitionTextureX, 0, width/2, height);
		batch.end();
	}
	
	// Progresses the transition textures towards each other. True if done
	public boolean close(float delta) {
		transitionTextureX += Gdx.graphics.getWidth()/2 * delta/transitionTime;
		if (transitionTextureX > Gdx.graphics.getWidth()/2) {
			// Clean up any peeking through the sides
			transitionTextureX = Gdx.graphics.getWidth()/2;
			return true;
		}
		return false;
	}
	
	// Runs while the curtains are closed and the button is disabled
	public void processRequests() {
		if (manager.update()) {
			nextButton.setDisabled(false);
			// Run init once if requested, assets are done loading by now
			if (initCall) {
				initCall = false;
				( (ScreenMaze) ScreenAdapterManager.getInstance().getScreenFromEnum(screenIn) ).init();
			}
		}
	}
	
	// Adds a fact table and button to game stage
	public void addTables(TGC_Engine game) {
		game.getStage().addActor(factTable);
	}
	
	// Requests asset loading from manager
	public void requestAssets() {
		screenIn.requestAssets(manager);
	}
	
	// Opens transition textures.  True if done.
	public boolean open(float delta) {
		transitionTextureX -= Gdx.graphics.getWidth()/2 * delta/transitionTime;
		if (transitionTextureX < 0) return true;
		return false;
	}
	
	@Override
	public void hide() {
		factTable.remove();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
