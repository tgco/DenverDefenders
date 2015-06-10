package org.TheGivingChild.Screens;

import java.util.Random;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
/**
 *Maze screen that the user will navigate around.
 *Player will be able to trigger a miniGame by finding a child in the maze.
 *@author mtzimour
 */

public class ScreenMaze extends ScreenAdapter implements InputProcessor{
	/** Map to be displayed */
	private TiledMap map;
	/** Orthographic Camera to look at map from top down */
	private OrthographicCamera camera;
	/** Tiled map renderer to display the map */
	private TiledMapRenderer mapRenderer;
	/** Sprite, SpriteBatch, and Texture for users sprite */
	private SpriteBatch spriteBatch;
	private Texture spriteTexture;
	private ChildSprite playerCharacter;
	/** Values to store which direction the sprite is moving */
	private float xMove, yMove, speed;
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
	private AssetManager manager;

	private Array<ChildSprite> mazeChildren;
	private Array<ChildSprite> followers;
	private MinigameRectangle miniRec;

	private Texture backdropTexture;
	private TextureRegion backdropTextureRegion;

	private MinigameRectangle lastRec;
	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */

	public ScreenMaze(){	
		//map = new TmxMapLoader().load("mapAssets/SampleUrban.tmx");
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


		spriteBatch = new SpriteBatch();
		spriteTexture = new Texture(Gdx.files.internal("ball.png"));

		playerCharacter = new ChildSprite(spriteTexture);
		playerCharacter.setSpeed(4*pixHeight);
		playerCharacter.setScale(.25f,.25f);

		//Get the rect for the heros headquarters
		RectangleMapObject startingRectangle = (RectangleMapObject)map.getLayers().get("HeroHeadquarters").getObjects().get(0);
		playerCharacter.setPosition(startingRectangle.getRectangle().x-24,startingRectangle.getRectangle().y-16);

		mazeChildren = new Array<ChildSprite>();
		followers = new Array<ChildSprite>();

		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i <collisionObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x-24, rect.y-24, rect.width, rect.height));
		}

		//Setup array of minigame rectangles
		MapObjects miniGameObjects = map.getLayers().get("Minigame").getObjects();
		for(int i = 0; i <miniGameObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) miniGameObjects.get(i);
			Rectangle rect = obj.getRectangle();

			Rectangle childRec = new Rectangle(rect.x, rect.y-pixHeight, rect.width*.25f, rect.height);
			miniRec = new MinigameRectangle(rect.x, rect.y-pixHeight, rect.width*.25f, rect.height);
			lastRec = new MinigameRectangle(rect.x, rect.y-pixHeight, rect.width*.25f, rect.height);

			//Add children to be drawn where minigames can be triggered
			//	Texture childTexture = new Texture(Gdx.files.internal("mapAssets/somefreesprites/Character Pink Girl.png"));
			//ChildSprite child = new ChildSprite(childTexture);
			//child.setScale(.25f);
			//	child.setPosition(rect.x - child.getWidth()/4, rect.y - child.getHeight()/4);
			//	child.setRectangle(childRec);

			//	mazeChildren.add(child);

			//	miniRec.setOccupied(child);

			minigameRects.add(miniRec);
		}

		//Remove a child at random so there is always an open spot
		//children.removeIndex(MathUtils.random(children.size));


		populate();

		game = ScreenAdapterManager.getInstance().game;
		manager = game.getAssetManager();
		game.setScreenSwitch(true);

		manager.load("mapAssets/UrbanMaze1Backdrop.png", Texture.class);
		manager.finishLoadingAsset("mapAssets/UrbanMaze1Backdrop.png");
		backdropTexture = manager.get("mapAssets/UrbanMaze1Backdrop.png");
		backdropTextureRegion = new TextureRegion(backdropTexture);
		
	}


	public void populate()
	{

		int theRand = 0;

		for(MinigameRectangle rect : minigameRects)
		{

			//Possible values 0,1,2,3,4
			theRand = MathUtils.random(0,5);
			//60% chance of kid being drawn
			if(theRand >= 2 )
			{
				//Add children to be drawn where minigames can be triggered
				Texture childTexture = new Texture(Gdx.files.internal("mapAssets/somefreesprites/Character Pink Girl.png"));
				ChildSprite child = new ChildSprite(childTexture);
				child.setScale(.25f);
				child.setPosition(rect.x - child.getWidth()/4, rect.y);

				//child.setRectangle(childRec);
				mazeChildren.add(child);

				rect.setOccupied(child);
			}



		}


	}



	/**
	 * Draws the maze on the screen with a red background
	 * Determines if the sprite is making a valid move within the 
	 * bounds of the maze and not on a collision, if so allow it to move.
	 * If the player triggers a miniGame set that to the current screen.
	 */

	@Override 
	public void render(float delta)
	{
		ScreenAdapterManager.getInstance().screenTransitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionInComplete) {
				//set a red background
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				
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
				boolean triggerGame = false;


				if(spriteMoveX >= 0 && (spriteMoveX+playerCharacter.getWidth()) <= mazeWidth)
				{
					if(spriteMoveY >= 0 && (spriteMoveY+playerCharacter.getHeight()) <= mazeHeight)
					{

						Rectangle spriteRec = new Rectangle(spriteMoveX, spriteMoveY, playerCharacter.getWidth()/4, playerCharacter.getHeight()/4);

						for(Rectangle r : collisionRects)
						{
							if(r.overlaps(spriteRec))
							{
								collision = true;
							}
						}


						for(MinigameRectangle m : minigameRects)
						{
							if(m.overlaps(spriteRec) && m.isOccupied() && !game.getPacketCompleted())
							{
								//followers.add(m.getOccupant());
								//m.empty();
								lastRec = m;
								playerCharacter.setPosition(m.getX(), m.getY());
								triggerGame = true;
								game.selectLevel();
								ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL);

							}
						}

						if(!collision){
							playerCharacter.setPosition(spriteMoveX, spriteMoveY);

							if(followers.size > 0)
							{
								followers.get(0).followSprite(playerCharacter);

								for(int i = 1; i <followers.size; i++)
								{
									followers.get(i).followSprite(followers.get(i-1));
									//System.out.println("set follow for" + followers.get(i).toString());
								}

							}


							playerCharacter.setPosition(spriteMoveX, spriteMoveY);

						}

					}	
				}

				//begin the batch that sprites will draw to
				spriteBatch.begin();
				//draw the main character sprite to the map
				playerCharacter.draw(spriteBatch);
				//Draw the children following
				if(followers.size != 0)
				{
					for(Sprite f: followers)
					{
						//System.out.println(followers.size);
						//System.out.println(f.get);
						f.draw(spriteBatch);
					}
				}

				//draw the children on the maze
				for(Sprite s : mazeChildren)
				{
					s.draw(spriteBatch);
				}
				//update the camera to be above the character
				camera.position.set(playerCharacter.getX(), playerCharacter.getY(), 0);
				//end the batch that sprites have drawn to
				spriteBatch.end();


			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}


	@Override
	public boolean keyDown(int keycode) {
		//need key down to have key up function
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
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

		if (Math.abs(delta.x) > Math.abs(delta.y))
		{
			//if the change was positive, move right, else move left
			if(delta.x > 0) xMove =  playerCharacter.getSpeed();
			if(delta.x <= 0) xMove = -playerCharacter.getSpeed();
			//no vertical movement
			yMove = 0;

		}
		//otherwise y>=x so move vertically 
		else
		{
			//move down if the change was positive, else move left
			if(delta.y > 0)	yMove = -playerCharacter.getSpeed();
			if(delta.y <= 0) yMove = playerCharacter.getSpeed();
			//no horizontal movement
			xMove = 0;
		}

		playerCharacter.setMove(xMove, yMove);

		return true;

	}

	/**
	 * Sets the maze to be shown, makes sure the player is not moving initially.
	 * Sets all layers of the maze to visible. 
	 * Sets input processor to the maze to begin getting user input for navigation.
	 */

	@Override
	public void show(){
		//game.loadLevelPackets();
		xMove = 0;
		yMove = 0;

		if (game.levelWin()) {
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
		}
		lastRec.empty();
		for(MapLayer layer: map.getLayers()){
			layer.setVisible(true);
		}
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();

		for(int i = 0; i <collisionObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x-24, rect.y-24, rect.width, rect.height));
		}
		Gdx.input.setInputProcessor(this);

	}

	/**
	 * Hides the maze by setting all layers to not visible.
	 * Sets input processor back to the stage.
	 */

	@Override
	public void hide(){
		for(MapLayer layer: map.getLayers()){
			layer.setVisible(false);
		}
		Gdx.input.setInputProcessor(ScreenAdapterManager.getInstance().game.getStage());
		collisionRects.clear();
		game.setScreenSwitch(true);
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


}
