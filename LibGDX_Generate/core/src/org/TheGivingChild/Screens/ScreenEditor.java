package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LoseEnum;

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
import com.badlogic.gdx.utils.Align;
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
	private Texture gridImage;
	private float gridSize;
	private int gridRows = 0;
	private int gridCol = 0;
	private Rectangle grid[][];
	//Sees if object can be placed, goes to true when ballButton is hit
	private boolean canSetObj = false;
	
	//Toggles the Textures used
	private int ballOrBox = 1;
	
	//create placeholder game
	private TGC_Engine mainGame;
	
	//Stores the texture that is going to be used by the a EditorGameObject
	private ObjectTexture objectImage;
	
	//Stores all created EditorGameObjects that were spawned by the user
	private Array<GameObject> gameObjects;
	
	public ScreenEditor() {
		//fill the placeholder from the ScreenManager
		mainGame = ScreenAdapterManager.getInstance().game;
		createEditorTable();
		
		//Instantiates the SpriteBatch, gridImage Texture and its Array
		batch = new SpriteBatch();
		gridImage = (Texture) mainGame.getAssetManager().get("editorAssets/Grid.png");		
		//Instantiate Array for the gameObjects
		gameObjects = new Array<GameObject>();
		
		//This is so that there is no nullPointerEx, so objectImage has a texture 
		selectImage();
		
		//Makes sure the grid is based off the image size and then fills the grid out
		gridSize = gridImage.getHeight();
		fillGrid();
	}
	//When hidden removes it's table
	@Override
	public void hide() {
		editorTable.remove();
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
		
		for (int i=0; i<gridCol; i++) {
			for (int j=0; j<gridRows; j++) {
				batch.draw((Texture) mainGame.getAssetManager().get("editorAssets/Grid.png"), grid[i][j].x, grid[i][j].y);
			}
		}
		
		for (GameObject obj : gameObjects) {
			batch.draw(((EditorGameObject) obj).getTexture(), obj.getX(), obj.getY());
		}
		batch.end();

	}
	//Shows the table when called upon
	@Override
	public void show() {
		mainGame.getStage().addActor(editorTable);
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
		skinTable.addRegions((TextureAtlas) mainGame.getAssetManager().get("Packs/ButtonsEditor.pack"));
		
		//Creates the buttons and sets table to origin
		createButtons();
		editorTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		editorTable.align(Align.bottom);
	}
	
	private void createButtons() {
		//Initializes all that is needed for the Back button and gets the textured needed
		font = new BitmapFont();
		skinBack = new Skin();
		skinBack.addRegions((TextureAtlas) mainGame.getAssetManager().get("Packs/ButtonsEditor.pack"));
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
		editorTable.add(backButton).align(Align.bottom);
		
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
		editorTable.add(ballButton).align(Align.bottom);
		
		TextButtonStyle styleExport = new TextButtonStyle();
		styleExport.font = font;
		styleExport.up = skinBack.getDrawable("Button_Editor_Export");
		styleExport.down = skinBack.getDrawable("Button_Editor_ExportPressed");
		
		TextButton exportButton = new TextButton("", styleExport);
		
		exportButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				WinEnum testWin1 = WinEnum.COLLISIONWITHOBJECTWIN;
				Array<String> testWin1Values = new Array<String>();
				testWin1Values.add("420");
				testWin1Values.add("9001");
				testWin1.setValues(testWin1Values);
				Array<WinEnum> testWinArray = new Array<WinEnum>();
				testWinArray.add(testWin1);
				
				LoseEnum testLose1 = LoseEnum.TIMEOUT;
				Array<String>testLose1Values =  new Array<String>();
				testLose1Values.add("42");
				testLose1.setValues(testLose1Values);
				Array<LoseEnum> testLoseArray = new Array<LoseEnum>();
				testLoseArray.add(testLose1);
				
				LoseEnum testLose2 = LoseEnum.COLLISIONWITHOBJECTLOSE;
				Array<String>testLose2Values =  new Array<String>();
				testLose2Values.add("42");
				testLose2Values.add("72");
				testLose2.setValues(testLose2Values);
				testLoseArray.add(testLose2);
				
				Level level = new Level("tester", "packageTest", "test.png",testWinArray, testLoseArray, gameObjects);
				mainGame.getXML_Writer().createLevel(level);
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);				
			}
			
		});
		
		editorTable.add(exportButton).align(Align.bottom);
	}
	
	//Fills the grid according to the gridImage size relative to the screen size
	private void fillGrid() {
		for (int i=0; i*gridSize<Gdx.graphics.getWidth(); i++) {
			gridCol++;
		}
		for (int j=(int) Gdx.graphics.getHeight(); j>150; j-=gridSize) {
			gridRows++;
		}
		
		grid = new Rectangle[gridCol][gridRows];
		for (int i=0; i<gridCol; i++) {
			for (int j=0; j<gridRows; j++) {
				grid[i][j] = new Rectangle(i*gridSize, Gdx.graphics.getHeight() - j*gridSize, gridSize, gridSize);
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
		
		for (int i=0; i<gridCol; i++) {
			for (int j=0; j<gridRows; j++) {
				if (grid[i][j].contains(x,y)) {
					x = grid[i][j].x;
					y = grid[i][j].y;
					float[] drawPos =  {x, y};
					int[] gridPos = {i, j};
					
					//Create the new editor game object
					obj = new EditorGameObject(gameObjects.size, objectImage.getFile(), drawPos, gridPos);
					for (int k=0; k<gameObjects.size; k++) {
						//If there is an object in the grid piece already, it gets replaced
						if(gameObjects.get(k).getX() == obj.getX() && gameObjects.get(k).getY() == obj.getY()) {
							gameObjects.set(k, obj);
							added = true;
						}
					}
					
					if (!added)
						gameObjects.add(obj);
					break; //Break out of the loop since the position is found.
				}
			}
		}
		
		canSetObj = false;
		
	}
	
	//Switches between two images
	private void selectImage() {
		ballOrBox++;
		ballOrBox = ballOrBox % 3;
		if (ballOrBox == 0) {
			objectImage = ObjectTexture.BALL_TEXTURE;
		}
		else if (ballOrBox == 1) {
			objectImage = ObjectTexture.BOX_TEXTURE;
		}
		else if (ballOrBox == 2) {
			objectImage = ObjectTexture.HALF_BOX_TEXTURE;
		}
	}
	
	//Extends the GameObject Class so that it can store the texture and a rectangle
	private class EditorGameObject extends GameObject{
		//Stores the Texture Enum and a rectangle
		private ObjectTexture texture;
		private Rectangle rectangle;
		private int grid[];
		//
		public EditorGameObject(int newID, String img, float[] newPosition, int[] gridPos) {
			super(newID, img, newPosition);
			grid = gridPos;
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
			return getEnum().getTexture();
		}
	}
}
