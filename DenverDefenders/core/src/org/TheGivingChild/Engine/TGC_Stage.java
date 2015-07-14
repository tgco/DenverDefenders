package org.TheGivingChild.Engine;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
/**
 * <p>This class is used as the main stage in TGC_Engine.</p>
 * <p>Overriden to have default Android back key functionality.</p>
 * 
 * @author janelson
 *
 */
public class TGC_Stage extends Stage{
	/**Override the back button to show the main menu for Android*/
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK || keyCode == Keys.B){
			// A transition would look nicer here
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	    }
		return super.keyDown(keyCode);
	}
}
