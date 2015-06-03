package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import sun.java2d.pipe.SpanClipRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

class ScreenOptions extends ScreenAdapter {
	private Skin skin;
	private Skin buttonSkin;
	private Skin sliderSkin;
	private Table optionsTable;
	private Array<CheckBox> options;
	private Table choicesTable;
	private BitmapFont font;
	private TextButtonStyle style;
	private CheckBoxStyle cbStyle;
	private TGC_Engine game;
	private AssetManager manager;
	private SpriteBatch batch;
	private Texture title;
	private boolean isRendered = false;
	private String[] optionsArray = {"   Option 1   ", 
			  						 "   Option 2   ", 
			  						 "   Option 3   ", 
			  						 "   Option 4   "};
	private boolean option1, option2, option3, option4 = false;
	private boolean allChanged = false;
	private Slider slider;
	private Label sliderValue;
	private Label sliderName;

	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		manager = game.getAssetManager();
		manager.load("optionsTitle.png", Texture.class);
		createOptionsTable();
		createRows();
		createSlider();
	}
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionOutComplete = ScreenAdapterManager.getInstance().screenTransitionOut();
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionOutComplete) {
				if(manager.isLoaded("optionsTitle.png"))
					title = manager.get("optionsTitle.png");
				Gdx.gl.glClearColor(1,1,0,1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.begin();
				batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
				batch.end();
				isRendered = true;
				show();
				for(CheckBox c : options) {
					if(c.isChecked()) {
						if(c.equals(options.get(0)))
							option1 = true;
						else if(c.equals(options.get(1)))
							option2 = true;
						else if(c.equals(options.get(2)))
							option3 = true;
						else if(c.equals(options.get(3)))
							option4 = true;
					}
					else if(c.equals(options.get(0)))
						option1 = false;
					else if(c.equals(options.get(1)))
						option2 = false;
					else if(c.equals(options.get(2)))
						option3 = false;
					else if(c.equals(options.get(3)))
						option4 = false;
				}
				
			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void show() {
		if(isRendered) {
			game.getStage().addActor(optionsTable);
			game.getStage().addActor(choicesTable);
			isRendered = false;
		}
	};
	
	@Override
	public void hide() {
		optionsTable.remove();
		choicesTable.remove();
	}
	
	private void createOptionsTable() {
		//Sets up the needed variables and parameters
		optionsTable = new Table();
		skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		//Creates the buttons and sets table to origin
		createButton();
		optionsTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		optionsTable.align(Align.bottom);
	}
	
	private void createRows() {
		choicesTable = new Table();
		buttonSkin = new Skin();
		buttonSkin.addRegions((TextureAtlas) manager.get("Packs/CheckBoxes.pack"));
		createChoices();
		choicesTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		choicesTable.align(Align.center);
	}

	private void createButton() {
		font = new BitmapFont();
		skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		style = new TextButtonStyle();
		style.font = font; 
		style.up = skin.getDrawable("Button_Editor_Back");
		style.down = skin.getDrawable("ButtonPressed_Editor_Back");
		TextButton backButton = new TextButton("", style);

		//Creates the listener for the Back button
		backButton.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Calls the screen manager and has main be the shown screen if Back is hit
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			}
		});
		backButton.setSize(150,300);
		optionsTable.add(backButton);
	}
	
	private void createChoices() {
		options = new Array<CheckBox>();
		font = game.getBitmapFontButton();
		cbStyle = new CheckBoxStyle();
		cbStyle.font = font;
		cbStyle.checkboxOff = buttonSkin.getDrawable("CheckBox");
		cbStyle.checkboxOn = buttonSkin.getDrawable("CheckBox_Checked");
		for(int i = 0; i < optionsArray.length; i++) {
			CheckBox checkbox = new CheckBox(optionsArray[i], cbStyle);
			checkbox.setSize(100, 50);
			choicesTable.add(checkbox);
			options.add(checkbox);
		}
	}
	 private void createSlider() {
		 sliderSkin = new Skin();
		 font = game.getBitmapFontButton();
		 sliderSkin.addRegions((TextureAtlas) manager.get("Packs/Buttons.pack"));
		 SliderStyle ss = new SliderStyle(sliderSkin.getDrawable("SliderBackground"), sliderSkin.getDrawable("SliderKnob"));
		 LabelStyle ls = new LabelStyle();
		 ls.font = font;
		 slider = new Slider(0, 100, 1, false, ss);
		 slider.setValue(0);
		 slider.addListener(new ChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				 float value = ((Slider) actor).getValue();
				 updateSliderValue(value);
			 }
		 });
		 sliderValue = new Label("0.0", ls);
		 sliderName = new Label("Volume", ls);
		 choicesTable.row();
		 choicesTable.add(sliderName);
		 choicesTable.add(slider);
		 choicesTable.add(slider);
		 choicesTable.add(sliderValue).width(40);
	 }
	 
	 private void updateSliderValue(float v) {
		 sliderValue.setText(Float.toString(v));
	 }
}
