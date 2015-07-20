package org.TheGivingChild.Screens.UI;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Convenience class in the hierarchy to allow access to the rendering batch and set its projection matrix
// to the desired camera matrix
public class UIScreenAdapter extends ScreenAdapter {
	// The batch for rendering
	protected SpriteBatch batch;
	// The background
	protected Texture background;
	
	public SpriteBatch getSpriteBatch() { return batch; }
}
