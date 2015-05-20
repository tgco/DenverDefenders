package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static AssetManager manager = new AssetManager();
	public static Skin skin;
	
	public static void queueLoading() {
		manager.load("Packs/Img.pack", TextureAtlas.class);
		manager.load("Packs/Buttons.pack", TextureAtlas.class);
	}
	
	public static void setSkin() {
		if (skin == null)
			skin = new Skin(Gdx.files.internal("Packs/Badlogic.jpg"), manager.get("Packs/Img.pack", TextureAtlas.class));
	}
	
	public static boolean update() {
		return manager.update();
	}
}
