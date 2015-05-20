package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

class ScreenEditor extends ScreenAdapter{	
	//Enumerator for the textures that we use for the objects
	public enum ObjectTexture {
		BALL_TEXTURE("ball.png"),
		HALF_BOX_TEXTURE("BoxHalf.png"),
		BOX_TEXTURE("Box.png");
		
		//The texture used, the whole file path and the file used like "ball.png"
		private Texture texture;
		private String filename = "editorAssets/"; //Folder they are stored in
		private String file;
		
		private ObjectTexture(String file) {
			this.file = file;
			//Adds the file to the folder path the create the correct path
			filename = filename.concat(file);
			//Sets the texture
			texture = new Texture(Gdx.files.internal(filename));
		}
		
		//Getters
		public Texture getTexture() {
			return texture;
		}
		public String getFile() {
			return file;
		}
	}
	
	//Style for the button
	private TextButtonStyle textButtonStyleBack;

	//Skins and the font
	private BitmapFont font;
	private Skin skinBack;
	private Skin skinTable;
	
	//TextureAtlas and the Table for the buttons
	private TextureAtlas buttonAtlas;
	private Table editorTable;
	
	//SpriteBatch that draws the objects
	private SpriteBatch batch;
	
	//Variables used for the grid, its data structure, texture, and size
	private Array<Rectangle> grid;
	private Texture gridImage;
	private float gridSize;

	//Sees if object can be placed, goes to true when ballButton is hit
	private boolean canSetObj = false;
	
	//Toggles the Textures used
	private boolean ballOrBox = true;
	
	//create placeholder game
	private TGC_Engine mainGame;
	
	//Stores the texture that is going to be used by the a EditorGameObject
	private ObjectTexture objectImage;
	
	//Stores all created EditorGameObjects that were spawned by the user
	private Array<EditorGameObject> gameObjects;
	
	public ScreenEditor() {
		//fill the placeholder from the ScreenManager
		mainGame = ScreenAdapterManager.getInstance().game;
		createEditorTable();
		
		//Instantiates the SpriteBatch, gridImage Texture and its Array
		batch = new SpriteBatch();
		gridImage = new Texture(Gdx.files.internal("editorAssets/Grid.png"));
		grid = new Array<Rectangle>();
		
		//Instantiate Array for the gameObjects
		gameObjects = new Array<ScreenEditor.EditorGameObject>();
		
		//This is so that there is no nullPointerEx, so objectImage has a texture 
		selectImage();
		
		//Makes sure the grid is based off the image size and then fills the grid out
		gridSize = gridImage.getHeight();
		fillGrid();
	}
	//When hidden removes it's table
	@Override
	public void hide() {
		mainGame.removeTable(editorTable);
	}
	//The render function. Listens for clicks on the board and draws the grid and objects that are spawned
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		//If touched, call spawnObjects
		if(Gdx.input.isTouched()) {
			spawnObject();
		}
		
		//Draws all EditorGameObjects stored and all grid pieces
		batch.begin();
		
		for (EditorGameObject obj : gameObjects) {
			batch.draw(obj.getTexture(), obj.getX(), obj.getY());
		}
		for (Rectangle gridPiece : grid) {
			batch.draw(gridImage, gridPiece.x, gridPiece.y);
		}
		batch.end();

	}
	//Shows the table when called upon
	@Override
	public void show() {
		mainGame.addTable(editorTable);
	};
	
	//Dispose, will be implemented later
