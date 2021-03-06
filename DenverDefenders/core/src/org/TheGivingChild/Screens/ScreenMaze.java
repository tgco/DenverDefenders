package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Maze.ChildSprite;
import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.MazeInputProcessor;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.PowerUpStage;
import org.TheGivingChild.Engine.Maze.Vertex;
import org.TheGivingChild.Engine.PowerUps.PowerUp;
import org.TheGivingChild.Engine.PowerUps.PowerUpEnum;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Screens.UI.CurtainScreenTransition;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
/**
 *Maze screen that the user will navigate around.
 *Player will be able to trigger a miniGame by finding a child in the maze.
 *@author mtzimour
 */

public class ScreenMaze extends ScreenAdapter {
	/** Orthographic Camera to look at map from top down */
	private OrthographicCamera camera;
	/** Tiled map renderer to display the map */
	private TiledMapRenderer mapRenderer;
	/** Sprite, SpriteBatch, and Texture for users sprite */
	private SpriteBatch spriteBatch;
	// Main character
	private PlayerSprite playerCharacter;
	// The input processor that allows player movement on drag
	private InputProcessor mazeInput;
	private TGC_Engine game;
	// All the children for the maze
	private Array<ChildSprite> mazeChildren;
	// Kids moved to this array when saved
	private Array<ChildSprite> savedChildren;
	// Duplicate reference to the ones that are following
	private Array<ChildSprite> followers;
	// Draw responsibilities
	private Texture heartTexture;
	private Texture childTexture;
	private BitmapFont font;
	// Set on collide with child, used to have a reference to set as a follower if needed
	private ChildSprite lastChild;
	//True if game progression logic is paused
	private boolean logicPaused;
	// True if won the minigame returned from
	private boolean levelWon;
	// True if all the children were found and the maze has been won
	private boolean mazeWon;
	// The maze object that manages allowed movement
	private Maze maze;
	// Stage which contains the powerup buttons
	private PowerUpStage powerUpStage;
	// Array of currently active powerups
	private Array<PowerUp> activePowerUps;
	/**{@link #currentLevel} keeps track of the current level being played.*/
	private Level currentLevel;
	
	/*
	 * Static vars used to set which assets must be loaded to construct/init the screen.
	 */
	/**{@link #levelSet} is the container for levels to be played during a maze.*/
	private static Array<Level> levelSet = new Array<Level>();
	// The boss level for this maze
	private static Level bossLevel;
	// The name of the maze which corresponds to the assets directory to look into
	public static String activeMaze = "UrbanMaze1";
	// The number of this maze.  Used to determine if another maze is unlocked on winning
	public static int mazeNumber;
	// Type of the maze, kids or tots
	public static String mazeType;

	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */

	public ScreenMaze(){
		game = ScreenAdapterManager.getInstance().game;
		mazeInput = new MazeInputProcessor(this, this.game);
		mazeChildren = new Array<ChildSprite>();
		savedChildren = new Array<ChildSprite>();
		followers = new Array<ChildSprite>();
		activePowerUps = new Array<PowerUp>();
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		font = new BitmapFont();
		font.getData().setScale(game.getGlobalFontScale()/2);
	}

