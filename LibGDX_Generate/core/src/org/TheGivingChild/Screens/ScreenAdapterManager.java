package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import sun.security.jca.GetInstance.Instance;

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
import com.sun.jndi.ldap.ManageReferralControl;
/**
 * 
 * The {@link ScreenAdapterManager} follows the Singleton pattern.
 *-Keeps track of all {@link Instance}s of screens.
 *-Handles screenTransitions via {@link #screenTransitionIn()} and {@link #screenTransitionOut()}.
 *-Handles disposal of {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} when the program ends. (Assuming the {@link ScreenAdapter}s disposal methods are correct)
 *-final to avoid inheritance, static so only one instance is referred to.
 * @author Jack Wesley Nelson
 *
 */

public final class ScreenAdapterManager {
	/**Instance of the {@link #ScreenAdapterManager}.*/
	private static ScreenAdapterManager instance;
	/**Enumeration of the current {@link #ScreenAdapter} shown. */
	private ScreenAdapterEnums currentEnum;
	/**Reference to {@link TGC_Engine}. */
	public TGC_Engine game;
	/**Map of {@link ScreenAdapter}s built from {@link ScreenAdapterEnums}.*/
	private IntMap<ScreenAdapter> screens;
	/**Reference to the {@link AssetManager} in {@link TGC_Engine}.*/
	private AssetManager manager;
	/**{@link SpriteBatch} used for rendering {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Batch batch = new SpriteBatch();
	/**{@link TextureRegion}'s to be drawn for {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Array<TextureRegion> screenTransitions;
	/**Start position for drawing the left screen during {@link #screenTransitionOut()}.*/
	protected float outLeftScreenStart;
	/**End position for drawing the left screen during {@link #screenTransitionOut()}.*/
	protected final float outLeftScreenEnd = -Gdx.graphics.getWidth()/2;
	/**Start position for drawing the right screen during {@link #screenTransitionOut()}.*/
	protected float outRightScreenStart;
	/**End position for drawing the right screen during {@link #screenTransitionOut()}.*/
	protected final float outRightScreenEnd = Gdx.graphics.getWidth();
	/**Start position for drawing the left screen during {@link #screenTransitionIn()}.*/
	protected float inLeftScreenStart;
	/**End position for drawing the left screen during {@link #screenTransitionIn()}.*/
	protected final float inLeftScreenEnd = 0;
	/**Start position for drawing the right screen during {@link #screenTransitionIn()}.*/
	protected float inRightScreenStart;
	/**End position for drawing the right screen during {@link #screenTransitionIn()}.*/
	protected final float inRightScreenEnd = Gdx.graphics.getWidth()/2;
	/**The amount of minimum time for the screen to transition. */
	protected float SCREEN_TRANSITION_TIME_LEFT;
	/**screenTransition state to reference when other events should occur*/
	public boolean screenTransitionInComplete;
	/**The speed at which the curtains should {@link #screenTransitionIn()} and {@link #screenTransitionOut()}.*/
	public float screenTransitionSpeed;

	/**
	 * Constructor: initializes an instance of the adapter. 
	 * Initializes the {@link ScreenAdapter} map for {@link #screens}.
	 */
	private ScreenAdapterManager() {
		screens = new IntMap<ScreenAdapter>();
		screenTransitions = new Array<TextureRegion>();
	}
	/**
	 * Allows access to {@link #instance} from outside the class.
	 * If the {@link #instance} is null, construct it.
	 * Return the {@link #instance} to the caller.
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
		screenTransitionSpeed = Gdx.graphics.getWidth()/30*.2f;
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
			inLeftScreenStart+= screenTransitionSpeed;
			inRightScreenStart-= screenTransitionSpeed;
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
			outLeftScreenStart-= screenTransitionSpeed;
			outRightScreenStart+= screenTransitionSpeed;
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