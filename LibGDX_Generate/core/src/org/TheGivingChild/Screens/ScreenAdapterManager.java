package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
/**
 * 
 * The ScreenAdapterManager class is follows the Singleton pattern.
 *-Keeps track of all instances of screens.
 *-Houses methods for the screen transitions.
 *-Handles disposal of screens when the program ends. (Assuming the ScreenAdpaters are disposed properly)
 *-final to avoid inheritance, static so only one instance is referred to.
 * @author Jack Wesley Nelson
 *
 */

public final class ScreenAdapterManager {
	//variable for the instance of the manager.
	private static ScreenAdapterManager instance;
	private ScreenAdapterEnums currentEnum;
	//variable to refer to the game.
	public TGC_Engine game;
	//map of screenAdapters built from enums
	private IntMap<ScreenAdapter> screens;
	//reference to asset manager
	private AssetManager manager;
	//batch for screen transitions
	private Batch batch = new SpriteBatch();
	//textures for curtainCall.
	private Array<TextureRegion> screenTransitions;
	protected float outLeftScreenStart;
	protected final float outLeftScreenEnd = -Gdx.graphics.getWidth()/2;
	protected float outRightScreenStart;
	protected final float outRightScreenEnd = Gdx.graphics.getWidth();
	protected float inLeftScreenStart;
	protected final float inLeftScreenEnd = 0;
	protected float inRightScreenStart;
	protected final float inRightScreenEnd = Gdx.graphics.getWidth()/2;
	protected float SCREEN_TRANSITION_TIME_LEFT;
	public boolean screenTransitionInComplete;
	public float screenTransitionTime;

	/**
	 * Constructor: initializes an instance of the adapter. initializes the empty map.
	 */
	private ScreenAdapterManager() {
		screens = new IntMap<ScreenAdapter>();
		screenTransitions = new Array<TextureRegion>();
	}
	/**
	 * Allows access to the instance of the adapter class from outside the class.
	 * If the instance is null, construct it.
	 * Return the instance to the caller.
	 */
	public static ScreenAdapterManager getInstance() {
		if (null == instance) {
			instance = new ScreenAdapterManager();
		}
		return instance;
	}
	/**
	 * dispose the manager
	 * Iterate over the map, dispose the screens
	 * Empty the map
	 * Set the instance to null.
	 * Dispose of the batch
	 */
	public void dispose() {
		for (ScreenAdapter screenAdapter : screens.values()) {
			screenAdapter.dispose();
		}
		screens.clear();
		instance = null;
		batch.dispose();
	}
	/**
	 * Dispose of the screen corresponding to the enumeration passed in.
	 * If the map doesn't contain the screen we're asked to dispose, then we have nothing to do.
	 * Otherwise, the map contains the screen. Call dispose on the screen, and remove it from the map.
	 * @param screenEnum is a ScreenAdapterEnums enumeration
	 */
	public void dispose(ScreenAdapterEnums screenEnum) {
		if (!screens.containsKey(screenEnum.ordinal())) return;
		screens.remove(screenEnum.ordinal()).dispose();
	}
	/**
	 * @return returns the ScreenAdapterEnums enumeration that is currently active
	 */
	public ScreenAdapterEnums getCurrentEnum(){
		return currentEnum;
	}
	/**
	 * Get a reference to the calling TGC_Engine
	 * Load the .pack of ScreenTransitions
	 * Finish loading the pack, since we need transitions everywhere
	 * Create a TextureAtlas from the .pack
	 * Fill the screenTransitons Texture array using the TextureAtlas
	 * Set the needed variables, specifically:
	 * 	-screenTransition state
	 * 	-screenStartPositions
	 * @param game is the reference to the TGC_Engine that calls initialize
	 */
	public void initialize(TGC_Engine game) {
		this.game = game;
		manager = game.getAssetManager();
		manager.load("Packs/ScreenTransitions.pack", TextureAtlas.class);
		manager.finishLoadingAsset("Packs/ScreenTransitions.pack");
		TextureAtlas screenTransitionAtlas = manager.get("Packs/ScreenTransitions.pack");
		for(AtlasRegion texture :screenTransitionAtlas.getRegions()){
			screenTransitions.add(texture);
		}
		screenTransitionInComplete = false;
		screenTransitionTime = Gdx.graphics.getWidth()/30*.2f;
		inLeftScreenStart = -Gdx.graphics.getWidth()/2;
		inRightScreenStart = Gdx.graphics.getWidth();
		outLeftScreenStart = 0f;
		outRightScreenStart = Gdx.graphics.getWidth()/2;
	}
	/**
	 * ScreenTransition is the static representation of the curtains covering the entire screen
	 * 
	 */
	public void screenTransition(){
		batch.begin();
		//draw the first curtain starting at the left edge of the screen
		batch.draw(screenTransitions.get(0), 0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		//draw the second curtain starting at the middle edge of the screen
		batch.draw(screenTransitions.get(1), Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		batch.end();
	}
	/**
	 * Moves the curtains from outside the rendered screen to the middle, closing in on a level
	 * Starts on the outside, moves curtains inward to cover the current level
	 * @return true if coverage is complete. Used for knowing when to call screenTransitionOut().
	 */
	public boolean screenTransitionIn(){
		if(inRightScreenStart >= inRightScreenEnd && inLeftScreenStart <= inLeftScreenEnd){
			batch.begin();
			batch.draw(screenTransitions.get(0), inLeftScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
			batch.draw(screenTransitions.get(1), inRightScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
			batch.end();
			inLeftScreenStart+= screenTransitionTime;
			inRightScreenStart-= screenTransitionTime;
			return false;
		}
		return true;
	}
	/**
	 * Moves the curtains from covering the screen to out of the rendered view
	 * Starts in the middle and then move curtains toward the outside to uncover the new level
	 * @return Returns true when transition is complete.
	 */
	public boolean screenTransitionOut(){
		if(outRightScreenStart <= outRightScreenEnd && outLeftScreenStart >= outLeftScreenEnd){
			batch.begin();
			batch.draw(screenTransitions.get(0), outLeftScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
			batch.draw(screenTransitions.get(1), outRightScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
			batch.end();
			outLeftScreenStart-= screenTransitionTime;
			outRightScreenStart+= screenTransitionTime;
			return false;
		}
		return true;
	}
	/**
	 * The show method used to switch to other screenAdapters
	 * Show the screen in the argument, hide the current.
	 * If the game is null, then we have no screens to show.
	 * Check if the map contains this ScreenAdapter enumeration.
	 * If not, then add the screenAdapter to the map.
	 * Reset the starting locations for screenTransitions
	 * setScreenTransitionState to false
	 * @param screenEnum The screen to switch to.
	 */
	public void show(ScreenAdapterEnums screenEnum) {
		if (null == game) return;
		if (!screens.containsKey(screenEnum.ordinal())) {
			screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
		} 
		inLeftScreenStart = -Gdx.graphics.getWidth()/2;
		inRightScreenStart = Gdx.graphics.getWidth();
		outLeftScreenStart = 0f;
		outRightScreenStart = Gdx.graphics.getWidth()/2;
		SCREEN_TRANSITION_TIME_LEFT = 1.0f;
		currentEnum = screenEnum;
		screenTransitionInComplete = false;
		game.setScreen(screens.get(screenEnum.ordinal()));
	}
}