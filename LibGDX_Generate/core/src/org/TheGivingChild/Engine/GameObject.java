package org.TheGivingChild.Engine;

import java.awt.Point;
import java.util.ArrayList;

public class GameObject {
	private int health;
	private ArrayList<Point> path = new ArrayList<Point>();
	private double speed;
	
	public GameObject(int hp, ArrayList<Point> p, double s){
		health = hp;
		path.addAll(p);
		speed = s;
		
	}
	
	
}
