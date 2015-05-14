package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class ScreenLevelManager extends ScreenAdapter{
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,0,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	}
}