	// Must run before starting a new maze, sets state appropriately
	public void init() {
		logicPaused = true;
		levelWon = false;
		mazeWon = false;
		currentLevel = null;
		lastChild = null;

		TiledMap map = game.getAssetManager().get("MazeAssets/" + activeMaze + "/" + activeMaze + ".tmx", TiledMap.class);
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		// Generate maze structure
		maze = new Maze(map);
		
		// Construct the powerup stage
		powerUpStage = new PowerUpStage(game, this);

		// Build the character
		playerCharacter = new PlayerSprite(game.getAssetManager().get("ObjectImages/temp_hero_D_1.png", Texture.class));
		playerCharacter.buildAnimations(game.getAssetManager(), 0.1f);
		playerCharacter.setOrigin(0, 0);
		playerCharacter.setSpeed(4*maze.getPixHeight());
		playerCharacter.setScale(.75f,.75f);
		playerCharacter.setHealth(3);
		//Get the rect for the heros headquarters
		Vertex heroHQVertex = maze.getHeroHQTile();
		playerCharacter.setPosition(heroHQVertex.getX(), heroHQVertex.getY());
		// move directions
		playerCharacter.setMoveDirection(Direction.DOWN);
		playerCharacter.setTargetDirection(Direction.DOWN);
		playerCharacter.setTarget(maze.getTileAt(playerCharacter.getX(), playerCharacter.getY()));
		playerCharacter.setCurrentWalkSequence(Direction.DOWN);
		
		// Camera settings
		camera.setToOrtho(false,12*maze.getPixWidth(), 7.5f*maze.getPixHeight());
		clampCamPosition(camera, playerCharacter, maze);
		camera.update();
		
		// Clear arrays to build new child objects
		mazeChildren.clear();
		savedChildren.clear();
		followers.clear();
		// Clear any powers
		activePowerUps.clear();
		
		// Get the loaded texture for maze kids
		childTexture = game.getAssetManager().get("MazeAssets/" + activeMaze + "/childSprite.png",Texture.class);
		// Creates child objects around the maze and stores them in the passed array
		populate(maze, mazeChildren);
		
		// Retrieve drawing responsibilities
		heartTexture = game.getAssetManager().get("ObjectImages/heart.png");
	}


	public void populate(Maze maze, Array<ChildSprite> mazeChildren) {
		//get the amount of spots possible to fill
		int maxAmount = maze.getMinigameTiles().size;
		//chose a percentage to try and fill
		float percentageToFill = .75f;
		//make a variable to store the amount to fill, and fill as closely as possible. Clamp to low and high bounds
		int chosenAmount = (int) Math.max(1f, percentageToFill*maxAmount);

		// Pick random tiles to fill
		int currentAmount = 0;
		Array<Vertex> fillThese = new Array<Vertex>();
		while(currentAmount < chosenAmount) {
			Vertex attempt = maze.getMinigameTiles().random();
			if (!fillThese.contains(attempt, false)) {
				fillThese.add(attempt);
				currentAmount += 1;
			}
		}

		// Fill them
		for (Vertex toFill : fillThese) {
			//create a new child with the texture
			ChildSprite child = new ChildSprite(childTexture);
			child.setOrigin(0, 0);
			child.setScale(.5f);
			//reset the bounds, as suggested after scaling
			child.setBounds(child.getX(), child.getY(), child.getWidth(), child.getHeight());
			//set the position to the minigame rect
			child.setPosition(toFill.getX(), toFill.getY());
			child.setSpeed(4*maze.getPixHeight());
			mazeChildren.add(child);
			// Mark vertex
			toFill.setOccupied(true);
		}
	}
	
	// Adds a power up to the active set so it may run
	public void addPowerUp(PowerUp p) {
		this.activePowerUps.add(p);
	}

	/**
	 * Draws the maze on the screen
	 * Determines if the sprite is making a valid move within the 
	 * bounds of the maze and not on a collision, if so allow it to move.
	 * If the player triggers a miniGame set that to the current screen.
	 */

	@Override 
	public void render(float delta) {
		//update the camera
		camera.update();
		// link camera to render objects
		mapRenderer.setView(camera);
		spriteBatch.setProjectionMatrix(camera.combined);
		//render the map
		mapRenderer.render();

		// character, children and hearts batch
		spriteBatch.begin();

		//children in maze
		for(ChildSprite s : mazeChildren) {
			spriteDrawWithOffset(s, spriteBatch);
		}
		for (ChildSprite s : savedChildren) {
			spriteDrawWithOffset(s, spriteBatch);
		}
		
		// Player draw in center of tile
		spriteDrawWithOffset(playerCharacter, spriteBatch);

		// player hearts
		for (int i = 0; i < playerCharacter.getHealth(); i++) {
			float xPos = camera.position.x - camera.viewportWidth/2;
			float heartSize = camera.viewportHeight/10;
			float yPos = camera.position.y + camera.viewportHeight/2 - heartSize;
			spriteBatch.draw(heartTexture, xPos + (heartSize*i), yPos, heartSize, heartSize);
		}
		
		// Remaining children counter
		// Draw counter
		spriteBatch.draw(childTexture, camera.position.x - camera.viewportWidth/2f, camera.position.y + camera.viewportHeight*3f/10f, camera.viewportHeight/10f, camera.viewportHeight/10f);
		font.draw(spriteBatch, " x " + mazeChildren.size, camera.position.x - camera.viewportWidth/2f + camera.viewportHeight/10f, camera.position.y + camera.viewportHeight*4f/10f);
		
		// Update powerups with the batch active to allow drawing
		if (!logicPaused) {
			for (PowerUp p : activePowerUps) {
				// remove if done
				if (p.update(this, spriteBatch))
					activePowerUps.removeValue(p, false);
			}
		}

		spriteBatch.end();
		
		// render the powerup stage
		powerUpStage.draw();

		if (!logicPaused) {
			// Update logic
			ScreenAdapterEnums screenSwitch = updateLogic();
			// Init screen transition if necessary
			if (screenSwitch != null) {
				String text = buildResponseText(screenSwitch);
				CurtainScreenTransition mazeToOther = new CurtainScreenTransition(ScreenAdapterEnums.MAZE, screenSwitch, text);
				game.setScreen(mazeToOther);
			}
		}
	}
	
