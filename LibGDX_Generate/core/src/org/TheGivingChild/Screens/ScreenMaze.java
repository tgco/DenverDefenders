package org.TheGivingChild.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
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

public class ScreenMaze extends ScreenAdapter implements InputProcessor{

	private TiledMap map;
	private OrthographicCamera camera;
	private TiledMapRenderer mapRenderer;
	private SpriteBatch spriteBatch;
	private Texture spriteTexture;
	private Sprite sprite;
	private float xMove, yMove, speed;
	private MapProperties properties;
	private int mapTilesX, mapTilesY;
	private float mazeWidth, mazeHeight;
	private Array<Rectangle> collisionRects = new Array<Rectangle>();

	private Vector2 lastTouch = new Vector2();


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
		Gdx.input.setInputProcessor(this);
		
		
		spriteBatch = new SpriteBatch();
		spriteTexture = new Texture(Gdx.files.internal("ball.png"));
		sprite = new Sprite(spriteTexture);
		//make sure all the layers are set to visible by default.
		//this is the source of error for the 'missing' background tiles
		for(MapLayer layer: map.getLayers()){
			layer.setVisible(true);
		}

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
			collisionRects.add(new Rectangle(rect.x / mapTilesX, rect.y / mapTilesY, rect.width / mapTilesX, rect.height / mapTilesY));
		}
					
		
	}

	@Override 
	public void render(float delta)
	{
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
		
		boolean collision = false;
		
		if(spriteMoveX >= 0 && (spriteMoveX+sprite.getWidth()) <= mazeWidth)
		{
			if(spriteMoveY >= 0 && (spriteMoveY+sprite.getHeight()) <= mazeHeight)
			{
				
				Rectangle spriteRec = new Rectangle(spriteMoveX, spriteMoveY, sprite.getWidth(), sprite.getHeight());
				
				for(Rectangle r : collisionRects)
				{
					if(spriteRec.overlaps(r))
					{
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

	@Override
	public boolean keyDown(int keycode) {
		//need key down to have key up function
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT)
			camera.translate(-32,0);
		if(keycode == Input.Keys.RIGHT)
			camera.translate(32,0);
		if(keycode == Input.Keys.UP)
			camera.translate(0,32);
		if(keycode == Input.Keys.DOWN)
			camera.translate(0,-32);
		//toggle background visibility
		if(keycode == Input.Keys.NUM_1){
			map.getLayers().get(0).setVisible(!map.getLayers().get(0).isVisible());
		}
		//toggle building layer visibility
		if(keycode == Input.Keys.NUM_2){
			map.getLayers().get(1).setVisible(!map.getLayers().get(1).isVisible());
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Get vector of touch down to calculate change in movement during swipe
		lastTouch = new Vector2(screenX, screenY);
		return true;
	}

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
