package org.TheGivingChild.Engine;

import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;
import org.TheGivingChild.Screens.UI.UIScreenAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
			// Reset the main screen cam zoom
			OrthographicCamera cam = new OrthographicCamera();
			cam.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0);
			cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.update();
			((UIScreenAdapter) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.MAIN)).getSpriteBatch().setProjectionMatrix(cam.combined);
	    }
		return super.keyDown(keyCode);
	}
}
