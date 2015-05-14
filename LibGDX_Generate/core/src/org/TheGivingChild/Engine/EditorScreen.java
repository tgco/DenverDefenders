package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EditorScreen extends ScreenAdapter{
	private Game mainGame;
	private Texture ballImage;
	private SpriteBatch batch;
	private Array<Rectangle> balls;
	
	public EditorScreen(TGC_Engine mainGame) {
		this.mainGame = mainGame;
		ballImage = new Texture(Gdx.files.internal("ball.png"));
		batch = new SpriteBatch();
		balls = new Array<Rectangle>();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isTouched()) {
			spawnBall();
		}
		
		batch.begin();
		for (Rectangle ball : balls) {
			batch.draw(ballImage, ball.x, ball.y);
		}
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		ballImage.dispose();
		
	}
	private void spawnBall() {
		Rectangle ball = new Rectangle();

		ball.width = 64;
		ball.height = 64;
		ball.x = Gdx.input.getX() - ball.getWidth()/2;
		ball.y = Gdx.graphics.getHeight()-Gdx.input.getY() - ball.getHeight()/2;
		
		balls.add(ball);
	}

}