//	@Override
//	public void dispose() {
//	}

	//Function to instantiate the button table
	private void createEditorTable() {
		//Sets up the needed variables and parameters
		editorTable = new Table();
		skinTable = new Skin();
		buttonAtlas = new TextureAtlas("Packs/ButtonsEditor.pack");
		skinTable.addRegions(buttonAtlas);
		
		//Creates the buttons and sets table to origin
		createButtons();
		editorTable.setPosition(0, 0);
	}
	
	private void createButtons() {
		//Initializes all that is needed for the Back button and gets the textured needed
		font = new BitmapFont();
		skinBack = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("Packs/ButtonsEditor.pack"));
		skinBack.addRegions(buttonAtlas);
		textButtonStyleBack = new TextButtonStyle();
		textButtonStyleBack.font = font; 
		textButtonStyleBack.up = skinBack.getDrawable("Button_Editor_Back");
		textButtonStyleBack.down = skinBack.getDrawable("ButtonPressed_Editor_Back");
		TextButton backButton = new TextButton("", textButtonStyleBack);
		
		//Creates the listener for the Back button
		backButton.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Calls the screen manager and has main be the shown screen if Back is hit
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			}
		});
		//Setting the size and adding the Back button to the table
		backButton.setSize(150,300);
		editorTable.add(backButton);
		
		//Uses some of the same variables, so gets images ready for the Ball button
		TextButtonStyle styleBall = new TextButtonStyle();
		styleBall.font = font;
		styleBall.up = skinBack.getDrawable("Button_Editor_Ball");
		styleBall.down = skinBack.getDrawable("ButtonPressed_Editor_Ball");
		TextButton ballButton = new TextButton("", styleBall);
		
		//Ball button listener
		ballButton.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Changes the image to be drawn and lets the user place one object
				selectImage();
				canSetObj = true;
			}
		});
		//Adds the Ball button
		editorTable.add(ballButton);
	}
	
	//Fills the grid according to the gridImage size relative to the screen size
	private void fillGrid() {
		for (int i=0; i*gridSize<Gdx.graphics.getWidth(); i++) {
			for (int j=(int) Gdx.graphics.getHeight(); j>150; j-=gridSize) {
				Rectangle gridPiece = new Rectangle(i*gridSize,j, gridSize, gridSize);
				grid.add(gridPiece); //Stored for when it's called later to be drawn
			}
		}
	}
	
	//Called when there is a touch on the screen
	private void spawnObject() {
		//If canSetObj is false, no object is spawned
		if (!canSetObj)
			return;
		
		//Variables used, EditorGameObject to store the new instance and a bool to keep track
		EditorGameObject obj;
		boolean added = false;
		
		//Store the coordinates of where the user clicked
		float x = Gdx.input.getX();
		float y = Gdx.graphics.getHeight()-Gdx.input.getY();
		
		//Goes through each grid piece
		for (Rectangle gridPos : grid) {
			//If the grid piece contains the point that was clicked, this is where the obj will go
			if (gridPos.contains(x, y)) {
				//Takes the grid piece's coordinates and stores into applicable data structure
				x = gridPos.x;
				y = gridPos.y;
				float[] pnt =  {x, y};
				
				//Instantiates EditorGameObject from the current image and its location
				obj = new EditorGameObject(gameObjects.size, objectImage.getFile(),pnt);
				for (int i=0; i<gameObjects.size; i++) {
					//If there is an object in the grid peice already, it gets replaced
					if(gameObjects.get(i).getX() == x && gameObjects.get(i).getY() == y) {
						gameObjects.set(i, obj);
						added = true;
					}
				}
				//If it hasn't been added, it gets stored in the container
				if (!added)
					gameObjects.add(obj);
				break; //Break out of the loop since the position is found.
			}
		}
		// Resets the boolean so only one object can be placed 
		canSetObj = false;
	}
	
	//Switches between two images
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
	
	//Extends the GameObject Class so that it can store the texture and a rectangle
	private class EditorGameObject extends GameObject{
		//Stores the Texture Enum and a rectangle
		private ObjectTexture texture;
		private Rectangle rectangle;
		
		//
		public EditorGameObject(int newID, String img, float[] newPosition) {
			super(newID, img, newPosition);
			//Goes through all the enums and when it finds the correct one, it stores it to the variable
			for (ObjectTexture itr : ObjectTexture.values()) {
				if (img.equals(itr.getFile())) {
					texture = itr;
					break;
				}
			}
			//Sets the rectangle to the correct position and correct dimensions
			rectangle = new Rectangle(newPosition[0], newPosition[1], 
					texture.getTexture().getWidth(), texture.getTexture().getHeight());
		}
		
		//Getters for the object
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
