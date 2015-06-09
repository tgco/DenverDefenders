package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
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
	
public class ScreenMaze extends ScreenAdapter implements InputProcessor{
	/** Map to be displayed */
	private TiledMap map;
	/** Orthographic Camera to look at map from top down */
	private OrthographicCamera camera;
	/** Tiled map renderer to display the map */
	private TiledMapRenderer mapRenderer;
	/** Sprite, SpriteBatch, and Textrue for users sprite */
	private SpriteBatch spriteBatch;
	private Texture spriteTexture;
	private Sprite sprite;
	/** Values to store which direction the sprite is moving */
	private float xMove, yMove, speed;
	/** Map properties to get dimensions of maze */
	private MapProperties properties;
	private int mapTilesX, mapTilesY;
	private float mazeWidth, mazeHeight;
	/** Array of rectangles to store locations of collisions */
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	/** Array of rectangle to store the location of miniGame triggers */
	private Array<Rectangle> minigameRects = new Array<Rectangle>();
	/** Vector to store the last touch of the user */
	private Vector2 lastTouch = new Vector2();
	
	private TGC_Engine game;
	private AssetManager manager;
	
	
	/**
	 * Creates a new maze screen and draws the players sprite on it.
	 * Sets up map properties such as dimensions and collision areas
	 * @param mazeFile The name of the maze file in the assets folder
	 * @param spriteFile The name of the sprite texture file in the assets folder
	 */
	
	public ScreenMaze()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		speed = Gdx.graphics.getHeight()/4;

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		map = new TmxMapLoader().load("mapAssets/SampleUrban.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);
				
		spriteBatch = new SpriteBatch();
		spriteTexture = new Texture(Gdx.files.internal("ball.png"));
		sprite = new Sprite(spriteTexture);
		//make sure all the layers are set to visible by default.
		//this is the source of error for the 'missing' background tiles
		
		//Setup map properties
		properties = map.getProperties();
		mapTilesX = properties.get("width", Integer.class);
		mapTilesY = properties.get("height", Integer.class);
		//width and height of tiles in pixels
		int pixWidth = properties.get("tilewidth", Integer.class);
		int pixHeight = properties.get("tileheight", Integer.class);
		
		mazeWidth = mapTilesX * pixWidth;
		mazeHeight = mapTilesY * pixHeight;
		
		//Setup array of collision rectangles
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();
		for(int i = 0; i <collisionObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		}
		
		MapObjects miniGameObjects = map.getLayers().get("Minigame").getObjects();
		for(int i = 0; i <miniGameObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) miniGameObjects.get(i);
			Rectangle rect = obj.getRectangle();
			minigameRects.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		}
		game = ScreenAdapterManager.getInstance().game;
		manager = game.getAssetManager();
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
				Gdx.gl.glClearColor(1, 0, 0, 1);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
						
						
				//update the camera
				camera.update();
				//set the map to be rendered by this camera
				mapRenderer.setView(camera);
				//render the map
				mapRenderer.render();
				//Make the sprite not move when the map is scrolled
				spriteBatch.setProjectionMatrix(camera.combined);
				//move the sprite left, right, up, or down
				//Calculate where the sprite is going to move
				float spriteMoveX = sprite.getX() + xMove*Gdx.graphics.getDeltaTime();
				float spriteMoveY = sprite.getY() + yMove*Gdx.graphics.getDeltaTime();
				//If the sprite is not going off the maze allow it to move
				//Check for a collision as well
				boolean collision = false;
				
				
				if(spriteMoveX >= 0 && (spriteMoveX+sprite.getWidth()) <= mazeWidth)
				{
					if(spriteMoveY >= 0 && (spriteMoveY+sprite.getHeight()) <= mazeHeight)
					{
						
						Rectangle spriteRec = new Rectangle(spriteMoveX, spriteMoveY, sprite.getWidth(), sprite.getHeight());
						
						for(Rectangle r : collisionRects)
						{
							if(r.overlaps(spriteRec))
							{
								collision = true;
							}
						}
						
						for(Rectangle m : minigameRects)
						{
							if(m.overlaps(spriteRec))
							{
								minigameRects.removeValue(m, true);
								//sprite.setAlpha(0);
								sprite.setPosition(m.getX(), m.getY());
								
								ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL);
																								
								collision = true;
								
							}
						}
									
						
						if(!collision) sprite.setPosition(spriteMoveX, spriteMoveY);
					}	
				}
				
				//begin the batch that sprites will draw to
				spriteBatch.begin();
				//draw the main character sprite to the map
				sprite.draw(spriteBatch);
				//update the camera to be above the character
				camera.position.set(sprite.getX(), sprite.getY(), 0);
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
			if(delta.x > 0) xMove =  speed;
			if(delta.x <= 0) xMove = -speed;
			//no vertical movement
			yMove = 0;
						
		}
		//otherwise y>=x so move vertically 
		else
		{
			//move down if the change was positive, else move left
			if(delta.y > 0)	yMove = -speed;
			if(delta.y <= 0) yMove = speed;
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
		
		xMove = 0;
		yMove = 0;
		
		for(MapLayer layer: map.getLayers()){
			layer.setVisible(true);
		}
		MapObjects collisionObjects = map.getLayers().get("Collision").getObjects();
		
		for(int i = 0; i <collisionObjects.getCount(); i++)
		{
			RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
			Rectangle rect = obj.getRectangle();
			collisionRects.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
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
