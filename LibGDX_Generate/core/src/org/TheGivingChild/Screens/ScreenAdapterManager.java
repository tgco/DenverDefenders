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

//final to avoid inheritance, static so only one instance is referred to.
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
	public boolean screenTransitionOutComplete = false;
    //Constructor: initializes an instance of the adapter. initializes the empty map.
    private ScreenAdapterManager() {
        screens = new IntMap<ScreenAdapter>();
        screenTransitions = new Array<TextureRegion>();
    }
    //allows access to the instance of the adapter class from outside the class.
    public static ScreenAdapterManager getInstance() {
    	//if the instance is null, construct it.
        if (null == instance) {
            instance = new ScreenAdapterManager();
        }
        //return the instance to the caller
        return instance;
    }
    //This is called from the TGC_Engine. Sets the reference to the game that the screens will refer to.
    public void initialize(TGC_Engine game) {
        this.game = game;
        manager = game.getAssetManager();
        //load the screen transition pack
        manager.load("Packs/ScreenTransitions.pack", TextureAtlas.class);
        //make sure it's done loading for screen transitions
        manager.finishLoadingAsset("Packs/ScreenTransitions.pack");
        //create an atlas from the pack
        TextureAtlas screenTransitionAtlas = manager.get("Packs/ScreenTransitions.pack");
        for(AtlasRegion texture :screenTransitionAtlas.getRegions()){
        	screenTransitions.add(texture);
        }

        screenTransitionOutComplete = false;
        
    }
    //show the screen in the argument, hide the current.
    public void show(ScreenAdapterEnums screenEnum) {
    	//if the game is null, then we have no screens to show
        if (null == game) return;
        //check if the map contains this screen enum
        if (!screens.containsKey(screenEnum.ordinal())) {
        	//it didn't, so add the ScreenAdapter to the map.
            screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
        } 
        //inLeftScreenStart = ;
        //inRightScreenStart = ;
        outLeftScreenStart = 0f;
        outRightScreenStart = Gdx.graphics.getWidth()/2;
        SCREEN_TRANSITION_TIME_LEFT = 1.0f;
        currentEnum = screenEnum;
        //Hide the current screen, show the new screen
        screenTransitionOutComplete = false;
        game.setScreen(screens.get(screenEnum.ordinal()));
    }
    //starts in the middle and then 
    public boolean screenTransitionOut(){
    	//move the screens in
    	if(outRightScreenStart <= outRightScreenEnd && outLeftScreenStart >= outLeftScreenEnd){
    		 batch.begin();
             batch.draw(screenTransitions.get(0), outLeftScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
             batch.draw(screenTransitions.get(1), outRightScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
             batch.end();
             outLeftScreenStart-= 5;
             outRightScreenStart+= 5;
             return false;
    	}
    	return true;
    }
    public boolean screenTransitionIn(){
    	if(inRightScreenStart >= inRightScreenEnd){
   		 batch.begin();
            batch.draw(screenTransitions.get(0), inLeftScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
            batch.draw(screenTransitions.get(1), inRightScreenStart, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
            batch.end();
            inLeftScreenStart+= 5;
            inRightScreenStart-= 5;
            return false;
   	}
    	return true;
    }
    public void screenTransition(){
    	 batch.begin();
    	 //draw the first curtain starting at the left edge of the screen
         batch.draw(screenTransitions.get(0), 0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
         //draw the second curtain starting at the middle edge of the screen
         batch.draw(screenTransitions.get(1), Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
         batch.end();
    }
    
    public ScreenAdapterEnums getCurrentEnum(){
    	return currentEnum;
    }
    //dispose of the screen passed in.
    public void dispose(ScreenAdapterEnums screenEnum) {
    	//if the map doesn't contain the screen we're asked to dispose, then we have nothing to do.
        if (!screens.containsKey(screenEnum.ordinal())) return;
        //otherwise, the map contains the screen. Call dispose on the screen, and remove it from the map.
        screens.remove(screenEnum.ordinal()).dispose();
    }
 
    //dispose the manager
    public void dispose() {
    	//iterate over the map, dispose the screens
        for (ScreenAdapter screenAdapter : screens.values()) {
            screenAdapter.dispose();
        }
        //empty the map
        screens.clear();
        //set the instance to null.
        instance = null;
        //get rid of the batch
        batch.dispose();
    }
}