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
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class ScreenMaze extends ScreenAdapter implements InputProcessor{
		
	private Texture mapImage;
	private TiledMap map;
	private OrthographicCamera camera;
	private TiledMapRenderer mapRenderer;
	private SpriteBatch spriteBatch;
	private Texture spriteTexture;
	private Sprite sprite;
	
	
	public ScreenMaze()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		map = new TmxMapLoader().load("mapAssets/TEST_crappymap.tmx");
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
		spriteBatch.begin();
		sprite.draw(spriteBatch);
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
		// TODO Auto-generated method stub
		Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
		Vector3 position = camera.unproject(clickCoordinates);
		sprite.setPosition(position.x, position.y);
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
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
