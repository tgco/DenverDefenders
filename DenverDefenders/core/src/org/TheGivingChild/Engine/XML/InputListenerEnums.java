package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
	TAP_DESTROY {
		@Override
		public InputListener construct(final GameObject object, ObjectMap<String, String> args) {
			//return a new input listener, overriding needed interactions
			return(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				//touch up disposes the game object when it is clicked.
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					object.dispose();
				}
			});
		}
	},
	// Sets velocity on tap
	TAP_SET_VELOCITY {
		@Override
		public InputListener construct(final GameObject object, ObjectMap<String, String> args) {
			// Get velocity args
			final float vx = Float.parseFloat(args.get("vx"));
			final float vy = Float.parseFloat(args.get("vy"));
			return(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					object.setVelocity(vx, vy);
				}
			});
		}
	},
	/**This drags the object to follow the current position of a drag event*/
	DRAGGABLE {
		@Override
		public InputListener construct(final GameObject object, ObjectMap<String, String> args) {
			// Set directions to drag of
			final boolean dragX = Boolean.parseBoolean(args.get("x"));
			final boolean dragY = Boolean.parseBoolean(args.get("y"));
			//return a new input listener, overriding needed interactions
			return(new DragListener() {
				//drag moves the object to the new location that drag returns every update
				@Override
				public void drag(InputEvent event, float x, float y, int pointer) {
					float moveX = 0;
					float moveY = 0;
					if (dragX) moveX = x-object.getWidth()/2;
					if (dragY) moveY = y-object.getHeight()/2;
						
					object.moveBy(moveX, moveY);
				};
			});
		}
	},
	/** Allows the object to be flung **/
	FLINGABLE {
		@Override
		public InputListener construct(final GameObject object, ObjectMap<String, String> args) {
			final float sensitivity = Float.parseFloat(args.get("sensitivity"));
			return(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					// Find distance from center of object
					float deltaX = x - object.getWidth()/2;
					float deltaY = y - object.getHeight()/2;
					// Set a velocity
					object.setVelocity(sensitivity*deltaX, sensitivity*deltaY);
				}
			});
		}
	};
	//every enum needs to override this abstract method, and return an Input Listener
	/**Abstract method that game objects can use to add listeners.*/
	public abstract InputListener construct(GameObject object, ObjectMap<String, String> args);
}
