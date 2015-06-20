package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
//1024x576
/**
 * Each GameObject holds a list of attributes, these attributes do various operations on the object based on their specific implementations of their methods<br>
 * Each Attribute must implement update, setup, getVariableNames, getValues, and getXMLName
 * @author Kevin D
 */
public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
	 */	
	/**
	 * If the object will EVER move, must have moves attribute, it has two values, initial x velocity, and initial y velocity, upon setup, it sets its GameObject's velocity to them
	 */
	MOVES{//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		public void update(GameObject myObject,Array<GameObject> allObjects){
			myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
		}
		
		public void setup(GameObject myObject){
			//if(!myObject.getAttributeData().keys().toArray().contains(MOVESONSETPATH, false))
			myObject.setVelocity(new float[] {Float.parseFloat(myObject.getAttributeData().get(MOVES).get(0)),Float.parseFloat(myObject.getAttributeData().get(MOVES).get(1))});
		}
		public Array<String> getValues(GameObject myObject){
			Array<String> temp = new Array<String>();
			temp.add(myObject.getAttributeData().get(MOVES).get(0));
			temp.add(myObject.getAttributeData().get(MOVES).get(1));
			return temp;
		}
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Initial X Velocity");
			variableNames.add("Initial Y Velocity");
			return variableNames;
		}
		
		public String getXMLName(){return "moves";}
	},
	/**
	 * Causes objects to collide with and bounce off of the edge of the screen
	 */
	BOUNCEOFFEDGEOFSCREEN{
		public void update(GameObject myObject,Array<GameObject> allObjects){
			if(myObject.getX() <= 0){//left
				float[] temp = myObject.getVelocity();
				temp[0] = Math.abs(temp[0]);
				myObject.setVelocity(temp);
			}if(myObject.getX() +myObject.getTextureWidth() >= Gdx.graphics.getWidth()){//right
				float[] temp = myObject.getVelocity();
				temp[0] = -Math.abs(temp[0]);
				myObject.setVelocity(temp);
			}if(myObject.getY() <= 0){//bottom
				float[] temp = myObject.getVelocity();
				temp[1] = Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}if(myObject.getY() + myObject.getTextureHeight() >= Gdx.graphics.getHeight()){//top
				float[] temp = myObject.getVelocity();
				temp[1] = -Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}
		}
		public Array<String> getVariableNames(){
			return new Array<String>();
		}
		public void setup(GameObject myObject){}//doesnt need to setup anything		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(BOUNCEOFFEDGEOFSCREEN);
		}
		public String getXMLName(){return "bounceOffEdgeOfScreen";}		
	},
	/**
	 * An object with this attribute will collide with the objects which it was told to.<br>
	 * All objects with this attribute AS WELL AS objects it will be colliding with, must have the MASS Attribute<br>
	 * Do not give this attribute to both objects which will be colliding, for instance, if you want object1 to collide with object2, only give this attribute to object1, and pass it object2's ID<br>
	 * currently buggy, not working as intended.
	 */
	COLLIDESWITHOBJECTSID{//only gonna get square objects working for now, circular objects wont be too hard later
		//data is stored as value1=mass of object, all other values are the objects it can collide with
		static final float MAX_VELOCITY = 400;
		static final float COLLISION_CONSTANT = 5;//5
		static final float COLLISION_OFFSET = 1;//1
		public void update(GameObject myObject,Array<GameObject> allObjects){
			Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
			for(int i =0; i < allObjects.size;i++){
				if(myObject.getID() != allObjects.get(i).getID() && myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
					Rectangle r2 = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
					if(r1.overlaps(r2) ){//&& juanSmall.overlaps(tooSmall)
						//collision has been detected, getting needed information for the collision equation(using momentum)
						float c1 = Float.parseFloat(myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).get(0));
						//float c2 = allObjects.get(i).getAttributeData();
						float m1 = Float.parseFloat(myObject.getAttributeData().get(MASS).get(0));
						float m2 = Float.parseFloat(allObjects.get(i).getAttributeData().get(MASS).get(0));
						float v1ix = myObject.getVelocity()[0];
						float v2ix = allObjects.get(i).getVelocity()[0];
						float v1iy = myObject.getVelocity()[1];
						float v2iy = allObjects.get(i).getVelocity()[1];
						
						float[] myObjectVelocity = new float[] {c1*((m1-m2)*v1ix + 2*m2*v2ix)/(m1+m2),c1*((m1-m2)*v1iy + 2*m2*v2iy)/(m1+m2)};
						myObject.setVelocity(myObjectVelocity);
						float mag1 =(float) Math.pow(myObjectVelocity[0]*myObjectVelocity[0] + myObjectVelocity[1]*myObjectVelocity[1],.5);
						float[] myObjectDirection = {myObjectVelocity[0]/mag1,myObjectVelocity[1]/mag1};

						//new float[] {-1*myObjectVelocity[0],-1*myObjectVelocity[1]};
						float[] otherObjectVelocity = new float[] {-1*myObjectVelocity[0],-1*myObjectVelocity[1]};//new float[] {c1*((2*m1*v1ix+(m1-m2)*v2ix)/(m1+m2))*.9f,c1*(2*m1*v1iy+(m1-m2)*v2iy/(m1+m2))*.9f};
						allObjects.get(i).setVelocity(otherObjectVelocity);
						float mag2=(float) Math.pow(otherObjectVelocity[0]*otherObjectVelocity[0] + otherObjectVelocity[1]*otherObjectVelocity[1],.5);
						float[] otherObjectDirection = {otherObjectVelocity[0]/mag2,otherObjectVelocity[1]/mag2};
						
						//this loop will make sure the objects aren't overlapping after collision has occured, will likely remove loop and change code if time allows.
						while(r1.overlaps(r2)){
							if(mag1>mag2){//if object 1 is travelling faster than object 2 after the collision, then we want it to travel slightly farther, otherwise collision continually happens and bad things happen :( CHANGE ME AFTER LUNCH
								myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,myObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
								allObjects.get(i).moveBy(otherObjectDirection[0]*COLLISION_CONSTANT,otherObjectDirection[1]*COLLISION_CONSTANT);
							}else{//else object 2 is faster than obj1, repeat.
								myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT,myObjectDirection[1]*COLLISION_CONSTANT);
								allObjects.get(i).moveBy(otherObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,otherObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
							}
							r1.setPosition(myObject.getX(),myObject.getY());
							r2.setPosition(allObjects.get(i).getX(),allObjects.get(i).getY());
						}
						
						Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/bounce.wav"));
						if(ScreenAdapterManager.getInstance().game.soundEnabled && !ScreenAdapterManager.getInstance().game.muteAll){
							mp3Sound.play(ScreenAdapterManager.getInstance().game.volume);
						}
						
						//MAX VELOCITY WORKAROUND SO OBJECTS DONT GO WARP SPEED
					if(myObject.getVelocity()[0] > MAX_VELOCITY)
							myObject.setVelocity(new float[] {MAX_VELOCITY,myObject.getVelocity()[1]});
						if(myObject.getVelocity()[1] > MAX_VELOCITY)
							myObject.setVelocity(new float[] {myObject.getVelocity()[0],MAX_VELOCITY});
						if(allObjects.get(i).getVelocity()[0] > MAX_VELOCITY)
							allObjects.get(i).setVelocity(new float[] {MAX_VELOCITY,allObjects.get(i).getVelocity()[1]});
						if(allObjects.get(i).getVelocity()[1] > MAX_VELOCITY)
							allObjects.get(i).setVelocity(new float[] {allObjects.get(i).getVelocity()[0],MAX_VELOCITY});
						//int[] direction = direction(myObject.getX(),myObject.getY(),allObjects.get(i).getX(),allObjects.get(i).getY());
						
					}
				}
			}
		}
		
		public void setup(GameObject myObject){
			String[] newValues = myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).get(1).split(",");
			myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).set(1, newValues[0]);
			for(int i = 1; i < newValues.length;i++)
				myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).add(newValues[i]);
		}
		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(COLLIDESWITHOBJECTSID);
		}
		
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Elasticity constant");
			variableNames.add("Object ID's in a list, delimited by commas");
			return variableNames;
		}
		public String getXMLName(){return "collidesWithObjectsID";}
	},
	/**
	 * mass is used in collisions, if the object will ever have an object colliding with it, it MUST have this attribute
	 */
	MASS{

		@Override
		public Array<String> getVariableNames() {
			Array<String> varName = new Array<String>();
			varName.add("mass");
			return varName;
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(MASS);
		}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {}
		@Override
		public void setup(GameObject myObject) {}
		@Override
		public String getXMLName() {
			return "mass";
		}
		
	},
	/**
	 * The attached object will move on a set path with the same speed as it's initial velocity
	 */
	MOVESONSETPATH{
		
		@Override
		public Array<String> getVariableNames() {
			Array<String> varName = new Array<String>();
			return varName;
		}
		
		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			//get next point
			float tolerance = Float.parseFloat(myObject.getAttributeData().get(MOVESONSETPATH).get(0));
			int index = Integer.parseInt(myObject.getAttributeData().get(MOVESONSETPATH).get(1));
			float[] targetPoint = new float[] {Float.parseFloat(myObject.getAttributeData().get(MOVESONSETPATH).get((index*2)+2)),Float.parseFloat(myObject.getAttributeData().get(MOVESONSETPATH).get((index*2)+3))};
			if(distance(myObject.getX(),myObject.getY(),targetPoint[0],targetPoint[1]) < tolerance){//if close enough to next point
				index++;
				if(index*2+3 >= myObject.getAttributeData().get(MOVESONSETPATH).size) index = 0;
				myObject.getAttributeData().get(MOVESONSETPATH).set(1,index+"");
				float[] nextPoint = new float[] {Float.parseFloat(myObject.getAttributeData().get(MOVESONSETPATH).get((index*2)+2)),Float.parseFloat(myObject.getAttributeData().get(MOVESONSETPATH).get((index*2)+3))};
				float[] distance = new float[] {nextPoint[0]-myObject.getX(),nextPoint[1]-myObject.getY()};
				float magD = (float) Math.pow(Math.pow(distance[0], 2) + Math.pow(distance[1], 2), .5);//total distance to travel
				float magO = (float) Math.pow(Math.pow(myObject.getVelocity()[0], 2) + Math.pow(myObject.getVelocity()[1], 2),.5);//magnitude of the object's velocity
				float[] newVelocity = new float[] {distance[0]*magO/magD,distance[1]*magO/magD};
				myObject.setVelocity(newVelocity);
			}
			//if close enough to next point, setup a new point by calculating direction then setting velocity
		}
		private float distance(float x1, float y1, float x2, float y2){
			return (float) Math.pow(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2), .5);
		}
		
		@Override
		public void setup(GameObject myObject){//tolderance, current goal index, list of points
			String[] points = myObject.getAttributeData().get(MOVESONSETPATH).get(1).split(",");
			myObject.getAttributeData().get(MOVESONSETPATH).set(1,"0");//this is the "current point", 0,1,2,3,4,5,...
			//parse points into the array
			for(int i = 0;i<points.length;i++)
				myObject.getAttributeData().get(MOVESONSETPATH).add(points[i]);
			//get first point
			float[] firstPoint = new float[] {Float.parseFloat(points[0]),Float.parseFloat(points[1])};
			if(firstPoint[0] == myObject.getX() && firstPoint[1] == myObject.getY()) firstPoint = new float[] {Float.parseFloat(points[2]),Float.parseFloat(points[3])};//if the object is already on the first point, go to the next point(avoids 0/0 error)
			//calculate direction to that point
			float[] distance = new float[] {firstPoint[0]-myObject.getX(),firstPoint[1]-myObject.getY()};
			float magD = (float) Math.pow(Math.pow(distance[0], 2) + Math.pow(distance[1], 2), .5);//total distance to travel
			float magO = (float) Math.pow(Math.pow(myObject.getVelocity()[0], 2) + Math.pow(myObject.getVelocity()[1], 2),.5);//magnitude of the object's velocity
			//set velocity
			float[] newVelocity = new float[] {distance[0]*magO/magD,distance[1]*magO/magD};
			myObject.setVelocity(newVelocity);
		}
		
		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(MOVESONSETPATH);
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
		private boolean spawned = false;
		@Override
		public void setup(GameObject myObject) {
			
		}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			long currentTime = MinigameClock.getInstance().getLevelTimeInSeconds();
			float time = Float.parseFloat(myObject.getAttributeData().get(SPAWNOBJECTONTIMER).get(0));
			if(!myObject.isDisposed() && currentTime - time < 2.0 && !spawned) {
				Array<String> newListeners = myObject.getListenerNames();
				//System.out.println(newListeners.size);
				GameObject newObj = new GameObject(allObjects.size, myObject.getImageFilename(), 
						new float[] {myObject.getX()+300,myObject.getY()+300},
						newListeners, myObject.getAttributeData());
				allObjects.add(newObj);
				spawned=true;
			}
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			Array<String> values = myObject.getAttributeData().get(this);
			return values;
		}

		@Override
		public Array<String> getVariableNames() {
			Array<String> varNames = new Array<String>();
			varNames.add("Seconds Till is Spawns");
			return varNames;
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
		public void setup(GameObject myObject){
			String[] list = myObject.getAttributeData().get(DESTROYSOBJECTSOFIDONCOLLISION).get(0).split(",");
			myObject.getAttributeData().get(DESTROYSOBJECTSOFIDONCOLLISION).set(0, list[0]);
			for(int i = 1;i<list.length;i++)
				myObject.getAttributeData().get(DESTROYSOBJECTSOFIDONCOLLISION).add(list[i]);
		}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getTextureWidth(),myObject.getTextureHeight());
			for(int i = 0; i<allObjects.size;i++){
				GameObject currentObject = allObjects.get(i);
				if(myObject.getAttributeData().get(DESTROYSOBJECTSOFIDONCOLLISION).contains(currentObject.getID()+"", false)){
					Rectangle r2 = new Rectangle(currentObject.getX(),currentObject.getY(),currentObject.getTextureWidth(),currentObject.getTextureHeight());
					if(r1.overlaps(r2)){
						allObjects.get(i).dispose();
					}
				}
			}
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(DESTROYSOBJECTSOFIDONCOLLISION);
		}

		@Override
		public Array<String> getVariableNames(){
			Array<String> names = new Array<String>();
			names.add("TODO");
			return names;
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
		public void setup(GameObject myObject){}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			float rate = Float.parseFloat(myObject.getAttributeData().get(GRAVITY).get(0));
			myObject.setVelocity(new float[] {myObject.getVelocity()[0],myObject.getVelocity()[1]-rate});
		}

		@Override
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(GRAVITY);
		}

		@Override
		public Array<String> getVariableNames(){
			Array<String> val = new Array<String>();
			val.add("Rate at which object falls");
			return val;
		}

		@Override
		public String getXMLName() {return "gravity";}
		
	},
	/**
	 * Causes objects to collide with and bounce off of the top and bottom of the screen, collisions are perfectly ellastic
	 */
	BOUNCEOFFTOPANDBOTTOM{
		public void update(GameObject myObject,Array<GameObject> allObjects){
			if(myObject.getY() <= 0){//bottom
				float[] temp = myObject.getVelocity();
				temp[1] = Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}if(myObject.getY() + myObject.getTextureHeight() >= Gdx.graphics.getHeight()){//top
				float[] temp = myObject.getVelocity();
				temp[1] = -Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}
		}
		public Array<String> getVariableNames(){
			return new Array<String>();
		}
		public void setup(GameObject myObject){}//doesnt need to setup anything		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(BOUNCEOFFTOPANDBOTTOM);
		}
		public String getXMLName(){return "bounceOffTopAndBottom";}		
	},
	/**
	 * The attached Object will attempt to math the y position of the other object, it will move towards the object at the predefined speed
	 */
	FOLLOWSOBJECTY{
		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			int ID = Integer.parseInt(myObject.getAttributeData().get(FOLLOWSOBJECTY).get(0));
			float velocity = Float.parseFloat(myObject.getAttributeData().get(FOLLOWSOBJECTY).get(1));
			//find the object we want to follow
			for(int i = 0; i<allObjects.size;i++){
				if(allObjects.get(i).getID() == ID){
					//object found, now decide which way to go
					if(allObjects.get(i).getY()+(allObjects.get(i).getTextureHeight()/2) > myObject.getY() + (myObject.getTextureHeight()/2))
						myObject.setVelocity(new float[] {myObject.getVelocity()[0],velocity});
					else
						myObject.setVelocity(new float[] {myObject.getVelocity()[0],-velocity});
				}
			}
			
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(FOLLOWSOBJECTY);
		}

		@Override
		public Array<String> getVariableNames() {
			Array<String> vals = new Array<String>();
			vals.add("ID of object to follow");
			vals.add("speed at which to follow object");
			return vals;
		}

		@Override
		public String getXMLName() {
			return "followsObjectY";
		}
		
		@Override
		public void setup(GameObject myObject){}
	},
	/**
	 * The attached object will collide with the listed objects, however it will not cause them to move, only itself
	 */
	COLLIDESWITHOBJECTSID_SELF{
		static final float MAX_VELOCITY = 400;
		static final float COLLISION_CONSTANT = 5;//5
		static final float COLLISION_OFFSET = 1;//1
		public void update(GameObject myObject,Array<GameObject> allObjects){
			Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
			for(int i =0; i < allObjects.size;i++){
				if(myObject.getID() != allObjects.get(i).getID() && myObject.getAttributeData().get(COLLIDESWITHOBJECTSID_SELF).contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
					Rectangle r2 = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
					if(r1.overlaps(r2)){
						//collision has been detected, getting needed information for the collision equation(using momentum)
						
						//float c2 = allObjects.get(i).getAttributeData();
						//float m1 = 1f;//Float.parseFloat(myObject.getAttributeData().get(MASS).get(0));
						float m2 = 1f;//Float.parseFloat(allObjects.get(i).getAttributeData().get(MASS).get(0));
						float v1ix = myObject.getVelocity()[0];
						float v2ix = allObjects.get(i).getVelocity()[0];
						float v1iy = myObject.getVelocity()[1];
						float v2iy = allObjects.get(i).getVelocity()[1];
						
						float[] myObjectVelocity = new float[] {m2*v2ix - v1ix,m2*v2iy + v1iy};
						myObject.setVelocity(myObjectVelocity);
						float mag1 =(float) Math.pow(myObjectVelocity[0]*myObjectVelocity[0] + myObjectVelocity[1]*myObjectVelocity[1],.5);
						float[] myObjectDirection = {myObjectVelocity[0]/mag1,myObjectVelocity[1]/mag1};

						//new float[] {-1*myObjectVelocity[0],-1*myObjectVelocity[1]};
						//float[] otherObjectVelocity = new float[] {-1*myObjectVelocity[0],-1*myObjectVelocity[1]};//new float[] {c1*((2*m1*v1ix+(m1-m2)*v2ix)/(m1+m2))*.9f,c1*(2*m1*v1iy+(m1-m2)*v2iy/(m1+m2))*.9f};
						//float[] otherObjectDirection = {otherObjectVelocity[0]/mag2,otherObjectVelocity[1]/mag2};
						
						
						//this loop will make sure the objects aren't overlapping after collision has occured, will likely remove loop and change code if time allows.
						while(r1.overlaps(r2)){
							myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,myObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
							r1.setPosition(myObject.getX(),myObject.getY());
						}
						
						Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/bounce.wav"));
						if(ScreenAdapterManager.getInstance().game.soundEnabled && !ScreenAdapterManager.getInstance().game.muteAll){
							mp3Sound.play(ScreenAdapterManager.getInstance().game.volume);
						}
						
						//MAX VELOCITY WORKAROUND SO OBJECTS DONT GO WARP SPEED
					if(myObject.getVelocity()[0] > MAX_VELOCITY)
							myObject.setVelocity(new float[] {MAX_VELOCITY,myObject.getVelocity()[1]});
						if(myObject.getVelocity()[1] > MAX_VELOCITY)
							myObject.setVelocity(new float[] {myObject.getVelocity()[0],MAX_VELOCITY});
						if(allObjects.get(i).getVelocity()[0] > MAX_VELOCITY)
							allObjects.get(i).setVelocity(new float[] {MAX_VELOCITY,allObjects.get(i).getVelocity()[1]});
						if(allObjects.get(i).getVelocity()[1] > MAX_VELOCITY)
							allObjects.get(i).setVelocity(new float[] {allObjects.get(i).getVelocity()[0],MAX_VELOCITY});
						//int[] direction = direction(myObject.getX(),myObject.getY(),allObjects.get(i).getX(),allObjects.get(i).getY());
						
					}
				}
			}
		}
	
	public void setup(GameObject myObject){
		String[] newValues = myObject.getAttributeData().get(COLLIDESWITHOBJECTSID_SELF).get(0).split(",");
		myObject.getAttributeData().get(COLLIDESWITHOBJECTSID_SELF).set(0, newValues[0]);
		for(int i = 1; i < newValues.length;i++)
			myObject.getAttributeData().get(COLLIDESWITHOBJECTSID_SELF).add(newValues[i]);
	}
	
	public Array<String> getValues(GameObject myObject){
		return myObject.getAttributeData().get(COLLIDESWITHOBJECTSID_SELF);
	}
	
	public Array<String> getVariableNames(){
		Array<String> variableNames = new Array<String>();
		variableNames.add("Object ID's in a list, delimited by commas");
		return variableNames;
	}
	public String getXMLName(){return "collidesWithObjectsID_self";}
	};
	
	/**
	 * @param 	A string(read in from the XML_Reader) to be converted into the associated Attribute
	 * @return	The Attribute associated with the given string
	 */
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
	
	/**
	 * Called once within the GameObject's constructor, used to setup initial values
	 * @param	myObject	the GameObject that currently holds this attribute
	 */
	public abstract void setup(GameObject myObject);
	/**
	 * Used to simulate the specific behavior for each game object, called each frame
	 * @param	myObject	the GameObject that currently holds the given attribute
	 * @param	allObjects	all of the gameObjects within the given level
	 */
	public abstract void update(GameObject myObject,Array<GameObject> allObjects);
	/**
	 * Used by XML_Writer, returns the values associated with the given GameObject's attribute
	 * @param	myObject	the GameObject that currently holds this attribute
	 * @return	A libGDX Array object containing the values to write
	 */
	public abstract Array<String> getValues(GameObject myObject);
	/**
	 * Used by the Editor to tell the user what values they need to input
	 * @return	A libGDX Array object containing descriptions of the values to setup
	 */
	public abstract Array<String> getVariableNames();
	/**
	 * Returns 	A string containing the name to write to the .xml file, this is used by the writer.
	 * @return	The string containing the name to write to the .xml file
	 */
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
}
