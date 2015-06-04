package org.TheGivingChild.Screens;

import java.awt.Checkbox;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Engine.XML.Attribute;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LoseEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
/**
 * This is the Editor Screen
 * This allows user to place objects with attributes on a screen and then export the level with a correct XML file.
 * @author Nathaniel Jacobi
 *
 */
class ScreenEditor extends ScreenAdapter{	
	private String levelName = "Base";
	private String packageName;
	//Style for the button
	private TextButtonStyle textButtonStyleBack;
	//Skins and the font
	private BitmapFont font;
	private Skin skinBack;
	private Skin skinTable;
	
	//TextureAtlas and the Table for the buttons
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
	private Texture objectImage;
	private Array<Texture> text;
	//Stores all created EditorGameObjects that were spawned by the user
	private Array<GameObject> gameObjects;
	
	private AssetManager manager;
	private boolean isRendered = false;
	private boolean isLoaded = false;
	
	private Dialog window;
	
	private Array<String> inputListeners;
	private ObjectMap<Attribute,Array<String>> attributes;
	private Array<CheckBox> checkButtons;
	/**
	 * The constructor for the screen editor.
	 * Gets the main game and the asset manager.
	 * Creates its button table, the dialog, and fills the grid. 
	 */
	public ScreenEditor() {
		//fill the placeholder from the ScreenManager
		text = new Array<Texture>();
		mainGame = ScreenAdapterManager.getInstance().game;
		manager = mainGame.getAssetManager();
		checkButtons = new Array<CheckBox>();
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
		inputListeners = new Array<String>();
		attributes = new ObjectMap<Attribute,Array<String>>();
	}
	//When hidden removes it's table
	
	/**
	 * Hides the editor button table and the GameObject dialog window if it's up.
	 */
	@Override
	public void hide() {
		editorTable.remove();
		window.remove();
	}
	//The render function. Listens for clicks on the board and draws the grid and objects that are spawned
	
