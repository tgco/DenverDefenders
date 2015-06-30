package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.MazeInputProcessor;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
	/** Map to be displayed */
	private TiledMap map;
	/** Orthographic Camera to look at map from top down */
	private OrthographicCamera camera;
	/** Tiled map renderer to display the map */
	private TiledMapRenderer mapRenderer;
	// The width and height of tiles in pixels
	private int pixHeight;
	private int pixWidth;
	/** Sprite, SpriteBatch, and Texture for users sprite */
	private SpriteBatch spriteBatch;
	private Animation walkD, walkR, walkU, walkL;
	private Animation currentWalkSequence;
	private ChildSprite playerCharacter;
	/** Values to store which direction the sprite is moving */
	private float xMove, yMove;
	/** Map properties to get dimensions of maze */
	private MapProperties properties;
	/** Array of rectangles to store locations of collisions */
	private Array<Rectangle> collisionRects;
	/** Array of rectangle to store the location of miniGame triggers */
	private Array<MinigameRectangle> minigameRects;
	// The input processor that allows player movement on drag
	private InputProcessor mazeInput;

	private TGC_Engine game;
	private Array<ChildSprite> mazeChildren;
	private Array<ChildSprite> followers;
	private MinigameRectangle miniRec;
	
	private Texture backdropTexture;
	
	private MinigameRectangle lastRec;
	private Rectangle heroHQ;
	
	private Texture heartTexture;
	private int playerHealth;
	
	//True if game progression logic is paused
	private boolean logicPaused;
	// True if won the minigame returned from
	private boolean levelWon;
	// True if all the children were found and the maze has been won
	private boolean mazeWon;
	
	// The name of the maze which corresponds to the assets directory to look into
	public static String activeMaze = "UrbanMaze1";
	
    /**{@link #levelSet} is the container for levels to be played during a maze.*/
	private static Array<Level> levelSet = new Array<Level>();
	/**{@link #currentLevel} keeps track of the current level being played.*/
	private Level currentLevel;
	
	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */

	public ScreenMaze(){
		game = ScreenAdapterManager.getInstance().game;
		mazeInput = new MazeInputProcessor(this, this.game);
		minigameRects = new Array<MinigameRectangle>();
		collisionRects = new Array<Rectangle>();
		mazeChildren = new Array<ChildSprite>();
		followers = new Array<ChildSprite>();
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.update();
	}
	
	// Must run before starting a new maze, sets state appropriately
	public void init() {
		logicPaused = true;
		levelWon = false;
		mazeWon = false;
		currentLevel = null;
		playerHealth = 3;

		map = game.getAssetManager().get("MazeAssets/" + activeMaze + "/" + activeMaze + ".tmx", TiledMap.class);

		//Setup map properties
		properties = map.getProperties();

		//width and height of tiles in pixels
		pixWidth = properties.get("tilewidth", Integer.class);
		pixHeight = properties.get("tileheight", Integer.class);

		mapRenderer = new OrthogonalTiledMapRenderer(map);

		// Setup animations (textures were loaded during screen transition)
		buildAnimations(game.getAssetManager(), 0.1f);
		playerCharacter = new ChildSprite(game.getAssetManager().get("ObjectImages/temp_hero_D_1.png", Texture.class));
		playerCharacter.setSpeed(4*pixHeight);
		playerCharacter.setScale(.75f,.75f);
		//mark it as the hero for following purposes
		playerCharacter.setHero();

		currentWalkSequence = walkD;

		//Get the rect for the heros headquarters
		RectangleMapObject startingRectangle = (RectangleMapObject)map.getLayers().get("HeroHeadquarters").getObjects().get(0);
		heroHQ = startingRectangle.getRectangle();
		playerCharacter.setPosition(heroHQ.x, heroHQ.y);

		camera.setToOrtho(false,12*pixWidth,7.5f*pixHeight);
		camera.position.set(playerCharacter.getX(), playerCharacter.getY(), 0);
		camera.update();
		
		mazeChildren.clear();
		followers.clear();

		collisionRects.clear();
		minigameRects.clear();
		
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i <collisionObjects.getCount(); i++) {
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		}

		//Setup array of minigame rectangles
		MapObjects miniGameObjects = map.getLayers().get("Minigame").getObjects();
		for(int i = 0; i <miniGameObjects.getCount(); i++) {
			RectangleMapObject obj = (RectangleMapObject) miniGameObjects.get(i);
			Rectangle rect = obj.getRectangle();

			miniRec = new MinigameRectangle(rect.x, rect.y-pixHeight/2, rect.width, rect.height);
			lastRec = new MinigameRectangle(rect.x, rect.y-pixHeight/2, rect.width, rect.height);

			//Add spots that can trigger minigames
			minigameRects.add(miniRec);
		}

		populate();

		backdropTexture = game.getAssetManager().get("MazeAssets/" + activeMaze + "/backdrop.png");
		heartTexture = game.getAssetManager().get("ObjectImages/heart.png");
	}


	public void populate() {
		//get the amount of spots possible to fill
		int maxAmount = minigameRects.size;
		//chose a percentage to try and fill
		float percentageToFill = .75f;
		//make a variable to store the amount to fill, and fill as closely as possible. Clamp to low and high bounds
		int chosenAmount = (int) Math.max(1f, percentageToFill*maxAmount);
		//placeholder to see how many spots need filled still
		int currentAmount = 0;
		
		while (currentAmount < chosenAmount) {
			//chose a random rectangle
			MinigameRectangle toFill = minigameRects.random();
			//if the rectangle is not occupied, then fill it
			if(!toFill.isOccupied()){
				//create a new child with the texture
				ChildSprite child = new ChildSprite(game.getAssetManager().get("MazeAssets/" + activeMaze + "/childSprite.png",Texture.class));
				//Scale down the child
				child.setScale(.5f);
				//reset the bounds, as suggested after scaling
				child.setBounds(child.getX(), child.getY(), child.getWidth(), child.getHeight());
				//set the position to the rectangle to fill
				child.setPosition(toFill.x, toFill.y);
				//Add the child to the array of mazeChildren
				mazeChildren.add(child);
				//Occupy the rectangle
				toFill.setOccupied(child);
				//increment the spots that have been filled
				currentAmount++;
			}
		}
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
		
		//background
		spriteBatch.begin();
		spriteBatch.draw(backdropTexture, playerCharacter.getX()-Gdx.graphics.getWidth()/2, playerCharacter.getY()-Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.end();
		//render the map
		mapRenderer.render();
		
		//character, children and hearts
		spriteBatch.begin();
		playerCharacter.draw(spriteBatch);
		//following children
		if(followers.size != 0) {
			for(Sprite f: followers) {
				f.draw(spriteBatch);
			}
		}
		//children in maze
		for(Sprite s : mazeChildren) {
			s.draw(spriteBatch);
		}
		// player hearts
		for (int i=0; i<playerHealth; i++) {
			float xPos = camera.position.x - camera.viewportWidth/2;
			float heartSize = camera.viewportHeight/10;
			float yPos = camera.position.y + camera.viewportHeight/2 - heartSize;
			spriteBatch.draw(heartTexture, xPos + (heartSize*i), yPos, heartSize, heartSize);
		}
		spriteBatch.end();
		
		if (!logicPaused) {
			// Update logic
			ScreenAdapterEnums screenSwitch = updateLogic();
			// Init screen transition if necessary
			if (screenSwitch != null) {
				String text = buildResponseText(screenSwitch);
				ScreenTransition mazeToOther = new ScreenTransition(ScreenAdapterEnums.MAZE, screenSwitch, text);
				game.setScreen(mazeToOther);
			}
		}
	}
	
	// Logic for character collisions and screen changes on each frame
	public ScreenAdapterEnums updateLogic() {
		// cam position update
		camera.position.set(playerCharacter.getX(), playerCharacter.getY(), 0);

		//Calculate new coordinates
		float spriteMoveX = playerCharacter.getX() + xMove*Gdx.graphics.getDeltaTime();
		float spriteMoveY = playerCharacter.getY() + yMove*Gdx.graphics.getDeltaTime();
		//rectangle around player sprite for collision check
		Rectangle spriteRec = new Rectangle(spriteMoveX, spriteMoveY, playerCharacter.getWidth(), playerCharacter.getHeight());
		
		// drop off at base if collision
		if (playerCharacter.getBoundingRectangle().overlaps(heroHQ)) {
			for (ChildSprite child: followers) {
				child.setSaved(true);
				followers.removeValue(child, false);
				playerCharacter.clearPositionQueue();
			}
		}
		
		// Updates if no wall collisions
		if ( !wallCollisionCheck(spriteRec, collisionRects) ) {
			//update player position
			playerCharacter.setPosition(spriteMoveX, spriteMoveY);
			//update follower positions
			if(followers.size > 0) {
				followers.get(0).followSprite(playerCharacter);

				for(int i = 1; i <followers.size; i++) {
					followers.get(i).followSprite(followers.get(i-1));
				}
			}

			// Animation update
			TextureRegion next = currentWalkSequence.getKeyFrame(game.getGlobalClock(), true);
			playerCharacter.setTexture(next.getTexture());
		}
		
		/*
		 * Possible screen changes
		 */
		
		// Minigame check
		if (minigameCollisionCheck(spriteRec, minigameRects)) {
			return ScreenAdapterEnums.LEVEL;
		}

		// Check if won the maze (all are saved) and set state appropriately if true
		if ( winMazeCheck(mazeChildren) ) {
			return ScreenAdapterEnums.MAIN;
		}

		// Check if lost the maze (no hearts left) and set state appropriately
		if ( loseMazeCheck(playerHealth) ) {
			return ScreenAdapterEnums.MAIN;
		}
		
		return null;
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
	public boolean minigameCollisionCheck(Rectangle player, Array<MinigameRectangle> gameRects) {
		for(MinigameRectangle m : gameRects) {
			if (m.isOccupied() && player.overlaps(m)) {
				// Keep track of the rectangle for returning to the maze screen
				lastRec = m;
				// select a random level
				selectLevel();
				ScreenLevel levelScreen = (ScreenLevel) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.LEVEL);
				levelScreen.setCurrentLevel(currentLevel);
				currentLevel.setObjectTextures(game.getAssetManager());
				return true;
			}
		}
		return false;
	}
	
	// True if the game is won (all kids saved), also sets game state appropriately
	public boolean winMazeCheck(Array<ChildSprite> children) {
		if (allSaved(children)) {
			mazeWon = true;
			return true;
		}
		return false;
	}
	
	// True if out of hearts and sets state appropriately
	public boolean loseMazeCheck(int health) {
		if (health <= 0) {
			mazeWon = false;
			return true;
		}
		return false;
	}
	
	// Returns minigame description, win or lose text
	public String buildResponseText(ScreenAdapterEnums toScreen) {
		String text;
		switch (toScreen) {
		case MAIN:
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

	/**
	 * Sets the maze to be shown, makes sure the player is not moving initially.
	 * Sets all layers of the maze to visible. 
	 * Sets input processor to the maze to begin getting user input for navigation.
	 */

	@Override
	public void show(){
		logicPaused = false;
		Gdx.input.setInputProcessor(mazeInput);
		
		xMove = 0;
		yMove = 0;

		if(allSaved(mazeChildren) || playerHealth <= 0) {
			reset();
		}
		else {
			/* BUG: RETURNING FROM MAIN TO A PAUSED GAME LOSES A HEART */
			// Returned from level screen (assuming no back button presses or backgrounded)
			if (levelWon) {
				// won the minigame
				//add the follower from this minigame panel
				followers.add(lastRec.getOccupant());
				levelWon = false;
			} 
			else if (lastRec.isOccupied()){
				//lost the minigame
				//find unoccupied minigame squares
				Array<MinigameRectangle> unoccupied = new Array<MinigameRectangle>();
				for (MinigameRectangle rect: minigameRects) {
					if (!rect.isOccupied()) {
						unoccupied.add(rect);
					}
				}

				if (unoccupied.size > 0) {
					// a space is available
					Random rand = new Random();
					int newPositionIndex = rand.nextInt(unoccupied.size);
					unoccupied.get(newPositionIndex).setOccupied(lastRec.getOccupant());
					ChildSprite child = unoccupied.get(newPositionIndex).getOccupant();
					child.moveTo(unoccupied.get(newPositionIndex));
				}
				playerHealth--;
			}

			lastRec.empty();
		}
		
		for(MapLayer layer : map.getLayers()) {
			layer.setVisible(true);
		}
		
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i < collisionObjects.getCount(); i++) {
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x-2, rect.y, rect.width, rect.height));
		}
	}

	public void reset() {
		mazeChildren.clear();
		//empty any positional queue information the followers are holding.
		for(ChildSprite f: followers){
			f.clearPositionQueue();
		}
		//empty they pc's positional queue
		playerCharacter.clearPositionQueue();
		//empty the array of followers.
		followers.clear();
		//clear the minigames
		for (MinigameRectangle rect: minigameRects) {
			rect.empty();
		}
		playerHealth = 3;
		playerCharacter.setPosition(heroHQ.x,heroHQ.y);
		mazeWon = false;
		populate();
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
		collisionRects.clear();
	}
	
	public void setLevelWon (boolean levelWon) {
		this.levelWon = levelWon;
	}
	
	// Builds animation sequences from the asset manager
	public void buildAnimations(AssetManager manager, float animationSpeed) {
		// Build walk down animation
		Array<TextureRegion> downWalk = new Array<TextureRegion>();
		String format;
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			downWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkD = new Animation(animationSpeed, downWalk);
		
		// Build walk up animation
		Array<TextureRegion> upWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			upWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkU = new Animation(animationSpeed, upWalk);
		
		// Buld walk right animation
		Array<TextureRegion> rightWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			rightWalk.add(new TextureRegion(manager.get(format, Texture.class)));
		}
		walkR = new Animation(animationSpeed, rightWalk);
		
		// Build walk left animation
		Array<TextureRegion> leftWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_L_%d.png", i);
			leftWalk.add(new TextureRegion(manager.get(format, Texture.class)));
		}
		walkL = new Animation(animationSpeed, leftWalk);
		
	}
	
	public ChildSprite getPlayerCharacter() {
		return playerCharacter;
	}
	
	// Sets move speed vars and animation
	public void setPlayerMovement(float x, float y) {
		this.xMove = x;
		this.yMove = y;
		
		if (x != 0) {
			if (x > 0) currentWalkSequence = walkR;
			else currentWalkSequence = walkL;
		}
		else if (y != 0) {
			if (y > 0) currentWalkSequence = walkU;
			else currentWalkSequence = walkD;
		}
	}
	
	/**{@link #selectLevel()} handles setting {@link #currentLevel} to which minigame should be played.*/
	public void selectLevel() {
		// Pick a random level and reset it for play
		currentLevel = levelSet.random();
		currentLevel.resetLevel();
	}
	
	/**{@link #loadLevelPackets()} loads the minigames into their corresponding packets. Packets are created based on folders in Assets/Levels, and the .xml files within these folders create the games for those packets.*/
	public static void loadLevelSet(TGC_Engine game) {
		// Clear old levels
		levelSet.clear();
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("MazeAssets/" + activeMaze);
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/MazeAssets/" + activeMaze);
		}
		// Load level objects
		for (FileHandle levelFile : dirHandle.child("Levels").list()) {
			if (levelFile.name().equals(".DS_Store")) continue; // Dumb OSX issue
			game.getReader().setupNewFile(levelFile);
			Level level = game.getReader().compileLevel();
			levelSet.add(level);
		}
	}

	public static void requestAssets(AssetManager manager) {
		String format;
		// Down anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			manager.load(format,Texture.class);
		}
	
		// Up anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			manager.load(format,Texture.class);
		}
		
		// Right anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			manager.load(format, Texture.class);
		}
		
		// Left anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_L_%d.png", i);
			manager.load(format, Texture.class);
		}
		
		// Child sprite
		manager.load("MazeAssets/" + activeMaze + "/childSprite.png", Texture.class);
		
		// Tiled map
		manager.load("MazeAssets/" + activeMaze + "/" + activeMaze + ".tmx", TiledMap.class);
		
		// UI and background
		manager.load("ObjectImages/heart.png", Texture.class);
		manager.load("MazeAssets/" + activeMaze + "/backdrop.png", Texture.class);
		// Audio assets (loads synchronously)
		AudioManager.getInstance().addAvailableSound("sounds/bounce.wav");
		
		// Load levels
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
		
	}

}
