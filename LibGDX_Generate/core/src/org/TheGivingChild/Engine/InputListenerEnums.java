package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.GameObject;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
/**
 * These enumerations return InputListener()'s that take in a game object, and are added to the game object.
 * This allows for easy addition of new listeners, such as dragAndDrop, and destroyOnClick.
 * @author Jack Wesley Nelson
 * 
 */

public enum InputListenerEnums{
	DESTROY_ON_CLICK{
		@Override
		public InputListener getInputListener(GameObject object) {
			//create a final reference to gameObject to be used within the anonymous class.
			final GameObject gameObject = object;
			//return a new input listener, overriding needed interactions
			return(new InputListener(){
				//touch down is needed along with touch up, in order for it to work
				@Override
				public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				//touch up disposes the game object when it is clicked.
				@Override
				public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					gameObject.dispose();
				}
			});
		}
	},
	DRAG_OBJECT{
		@Override
		public InputListener getInputListener(GameObject object) {

			//create a final reference to gameObject to be used within the anonymous class.
			final GameObject gameObject = object;
			//return a new input listener, overriding needed interactions
			return(new DragListener(){
				//drag moves the object to the new location that drag returns every update
				@Override
				public void drag(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
					gameObject.moveBy(x-gameObject.getWidth()/2, y-gameObject.getHeight()/2);
				};
			});
		}
	};
	//every enum needs to override this abstract method, and return an Input Listener
	public abstract InputListener getInputListener(GameObject object);
}
