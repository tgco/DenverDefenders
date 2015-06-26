package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class MovesOnSetPathAttribute extends Attribute {
	@Override
	public Array<String> getVariableNames() {
		Array<String> varName = new Array<String>();
		return varName;
	}
	
	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {
		//get next point
		float tolerance = Float.parseFloat(data.get(0));
		int index = Integer.parseInt(data.get(1));
		float[] targetPoint = new float[] {Float.parseFloat(data.get((index*2)+2)),Float.parseFloat(data.get((index*2)+3))};
		if(distance(myObject.getX(),myObject.getY(),targetPoint[0],targetPoint[1]) < tolerance){//if close enough to next point
			index++;
			if(index*2+3 >= data.size) index = 0;
			data.set(1,index+"");
			float[] nextPoint = new float[] {Float.parseFloat(data.get((index*2)+2)),Float.parseFloat(data.get((index*2)+3))};
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
		String[] points = data.get(1).split(",");
		data.set(1,"0");//this is the "current point", 0,1,2,3,4,5,...
		//parse points into the array
		for(int i = 0;i<points.length;i++)
			data.add(points[i]);
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
}
