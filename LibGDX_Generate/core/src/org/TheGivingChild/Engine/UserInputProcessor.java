package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.Attributes.GameObject;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;


public class UserInputProcessor implements InputProcessor, GestureListener {
		
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
		System.out.println("user has either pressed a button or moved the mouse");
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

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		System.out.println("user tapped on their device at point " + x + " , " + y );
		return true;
		
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		System.out.println("user long pressed their device");
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		System.out.println("user dragged their finger across screen, I know the velcoity in x an y directions");
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		System.out.println("user dragged their finger over their device, i know delta x and delta y");
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		System.out.println("not panning anymore");
		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		System.out.println("user perfomed a zoom pinch from a distance of " + initialDistance + " in pixels.");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

}