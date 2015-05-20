package org.TheGivingChild.Engine;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public abstract class UserInputProcessor implements InputProcessor {

	GameObject o;
	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		System.out.println("key down: " + Keys.toString(keycode));
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		System.out.println("key released: " + Keys.toString(keycode));
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		System.out.println("key pressed: " + Keys.valueOf(Character.toString(character)));
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		System.out.println("user touched down screen at " + screenX + ", " + screenY);
		 	return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		System.out.println("user released from scree at" + screenX + ", " + screenY);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		System.out.println("user dragged across screen from " + screenX  + " to " + screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		System.out.println("user moved mouse from " + screenX + " to " + screenY);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		System.out.println("user has scrolled the mouse by " + amount);
		return true;
	}

}
