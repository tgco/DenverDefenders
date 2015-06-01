package org.TheGivingChild.Engine;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TGC_Stage extends Stage{
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK){
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	    }
		return super.keyDown(keyCode);
	}
}
