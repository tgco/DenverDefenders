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
	/**This destroys the game object when clicked.*/
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

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return "destroy_on_click";
		}
	},
	/**This drags the object to follow the current position of a drag event*/
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

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return "drag_object";
		}
	},
	/**This allows the object to be drug left and right only*/
	DRAG_OBJECT_X{
		@Override
		public InputListener getInputListener(GameObject object) {

			//create a final reference to gameObject to be used within the anonymous class.
			final GameObject gameObject = object;
			//return a new input listener, overriding needed interactions
			return(new DragListener(){
				//drag moves the object to the new location that drag returns every update
				@Override
				public void drag(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
					gameObject.moveBy(x-gameObject.getWidth()/2, 0);
				};
			});
		}

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return "drag_object_x";
		}
	},
	/**This allows the object to be drug up and down only.*/
	DRAG_OBJECT_Y{
		@Override
		public InputListener getInputListener(GameObject object) {

			//create a final reference to gameObject to be used within the anonymous class.
			final GameObject gameObject = object;
			//return a new input listener, overriding needed interactions
			return(new DragListener(){
				//drag moves the object to the new location that drag returns every update
				@Override
				public void drag(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
					gameObject.moveBy(0, y-gameObject.getHeight()/2);
				};
			});
		}

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return "drag_object_y";
		}
	};
	//every enum needs to override this abstract method, and return an Input Listener
	/**Abstract method that game objects can use to add listeners.*/
	public abstract InputListener getInputListener(GameObject object);
	/**Abstract method that returns the name of the enum so the editor can use it in the xml writer.*/
	public abstract String getXMLName();
}
