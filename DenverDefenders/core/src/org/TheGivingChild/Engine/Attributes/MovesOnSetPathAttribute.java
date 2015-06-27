package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

public class MovesOnSetPathAttribute extends Attribute {

	public MovesOnSetPathAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	@Override
	public void update(Level level) {
		/* REWRITE
		//get next point
		float tolerance = Float.parseFloat(args.get(0));
		int index = Integer.parseInt(args.get(1));
		float[] targetPoint = new float[] {Float.parseFloat(args.get((index*2)+2)),Float.parseFloat(args.get((index*2)+3))};
		if(distance(myObject.getX(),myObject.getY(),targetPoint[0],targetPoint[1]) < tolerance){//if close enough to next point
			index++;
			if(index*2+3 >= args.size) index = 0;
			args.set(1,index+"");
			float[] nextPoint = new float[] {Float.parseFloat(args.get((index*2)+2)),Float.parseFloat(args.get((index*2)+3))};
			float[] distance = new float[] {nextPoint[0]-myObject.getX(),nextPoint[1]-myObject.getY()};
			float magD = (float) Math.pow(Math.pow(distance[0], 2) + Math.pow(distance[1], 2), .5);//total distance to travel
			float magO = (float) Math.pow(Math.pow(myObject.getVelocity()[0], 2) + Math.pow(myObject.getVelocity()[1], 2),.5);//magnitude of the object's velocity
			float[] newVelocity = new float[] {distance[0]*magO/magD,distance[1]*magO/magD};
			myObject.setVelocity(newVelocity);
		}
		//if close enough to next point, setup a new point by calculating direction then setting velocity
		 */
	}
	/*
	private float distance(float x1, float y1, float x2, float y2){
		return (float) Math.pow(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2), .5);
	}
	*/
	
	@Override
	public void setup(GameObject myObject){
		super.setup(myObject);
		/* REWRITE
		//tolerance, current goal index, list of points
		String[] points = args.get(1).split(",");
		args.set(1,"0");//this is the "current point", 0,1,2,3,4,5,...
		//parse points into the array
		for(int i = 0;i<points.length;i++)
			args.add(points[i]);
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
		*/
	}
}
