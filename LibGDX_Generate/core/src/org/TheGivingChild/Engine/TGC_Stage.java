package org.TheGivingChild.Engine;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TGC_Stage extends Stage{
	//override the back button to show the main menu for Android
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK){
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	    }
		return super.keyDown(keyCode);
	}
}
