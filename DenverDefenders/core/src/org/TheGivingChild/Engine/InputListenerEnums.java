package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ObjectMap;
/**
 * These enumerations return InputListener()'s that take in a game object, and are added to the game object.
 * This allows for easy addition of new listeners, such as dragAndDrop, and destroyOnClick.
 * @author Jack Wesley Nelson
 * 
 */

public enum InputListenerEnums{
	/**This destroys the game object when clicked.*/
	DESTROY_ON_CLICK{
		@Override
		public InputListener construct(GameObject object, ObjectMap<String, String> args) {
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
	/**This drags the object to follow the current position of a drag event*/
	DRAGGABLE{
		@Override
		public InputListener construct(final GameObject object, ObjectMap<String, String> args) {
			// Set directions to drag of
			final boolean dragX = Boolean.parseBoolean(args.get("x"));
			final boolean dragY = Boolean.parseBoolean(args.get("false"));
			//return a new input listener, overriding needed interactions
			return(new DragListener() {
				//drag moves the object to the new location that drag returns every update
				@Override
				public void drag(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
					float moveX = 0;
					float moveY = 0;
					if (dragX) moveX = x-object.getWidth()/2;
					if (dragY) moveY = y-object.getHeight()/2;
						
					object.moveBy(moveX, moveY);
				};
			});
		}
	};
	//every enum needs to override this abstract method, and return an Input Listener
	/**Abstract method that game objects can use to add listeners.*/
	public abstract InputListener construct(GameObject object, ObjectMap<String, String> args);
}
