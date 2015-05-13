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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public ArrayList<Point> getPath() {
		return path;
	}

	public void setPath(ArrayList<Point> path) {
		this.path = path;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	
}
