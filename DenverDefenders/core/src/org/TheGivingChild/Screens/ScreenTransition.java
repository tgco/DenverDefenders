package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

// This class takes a reference to two screens and transfers between them.  It displays a fact, and also loads any required assets for the incoming screen.
public class ScreenTransition extends ScreenAdapter {
	// The current screen which is exiting
	private ScreenAdapterEnums screenOut;
	// The screen to transition into
	private ScreenAdapterEnums screenIn;
	// Two curtain textures to close/open
	private Array<TextureRegion> transitionTextures;
	// The coordinate of the transition textures
	private float transitionTextureX;
	private SpriteBatch batch;
	// State to determine how to move curtains
	private enum TransitionState { CLOSING, DONE, CLOSED, OPENING; }
	private TransitionState state;
	// Time for curtains to open/close
	private float transitionTime;
	private Random rand;
	// Possible facts to show
	private static final String[] FACTS = {"The number of children living in poverty has increased 85 percent since 2000.\n--Colorado Coalition for the Homeless",
			  "The key characteristics of the 1/3 of children who end up making it in life have high self-esteem, hope (future sense of self), good social skills, positive peer influence, self-confidence and independence.\n--Heart and Hand",
			  "Heart and Hand provides hot, nutritious meals to kids along with academic support and enrichment activities!",
			  "Many people don't know this but Heart and Hand is a disguise for Hero Headquarters...And Hero Headquarters needs your Superhero powers! Are you ready to help?!"};
	private Table factTable;
	
	public ScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn) {
		this.screenOut = screenOut;
		this.screenIn = screenIn;
		transitionTextures = new Array<TextureRegion>();
		batch = new SpriteBatch();
		// Init the position of the transition textures to 0
		transitionTextureX = 0;
		state = TransitionState.CLOSING;
		transitionTime = 0.3f;
		rand = new Random();
		// Get references to the transition textures from an asset manager
		fillTransitionTextures(transitionTextures, ScreenAdapterManager.getInstance().game.getAssetManager());
		factTable = buildFactTable(ScreenAdapterManager.getInstance().game.getAssetManager());
	}
	
	public void fillTransitionTextures(Array<TextureRegion> transitionTextures, AssetManager manager) {
		// Retrieve atlas, which must be loaded on initial app creation
		TextureAtlas screenTransitionAtlas = manager.get("Packs/ScreenTransitions.pack");
		for(AtlasRegion texture : screenTransitionAtlas.getRegions()){
			transitionTextures.add(texture);
		}
	}
	
	// Builds a table for the fact label shown on closed transition
	public Table buildFactTable(AssetManager manager) {
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
		Button next = new Button(style);
		next.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				state = TransitionState.OPENING;
				container.remove();
			}
		});
		buttonContainer.add(next).center().bottom();
		buttonContainer.setPosition(Gdx.graphics.getWidth()/2, buttonContainer.getHeight());
		
		// Build the fact table
		Table factContainer = new Table();
		LabelStyle ls = new LabelStyle();
		ls.font = new BitmapFont();
		int fNum = Math.abs(rand.nextInt()) % FACTS.length;
		Label fact = new Label(FACTS[fNum], ls);
		fact.setColor(1, 1, 1, 1);
		// Font scale set
		switch(Gdx.app.getType()) {
		case Android:
			fact.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*1.5f));
			break;
			//if using the desktop set the width and height to a 16:9 resolution.
		case Desktop:
			fact.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*6.5f));
			break;
		case iOS:
			fact.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
			break;
		default:
			fact.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
			break;
		}
		fact.setWrap(true);
		fact.setAlignment(Align.center, Align.center);
		factContainer.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		factContainer.add(fact).width(Gdx.graphics.getWidth()/2);
		
		// Add to container
		container.row();
		container.add(buttonContainer);
		container.row();
		container.add(factContainer).align(Align.center);
		
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
		case CLOSING:
			ScreenAdapterManager.getInstance().getScreenFromEnum(screenOut).render(delta);
			if (close(delta) == true) state = TransitionState.DONE;
			break;
		case DONE:
			addTables(ScreenAdapterManager.getInstance().game);
			state = TransitionState.CLOSED;
			break;
		case CLOSED:
			processLoad();
			break;
		case OPENING:
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
	
	// Adds a fact table and button to game stage
	public void addTables(TGC_Engine game) {
		game.getStage().addActor(factTable);
	}
	
	// Runs continuously while fact is displayed.  Use for loading assets, set button to active when done
	public void processLoad() {
		
	}
	
	public boolean open(float delta) {
		transitionTextureX -= Gdx.graphics.getWidth()/2 * delta/transitionTime;
		if (transitionTextureX < 0) return true;
		return false;
	}
}
