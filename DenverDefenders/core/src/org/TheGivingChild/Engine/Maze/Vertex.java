package org.TheGivingChild.Engine.Maze;

// Graph vertex used to store parent field for BFS and a property for minigame tile, etc
public class Vertex {
	// parent field for BFS tree
	private Direction parent;
	// discovered field for BFS
	private boolean discovered;
	// true if this vertex has a kid on it
	private boolean occupied;
	// The world coordinates of this vertex
	private float x, y;
	
	public Vertex(float x, float y) {
		parent = null;
		occupied = false;
		discovered = false;
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean b) {
		this.occupied = b;
	}
	
	public Direction getParent() {
		return parent;
	}
	
	public void setParent(Direction d) {
		this.parent = d;
	}
	
	public boolean isDiscovered() {
		return discovered;
	}
	
	public void setDiscovered(boolean b) {
		this.discovered = b;
	}
}
