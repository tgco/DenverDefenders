package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import sun.security.jca.GetInstance.Instance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
/**
 * 
 * Singleton
 * <p>
 * -Keeps track of all {@link Instance}s of screens.
 * </p>
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
	/**Reference to {@link org.TheGivingChild.Engine.TGC_Engine TGC_Engine}. */
	public TGC_Engine game;
	/**Map of {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} built from {@link ScreenAdapterEnums}.*/
	private IntMap<ScreenAdapter> screens;
	/**{@link SpriteBatch} used for rendering {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Batch batch;
	/**The texture region that takes {@link #backgroundTexture} and allows it to be stretched when batch.drawn */
	private TextureRegion backgroundRegion;

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
	 * Constructor: initializes an instance of the adapter. 
	 * Initializes the {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} map for {@link #screens}.
	 */
	private ScreenAdapterManager() {
		screens = new IntMap<ScreenAdapter>();
		batch = new SpriteBatch();
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
	
	// Returns the instance of the screen object that corresponds to the enum
	public ScreenAdapter getScreenFromEnum(ScreenAdapterEnums screenEnum) {
		if (!screens.containsKey(screenEnum.ordinal())) {
			screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
		} 
		return screens.get(screenEnum.ordinal());
	}
	
	/**
	 * <p>Get a reference to the calling TGC_Engine</p>
	 * 
	 * @param game is the reference to the TGC_Engine that calls initialize
	 */
	public void initialize(TGC_Engine game) {
		this.game = game;
		backgroundRegion = new TextureRegion(game.getAssetManager().get("ColdMountain.png", Texture.class));
	}
	/**Draws the {@link #backgroundRegion} to the screen*/
	public void backgroundImage() {
		batch.begin();
		batch.draw(backgroundRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}

	/**
	 * <p>The show method used to switch to other screenAdapters</p>
	 * <p>Check if {@link #screens} contains this {@link ScreenAdapterEnums} enumeration.</p>
	 * <p>If not, then construct and add the screenAdapter to the map, and then set as the active screen</p>
	 * 
	 * @param screenEnum The screen to switch to.
	 */
	public void show(ScreenAdapterEnums screenEnum) {
		if (null == game) return;
		if (!screens.containsKey(screenEnum.ordinal())) {
			screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
		} 
		
		game.setScreen(screens.get(screenEnum.ordinal()));
	}
}