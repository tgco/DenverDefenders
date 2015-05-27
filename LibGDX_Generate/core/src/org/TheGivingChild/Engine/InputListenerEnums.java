package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.scenes.scene2d.InputListener;


public enum InputListenerEnums {
	DESTROY_ON_CLICK{
		@Override
		public InputListener getInputListener(GameObject object) {
			final GameObject gameObject = object;
			return(new InputListener(){
				@Override
				public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("The object was touch down'ed!/n" + "The objects name is: " + gameObject.getName());
					return true;
				}
				@Override
				public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("The object was touch up'ed!/n" + "The objects name is: " + gameObject.getName());
				}
				@Override
				public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
					System.out.println("The object was hovered over!/n" + "The objects name is: " + gameObject.getName());
				}
			});
		}
	};


		public abstract InputListener getInputListener(GameObject object);
	}