	/**
	 * Draws the screen transition when the screen is shown.
	 * When transition is complete, if draws the grid and all GameObjects added.
	 * Also controls the drawing of the button table, so that it is hidden when not moused over or tapped.
	 * If the screen ever has a {@link com.badlogic.gdx.Input#isTouched() isTouched} input call, {@link #spawnObject() spawnObject()} is called.
	 * @param delta Amount of time passed between each render call. In seconds
	 */
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionInComplete) {
				Gdx.gl.glClearColor(0, 1, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				if(mainGame.getHeight() - Gdx.input.getY() <= 75 || 
						(editorTable.isVisible() && mainGame.getHeight() - Gdx.input.getY() < 150))
					enableButtons();
				else
					disableButtons();
				if(canSetObj) {
					batch.begin();
					batch.draw(objectImage, Gdx.input.getX() - objectImage.getWidth()/2
							, Gdx.graphics.getHeight() - Gdx.input.getY() - objectImage.getHeight()/2);
					batch.end();
				}
				
				//If touched, call spawnObjects
				if(Gdx.input.isTouched()) {
					spawnObject();
				}
				
				//Draws all EditorGameObjects stored and all grid pieces
				batch.begin();
				
				for (int i=0; i<gridCol; i++) {
					for (int j=0; j<gridRows; j++) {
						batch.draw((Texture) manager.get("editorAssets/Grid.png"), grid[i][j].x, grid[i][j].y);
					}
				}
				
				for (GameObject obj : gameObjects) {
					batch.draw((obj).getTexture(), obj.getX(), obj.getY());
				}
				batch.end();
				isRendered = true;
				if(!isLoaded) {
					show();
					isLoaded = true;
				}
			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();

	}
	//Shows the table when called upon
	
	/**
	 * Called when the screen is selected and everything is rendered.
	 * Adds the {@link #editorTable editorTable} to the game's stage and has the levelName window pop.
	 */
	@Override
	public void show() {
		if(isRendered) {
			mainGame.getStage().addActor(editorTable);
			editorTable.setVisible(false);
			window.setX(Gdx.graphics.getWidth()/2);
			window.setY(Gdx.graphics.getHeight()/2);

			EditorTextInputListener listener = new EditorTextInputListener();
			Gdx.input.getTextInput(listener, "Level Name", "", "Level Name");
			isRendered = false;
		}
	};
	
//	//Dispose, will be implemented later
//	@Override
//	public void dispose() {
//		
//	}

	//Function to instantiate the button table
	/**
	 * Function that sets up the buttonTable at the bottom of the screen
	 * Calls {@link #createButtons() createButtons()}  to add to the table.
	 */
	private void createEditorTable() {
		//Sets up the needed variables and parameters
		editorTable = new Table();
		skinTable = new Skin();
		skinTable.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		
		//Creates the buttons and sets table to origin
		createButtons();
		editorTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		editorTable.align(Align.bottom);
		editorTable.setVisible(false);
	}
	
	
	
	//Fills the grid according to the gridImage size relative to the screen size
	/**
	 * Creates the grid used for the editor.
	 * Based on the gridImage size that will be used and the screen size.
	 * Initializes rectangles to go with the images to deal with detection.
	 */
	private void fillGrid() {
		for (int i=0; i*gridSize<Gdx.graphics.getWidth(); i++) {
			gridCol++;
		}
		for (int j=(int) Gdx.graphics.getHeight(); j>0; j-=gridSize) {
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
	/**
	 * Called when Gdx.input.isTouched() is true, so there was a touch on the screen.
	 * If the user is able to make a {@link org.TheGivingChild.Engine.XML.GameObject GameObject}, the object is placed in a particular place.
	 * It checks the grid rectangles to see which one was touched and this is where the GameObject will go.
	 * If there is a {@link org.TheGivingChild.Engine.XML.GameObject GameObject} there already, it replaces it and assumes its ID.
	 * It is then added to and array of {@link org.TheGivingChild.Engine.XML.GameObject GameObject}, and the {@link #canSetObj canSetObj} boolean is reset.
	 */
	private void spawnObject() {
		//If canSetObj is false, no object is spawned
		if (!canSetObj)
			return;
		
		//Variables used, EditorGameObject to store the new instance and a bool to keep track
		GameObject obj;
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
					//Create the new editor game object
					attributes = attributesSelected();
					obj = new GameObject(gameObjects.size, manager.getAssetFileName(objectImage), drawPos, 
							inputListeners, attributes);
					for (int k=0; k<gameObjects.size; k++) {
						//If there is an object in the grid piece already, it gets replaced
						if(gameObjects.get(k).getX() == obj.getX() && gameObjects.get(k).getY() == obj.getY()) {
							obj = new GameObject(gameObjects.get(k).getID(), manager.getAssetFileName(objectImage),	
									drawPos, inputListeners, attributes);
							gameObjects.set(k, obj);
							added = true;
						}
					}
					System.out.println(obj.getAttributes());
					if (!added)
						gameObjects.add(obj);
					break; //Break out of the loop since the position is found.
				}
			}
		}
		canSetObj = false;
		resetCheckBoxes();
	}
	
	//Switches between two images
	
	/**
	 * Increments every time the ballButton is pressed and selects a particular image for the object to use
	 */
	private void selectImage() {
		ballOrBox++;
		ballOrBox = ballOrBox % 3;
		if (ballOrBox == 0) {
			objectImage =  manager.get("editorAssets/ball.png");
		}
		else if (ballOrBox == 1) {
			objectImage =  manager.get("editorAssets/Box.png");
		}
		else if (ballOrBox == 2) {
			objectImage = manager.get("editorAssets/BoxHalf.png");
		}
	}
	
	/**
	 * Shows the editorButtonTable when applicable.
	 */
	private void enableButtons() {
		editorTable.setVisible(true);	
	}
	
	/**
	 * Shows the editorButtonTable when applicable.
	 */
	private void disableButtons() {
		editorTable.setVisible(false);
	}
	
	/**
	 * Class for the little textBox that comes up when the screen is shown
	 * @author Nathaniel Jacobi
	 *
	 */
	private class EditorTextInputListener implements TextInputListener {
		/**
		 * Called when the OK button is selected.
		 * Sets the textInput to be the {@link ScreenEditor#levelName levelName}
		 * @param text The inputed text
		 */
		@Override
		public void input(String text) {
			levelName = text;
		}
		
		/**
		 * Empty, called when the cancel button is selected. 
		 */
		@Override
		public void canceled() {
			
		}
		
	}
	
	/**
	 * Creates (for now) all the buttons that are going to be used by the screen
	 * First the table buttons are created and added.
	 * Then the dialog window's buttons are created and added.
	 */
	private void createButtons() {
		//Initializes all that is needed for the Back button and gets the textured needed
		font = new BitmapFont();
		skinBack = new Skin();
		skinBack.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		textButtonStyleBack = new TextButtonStyle();
		textButtonStyleBack.font = font; 
		textButtonStyleBack.up = skinBack.getDrawable("Button_Editor_Back");
		textButtonStyleBack.down = skinBack.getDrawable("ButtonPressed_Editor_Back");
		TextButton backButton = new TextButton("", textButtonStyleBack);
		
		//Creates the listener for the Back button
		backButton.addListener(new ChangeListener() { 
			/**
			 * Called when the backButton is selected.
			 * Uses the ScreenAdapterManager and selects the screen to be the Main screen.
			 */
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Calls the screen manager and has main be the shown screen if Back is hit
				//window.setVisible(false);
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
			/**
			 * Called when the ballButton is selected.
			 * Calls selectImage() and sets the dialog window for GameObjects to be visible.
			 */
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Changes the image to be drawn and lets the user place one object
				selectImage();
				window.setVisible(true);
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
			/**
			 * Called when the exportButton is selected.
			 * Calls the game's {@link org.TheGivingChild.Engine.XML.XML_Writer XMLWriter} and outputs the level file.
			 */
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
				
				Level level = new Level(levelName, "packageTest", "test.png",testWinArray, testLoseArray, gameObjects);
				mainGame.getXML_Writer().createLevel(level);
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);				
			}
		});
		
		editorTable.add(exportButton).align(Align.bottom);
		Window.WindowStyle winStyle = new Window.WindowStyle();
		winStyle.titleFont = font;
		window = new Dialog("Onject Select" , winStyle);
		
		TextButtonStyle okStyle = new TextButtonStyle();
		okStyle.font = font;
		okStyle.up = skinBack.getDrawable("Button_Editor_Ok");
		okStyle.down = skinBack.getDrawable("Button_Editor_OkPressed");
		TextButton okButton = new TextButton("", okStyle);
		
		TextButtonStyle cancelStyle = new TextButtonStyle();
		cancelStyle.font = font;
		cancelStyle.up = skinBack.getDrawable("Button_Editor_Cancel");
		cancelStyle.down = skinBack.getDrawable("Button_Editor_CancelPressed");
		TextButton cancelButton = new TextButton("", cancelStyle);

		CheckBoxStyle attStyle = new CheckBoxStyle();
		attStyle.font = font;
		attStyle.font.setColor(Color.RED);
		attStyle.checkedOverFontColor = Color.CYAN;
		attStyle.checkboxOff = skinBack.getDrawable("CheckBox_Editor_Destroy");
		attStyle.checkboxOn = skinBack.getDrawable("CheckBox_Editor_DestroyChecked");
		CheckBox destroyBox = new CheckBox("Destroy", attStyle);
		window.add(destroyBox);
		checkButtons.add(destroyBox);
		for (final Attribute enums: Attribute.values()) {
			CheckBoxStyle attributeStyle = new CheckBoxStyle();
			attributeStyle.font = font;
			attributeStyle.checkboxOff = skinBack.getDrawable("CheckBox_Editor_Destroy");
			attributeStyle.checkboxOn = skinBack.getDrawable("CheckBox_Editor_DestroyChecked");
			CheckBox attributeBox = new CheckBox(enums.getXMLName(), attributeStyle);
			window.row();
			window.add(attributeBox);
			checkButtons.add(attributeBox);
			window.setKeepWithinStage(true);
			attributeBox.setName(enums.getXMLName());
			//attributeBox.addListener(new ChangeListener()  {
				/**
				 * Called when any of the {@link org.TheGivingChild.Engine.XML.Attribute Attribute} checkboxes are selected.
				 * If the {@link org.TheGivingChild.Engine.XML.Attribute Attribute} is in the map, it is removed.
				 * If it is not, it is added.
				 */
//				@Override
//				public void changed(ChangeEvent event, Actor actor) {
//					if (attributes.containsKey(enums)) {
//						attributes.remove(enums);
//				}
//					else {
//						Array<String> attributeValues = new Array<String>();
//						for (int i=0; i < enums.getVariableNames().size; i++) {
//							attributeValues.add("0.0");
//						}
//						attributes.put(enums, attributeValues);
//						System.out.println(attributes.size);
//					}}
//			});
		}
		window.row();
		window.add(okButton);
		window.add(cancelButton);
		
		okButton.addListener(new ChangeListener() {
			/**
			 * When OK is pressed, allows the user to place a GameObject.
			 * Sets the attribute window to not visible.
			 */
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				canSetObj = true;
				window.setVisible(false);
			}
			
		});
		
		cancelButton.addListener(new ChangeListener() {
			/**
			 * When cancel is selected, sets the attribute window to not visible
			 */
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				window.setVisible(false);
			}
		});
		
		destroyBox.addListener(new ChangeListener() {
			/**
			 * Called if the destroy in click listener is checked. 
			 * It adds it to the list of listeners if it isn't in the list, removes if it is.  
			 */
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String destroy = "destroy_on_click";
				if (inputListeners.contains(destroy, false))
					inputListeners.removeValue(destroy, false);
				else 
					inputListeners.add(destroy);	
					
			}
			
		});
		
		window.align(Align.topLeft);
		window.setVisible(false);
		window.show(mainGame.getStage());
	}
	
	private void resetCheckBoxes() {
		for (CheckBox button: checkButtons)
			button.setChecked(false);
	}
	
	private ObjectMap<Attribute,Array<String>> attributesSelected() {
		ObjectMap<Attribute,Array<String>> selectedAttributes = new ObjectMap<Attribute, Array<String>>();
		for (CheckBox button: checkButtons) {
			if (button.isChecked()) {
				Attribute enums = Attribute.newType(button.getName());
				Array<String> attributeValues = new Array<String>();
				for (int i=0; i < enums.getVariableNames().size; i++) {
					attributeValues.add("0.0");
				}
				selectedAttributes.put(enums, attributeValues);
			}
		}
		return selectedAttributes;
	}
}
