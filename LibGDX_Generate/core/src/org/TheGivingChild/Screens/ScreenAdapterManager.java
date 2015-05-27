package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
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
    //Constructor: initializes an instance of the adapter. initializes the empty map.
    private ScreenAdapterManager() {
        screens = new IntMap<ScreenAdapter>();
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
        game.setScreen(screens.get(screenEnum.ordinal()));
    }
    
    public ScreenAdapterEnums getCurrentEnum(){
    	return currentEnum;
    }
 
    //dispose of the screen passed in.
    public void dispose(ScreenAdapterEnums screenEnum) {
    	//if the map doesn't contain the screen we're asked to dipose, then we have nothing to do.
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