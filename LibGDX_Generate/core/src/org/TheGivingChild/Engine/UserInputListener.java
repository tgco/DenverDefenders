package org.TheGivingChild.Engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class UserInputListener extends InputListener implements InputProcessor {

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
	{
		
	}
	
	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
	{
		
	}
	
	@Override
	public boolean handle(Event e)
	{
		System.out.println("custom listener handled");
		return true;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		
		System.out.println("custom listener");
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
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
