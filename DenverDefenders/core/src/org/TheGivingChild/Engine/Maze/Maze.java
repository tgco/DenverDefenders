package org.TheGivingChild.Engine.Maze;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;

// Reads the tiled map and creates the maze as 2d array/graph hybrid class.
public class Maze {
	// Array of vertex, entry is null if no road is there
	private Vertex[][] mazeArray;
	// Pixel size for tiles
	private int pixWidth, pixHeight;
	// Reference to base and minigame entries for easy access
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
	
	// Returns the vertex at thee given world coordinates
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
	
	// Debug console printing
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
