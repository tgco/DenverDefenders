package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MyChangeListener extends ChangeListener{

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		Sound click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
		click.play(.75f);//turned the sound down a bit
	}

}
