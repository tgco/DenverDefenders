package org.TheGivingChild.Engine.Maze;

// Direction enum for where to move in the maze
public enum Direction {
	UP {
		@Override
		public Direction opposite() {
			return DOWN;
		}
	},
	DOWN {
		@Override
		public Direction opposite() {
			return UP;
		}
	},
	LEFT {
		@Override
		public Direction opposite() {
			return RIGHT;
		}
	},
	RIGHT {
		@Override
		public Direction opposite() {
			return LEFT;
		}
	};
	
	public abstract Direction opposite();
}
