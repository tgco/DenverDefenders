package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.math.MathUtils;
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
	/**Reference to {@link org.TheGivingChild.Engine.TGC_Engine TGC_Engine}. */
	public TGC_Engine game;
	/**Map of {@link com.badlogic.gdx.ScreenAdapter ScreenAdapters} built from {@link ScreenAdapterEnums}.*/
	private IntMap<ScreenAdapter> screens;
	/**Reference to the {@link AssetManager} in {@link org.TheGivingChild.Engine.TGC_Engine TGC_Engine}.*/
	private AssetManager manager;
	/**{@link SpriteBatch} used for rendering {@link #screenTransitionIn()} and {@link #screenTransitionOut()}. */
	private Batch batch;
	/**The texture region that takes {@link #backgroundTexture} and allows it to be stretched when batch.drawn */
	public TextureRegion backgroundRegion;
	private Table minigameTable;
	private Table mazeTable;
	private Table overallTable;
	private Label minigame;
	private Label maze;
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
	
	public ScreenAdapter getScreenFromEnum(ScreenAdapterEnums screenEnum) {
		if (!screens.containsKey(screenEnum.ordinal())) {
			screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
		} 
		return screens.get(screenEnum.ordinal());
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
		backgroundRegion = new TextureRegion(manager.get("ColdMountain.png", Texture.class));
		createLabels(MathUtils.random(100));
		overallTable = new Table();
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
		Gdx.app.log("Manager", "Transition called");
	}
	/**
	 * <p>Moves the curtains from outside the rendered screen to the middle, closing in on a level</p>
	 * <p>Starts on the outside, moves curtains inward to cover the current level</p>
	 * @return true if coverage is complete. Used for knowing when to call screenTransitionOut().
	 */
	public boolean screenTransitionIn(){
		Gdx.app.log("Manager", "Transition IN");
		return false;
	}
	/**
	 * <p>Moves the curtains from covering the screen to out of the rendered view</p>
	 * <p>Starts in the middle and then move curtains toward the outside to uncover the new level</p>
	 * @return Returns true when transition is complete.
	 */
	public boolean screenTransitionOut(){
		Gdx.app.log("Manager", "Transition OUT");
		return false;
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
		
		minigameTable.remove();
		mazeTable.remove();
		
		createLabels(MathUtils.random(100));

		if ((screenEnum.equals(ScreenAdapterEnums.MAZE) && game.getFromGame()) || getInstance().game.getCurrentLevel() != null) {
			overallTable.add(minigameTable.align(Align.center));			
			game.setFromGame(false);
		}

		if (screenEnum.equals(ScreenAdapterEnums.MAIN) && game.getMazeCompleted()) {
			overallTable.add(mazeTable.align(Align.center));
			game.setMazeCompleted(false);
		}
		
		overallTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		overallTable.align(Align.center);
		
		game.setScreen(screens.get(screenEnum.ordinal()));
	}
	
	public void createLabels(int r) {
		minigame = null;
		minigameTable = new Table();
		LabelStyle ls = new LabelStyle();
		ls.font = new BitmapFont();
		if (game.getFromGame() && game.levelWin()) {
			minigame = new Label("You WON!", ls);
			minigame.setColor(1, 1, 1, 1);
			minigame.setWrap(true);
			//win message
			switch(Gdx.app.getType()){
			case Android:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*1.5f));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*2.5f));
				break;
			case iOS:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			minigame.setAlignment(Align.center, Align.center);
			minigameTable.add(minigame).width(Gdx.graphics.getWidth()/2);
			minigameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		else if (game.getFromGame() && !game.levelWin()){
			minigame = new Label("You lost", ls);
			minigame.setColor(1, 1, 1, 1);
			minigame.setWrap(true);
			//lose message
			switch(Gdx.app.getType()){
			case Android:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*1.5f));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*2.5f));
				break;
			case iOS:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			minigame.setAlignment(Align.center, Align.center);
			minigameTable.add(minigame).width(Gdx.graphics.getWidth()/2);
			minigameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		else if(game.getCurrentLevel() != null && !game.getFromGame()) {
			Level current = getInstance().game.getCurrentLevel();
			minigame = new Label(current.getDescription(), ls);
			minigame.setColor(1, 1, 1, 1);
			minigame.setWrap(true);
			//game description
			switch(Gdx.app.getType()){
			case Android:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*2.5f));
				break;
			case iOS:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				minigame.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			minigame.setAlignment(Align.center, Align.center);
			minigameTable.add(minigame).width(Gdx.graphics.getWidth()/2);
			minigameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		else {
			minigame = null;
		}
		
		mazeTable = new Table();
		if (game.getMazeCompleted() && game.getAllSaved()) {
			maze = new Label("You Saved All the Kids! Congratulations!", ls);
			maze.setColor(1, 1, 1, 1);
			maze.setWrap(true);
			//win maze message
			switch(Gdx.app.getType()){
			case Android:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*1.5f));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*2.5f));
				break;
			case iOS:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			maze.setAlignment(Align.center, Align.center);
			mazeTable.add(maze).width(Gdx.graphics.getWidth()/2);
			mazeTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		else if (game.getMazeCompleted() && !game.getAllSaved()) {
			maze = new Label("You Ran Out of Lives! Try Again!", ls);
			maze.setColor(1, 1, 1, 1);
			maze.setWrap(true);
			//maze lose message
			switch(Gdx.app.getType()){
			case Android:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*1.5f));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*2.5f));
				break;
			case iOS:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				maze.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			maze.setAlignment(Align.center, Align.center);
			mazeTable.add(maze).width(Gdx.graphics.getWidth()/2);
			mazeTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		else {
			maze = null;
		}
	}

}