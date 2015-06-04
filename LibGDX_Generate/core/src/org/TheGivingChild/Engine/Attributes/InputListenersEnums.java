package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public enum InputListenersEnums {
	DESTROY_ON_CLICK{

		@Override
		public InputListener getInputListener(GameObject gameObject) {
			return new InputListener(){
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("Shouldn't do anything.");
					super.touchUp(event, x, y, pointer, button);
				}
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("The object should be destroyed here.");
					return super.touchDown(event, x, y, pointer, button);
				}
			};
		}

		@Override
		public String getXMLName() {
			return "destroy_on_click";
		}
		
	};
	
	public abstract InputListener getInputListener(GameObject gameObject);
	public abstract String getXMLName();
}
