package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
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
					//Clip to only on screen movement
					// X Clip
					if (moveX > 0) {
						if (object.getX() + object.getWidth() + moveX > Gdx.graphics.getWidth())
							moveX = 0;
					}
					else if (moveX < 0) {
						if (object.getX() + moveX < 0)
							moveX = 0;
					}
					// Y clip
					if (moveY > 0) {
						if (object.getY() + object.getHeight() + moveY > Gdx.graphics.getHeight())
							moveY = 0;
					}
					else if (moveY < 0) {
						if (object.getY() + moveY < 0)
							moveY = 0;
					}
						
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
			// TGC_Engine is currently the only way to access the global clock
			final TGC_Engine game = ScreenAdapterManager.getInstance().game;
			return(new InputListener() {
				private float time;
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					// Get touch down time
					time = game.getGlobalClock();
					return true;
				}
				
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					// Return if too much time passed (not a fling)
					if (game.getGlobalClock() - time > 1f) return;
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
	public abstract InputListener construct(GameObject object, ObjectMap<String, String> args);
}
