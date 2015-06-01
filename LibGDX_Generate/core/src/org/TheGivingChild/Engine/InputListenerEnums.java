package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.scenes.scene2d.InputListener;


public enum InputListenerEnums {
	DESTROY_ON_CLICK{
		@Override
		public InputListener getInputListener(GameObject object) {
			final GameObject gameObject = object;
			System.out.println("The gameObject associated with DESTROY_ON_CLICK is: " +gameObject.getID());
			return(new InputListener(){
				@Override
				public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				@Override
				public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("Destroying object: " + gameObject.getID());
					gameObject.dispose();
					
				}
			});
		}
	};
	public abstract InputListener getInputListener(GameObject object);
}
