package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

class ScreenOptions extends ScreenAdapter {
	private Skin skin;
	private Skin buttonSkin;
	private Skin sliderSkin;
	private Table optionsTable;
	private Array<CheckBox> options;
	private Table choicesTable;
	private Table sliderTable;
	private Table overallTable;
	private BitmapFont font;
	private TextButtonStyle style;
	private CheckBoxStyle cbStyle;
	private CheckBoxStyle muteStyle;
	private CheckBox mute;
	private TGC_Engine game;
	private AssetManager manager;
	private SpriteBatch batch;
	private Texture title;
	private boolean isRendered = false;
	private String[] optionsArray = {"   Music   ", 
			  						 "   Sound   "};
	private boolean option1, option2 = false;
	private Slider slider;
	private Label sliderName;
	private float volume;
	private boolean before1, before2 = false;

	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		manager = game.getAssetManager();
		manager.load("titleOptionScreen.png", Texture.class);
		createOptionsTable();
		createOverallTable();
		ScreenAdapterManager.getInstance().cb.setChecked(false);
		//set the inital state of sound to be on
		for(CheckBox c: options){
			c.setChecked(true);
		}
	}
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionInComplete) {
				if(manager.isLoaded("titleOptionScreen.png"))
					title = manager.get("titleOptionScreen.png");
				Gdx.gl.glClearColor(1,1,0,1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				ScreenAdapterManager.getInstance().backgroundImage();
				batch.begin();
				batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
				batch.end();
				isRendered = true;
				show();
				for(CheckBox c : options) {
					if(c.isChecked()) {
						if(c.equals(options.get(0))){
							option1 = true;
							game.musicEnabled = true;
						}
						else if(c.equals(options.get(1))){
							option2 = true;
							game.soundEnabled = true;
						}
					}
					else if(c.equals(options.get(0))){
						option1 = true;
						game.musicEnabled = false;
					}
					else if(c.equals(options.get(1))){
						option2 = true;
						game.soundEnabled = false;
					}
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
			game.getStage().addActor(overallTable);
			isRendered = false;
		}
	};
		
	@Override
	public void hide() {
		optionsTable.remove();
		overallTable.remove();
		ScreenAdapterManager.getInstance().cb.setChecked(false);
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
	
	private void createChoices() {
		choicesTable = new Table();
		buttonSkin = new Skin();
		buttonSkin.addRegions((TextureAtlas) manager.get("Packs/CheckBoxes.pack"));
		createCheckBoxes();
	}

	private void createButton() {
		font = new BitmapFont();
		skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/Buttons.pack"));
		style = new TextButtonStyle();
		style.font = font; 
		style.up = skin.getDrawable("Button_MainScreen");
		style.down = skin.getDrawable("ButtonPressed_MainScreen");
		TextButton backButton = new TextButton("", style);

		//Creates the listener for the Back button
		backButton.addListener(new MyChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				//Calls the screen manager and has main be the shown screen if Back is hit
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			}
		});
		backButton.setSize(150,300);
		optionsTable.add(backButton);
	}
	
	private void createCheckBoxes() {
		options = new Array<CheckBox>();
		font = game.getBitmapFontButton();
		cbStyle = new CheckBoxStyle();
		Skin bSkin = new Skin();
		bSkin.add("background", manager.get("SemiTransparentBG.png"));
		cbStyle.font = font;
		cbStyle.checkboxOff = buttonSkin.getDrawable("CheckBox");
		cbStyle.checkboxOn = buttonSkin.getDrawable("CheckBox_Checked");
		for(int i = 0; i < optionsArray.length; i++) {
			CheckBox checkbox = new CheckBox("", cbStyle);
			LabelStyle ls = new LabelStyle();
			ls.font = font;
			ls.background = bSkin.getDrawable("background");
			Label label = new Label(optionsArray[i], ls);switch(Gdx.app.getType()){
			case Android:
				label.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				label.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			case iOS:
				label.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				label.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
			checkbox.setSize(0.2f*Gdx.graphics.getWidth(), 0.2f*Gdx.graphics.getHeight());
			checkbox.setScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
			checkbox.addListener(new MyChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
				}
			});
			choicesTable.add(checkbox).width(Gdx.graphics.getWidth()/4).height(Gdx.graphics.getHeight()/4);
			choicesTable.add(label);
			options.add(checkbox);
		}
	}
	 private void createSlider() {
		 sliderTable = new Table();
		 sliderSkin = new Skin();
		 muteStyle = new CheckBoxStyle();
		 font = game.getBitmapFontButton();
		 sliderSkin.addRegions((TextureAtlas) manager.get("Packs/Slider.pack"));
		 SliderStyle ss = new SliderStyle();
		 ss.background = sliderSkin.getDrawable("Slider_After");
		 ss.knobBefore = sliderSkin.getDrawable("Slider_Before");
		 ss.knobAfter = sliderSkin.getDrawable("Slider_After");
		 ss.knob = sliderSkin.getDrawable("Knob");
		 LabelStyle ls = new LabelStyle();
		 ls.font = font;
		 muteStyle.font = font;
		 muteStyle.checkboxOff = sliderSkin.getDrawable("Volume_On");
		 muteStyle.checkboxOn = sliderSkin.getDrawable("Mute");
		 mute = new CheckBox(" ", muteStyle);
		 mute.addListener(new MyChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				 super.changed(event, actor);
				 if(slider.getValue() != 0)
					 volume = slider.getValue();
				 if(option1)
					 before1 = true;
				 if(option2)
					 before2 = true;
				 if(option1 && !option2) {
					 before1 = true;
					 before2 = false;
				 }
				 if(!option1 && option2) {
					 before1 = false;
					 before2 = true;
				 }
				 if(mute.isChecked()) {
					 slider.setValue(0);
					 ScreenAdapterManager.getInstance().game.volume = slider.getValue()/100f;
					 slider.setDisabled(true);
					 game.muteAll = true;
				 }
				 else {
					 slider.setValue(volume);
					 slider.setDisabled(false);
					 game.muteAll = false;
				 }
			 }
		 });
		 slider = new Slider(0, 100, 1, false, ss);
		 slider.setValue(75);
		 slider.setDisabled(false);
		 ScreenAdapterManager.getInstance().game.volume = slider.getValue()/100f;
		 slider.addListener(new MyChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				 float value = ((Slider) actor).getValue();
				 ScreenAdapterManager.getInstance().game.volume = value/100f;
			 }
		 });
		 sliderName = new Label("Volume  ", ls);switch(Gdx.app.getType()){
			case Android:
				sliderName.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
				//if using the desktop set the width and height to a 16:9 resolution.
			case Desktop:
				sliderName.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			case iOS:
				sliderName.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
				break;
			default:
				sliderName.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
				break;
			}
		 sliderTable.add(sliderName).height(Gdx.graphics.getHeight()/3);
		 sliderTable.add(slider).width(600).height(Gdx.graphics.getHeight()/3);
		 sliderTable.add(mute).height(Gdx.graphics.getHeight()/3);
	 }
	 
	 
	 private void createOverallTable() {
		 overallTable = new Table();
		 createChoices();
		 createSlider();
		 overallTable.add(choicesTable);
		 overallTable.row();
		 overallTable.add(sliderTable);
		 overallTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 overallTable.align(Align.center);
	 }
	 
	 public boolean getMusic() {
		 return options.get(0).isChecked();
	 }
	 public boolean getSound() {
		 return options.get(1).isChecked();
	 }
	 public boolean getMute() {
		 return mute.isChecked();
	 }
	 public float getVolume() {
		 return slider.getValue();
	 }
}
