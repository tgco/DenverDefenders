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
    private Array<Texture> screenTransitions;
    
    //Constructor: initializes an instance of the adapter. initializes the empty map.
    private ScreenAdapterManager() {
        screens = new IntMap<ScreenAdapter>();
        screenTransitions = new Array<Texture>();
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
        for(Texture texture :screenTransitionAtlas.getTextures()){
        	screenTransitions.add(texture);
        }
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
        currentEnum = screenEnum;
        //Hide the current screen, show the new screen
        batch.begin();
        batch.draw((Texture) manager.get("MainScreen_Splash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(screenTransitions.get(0), 0, 0);
        batch.draw(screenTransitions.get(1), 0, 0);
        batch.end();
        game.setScreen(screens.get(screenEnum.ordinal()));
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
    }
}