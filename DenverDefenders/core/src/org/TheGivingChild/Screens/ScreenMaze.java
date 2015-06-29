package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
/**
 *Maze screen that the user will navigate around.
 *Player will be able to trigger a miniGame by finding a child in the maze.
 *@author mtzimour
 */

public class ScreenMaze extends ScreenAdapter implements InputProcessor {
	/** Map to be displayed */
	private TiledMap map;
	/** Orthographic Camera to look at map from top down */
	private OrthographicCamera camera;
	/** Tiled map renderer to display the map */
	private TiledMapRenderer mapRenderer;
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
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	/** Array of rectangle to store the location of miniGame triggers */
	private Array<MinigameRectangle> minigameRects = new Array<MinigameRectangle>();
	/** Vector to store the last touch of the user */
	private Vector2 lastTouch = new Vector2();

	private TGC_Engine game;
	private Array<ChildSprite> mazeChildren;
	private Array<ChildSprite> followers;
	private MinigameRectangle miniRec;
	
	private Texture backdropTexture;
	
	private MinigameRectangle lastRec;
	private Rectangle heroHQ;
	
	private Texture heartTexture;
	private int playerHealth = 3;
	
	//True if game progression logic is paused
	private boolean logicPaused;
	// True if won the minigame returned from
	private boolean levelWon;
	// True if all the children were found and the maze has been won
	private boolean mazeWon;
	
	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */

