package org.TheGivingChild.Screens.UI;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

// Screen that interpolates between two other screens
public abstract class ScreenTransition extends ScreenAdapter {
	// State of the transition
	// DRAW_IN: Draw effects to begin the transition
	// MESSAGE_SET: Set any text or tables for the transition to show while loading
	// ASSET_REQUEST: Let screen in request the assets to load in the manager
	// WAIT_FOR_LOAD: Call manager.update() to load until done
	// DRAW_OUT: Draw effects to leave the transition
	protected enum TransitionState { DRAW_IN, MESSAGE_SET, ASSET_REQUEST, WAIT_FOR_LOAD, DRAW_OUT; }
	protected TransitionState state;
	// The current screen which is exiting
	protected ScreenAdapterEnums screenOut;
	// The screen to transition into
	protected ScreenAdapterEnums screenIn;
	// Reference to manager to progress loading
	protected AssetManager manager;
	
	public ScreenTransition(ScreenAdapterEnums screenOut, ScreenAdapterEnums screenIn) {
		this.screenOut = screenOut;
		this.screenIn = screenIn;
		manager = ScreenAdapterManager.getInstance().game.getAssetManager();
		state = TransitionState.DRAW_IN;
	}
}
