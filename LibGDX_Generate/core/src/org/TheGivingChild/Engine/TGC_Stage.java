package org.TheGivingChild.Engine;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
/**
 * <p>This class is used as the main stage in TGC_Engine.</p>
 * <p>Used as the main inputProcessor except when the {@link org.TheGivingChild.Screens.ScreenMaze} is shown.</p>
 * 
 * @author janelson
 *
 */
public class TGC_Stage extends Stage{
	/**Override the back button to show the main menu for Android*/
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK){
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	    }
		return super.keyDown(keyCode);
	}
}
