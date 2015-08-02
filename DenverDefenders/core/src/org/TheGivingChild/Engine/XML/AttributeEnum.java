package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Engine.Attributes.BounceOffEdgeOfScreenAttribute;
import org.TheGivingChild.Engine.Attributes.BounceOffTopAndBottomAttribute;
import org.TheGivingChild.Engine.Attributes.CatchTouchAttribute;
import org.TheGivingChild.Engine.Attributes.CollidesAttribute;
import org.TheGivingChild.Engine.Attributes.DestroyAttribute;
import org.TheGivingChild.Engine.Attributes.FloorAttribute;
import org.TheGivingChild.Engine.Attributes.FollowsObjectYAttribute;
import org.TheGivingChild.Engine.Attributes.MoveByAttribute;
import org.TheGivingChild.Engine.Attributes.MoveToObjectAttribute;
import org.TheGivingChild.Engine.Attributes.MovesAttribute;
import org.TheGivingChild.Engine.Attributes.PlaceAtObjectAttribute;
import org.TheGivingChild.Engine.Attributes.ScreenCheckAttribute;
import org.TheGivingChild.Engine.Attributes.SetVelocityAttribute;
import org.TheGivingChild.Engine.Attributes.StopAttribute;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Each GameObject holds a list of attributes, these attributes do various operations on the object based on their specific implementations of their methods<br>
 * Each Attribute must implement update, setup, getVariableNames, getValues, and getXMLName
 * @author Kevin D
 */
// Convenience enum wrapper to convert a string in a level file into an object of the correct attribute class
public enum AttributeEnum {
	/**
	 * If the object will EVER move on its own, must have moves attribute.
	 */
	MOVES {
		//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new MovesAttribute(args);
		}
	},
	// Moves by amount passed
	MOVE_BY {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new MoveByAttribute(args);
		}
	},
	/**
	 * Destroys the object, throws its event.  Used as a triggered attribute
	 */
	DESTROY {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new DestroyAttribute(args);
		}
	},
	// Throws an event when touched
	CATCH_TOUCH {

		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new CatchTouchAttribute(args);
		}
		
	},
	/**
	 * Continuously checks for collision with objects of passed ids and throws a corresponding condition
	 */
	COLLIDES {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new CollidesAttribute(args);
		}
	},
	/**
	 * Causes objects to collide with and bounce off of the edge of the screen
	 */
	BOUNCEOFFEDGEOFSCREEN{
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new BounceOffEdgeOfScreenAttribute(args);
		}
	},
	/**
	 * Causes objects to collide with and bounce off of the top and bottom of the screen, collisions are perfectly ellastic
	 */
	BOUNCEOFFTOPANDBOTTOM{
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new BounceOffTopAndBottomAttribute(args);
		}
	},
	/**
	 * The attached Object will attempt to math the y position of the other object, it will move towards the object at the predefined speed
	 */
	FOLLOWSOBJECTY{
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FollowsObjectYAttribute(args);
		}
	},
	// Sets velocity towards the passed object
	MOVE_TO_OBJECT {

		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new MoveToObjectAttribute(args);
		}
	},
	// Changes position to center of passed object
	PLACE_AT_OBJECT {

		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new PlaceAtObjectAttribute(args);
		}
		
	},
	/**
	 * Object will throw condition if off screen
	 */
	SCREENCHECK {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new ScreenCheckAttribute(args);
		}
	},
	SET_VELOCITY {

		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new SetVelocityAttribute(args);
		}
		
	},
	STOP {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new StopAttribute(args);
		}
	},
	FLOOR {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FloorAttribute(args);
		}
	};
	
	// Constructs the appropriate object
	public abstract Attribute construct(ObjectMap<String, String> args);
}
