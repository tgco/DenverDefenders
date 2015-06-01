package org.TheGivingChild.Screens;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ScreenMaze extends ScreenAdapter implements InputProcessor{
		
	private Texture mapImage;
	private TiledMap map;
	private OrthographicCamera camera;
	private TiledMapRenderer mapRenderer;
	private SpriteBatch spriteBatch;
	private Texture spriteTexture;
	private Sprite sprite;
	private float xMove, yMove, speed;
	
	private Vector2 lastTouch = new Vector2();
	
	
	public ScreenMaze()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		speed = Gdx.graphics.getHeight()/150;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
	//	map = new TmxMapLoader().load("mapAssets/TEST_Crappymap.tmx");
		map = new TmxMapLoader().load("mapAssets/SampleUrban.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		Gdx.input.setInputProcessor(this);
		
		spriteBatch = new SpriteBatch();
		spriteTexture = new Texture(Gdx.files.internal("ball.png"));
		sprite = new Sprite(spriteTexture);
		
		
	}
	
	@Override 
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		spriteBatch.setProjectionMatrix(camera.combined); //Make the sprite not move when the map is scrolled
		sprite.setPosition(sprite.getX() + xMove, sprite.getY() + yMove);
		
		spriteBatch.begin();
		sprite.draw(spriteBatch);
		camera.position.set(sprite.getX(), sprite.getY(), 0);	//Set camera to focus on character
		spriteBatch.end();
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
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
		if(keycode == Input.Keys.NUM_1)
			map.getLayers().get(0).setVisible(!map.getLayers().get(0).isVisible());
		if(keycode == Input.Keys.NUM_2)
			map.getLayers().get(1).setVisible(!map.getLayers().get(1).isVisible());
		// TODO Auto-generated method stub
		return false;
	}

	//input to navigate the map
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		//Get vector of touch down		
		lastTouch = new Vector2(screenX, screenY);
	
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
		Vector2 newTouch = new Vector2(screenX, screenY);
		Vector2 delta = newTouch.cpy().sub(lastTouch);
		
		if (Math.abs(delta.x) > Math.abs(delta.y))
		{
			if(delta.x > 0) xMove =  speed;
			if(delta.x <= 0) xMove = -speed;
			yMove = 0;
		}
		else
		{
			if(delta.y > 0)	yMove = -speed;
			if(delta.y <= 0) yMove = +speed;
			xMove = 0;
		}
		
			
		
		lastTouch = newTouch;
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		
		//Compare the dragged touch to last touch to see what direction
		
		
		//sprite.setX(sprite.getX() - delta.x);
		//sprite.setY(sprite.getY() + delta.y);
		
		
	
		
		
		//sprite.setX(sprite.getX() + delta.x);
	//	sprite.setY(sprite.getY() - delta.y);
		
		
		
	//	lastTouch = newTouch;
		
		
		//sprite.setPosition(screenX, screenY);
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