	// Draws the player in the center of its tile
	public void spriteDrawWithOffset(Sprite sprite, SpriteBatch batch) {
		float oldX = sprite.getX();
		float oldY = sprite.getY();
		// Find offset
		float offsetX = (maze.getPixWidth() - sprite.getWidth())/2f;
		float offsetY = (maze.getPixHeight() - sprite.getHeight())/2f;
		sprite.setPosition(oldX + offsetX, oldY + offsetY);
		sprite.draw(batch);
		sprite.setPosition(oldX, oldY);
	}

	// Logic for character collisions and screen changes on each frame
	public ScreenAdapterEnums updateLogic() {
		// cam position update, clamped so that area behind the maze is not shown
		clampCamPosition(camera, playerCharacter, maze);
		// Catch button presses on stage
		powerUpStage.act();
		
		//rectangle around player sprite for collision check
		Rectangle spriteRec = new Rectangle(playerCharacter.getX(), playerCharacter.getY(), playerCharacter.getWidth(), playerCharacter.getHeight());
		// Hero hq collision rect
		Rectangle heroHQ = new Rectangle(maze.getHeroHQTile().getX(), maze.getHeroHQTile().getY(), maze.getPixWidth(), maze.getPixHeight());

		// drop off at base if collision
		if (playerCharacter.getBoundingRectangle().overlaps(heroHQ)) {
			for (ChildSprite child : followers) {
				child.setSaved(true);
				child.moveTo(maze.getHeroHQTile());
				followers.removeValue(child, false);
			}
		}
		
		// true if player moved without wall collisions
		if (moveUpdate()) {
			// Animation update
			playerCharacter.animationUpdate(game.getGlobalClock(), true);
		}

		/*
		 * Possible screen changes
		 */

		// Check if won the maze (all are saved) and set state appropriately if true
		if ( winMazeCheck(savedChildren, mazeChildren) ) {
			mazeWon = true;
			// go to the boss game if exists or main
			if (bossLevel == null) {
				// no boss, do an unlock check
				if (game.data.unlockLevelCheck(ScreenMaze.mazeNumber, ScreenMaze.mazeType)) {
					// Save data
					game.data.save();
					// Go to unlock screen
					Array<String> powers = game.data.getUnlockedPowerUps(ScreenMaze.mazeType);
					String newPower = powers.get(powers.size - 1);
					ScreenUnlock.powerUpName = newPower;
					return ScreenAdapterEnums.UNLOCK;
				}
				return ScreenAdapterEnums.MAIN;
			}
			else
				return ScreenAdapterEnums.LEVEL;
		}

		// Check if lost the maze (no hearts left) and set state appropriately
		if ( loseMazeCheck(playerCharacter.getHealth()) ) {
			mazeWon = false;
			return ScreenAdapterEnums.MAIN;
		}
		
		// Minigame check
		if (minigameCollisionCheck(spriteRec, mazeChildren)) {
			return ScreenAdapterEnums.LEVEL;
		}

		return null;
	}
	
	// Updates the players position, true if moved, false if hit a wall
	public boolean moveUpdate() {
		//update follower positions
		if(followers.size > 0) {
			followers.get(0).followSprite(playerCharacter, maze);

			for(int i = 1; i <followers.size; i++) {
				followers.get(i).followSprite(followers.get(i-1), maze);
			}
		}
		return playerCharacter.move(maze);
	}

