package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Engine.Attributes.CatchTouchAttribute;
import org.TheGivingChild.Engine.Attributes.CollidesAttribute;
import org.TheGivingChild.Engine.Attributes.DestroyAttribute;
import org.TheGivingChild.Engine.Attributes.FlagAttribute;
import org.TheGivingChild.Engine.Attributes.FlagCheckAttribute;
import org.TheGivingChild.Engine.Attributes.FloorAttribute;
import org.TheGivingChild.Engine.Attributes.FreezeAttribute;
import org.TheGivingChild.Engine.Attributes.MoveByAttribute;
import org.TheGivingChild.Engine.Attributes.MoveToObjectAttribute;
import org.TheGivingChild.Engine.Attributes.MovesAttribute;
import org.TheGivingChild.Engine.Attributes.PlaceAtObjectAttribute;
import org.TheGivingChild.Engine.Attributes.ScreenCheckAttribute;
import org.TheGivingChild.Engine.Attributes.SetPositionAttribute;
import org.TheGivingChild.Engine.Attributes.SetVelocityAttribute;
import org.TheGivingChild.Engine.Attributes.StopAttribute;

import com.badlogic.gdx.utils.ObjectMap;

// Convenience enum wrapper to convert a string in a level file into an object of the correct attribute class
public enum AttributeEnum {
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
	 * Destroys the object, throws its event.  Used as a triggered attribute
	 */
	DESTROY {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new DestroyAttribute(args);
		}
	},
	FLAG {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FlagAttribute(args);
		}
	},
	FLAG_CHECK {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FlagCheckAttribute(args);
		}
	},
	FLOOR {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FloorAttribute(args);
		}
	},
	FREEZE {
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new FreezeAttribute(args);
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
	 * If the object will EVER move on its own, must have moves attribute.
	 */
	MOVES {
		//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new MovesAttribute(args);
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
	SET_POSITION {

		@Override
		public Attribute construct(ObjectMap<String, String> args) {
			return new SetPositionAttribute(args);
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
	};

	// Constructs the appropriate object
	public abstract Attribute construct(ObjectMap<String, String> args);
}
