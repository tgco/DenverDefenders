package org.TheGivingChild.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;

public final class ScreenManager {
	 
    private static ScreenManager instance;
 
    private Game game;
 
    private IntMap<com.badlogic.gdx.Screen> screens;
 
    private ScreenManager() {
        screens = new IntMap<com.badlogic.gdx.Screen>();
    }
 
    public static ScreenManager getInstance() {
        if (null == instance) {
            instance = new ScreenManager();
        }
        return instance;
    }
 
    public void initialize(Game game) {
        this.game = game;
    }
 
    public void show(ScreenEnums screenEnum) {
        if (null == game) return;
        if (!screens.containsKey(screenEnum.ordinal())) {
            screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
        }
        game.setScreen(screens.get(screenEnum.ordinal()));
    }
 
    public void dispose(ScreenEnums screenEnum) {
        if (!screens.containsKey(screenEnum.ordinal())) return;
        screens.remove(screenEnum.ordinal()).dispose();
    }
 
    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    } 