	// True if the passed rectangle collides with a rectangle in the collision layer
	public boolean wallCollisionCheck(Rectangle player, Array<Rectangle> collideRects) {
		//check for collision with walls
		for(Rectangle collideWith : collideRects) {
			if(player.overlaps(collideWith)) {
				return true;
			}
		}
		return false;
	}

	// True if collide with minigame that is occupied, and sets state variables appropriately
	public boolean minigameCollisionCheck(Rectangle player, Array<ChildSprite> children) {
		for(ChildSprite child : children) {
			if (!child.getFollow() && player.overlaps(child.getBoundingRectangle())) {
				// Keep track of the child for returning to the maze screen
				lastChild = child;
				// select a random level
				currentLevel = selectLevel();
				ScreenLevel levelScreen = (ScreenLevel) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.LEVEL);
				levelScreen.setCurrentLevel(currentLevel);
				currentLevel.setObjectTextures(game.getAssetManager());
				return true;
			}
		}
		return false;
	}

	// True if the game is won (all kids saved), also sets game state appropriately
	public boolean winMazeCheck(Array<ChildSprite> savedChildren, Array<ChildSprite> mazeChildren) {
		// No child found yet check
		if (savedChildren.size == 0) return false;
		if (mazeChildren.size == 0 && allSaved(savedChildren)) {
			// Play the boss minigame if there is one
			if (bossLevel != null) {
				ScreenLevel levelScreen = (ScreenLevel) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.LEVEL);
				currentLevel = bossLevel;
				currentLevel.resetLevel();
				levelScreen.setCurrentLevel(currentLevel);
				currentLevel.setObjectTextures(game.getAssetManager());
			}
			return true;
		}
		return false;
	}

	// True if out of hearts and sets state appropriately
	public boolean loseMazeCheck(int health) {
		if (health <= 0) {
			return true;
		}
		return false;
	}

	// Returns minigame description, win or lose text
	public String buildResponseText(ScreenAdapterEnums toScreen) {
		String text;
		switch (toScreen) {
		case MAIN: case UNLOCK:
			if (mazeWon) text = "You saved all the kids! Congratulations!";
			else text = "You ran out of hearts, try again!";
			break;
		case LEVEL:
			text = currentLevel.getDescription();
			break;
		default:
			text = "";
			break;
		}
		return text;
	}
	
	// Sets the cam position to follow the player, but clamped to not look past the
	// edge of the maze
	public void clampCamPosition(OrthographicCamera cam, PlayerSprite player, Maze maze) {
		float x = Math.max(cam.viewportWidth/2, Math.min(maze.getMazeSize() - cam.viewportWidth/2, player.getX()));
		float y = Math.max(cam.viewportHeight/2, Math.min(maze.getMazeSize() - cam.viewportHeight/2, player.getY()));
		// Set coords
		cam.position.set(x, y, 0);
	}

	/**
	 * Sets the maze to be shown, makes sure the player is not moving initially.
	 * Sets all layers of the maze to visible. 
	 * Sets input processor to the maze to begin getting user input for navigation.
	 */

	@Override
	public void show(){
		logicPaused = false;
		// Set input to the mazeInput and powerUpStage
		InputMultiplexer mult = new InputMultiplexer();
		mult.addProcessor(powerUpStage);
		mult.addProcessor(mazeInput);
		Gdx.input.setInputProcessor(mult);
		// Returned from level screen (assuming no back button presses or backgrounded)
		if (levelWon) {
			// won the minigame
			//add the follower from this minigame panel
			followers.add(lastChild);
			mazeChildren.removeValue(lastChild, false);
			savedChildren.add(lastChild); // Used for counting how many children are left to save
			lastChild.setFollow(true);
			maze.getTileAt(lastChild.getX(), lastChild.getY()).setOccupied(false);
		} 
		else if (lastChild != null){
			//lost the minigame
			//find unoccupied minigame squares
			Array<Vertex> unoccupied = new Array<Vertex>();
			for (Vertex tile: maze.getMinigameTiles()) {
				if (!tile.isOccupied()) {
					unoccupied.add(tile);
				}
			}

			if (unoccupied.size > 0) {
				// a space is available
				Random rand = new Random();
				int newPositionIndex = rand.nextInt(unoccupied.size);
				unoccupied.get(newPositionIndex).setOccupied(true);
				lastChild.moveTo(unoccupied.get(newPositionIndex));
			}
			playerCharacter.setHealth(playerCharacter.getHealth() - 1);
		}
	}

	/**
	 * Returns true if all children in the maze have been saved
	 */
	public boolean allSaved(Array<ChildSprite> children) {
		for (ChildSprite child : children) {
			if (!child.getSaved()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Hides the maze by setting all layers to not visible.
	 * Sets input processor back to the stage.
	 */
	@Override
	public void hide() {
		// Pause logic for transition drawing
		logicPaused = true;
		Gdx.input.setInputProcessor(ScreenAdapterManager.getInstance().game.getStage());
	}

	public void setLevelWon (boolean levelWon) {
		this.levelWon = levelWon;
	}

	public PlayerSprite getPlayerCharacter() {
		return playerCharacter;
	}
	
	public Array<ChildSprite> getMazeChildren() {
		return mazeChildren;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public Maze getMaze() {
		return maze;
	}

	/**{@link #selectLevel()} handles setting {@link #currentLevel} to which minigame should be played.*/
	public Level selectLevel() {
		// Pick a random level and reset it for play
		Level l = levelSet.random();
		l.resetLevel();
		return l;
	}

	/**{@link #loadLevelPackets()} loads the minigames into their corresponding packets. Packets are created based on folders in Assets/Levels, and the .xml files within these folders create the games for those packets.*/
	public static void loadLevelSet(TGC_Engine game) {
		// Clear old levels
		levelSet.clear();
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			dirHandle = Gdx.files.internal("./bin/MazeAssets/" + activeMaze + "/Levels/");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("MazeAssets/" + activeMaze + "/Levels/");
		}
		// Load level objects from levels text file
		FileHandle levelFile = dirHandle.child("levels.txt");
		String slurp = levelFile.readString();
		String[] tokens = slurp.split("\n");
		for (String name : tokens) {
			FileHandle file = Gdx.files.internal("Minigames/" + name);
			XML_Reader read = new XML_Reader(file);
			Level level = read.compileLevel();
			levelSet.add(level);
		}
		// Get boss level if one exists
		FileHandle bossHandle = dirHandle.child("Boss.xml");
		if (bossHandle.exists()) {
			XML_Reader read = new XML_Reader(bossHandle);
			bossLevel = read.compileLevel();
			bossLevel.setBossGame(true);
		} else bossLevel = null;
		
	}

	public static void requestAssets(AssetManager manager) {
		// Player sprite
		PlayerSprite.requestAssets(manager);

		// Child sprite
		manager.load("MazeAssets/" + activeMaze + "/childSprite.png", Texture.class);

		// Tiled map
		manager.load("MazeAssets/" + activeMaze + "/" + activeMaze + ".tmx", TiledMap.class);

		// UI and background
		manager.load("ObjectImages/heart.png", Texture.class);

		// Load levels ( sets boss game )
		loadLevelSet(ScreenAdapterManager.getInstance().game);

		// Minigame assets
		for (Level l : levelSet) {
			// Background assets
			String background = l.getLevelImage();
			manager.load("LevelBackgrounds/" + background, Texture.class);
			// Object assets
			for (GameObject ob : l.getGameObjects()) {
				manager.load("LevelImages/" + ob.getImageFilename(), Texture.class);
			}
		}
		// Boss game
		
		if (bossLevel != null) {
			String background = bossLevel.getLevelImage();
			// Background assets
			manager.load("LevelBackgrounds/" + background, Texture.class);
			// Object assets
			for (GameObject ob : bossLevel.getGameObjects()) {
				manager.load("LevelImages/" + ob.getImageFilename(), Texture.class);
			}
		}
		
		// Load clock for time based minigames
		manager.load("clock.png", Texture.class);
		manager.load("clockHand.png", Texture.class);
		
		// Load the assets for powerups that have been unlocked
		Array<String> unlocked = ScreenAdapterManager.getInstance().game.data.getUnlockedPowerUps(mazeType);
		for (String s : unlocked) {
			PowerUpEnum.valueOf(s.toUpperCase()).requestAssets(manager);
		}
	}

}
