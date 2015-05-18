package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Select;
import com.sun.xml.internal.ws.encoding.policy.SelectOptimalEncodingFeatureConfigurator;

public class EditorScreen extends ScreenAdapter{
	private Stage stage;
	private TextButton ballButton;
	private TextButton backButton;
	TextButtonStyle textButtonStyle;

	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;
	private Table editorTable;
	private TGC_Engine mainGame;
	private Texture ballImage;
	private SpriteBatch batch;
	private Array<Rectangle> balls;
	private Array<String> objBox;
	private SelectBox<String> selection;

	public EditorScreen(final TGC_Engine mainGame) {
		this.mainGame = mainGame;

		//createStage();
		createEditorTable();

		ballImage = new Texture(Gdx.files.internal("ball.png"));
		batch = new SpriteBatch();
		balls = new Array<Rectangle>();
		//	backButton.setVisible(true);

	}
	private void createEditorTable() {
		editorTable = new Table();
		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas("Packs/ButtonsEditor.pack");
		skin.addRegions(buttonAtlas);
		TextButton button = createButtons();
		button.setSize(150,300);
		editorTable.add(button);
		editorTable.setPosition(0, 0);
	}
	private void createStage() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		//editorTable = new Table();
	}
	private TextButton createButtons() {
		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("Packs/ButtonsEditor.pack"));
		skin.addRegions(buttonAtlas);
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("Button_Editor_Back");
		textButtonStyle.down = skin.getDrawable("ButtonPressed_Editor_Back");
		TextButton button = new TextButton("", textButtonStyle);

		button.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Back Button Pressed");
				mainGame.setScreen(mainGame.screens[3]);
			}
		});
		return button;
	}
	@Override
	public void render(float delta) {
//		//		super.render(delta);
//		//		stage.draw();
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		if(Gdx.input.isTouched()) {
//			spawnBall();
//		}
//
//		batch.begin();
//		backButton.draw(batch, 1);
//		//		for (Rectangle ball : balls) {
//		//			batch.draw(ballImage, ball.x, ball.y);
//		//		}
//		batch.end();
		
	}
	@Override
	public void show() {
		mainGame.addTable(editorTable);
	};
	
	@Override
	public void hide() {
		mainGame.removeTable(editorTable);
	};
//	@Override
//	public void dispose() {
//		batch.dispose();
//		ballImage.dispose();
//
//	}
	private void spawnBall() {
		Rectangle ball = new Rectangle();

		ball.width = 64;
		ball.height = 64;
		ball.x = Gdx.input.getX() - ball.getWidth()/2;
		ball.y = Gdx.graphics.getHeight()-Gdx.input.getY() - ball.getHeight()/2;

		balls.add(ball);
	}

}
