package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Engine.Attributes.BounceOffEdgeOfScreenAttribute;
import org.TheGivingChild.Engine.Attributes.BounceOffTopAndBottomAttribute;
import org.TheGivingChild.Engine.Attributes.CollidesWithObjectsID;
import org.TheGivingChild.Engine.Attributes.CollidesWithObjectsIDSelfAttribute;
import org.TheGivingChild.Engine.Attributes.DestroysObjectsOfIDOnCollisionAttribute;
import org.TheGivingChild.Engine.Attributes.FollowsObjectYAttribute;
import org.TheGivingChild.Engine.Attributes.GravityAttribute;
import org.TheGivingChild.Engine.Attributes.MassAttribute;
import org.TheGivingChild.Engine.Attributes.MovesAttribute;
import org.TheGivingChild.Engine.Attributes.MovesOnSetPathAttribute;
import org.TheGivingChild.Engine.Attributes.SpawnObjectOnTimerAttribute;

/**
 * Each GameObject holds a list of attributes, these attributes do various operations on the object based on their specific implementations of their methods<br>
 * Each Attribute must implement update, setup, getVariableNames, getValues, and getXMLName
 * @author Kevin D
 */
// Convenience Enum wrapper to convert a string in a level file into an object of the correct class
public enum AttributeEnum {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
	 */	
	/**
	 * If the object will EVER move, must have moves attribute, it has two values, initial x velocity, and initial y velocity, upon setup, it sets its GameObject's velocity to them
	 */
	MOVES {
		//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		@Override
		public Attribute construct() {
			return new MovesAttribute();
		}
		@Override
		public String getXMLName() { return "moves"; }
	},
	/**
	 * Causes objects to collide with and bounce off of the edge of the screen
	 */
	BOUNCEOFFEDGEOFSCREEN{
		@Override
		public Attribute construct() {
			return new BounceOffEdgeOfScreenAttribute();
		}
		
		@Override
		public String getXMLName() { return "bounceOffEdgeOfScreen"; }	
	},
	/**
	 * An object with this attribute will collide with the objects which it was told to.<br>
	 * All objects with this attribute AS WELL AS objects it will be colliding with, must have the MASS Attribute<br>
	 * Do not give this attribute to both objects which will be colliding, for instance, if you want object1 to collide with object2, only give this attribute to object1, and pass it object2's ID<br>
	 * currently buggy, not working as intended.
	 */
	COLLIDESWITHOBJECTSID{
		//data is stored as value1=mass of object, all other values are the objects it can collide with
		@Override
		public Attribute construct() {
			return new CollidesWithObjectsID();
		}
		@Override
		public String getXMLName(){return "collidesWithObjectsID";}
	},
	/**
	 * mass is used in collisions, if the object will ever have an object colliding with it, it MUST have this attribute
	 */
	MASS {
		@Override
		public Attribute construct() {
			return new MassAttribute();
		}
		@Override
		public String getXMLName() {
			return "mass";
		}
	},
	/**
	 * The attached object will move on a set path with the same speed as it's initial velocity
	 */
	MOVESONSETPATH {
		@Override
		public Attribute construct() {
			return new MovesOnSetPathAttribute();
		}
		
		@Override
		public String getXMLName() {
			return "movesOnSetPath";
		}
	},
	/**
	 * NOT WORKING
	 * The attached Object will spawn objects with the predefined aspects
	 */
	SPAWNOBJECTONTIMER{
		@Override
		public Attribute construct() {
			return new SpawnObjectOnTimerAttribute();
		}
		
		@Override
		public String getXMLName() {
			return "spawnObjectOnTimer";
		}
	},
	/**
	 * The attached Object will destroy objects of the set IDs upon collision with them
	 */
	DESTROYSOBJECTSOFIDONCOLLISION{
		@Override
		public Attribute construct() {
			return new DestroysObjectsOfIDOnCollisionAttribute();
		}
		
		@Override
		public String getXMLName() {
			return "destroysObjectsOfIDOnCollision";
		}
	},
	/**
	 * The attached object will have a constant downward acceleration
	 */
	GRAVITY{
		@Override
		public Attribute construct() {
			return new GravityAttribute();
		}
		
		@Override
		public String getXMLName() {return "gravity";}
	},
	/**
	 * Causes objects to collide with and bounce off of the top and bottom of the screen, collisions are perfectly ellastic
	 */
	BOUNCEOFFTOPANDBOTTOM{
		@Override
		public Attribute construct() {
			return new BounceOffTopAndBottomAttribute();
		}
		@Override
		public String getXMLName(){return "bounceOffTopAndBottom";}	
	},
	/**
	 * The attached Object will attempt to math the y position of the other object, it will move towards the object at the predefined speed
	 */
	FOLLOWSOBJECTY{
		@Override
		public Attribute construct() {
			return new FollowsObjectYAttribute();
		}
		
		@Override
		public String getXMLName() {
			return "followsObjectY";
		}
	},
	/**
	 * The attached object will collide with the listed objects, however it will not cause them to move, only itself
	 */
	COLLIDESWITHOBJECTSID_SELF{
		@Override
		public Attribute construct() {
			return new CollidesWithObjectsIDSelfAttribute();
		}
		@Override
		public String getXMLName(){return "collidesWithObjectsID_self";}
	};
	
	/**
	 * @param 	A string(read in from the XML_Reader) to be converted into the associated Attribute
	 * @return	The Attribute associated with the given string
	 */
	public abstract Attribute construct();
	
	public abstract String getXMLName();
}
