package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Select;
import com.sun.xml.internal.ws.encoding.policy.SelectOptimalEncodingFeatureConfigurator;

public class EditorScreen extends ScreenAdapter{
	private OrthographicCamera camera;
	
	private Stage stage;
	private TextButton ballButton;
	private TextButton backButton;
	TextButtonStyle textButtonStyleBack;

	private BitmapFont font;
	private Skin skinBack;
	private Skin skinBox;
	private Skin skinTable;
	
	private TextureAtlas buttonAtlas;
	private Table editorTable;
	private TGC_Engine mainGame;
	private Texture ballImage;
	private Texture objectImage;
	private Texture boxImage;
	
	private SpriteBatch batch;
	private Array<Rectangle> balls;
	private Array<Rectangle> boxes;
	private Array<String> objBox;
	private SelectBox<String> selection;

	private Array<Rectangle> grid;
	private Texture gridImage;
	
	private boolean ballOrBox = true;
	
	public EditorScreen(final TGC_Engine mainGame) {
		this.mainGame = mainGame;
		//camera = new OrthographicCamera(5,5);
		//createStage();
		createEditorTable();

		ballImage = new Texture(Gdx.files.internal("ball.png"));
		batch = new SpriteBatch();
		balls = new Array<Rectangle>();
		boxes = new Array<Rectangle>();
		boxImage = new Texture(Gdx.files.internal("Box.png"));
		gridImage = new Texture(Gdx.files.internal("Grid.png"));
		grid = new Array<Rectangle>();
		fillGrid();
		//	backButton.setVisible(true);

	}

	private TextButton createButtons() {
		font = new BitmapFont();
		skinBack = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("Packs/ButtonsEditor.pack"));
		skinBack.addRegions(buttonAtlas);
		textButtonStyleBack = new TextButtonStyle();
		textButtonStyleBack.font = font;
		textButtonStyleBack.up = skinBack.getDrawable("Button_Editor_Back");
		textButtonStyleBack.down = skinBack.getDrawable("ButtonPressed_Editor_Back");
		TextButton button = new TextButton("", textButtonStyleBack);
		
		button.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Back Button Pressed");
				mainGame.setScreen(mainGame.screens[3]);
			}
		});
		
		TextButtonStyle styleBall = new TextButtonStyle();
		styleBall.font = font;
		styleBall.up = skinBack.getDrawable("Button_Editor_Ball");
		styleBall.down = skinBack.getDrawable("ButtonPressed_Editor_Ball");
		TextButton ballButton = new TextButton("", styleBall);
		
		ballButton.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Ball Button Pressed");
				selectImage();
				System.out.println(objectImage.toString());
			}
		});		
		editorTable.add(ballButton);
		return button;
	}
	//	
//	private SelectBox<String[]> createSelectBox() {
//		skinBox = new Skin();
//        BitmapFont font = mainGame.getBitmapFontButton();
//        buttonAtlas = new TextureAtlas(Gdx.files.internal("Packs/ButtonsEditor.pack"));
//		skinBox.addRegions(buttonAtlas);
//		SelectBoxStyle style = new SelectBoxStyle();
//		
//		style.font =  font;
//		style.background = skinBox.getDrawable("ButtonPressed_Editor_Back");
//		
//		
//		SelectBox<String[]> box =  new SelectBox<String[]>();
//		System.out.println(box.getHeight());
//		String[] options = new String[2];
//		options[0] = "Ball";
//		options[1] = "Box";
//		box.setItems(options);
//		return box;
//	}
	
	private void createEditorTable() {
		editorTable = new Table();
		font = new BitmapFont();
		skinTable = new Skin();
		buttonAtlas = new TextureAtlas("Packs/ButtonsEditor.pack");
		skinTable.addRegions(buttonAtlas);
		TextButton button = createButtons();
		//SelectBox<String[]> box = createSelectBox();
		button.setSize(150,300);
		editorTable.add(button);
		//editorTable.add(box);
		editorTable.setPosition(0, 0);
	}
	
	private void createStage() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		//editorTable = new Table();
	}

	private void fillGrid() {
		for (int i=0; i<10; i++) {
			for (int j=(int) mainGame.getHeight(); j>150; j-=100) {
				Rectangle gridPiece = new Rectangle(i*100,j, 100, 100);
				grid.add(gridPiece);
			}
		}
	}
	
	@Override
	public void hide() {
		mainGame.removeTable(editorTable);
	}
	@Override
	public void render(float delta) {
//		//		super.render(delta);
//		//		stage.draw();
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
		if(Gdx.input.isTouched()) {
			spawnObject();
		}
//
		batch.begin();
		//		backButton.draw(batch, 1);
		for (Rectangle ball : balls) {
			batch.draw(ballImage, ball.x, ball.y);
		}
		for (Rectangle box : boxes) {
			batch.draw(boxImage, box.x, box.y);
		}
		for (Rectangle gridPiece : grid) {
			batch.draw(gridImage, gridPiece.x, gridPiece.y);
		}
		batch.end();

	};
	
	@Override
	public void show() {
		mainGame.addTable(editorTable);
	};
//	@Override
//	public void dispose() {
//		batch.dispose();
//		ballImage.dispose();
//
//	}
	private void spawnObject() {
		Rectangle object = new Rectangle();

		object.width = 64;
		object.height = 64;
		object.x = Gdx.input.getX() - object.getWidth()/2;
		object.y = Gdx.graphics.getHeight()-Gdx.input.getY() - object.getHeight()/2;
		for (Rectangle gridPos : grid) {
			if (gridPos.contains(object.x, object.y)) {
				object.x = gridPos.x;
				object.y = gridPos.y;
				break;
			}
		}
		if (ballOrBox) 
			balls.add(object);
		else
			boxes.add(object);
	}
	private void selectImage() {
		if (ballOrBox) {
			objectImage = ballImage;
			ballOrBox = !ballOrBox;
		}
		else {
			objectImage = boxImage;
			ballOrBox = !ballOrBox;
		}
	}
}