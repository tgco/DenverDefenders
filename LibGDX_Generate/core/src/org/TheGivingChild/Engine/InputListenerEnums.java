package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;


public enum InputListenerEnums{
	DESTROY_ON_CLICK{
		@Override
		public InputListener getInputListener(GameObject object) {
			final GameObject gameObject = object;
			return(new InputListener(){
				@Override
				public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
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
			final GameObject gameObject = object;
			return(new DragListener(){
				@Override
				public void drag(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
					gameObject.moveBy(x, y);
				};
			});
		}
	};
	public abstract InputListener getInputListener(GameObject object);
}
