package org.TheGivingChild.Engine.Maze;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;

// Reads the tiled map and creates the maze as 2d array/graph object.
// Author: Walter Schlosser
public class Maze {
	// Array of vertex, entry is null if no road is there
	private Vertex[][] mazeArray;
	// Pixel size for tiles
	private int pixWidth, pixHeight;
	// Reference to base and minigame tiles for easy access without a full iteration over the map
	private Vertex heroHQTile;
	private Array<Vertex> minigameTiles;
	
	public Maze(TiledMap map) {
		minigameTiles = new Array<Vertex>();
		//width and height of tiles in pixels
		pixWidth = map.getProperties().get("tilewidth", Integer.class);
		pixHeight = map.getProperties().get("tileheight", Integer.class);
		// Get the walkable layer
		TiledMapTileLayer walkableLayer = (TiledMapTileLayer) map.getLayers().get("Road");
		mazeArray = new Vertex[walkableLayer.getHeight()][walkableLayer.getWidth()];
		// Fill array with tiles as vertices from the map layer
		for (int i = 0; i < walkableLayer.getHeight(); i++) {
			for (int j = 0; j < walkableLayer.getWidth(); j++) {
				// get cell, account for the coordinate differences between Tiled and libGDX
				Cell cell = walkableLayer.getCell(j, i);
				if (cell != null) {
					// Create vertex with correct world coords, account for the difference in array/getCell coordinates
					mazeArray[walkableLayer.getHeight() - 1 - i][j] = new Vertex(j*pixHeight, i*pixWidth);
					// Check if any special properties
					if (cell.getTile().getProperties().get("minigame") != null) {
						// Save reference for easy access
						minigameTiles.add(mazeArray[walkableLayer.getHeight() - 1 - i][j]);
					}
					if (cell.getTile().getProperties().get("heroHQ") != null) {
						// Save reference for easy access
						heroHQTile = mazeArray[walkableLayer.getHeight() - 1 - i][j];
					}
				}
			}
		}
	}
	
	// Returns the vertex at the given world coordinates
	public Vertex getTileAt(float x, float y) {
		// Find indices
		int col = (int) (x/pixWidth);
		int row = (int) (y/pixHeight);
		return mazeArray[(mazeArray.length) - 1 - row][col];
	}
	
	// Returns the tile in the given direction from the passed tile
	public Vertex getTileRelativeTo(Vertex v, Direction d) {
		// find indices
		int col = (int) (v.getX()/pixWidth);
		int row = (int) (v.getY()/pixWidth);
		// Shift indices
		switch(d) {
		case UP:
			++row;
			break;
		case DOWN:
			--row;
			break;
		case RIGHT:
			++col;
			break;
		case LEFT:
			--col;
			break;
		}
		// Check bounds and return
		if (row < mazeArray.length && col < mazeArray[0].length) {
			return mazeArray[(mazeArray.length) - 1 - row][col];
		}
		return null;
	}
	
	// Uses a Breadth First graph search to construct a BFS Tree with the parent pointers in each Vertex
	// Begins at the source and continues until the destination is found
	public void bfSearch(Vertex source, Vertex destination) {
		// Reinit discovered fields
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[0].length; j++) {
				if (mazeArray[i][j] != null)
					mazeArray[i][j].setDiscovered(false);
			}
		}
		// BFS queue
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(source);
		while (!queue.isEmpty()) {
			Vertex v = queue.poll();
			v.setDiscovered(true);
			// End if found destination
			if (v == destination) break;
			// Find cell coordinates
			int col = (int) (v.getX()/pixWidth);
			int row = (int) (v.getY()/pixWidth);
			// Flip y axis
			row = mazeArray.length - 1 - row;
			// Check four possible edges
			if (row - 1 > 0) {
				if (mazeArray[row - 1][col] != null && !mazeArray[row - 1][col].isDiscovered()) {
					// found a tile up
					mazeArray[row - 1][col].setParent(Direction.DOWN);
					queue.add(mazeArray[row - 1][col]);
				}
			}
			if (row + 1 < mazeArray.length) {
				if (mazeArray[row + 1][col] != null && !mazeArray[row + 1][col].isDiscovered()) {
					// found a tile down
					mazeArray[row + 1][col].setParent(Direction.UP);
					queue.add(mazeArray[row + 1][col]);
				}
			}
			if (col - 1 > 0) {
				if (mazeArray[row][col - 1] != null && !mazeArray[row][col - 1].isDiscovered()) {
					// Found a tile left
					mazeArray[row][col - 1].setParent(Direction.RIGHT);
					queue.add(mazeArray[row][col - 1]);
				}
			}
			if (col + 1 < mazeArray[0].length) {
				if (mazeArray[row][col + 1] != null && !mazeArray[row][col + 1].isDiscovered()) {
					// found a tile right
					mazeArray[row][col + 1].setParent(Direction.LEFT);
					queue.add(mazeArray[row][col + 1]);
				}
			}
		}
		
		// Destination found or entire maze searched
		return;
	}
	
	public int getPixWidth() {
		return pixWidth;
	}
	
	public int getPixHeight() {
		return pixHeight;
	}
	
	public Vertex getHeroHQTile() {
		return heroHQTile;
	}
	
	public Array<Vertex> getMinigameTiles() {
		return minigameTiles;
	}
	
	// Debug console printing to check that maze in Tiled has been converted correctly
	public void printMaze() {
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[i].length; j++) {
				if (mazeArray[i][j] == null)
					System.out.print(' ');
				else
					System.out.print('x');
			}
			System.out.print('\n');
		}
	}
	
}
