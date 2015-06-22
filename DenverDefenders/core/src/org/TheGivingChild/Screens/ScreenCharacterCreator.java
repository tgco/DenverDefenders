package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Actor;
/**
 * Screen for character creation. 
 * Put off for more pressing features.
 * @author janelson
 *
 */
public class ScreenCharacterCreator extends ScreenAdapter {
	//reference to Engine
	TGC_Engine game;
	//Arrays with Character Textures
	Array<Image> headImages;
	Array<Image> bodyImages;
	Array<Image> feetImages;
	//Left Side of screen for character display
	Table characterTable;
	//Right side of screen for character editing
	Table editTable;
	//reference to the assetManager
	AssetManager assetManager;
	//skin for the back button
	private Skin skin;
	//bitmap font for back button
	private BitmapFont font;
	//text button style for back button
	private TextButtonStyle style;
	//table for back button
	private Table buttonTable;
	//ok to draw button?
	private boolean drawButton = false;
	
	//constructor for the new character creator screen
	public ScreenCharacterCreator(){
		game = ScreenAdapterManager.getInstance().game;
		//get the assetManager
		assetManager = game.getAssetManager();

		//load the packs in the assetManager queue
		assetManager.load("Packs/Heads.pack", TextureAtlas.class);
		assetManager.load("Packs/Body.pack", TextureAtlas.class);
		assetManager.load("Packs/Feet.pack", TextureAtlas.class);
		headImages = new Array<Image>();
		bodyImages = new Array<Image>();
		feetImages = new Array<Image>();
		buttonTable = createButton();
	}
	
	public void fillImageArrays(){
		TextureAtlas headAtlas = assetManager.get("Packs/Heads.pack");
		TextureAtlas bodyAtlas = assetManager.get("Packs/Body.pack");
		TextureAtlas feetAtlas = assetManager.get("Packs/Feet.pack");
		
		//add the images to each array based on the textures
		for(TextureRegion t: headAtlas.getRegions()){
			Image headImage = new Image(t);
			headImages.add(headImage);
		}
		for(TextureRegion t: bodyAtlas.getRegions()){
			Image bodyImage = new Image(t);
			bodyImages.add(bodyImage);
		}
		for(TextureRegion t: feetAtlas.getRegions()){
			Image feetImage = new Image(t);
			feetImages.add(feetImage);
		}

		
	}
	
	public Table createButton() {
		Table t = new Table();
		font = game.getBitmapFontButton();
		skin = new Skin();
		skin.addRegions((TextureAtlas) assetManager.get("Packs/ButtonsEditor.pack"));
		style = new TextButtonStyle();
		style.font = font;
		style.up = skin.getDrawable("Button_Editor_Back");
		style.down = skin.getDrawable("ButtonPressed_Editor_Back");
		TextButton backButton = new TextButton("", style);
		backButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
				hide();
			}
		});
		backButton.setSize(150,300);
		t.add(backButton);
		t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		t.align(Align.bottom);
		return t;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fillImageArrays();
		characterTable = characterAppearanceTable();
		game.getStage().addActor(characterTable);
		drawButton = true;
		show();
	}
	
	@Override
	public void hide() {
		characterTable.remove();
		buttonTable.remove();
	}
	
	@Override
	public void show() {
		if(drawButton)
			game.getStage().addActor(buttonTable);
	}


	public Table characterAppearanceTable(){
		Table characterTable = new Table();
		Image head = headImages.get(0);
		Image body = bodyImages.get(1);
		Image feet = feetImages.get(2);
		
		characterTable.add(head).row();
		characterTable.add(body).row();
		characterTable.add(feet).row();

		characterTable.setSize((int)(Gdx.graphics.getWidth()*.8), Gdx.graphics.getHeight());
		characterTable.align(Align.center);
		return characterTable;
	}
	//fills in data from a saved character file, if one exists.
	public void loadSavedCharacter(){

	}
	//fills in data to a file corresponding to the users current character.
	public void saveCharacter(){

	}

}