	public ScreenMaze(){
		logicPaused = true;
		levelWon = false;
		mazeWon = false;
		game = ScreenAdapterManager.getInstance().game;
		map = game.getAssetManager().get("MazeAssets/UrbanMaze1/UrbanMaze1.tmx", TiledMap.class);
		
		//Setup map properties
		properties = map.getProperties();

		int mapTilesX = properties.get("width", Integer.class);
		int mapTilesY = properties.get("height", Integer.class);
		//width and height of tiles in pixels
		int pixWidth = properties.get("tilewidth", Integer.class);
		int pixHeight = properties.get("tileheight", Integer.class);

		mapRenderer = new OrthogonalTiledMapRenderer(map);

		spriteBatch = new SpriteBatch();
		// Setup animations (textures were loaded during screen transition)
		buildAnimations(game.getAssetManager(), 0.1f);
		currentWalkSequence = walkD;
		playerCharacter = new ChildSprite(game.getAssetManager().get("ObjectImages/temp_hero_D_1.png", Texture.class));
		playerCharacter.setSpeed(4*pixHeight);
		playerCharacter.setScale(.75f,.75f);

		//Get the rect for the heros headquarters
		RectangleMapObject startingRectangle = (RectangleMapObject)map.getLayers().get("HeroHeadquarters").getObjects().get(0);
		heroHQ = startingRectangle.getRectangle();
		playerCharacter.setPosition(heroHQ.x, heroHQ.y);
		//mark it as the hero for following purposes
		playerCharacter.setHero();
		
		// move camera to player
		camera = new OrthographicCamera();
		camera.setToOrtho(false,12*pixWidth,7.5f*pixHeight);
		camera.position.set(playerCharacter.getX(), playerCharacter.getY(), 0);
		camera.update();

		mazeChildren = new Array<ChildSprite>();
		followers = new Array<ChildSprite>();

		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i <collisionObjects.getCount(); i++) {
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x-2, rect.y, rect.width, rect.height));
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
		
		backdropTexture = game.getAssetManager().get("MazeAssets/UrbanMaze1/backdrop.png");
		
		heartTexture = game.getAssetManager().get("ObjectImages/heart.png");
	}


	public void populate() {
		//get the amount of spots possible to fill
		int maxAmount = minigameRects.size;
		//chose a percentage to try and fill
		float percentageToFill = .75f;
		//make a variable to store the amount to fill, and fill as closely as possible. Clamp to low and high bounds
		int chosenAmount = (int) Math.max(1f, Math.min(maxAmount, percentageToFill*maxAmount));
		//placeholder to see how many spots need filled still
		int currentAmount = 0;
		
		while (currentAmount < chosenAmount) {
			//chose a random rectangle
			MinigameRectangle toFill = minigameRects.random();
			//if the rectangle is not occupied, then fill it
			if(!toFill.isOccupied()){
				//create a new child with the texture
				ChildSprite child = new ChildSprite(game.getAssetManager().get("MazeAssets/UrbanMaze1/Character Pink Girl.png",Texture.class));
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
				game.selectLevel();
				ScreenLevel levelScreen = (ScreenLevel) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.LEVEL);
				levelScreen.setCurrentLevel(game.getCurrentLevel());
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
			text = game.getCurrentLevel().getDescription();
			break;
		default:
			text = "";
			break;
		}
		return text;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == 'e') {
			ScreenTransition mazeToMain = new ScreenTransition(ScreenAdapterEnums.MAZE, ScreenAdapterEnums.MAIN);
			game.setScreen(mazeToMain);
		}
		return true;
	}

	/**
	 * Gets where a user first touched down on their device and 
	 * saves its position as a vector.
	 * @param screenX x coordinate of users down touch
	 * @param screenY y coordinate of users down touch
	 * @param pointer not used
	 * @param button not used
	 */

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Get vector of touch down to calculate change in movement during swipe
		lastTouch = new Vector2(screenX, screenY);
		return true;
	}

	/**
	 * Gets where a user touches up from their device and saves
	 * its position as a vector. Uses the touch up and the previous recorded
	 * touch to determine what direction the swipe was in and sets the
	 * players movement to the corresponding direction.
	 * @param screenX x coordinate of users up touch
	 * @param screenY y coordinate of users up touch
	 * @param pointer not used
	 * @param button not used
	 */

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//vectors contain the new position where the swipe ended
		Vector2 newTouch = new Vector2(screenX, screenY);
		//calculate the difference between the begin and end point
		Vector2 delta = newTouch.cpy().sub(lastTouch);
		//if the magnitude of x is greater than the y, then move the sprite in the horizontal direction
		if (Math.abs(delta.x) > Math.abs(delta.y)) {
			//if the change was positive, move right, else move left
			if(delta.x > 0)  {
				xMove =  playerCharacter.getSpeed();
				currentWalkSequence = walkR;
			}
			if(delta.x <= 0) {
				xMove = -playerCharacter.getSpeed();
				currentWalkSequence = walkL;
			}
			//no vertical movement
			yMove = 0;
		}
		//otherwise y>=x so move vertically 
		else {
			//move down if the change was positive, else move up
			if(delta.y > 0)	{
				yMove = -playerCharacter.getSpeed();
				currentWalkSequence = walkD;
			}
			if(delta.y <= 0) {
				yMove = playerCharacter.getSpeed();
				currentWalkSequence = walkU;
			}
			//no horizontal movement
			xMove = 0;
		}
		return true;
	}

	/**
	 * Sets the maze to be shown, makes sure the player is not moving initially.
	 * Sets all layers of the maze to visible. 
	 * Sets input processor to the maze to begin getting user input for navigation.
	 */

	@Override
	public void show(){
		logicPaused = false;
		Gdx.input.setInputProcessor(this);
		
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

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return true;
	}

	/**Override the back button to show the main menu for Android*/
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK){
			ScreenTransition mazeToMain = new ScreenTransition(ScreenAdapterEnums.MAZE, ScreenAdapterEnums.MAIN);
			game.setScreen(mazeToMain);
	    }
		return true;
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
		manager.load("MazeAssets/UrbanMaze1/Character Pink Girl.png", Texture.class);
		
		// Tiled map
		manager.load("MazeAssets/UrbanMaze1/UrbanMaze1.tmx", TiledMap.class);
		
		// UI and background
		manager.load("ObjectImages/heart.png", Texture.class);
		manager.load("MazeAssets/UrbanMaze1/backdrop.png", Texture.class);
		// Minigame assets, load on maze select and levels constructed (into maze screen)
		manager.load("LevelBackgrounds/black.png", Texture.class);
		manager.load("LevelBackgrounds/Table.png", Texture.class);
		manager.load("LevelBackgrounds/Window.png", Texture.class);
		
		// Audio assets (loads synchronously)
		AudioManager.getInstance().addAvailableSound("sounds/bounce.wav");
	}

}
