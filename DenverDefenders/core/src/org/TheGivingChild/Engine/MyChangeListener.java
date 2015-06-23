package org.TheGivingChild.Engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/**
 * <p>
 * {@link MyChangeListener} is the listener attatched to UI elements such as {@link Button Button's}, and {@link CheckBox Checkbox's}.
 * </p>
 * <p>
 * It is overridden to easily add sound, or other effects to interactions with UI.
 * </p>
 * @author janelson
 *
 */
public class MyChangeListener extends ChangeListener{

	/**changed is overridden to add sound to UI elements. */
	@Override
	public void changed(ChangeEvent event, Actor actor) {
		AudioManager.getInstance().play("sounds/click.wav");
	}

}
