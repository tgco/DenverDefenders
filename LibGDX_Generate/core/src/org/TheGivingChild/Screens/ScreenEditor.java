package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.sun.org.glassfish.gmbal.GmbalException;

class ScreenEditor extends ScreenAdapter{	
	public enum ObjectTexture {
		BALL_TEXTURE("ball.png"),
		HALF_BOX_TEXTURE("BoxHalf.png"),
		BOX_TEXTURE("Box.png");
		
		private Texture texture;
		private String filename = "editorAssets/"; //Folder they are stored in
		private String file;
		
		private ObjectTexture(String file) {
			this.file = file;
			filename = filename.concat(file);
			texture = new Texture(Gdx.files.internal(filename));
		}
		public Texture getTexture() {
			return texture;
		}
		public String getFile() {
			return file;
		}
	}
	
	private TextButton ballButton;
	private TextButton backButton;
	private TextButtonStyle textButtonStyleBack;

	private BitmapFont font;
	private Skin skinBack;
	private Skin skinTable;
	
	private TextureAtlas buttonAtlas;
	private Table editorTable;
	
	private ObjectTexture objectImage;
	
	private SpriteBatch batch;
	private Array<Rectangle> balls;
	private Array<Rectangle> boxes;
		
	private boolean canSetObj = false;
	private Array<Rectangle> grid;
	private Texture gridImage;
	private boolean ballOrBox = true;
	private float gridSize;
	
	//create placeholder game
	private TGC_Engine mainGame;
	
	private Array<EditorGameObject> gameObjects;
	
	public ScreenEditor() {
		//fill the placeholder from the ScreenManager
		mainGame = ScreenAdapterManager.getInstance().game;
		//createStage();
		createEditorTable();
		//textureSize();
		
		//selection = new SelectBox<String>(skinTable);
		
		batch = new SpriteBatch();
		gridImage = new Texture(Gdx.files.internal("editorAssets/Grid.png"));
		grid = new Array<Rectangle>();
		gameObjects = new Array<ScreenEditor.EditorGameObject>();
		selectImage();
		createSelectBox();
		gridSize = gridImage.getHeight();
		fillGrid();
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
				
		if(Gdx.input.isTouched()) {
			//System.out.println("X: " + Gdx.input.getX());
			//System.out.println("Y: " + Gdx.input.getY());

			spawnObject();
		}
//
		batch.begin();
		
		for (EditorGameObject obj : gameObjects) {
			batch.draw(obj.getTexture(), obj.getX(), obj.getY());
		}
		for (Rectangle gridPiece : grid) {
			batch.draw(gridImage, gridPiece.x, gridPiece.y);
			//System.out.println("Height: " + gridImage.getHeight());
			//System.out.println(gridPiece.height + " " + gridPiece.getAspectRatio() + " " + gridPiece.getX());
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

	private void createEditorTable() {
		editorTable = new Table();
		font = new BitmapFont();
		skinTable = new Skin();
		buttonAtlas = new TextureAtlas("Packs/ButtonsEditor.pack");
		skinTable.addRegions(buttonAtlas);
		TextButton button = createButtons();
		button.setSize(150,300);
		editorTable.add(button);
		editorTable.setPosition(0, 0);
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
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
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
				selectImage();
				canSetObj = true;
			}
		});		
		editorTable.add(ballButton);
		return button;
	}
	
	private void fillGrid() {
		for (int i=0; i*gridSize<Gdx.graphics.getWidth(); i++) {
			for (int j=(int) Gdx.graphics.getHeight(); j>150; j-=gridSize) {
				Rectangle gridPiece = new Rectangle(i*gridSize,j, gridSize, gridSize);
				grid.add(gridPiece);
			}
		}
	}
	
	private void spawnObject() {
		if (!canSetObj)
			return;
		EditorGameObject obj;
		boolean added = false;
		float x = Gdx.input.getX();
		float y = Gdx.graphics.getHeight()-Gdx.input.getY();
		for (Rectangle gridPos : grid) {
			if (gridPos.contains(x, y)) {
				x = gridPos.x;
				y = gridPos.y;
				float[] pnt =  {x, y};
				obj = new EditorGameObject(gameObjects.size, objectImage.getFile(),pnt);
				for (int i=0; i<gameObjects.size; i++) {
					if(gameObjects.get(i).getX() == x && gameObjects.get(i).getY() == y) {
						gameObjects.set(i, obj);
						added = true;
					}
				}
				if (!added)
					gameObjects.add(obj);
				break;
			}
		}
		canSetObj = false;
	}
	
	private void selectImage() {
		if (ballOrBox) {
			objectImage = ObjectTexture.BALL_TEXTURE;
			ballOrBox = !ballOrBox;
		}
		else {
			objectImage = ObjectTexture.HALF_BOX_TEXTURE;
			ballOrBox = !ballOrBox;
		}
	}
	
	private void createSelectBox() {
	}
	
	private class EditorGameObject extends GameObject{
		private ObjectTexture texture;
		private Rectangle rectangle;
		
		public EditorGameObject(int newID, String img, float[] newPosition) {
			super(newID, img, newPosition);
			for (ObjectTexture itr : ObjectTexture.values()) {
				if (img.equals(itr.getFile())) {
					texture = itr;
					break;
				}
			}
			rectangle = new Rectangle(newPosition[0], newPosition[1], 
					texture.getTexture().getWidth(), texture.getTexture().getHeight());
		}
		
		public ObjectTexture getEnum() {
			return texture;
		}
		public Rectangle getRectangle() {
			return rectangle;
		}
		public Texture getTexture() {
			return texture.getTexture();
		}
	}
}
