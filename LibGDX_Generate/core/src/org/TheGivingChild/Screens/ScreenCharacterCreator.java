package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class ScreenCharacterCreator extends ScreenAdapter {
	//reference to Engine
	TGC_Engine game;
	//Arrays with Character Textures
	Array<Image> headImages;
	Array<Image> bodyImages;
	Array<Image> feetImages;
	//Table for the loading screen
	Table loadingScreen;
	//Left Side of screen for character display
	Table characterTable;
	//Right side of screen for character editing
	Table editTable;
	//reference to the assetManager
	AssetManager assetManager;
	//has the loading screen been drawn?
	boolean loadingScreenDrawn;
	
	

	//constructor for the new character creator screen
	public ScreenCharacterCreator(){
		game = ScreenAdapterManager.getInstance().game;
		//get the assetManager
		AssetManager assetManager = game.getAssetManager();
		//load the packs in the assetManager queue
		assetManager.load("Packs/Heads.pack", TextureAtlas.class);
		assetManager.load("Packs/Body.pack", TextureAtlas.class);
		assetManager.load("Packs/Feet.pack", TextureAtlas.class);
		//while they haven't been loaded yet, display the transition screen
		//track if the screen was drawn or not
		loadingScreenDrawn = false;
		
	}
	
	public void fillImageArrays(){
		TextureAtlas headAtlas = assetManager.get("Packs/Heads.pack");
		TextureAtlas bodyAtlas = assetManager.get("Packs/Heads.pack");
		TextureAtlas feetAtlas = assetManager.get("Packs/Heads.pack");
		
		for(Texture t: headAtlas.getTextures()){
			Image headImage = new Image(t);
			headImages.add(headImage);
		}
		for(Texture t: bodyAtlas.getTextures()){
			Image bodyImage = new Image(t);
			bodyImages.add(bodyImage);
		}
		for(Texture t: feetAtlas.getTextures()){
			Image feetImage = new Image(t);
			feetImages.add(feetImage);
		}
	}
	
	@Override
	public void render(float delta) {
		if(!assetManager.update()) {
			if(!loadingScreenDrawn
					&& !assetManager.isLoaded("Packs/Heads.pack", TextureAtlas.class)
					&& !assetManager.isLoaded("Packs/Body.pack", TextureAtlas.class)
					&& !assetManager.isLoaded("Packs/Feet.pack", TextureAtlas.class)){
				//initialize the table
				loadingScreen = new Table();
				//get the loading screen texture
				Texture splash = assetManager.get("MainScreen_Splash.png");
				//make an image from the texture
				Image splashImage = new Image(splash);
				//add the image to the table
				loadingScreen.add(splashImage).align(Align.center);
				//set the size of the table to the screen
				loadingScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				//add the table to the stage to be drawn
				game.getStage().addActor(loadingScreen);
				loadingScreenDrawn = true;
			}
		}
		//once the manager is done updating, it prepares to switch to the main screen
		else {
			loadingScreen.remove();
			characterTable = characterAppearanceTable();
			game.getStage().addActor(characterTable);
		}
	};


	public Table characterAppearanceTable(){
		Table characterTable = new Table();
		Image head = headImages.get(0);
		Image body = bodyImages.get(0);
		Image feet = feetImages.get(0);
		
		characterTable.add(head).align(Align.right);
		characterTable.add(body).align(Align.center);
		characterTable.add(feet).align(Align.left);
		

		characterTable.setSize((int)(Gdx.graphics.getWidth()*.8), Gdx.graphics.getHeight());
		characterTable.align(Align.bottomLeft);
		return characterTable;
	}
	//fills in data from a saved character file, if one exists.
	public void loadSavedCharacter(){

	}
	//fills in data to a file corresponding to the users current character.
	public void saveCharacter(){

	}

}
