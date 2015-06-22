package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
	private Texture spriteTextureD,spriteTextureU,spriteTextureR,spriteTextureL;
	private Array<Texture> arrayWalkD, arrayWalkR, arrayWalkU, arrayWalkL;
	private Array<Texture> currentWalkSequence;
	
	private ChildSprite playerCharacter;
	/** Values to store which direction the sprite is moving */
	private float xMove, yMove;
	/** Map properties to get dimensions of maze */
	private MapProperties properties;
	private int mapTilesX, mapTilesY;
	private float mazeWidth, mazeHeight;
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
	private TextureRegion backdropTextureRegion;
	
	private MinigameRectangle lastRec;
	private Rectangle heroHQ;
	
	private Texture heartTexture;
	private TextureRegion healthTextureRegion;
	private int playerHealth = 3;
	
	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */

	public ScreenMaze(){	
		map = new TmxMapLoader().load("mapAssets/UrbanMaze1.tmx");
		camera = new OrthographicCamera();
		//Setup map properties
		properties = map.getProperties();

		mapTilesX = properties.get("width", Integer.class);
		mapTilesY = properties.get("height", Integer.class);
		//width and height of tiles in pixels
		int pixWidth = properties.get("tilewidth", Integer.class);
		int pixHeight = properties.get("tileheight", Integer.class);

		mazeWidth = mapTilesX * pixWidth;
		mazeHeight = mapTilesY * pixHeight;
		camera.setToOrtho(false,12*pixWidth,7.5f*pixHeight);
		camera.update();
		mapRenderer = new OrthogonalTiledMapRenderer(map);
				
		arrayWalkD = new Array<Texture>();
		arrayWalkR = new Array<Texture>();
		arrayWalkL = new Array<Texture>();
		arrayWalkU = new Array<Texture>();
		
		currentWalkSequence = new Array<Texture>();

		spriteBatch = new SpriteBatch();
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_1.png"));
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_1.png"));
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_1.png"));
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_1.png"));

		//REFACTOR ASSET LOADING HERE
		//get an array for walking down

		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_2.png"));
		
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_3.png"));
	
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_4.png"));
	
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_5.png"));
	
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_6.png"));
		
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_7.png"));
		
		arrayWalkD.add(spriteTextureD);
		spriteTextureD = new Texture(Gdx.files.internal("ObjectImages/temp_hero_D_8.png"));
	
		arrayWalkD.add(spriteTextureD);

		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_2.png"));
		
		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_3.png"));
		
		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_4.png"));
		
		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_5.png"));
		
		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_6.png"));
		
		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_7.png"));

		arrayWalkU.add(spriteTextureU);
		spriteTextureU = new Texture(Gdx.files.internal("ObjectImages/temp_hero_U_8.png"));
		
		arrayWalkU.add(spriteTextureU);
		
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_2.png"));
	
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_3.png"));
		
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_4.png"));
		
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_5.png"));
	
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_6.png"));
	
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_7.png"));
	
		arrayWalkR.add(spriteTextureR);
		spriteTextureR = new Texture(Gdx.files.internal("ObjectImages/temp_hero_R_8.png"));
		
		arrayWalkR.add(spriteTextureR);
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_2.png"));
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_3.png"));
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_4.png"));
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_5.png"));

		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_6.png"));
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_7.png"));
		
		arrayWalkL.add(spriteTextureL);
		spriteTextureL = new Texture(Gdx.files.internal("ObjectImages/temp_hero_L_8.png"));
		
		arrayWalkL.add(spriteTextureL);
		
		
		playerCharacter = new ChildSprite(spriteTextureD);
		playerCharacter.setSpeed(4*pixHeight);
		playerCharacter.setScale(.75f,.75f);

		//Get the rect for the heros headquarters
		RectangleMapObject startingRectangle = (RectangleMapObject)map.getLayers().get("HeroHeadquarters").getObjects().get(0);
		heroHQ = startingRectangle.getRectangle();
		playerCharacter.setPosition(heroHQ.x, heroHQ.y);
		//mark it as the hero for following purposes
		playerCharacter.setHero();

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

		game = ScreenAdapterManager.getInstance().game;
		
		backdropTexture = game.getAssetManager().get("mapAssets/UrbanMaze1Backdrop.png");
		backdropTextureRegion = new TextureRegion(backdropTexture);
		
		heartTexture = game.getAssetManager().get("ObjectImages/heart.png");
		healthTextureRegion = new TextureRegion(heartTexture);
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
				//Get the texture for the child to occupy this spot
				Texture childTexture = new Texture(Gdx.files.internal("mapAssets/somefreesprites/Character Pink Girl.png"));
				//create a new child with the texture
				ChildSprite child = new ChildSprite(childTexture);
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
		//set the map to be rendered by this camera
		mapRenderer.setView(camera);
		spriteBatch.begin();
		//draw the background texture
		spriteBatch.draw(backdropTextureRegion, playerCharacter.getX()-Gdx.graphics.getWidth()/2, playerCharacter.getY()-Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.end();
		//render the map
		mapRenderer.render();
		//Make the sprite not move when the map is scrolled
		spriteBatch.setProjectionMatrix(camera.combined);
		
		//move the sprite left, right, up, or down
		//Calculate where the sprite is going to move
		float spriteMoveX = playerCharacter.getX() + xMove*Gdx.graphics.getDeltaTime();
		float spriteMoveY = playerCharacter.getY() + yMove*Gdx.graphics.getDeltaTime();
		//If the sprite is not going off the maze allow it to move
		//Check for a collision as well
		boolean collision = false;
		
		if(spriteMoveX >= 0 && (spriteMoveX+playerCharacter.getWidth()) <= mazeWidth) {
			if(spriteMoveY >= 0 && (spriteMoveY+playerCharacter.getHeight()) <= mazeHeight) {
				Rectangle spriteRec = new Rectangle(spriteMoveX, spriteMoveY, playerCharacter.getWidth(), playerCharacter.getHeight());
				
				for(Rectangle r : collisionRects) {
					if(r.overlaps(spriteRec)) {
						collision = true;
					}
				}

				for(MinigameRectangle m : minigameRects) {
					if(m.overlaps(spriteRec) && m.isOccupied()) {
						lastRec = m;
						playerCharacter.setPosition(m.getX(), m.getY());
						game.selectLevel();
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL);
					}
				}

				if (playerCharacter.getBoundingRectangle().overlaps(heroHQ)) {
					for (ChildSprite child: followers) {
						child.setSaved(true);
						followers.removeValue(child, false);
						playerCharacter.clearPositionQueue();
					}
				}

				if(!collision) {
					playerCharacter.setPosition(spriteMoveX, spriteMoveY);
					if(followers.size > 0) {
						followers.get(0).followSprite(playerCharacter);

						for(int i = 1; i <followers.size; i++) {
							followers.get(i).followSprite(followers.get(i-1));
						}
					}
				}
			}	
		}
		
		if(currentWalkSequence.size > 0 && collision == false) {
			Texture next = currentWalkSequence.get(0);
			currentWalkSequence.removeIndex(0);
			currentWalkSequence.add(next);
			playerCharacter.setTexture(next);
		}
		spriteBatch.begin();
		//draw the main character sprite to the map
		playerCharacter.draw(spriteBatch);
		//Draw the children following
		if(followers.size != 0) {
			for(Sprite f: followers) {
				f.draw(spriteBatch);
			}
		}

		//draw the children on the maze
		for(Sprite s : mazeChildren) {
			s.draw(spriteBatch);
		}
		//update the camera to be above the character
		for (int i=0; i<playerHealth; i++) {
			float xPos = camera.position.x - camera.viewportWidth/2;
			float heartSize = camera.viewportHeight/10;
			float yPos = camera.position.y + camera.viewportHeight/2 - heartSize;
			spriteBatch.draw(healthTextureRegion, xPos + (heartSize*i), yPos, heartSize, heartSize);
		}
		camera.position.set(playerCharacter.getX(), playerCharacter.getY(), 0);
		//end the batch that sprites have drawn to
		spriteBatch.end();
			
		if (allSaved()) {
			game.setMazeCompleted(true);
			game.setAllSaved(true);
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
		}
		
		if (playerHealth <= 0) {
			game.setMazeCompleted(true);
			game.setAllSaved(false);
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == 'e') {
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
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
				playerCharacter.setTexture(spriteTextureR);
				currentWalkSequence = arrayWalkR;
			}
			if(delta.x <= 0) {
				xMove = -playerCharacter.getSpeed();
				playerCharacter.setTexture(spriteTextureL);
				currentWalkSequence = arrayWalkL;
			}
			//no vertical movement
			yMove = 0;
		}
		//otherwise y>=x so move vertically 
		else {
			//move down if the change was positive, else move up
			if(delta.y > 0)	{
				yMove = -playerCharacter.getSpeed();
				currentWalkSequence = arrayWalkD;
			}
			if(delta.y <= 0) {
				yMove = playerCharacter.getSpeed();
				currentWalkSequence = arrayWalkU;
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
		Gdx.input.setInputProcessor(this);
		// SOUND ASSET LEAKING EVERY CALL
		Sound click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
		if(ScreenAdapterManager.getInstance().game.soundEnabled && !ScreenAdapterManager.getInstance().game.muteAll){
			click.play(ScreenAdapterManager.getInstance().game.volume);
		}

		xMove = 0;
		yMove = 0;

		if(allSaved() || playerHealth <= 0) {
			reset();
		}

		else {
			if (game.levelWin()) {
				//add the follower from this minigame panel
				followers.add(lastRec.getOccupant());
			} 
			else if (lastRec.isOccupied()){
				Array<MinigameRectangle> unoccupied = new Array<MinigameRectangle>();
				for (MinigameRectangle rect: minigameRects) {
					if (!rect.isOccupied()) {
						unoccupied.add(rect);
					}
				}

				if (unoccupied.size > 0) {
					Random rand = new Random();
					int newPositionIndex = rand.nextInt(1000) % unoccupied.size;
					unoccupied.get(newPositionIndex).setOccupied(lastRec.getOccupant());
					ChildSprite child = unoccupied.get(newPositionIndex).getOccupant();
					child.moveTo(unoccupied.get(newPositionIndex));
				}
				playerHealth--;
			}

			lastRec.empty();
			game.levelCompleted(false);
			game.nullCurrentLevel();
		}
		for(MapLayer layer: map.getLayers()) {
			layer.setVisible(true);
		}
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i <collisionObjects.getCount(); i++) {
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
		game.setAllSaved(false);
		game.setMazeCompleted(false);
		populate();
	}

	/**
	 * Returns true if all children in the maze have been saved
	 */
	public boolean allSaved() {
		for (ChildSprite child : mazeChildren) {
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
		for(MapLayer layer: map.getLayers()){
			layer.setVisible(false);
		}
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
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	    }
		return true;
	}

}
