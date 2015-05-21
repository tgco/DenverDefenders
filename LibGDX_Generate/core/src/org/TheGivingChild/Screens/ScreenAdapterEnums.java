package org.TheGivingChild.Screens;

import com.badlogic.gdx.ScreenAdapter;
//Enumerations for the screens that our manager switches between.
public enum ScreenAdapterEnums {
	//The level packet screen, returns a new ScreenLevelPackets
	LEVEL_PACKETS{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenLevelPackets();
        }
	},
	//The how to play screen, returns a new ScreenHowToPlay
	HOW_TO_PLAY{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenHowToPlay();
        }
	}, 
	//The editor screen, returns a new ScreenEditor
	EDITOR{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenEditor();
        }
	},
	//The options screen, returns a new ScreenOptions
	OPTIONS{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenOptions();
        }
	},
	//The options screen, returns a new ScreenOptions
	CHARACTER_CREATOR{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenCharacterCreator();
        }
	},
	//The main screen, returns a new ScreenMain
	MAIN{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenMain();
        }
	};//add any new screens after MAIN, following the same format as above.
	
	//abstract protected class that the manager uses to return a ScreenAdapter
	protected abstract ScreenAdapter getScreenInstance();
}
