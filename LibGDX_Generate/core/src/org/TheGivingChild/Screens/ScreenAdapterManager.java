package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
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
 * The {@link ScreenAdapterManager} follows the Singleton pattern.
 * <p>
 * -Keeps track of all {@link Instance}s of screens.
 * </p>
 * <p>
 *-Handles screenTransitions via {@link #screenTransitionIn()} and {@link #screenTransitionOut()}.
 *</p>
 *<p>
 *-Handles disposal of {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} when the program ends. (Assuming the {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} disposal methods are correct)
 *</p>
 *<p>
 *-Final to avoid inheritance, static so only one instance is referred to.
 *</p>
 * @author Jack Wesley Nelson
 *
 */

public final class ScreenAdapterManager {
	/**Instance of the {@link #ScreenAdapterManager}.*/
	private static ScreenAdapterManager instance;
	/**Enumeration of the current {@link com.badlogic.gdx.ScreenAdapter ScreenAdapter} shown. */
	private ScreenAdapterEnums currentEnum;
	/**Reference to {@link org.TheGivingChild.Engine.TGC_Engine TGC_Engine}. */
	public TGC_Engine game;
	/**Map of {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} built from {@link ScreenAdapterEnums}.*/
	private IntMap<ScreenAdapter> screens;
	/**Reference to the {@link AssetManager} in {@link org.TheGivingChild.Engine.TGC_Engine TGC_Engine}.*/
	private AssetManager manager;
	/**{@link SpriteBatch} used for rendering {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Batch batch = new SpriteBatch();
	/**{@link TextureRegion}'s to be drawn for {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Array<TextureRegion> screenTransitions;
	/**Start position for drawing the left screen during {@link #screenTransitionOut()}.*/
	private float outLeftScreenStart;
	/**End position for drawing the left screen during {@link #screenTransitionOut()}.*/
	private final float outLeftScreenEnd = -Gdx.graphics.getWidth()/2;
	/**Start position for drawing the right screen during {@link #screenTransitionOut()}.*/
	private float outRightScreenStart;
	/**End position for drawing the right screen during {@link #screenTransitionOut()}.*/
	private final float outRightScreenEnd = Gdx.graphics.getWidth();
	/**Start position for drawing the left screen during {@link #screenTransitionIn()}.*/
	private float inLeftScreenStart;
	/**End position for drawing the left screen during {@link #screenTransitionIn()}.*/
	private final float inLeftScreenEnd = 0;
	/**Start position for drawing the right screen during {@link #screenTransitionIn()}.*/
	private float inRightScreenStart;
	/**End position for drawing the right screen during {@link #screenTransitionIn()}.*/
	private final float inRightScreenEnd = Gdx.graphics.getWidth()/2;
	/**The amount of minimum time for the screen to transition. */
	public float SCREEN_TRANSITION_TIME_LEFT;
	/**screenTransition state to reference when other events should occur*/
	public boolean screenTransitionInComplete;
	/**The speed at which the curtains should {@link #screenTransitionIn()} and {@link #screenTransitionOut()}.*/
	public float screenTransitionSpeed;
	/**The texture region that takes {@link #backgroundTexture} and allows it to be stretched when batch.drawn */
	public TextureRegion backgroundRegion;
	/**The initial Texture to be applied to {@link #backgroundRegion}*/
	private Texture backgroundTexture;

	/**
	 * Constructor: initializes an instance of the adapter. 
	 * Initializes the {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} map for {@link #screens}.
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
	 * <p>Dispose of the screen corresponding to the enumeration passed in.</p>
	 * <p>If the map doesn't contain the screen we're asked to dispose, then we have nothing to do.</p>
	 * <p>Otherwise, the map contains the screen. Call dispose on the screen, and remove it from the map.</p>
	 * @param screenEnum is a {@link org.TheGivingChild.Screens.ScreenAdapterEnums ScreenAdapterEnums} enumeration
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
	 * <p>Get a reference to the calling TGC_Engine</p>
	 * <p>Load the .pack of ScreenTransitions</p>
	 * <p>Finish loading the pack, since we need transitions everywhere.</p>
	 * <p>Create a TextureAtlas from the .pack</p>
	 * <p>Fill the screenTransitons Texture array using the TextureAtlas</p>
	 * <p>Set the needed variables, specifically:</p>
	 * <p>	-screenTransition state</p>
	 * <p>	-screenStartPositions</p>
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
		screenTransitionSpeed = Gdx.graphics.getWidth()/3*.5f;
		inLeftScreenStart = -Gdx.graphics.getWidth()/2;
		inRightScreenStart = Gdx.graphics.getWidth();
		outLeftScreenStart = 0f;
		outRightScreenStart = Gdx.graphics.getWidth()/2;
		backgroundTexture = manager.get("DenverSkyline.jpg");
		backgroundRegion = new TextureRegion(backgroundTexture);
	}
	/**Draws the {@link #backgroundRegion} to the screen, allowing for resizing. */
	public void backgroundImage() {
		batch.begin();
		batch.draw(backgroundRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
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
	 * <p>Moves the curtains from outside the rendered screen to the middle, closing in on a level</p>
	 * <p>Starts on the outside, moves curtains inward to cover the current level</p>
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
	 * <p>Moves the curtains from covering the screen to out of the rendered view</p>
	 * <p>Starts in the middle and then move curtains toward the outside to uncover the new level</p>
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
	 * <p>The show method used to switch to other screenAdapters</p>
	 * <p>Show the screen in the argument, hide the current.</p>
	 * <p>If the game is null, then we have no screens to show.</p>
	 * <p>Check if {@link #screens} contains this {@link ScreenAdapterEnums} enumeration.</p>
	 * <p>If not, then add the screenAdapter to the map.</p>
	 * <p>Reset the starting locations for screenTransitions</p>
	 * <p>set {@link #screenTransitionInComplete} to false</p>
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