package org.TheGivingChild.Engine.Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// Main player class, handles movement/animation logic
public class PlayerSprite extends Sprite {
	// Player health
	private int health;
	// Walking animations
	private Animation walkD, walkR, walkU, walkL;
	private Animation currentWalkSequence;
	// The direction the player is moving and the direction the player wants to move
	private Direction moveDirection;
	private Direction targetDirection;
	// The target tile the character is moving to
	private Vertex target;
	// Speed this char moves at
	private float speed;

	public PlayerSprite(Texture drawTexture) {
		super(drawTexture);
		speed = 0;
		// Default health
		health = 3;
	}

	// Determines how to move the player.
	// Returns true if the player moves, false if wall collision
	public boolean move(Maze maze) {
		boolean moved = false;
		// Calculate amount to move in a frame
		float spriteMoveX = 0;
		float spriteMoveY = 0;
		float delta = 0;
		boolean reached = false;
		switch(moveDirection) {
		case UP:
			spriteMoveY = speed*Gdx.graphics.getDeltaTime();
			delta = target.getY() - getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case DOWN:
			spriteMoveY = -speed*Gdx.graphics.getDeltaTime();
			delta = target.getY() - getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case RIGHT:
			spriteMoveX = speed*Gdx.graphics.getDeltaTime();
			delta = target.getX() - getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		case LEFT:
			spriteMoveX = -speed*Gdx.graphics.getDeltaTime();
			delta = target.getX() - getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		}

		// Update position
		if (reached) {
			// New direction check
			if (targetDirection != moveDirection) {
				Vertex next = maze.getTileRelativeTo(target, targetDirection);
				if (next != null) {
					// snap to target and move in new direction
					setPosition(target.getX(), target.getY());
					target = next;
					moveDirection = targetDirection;
					setCurrentWalkSequence(targetDirection);
				} else {
					// can't go in target direction yet, continue in move direction
					next = maze.getTileRelativeTo(target, moveDirection);
					if (next != null) {
						target = next;
						setPosition(getX() + spriteMoveX, getY() + spriteMoveY);
						moved = true;
					} else {
						// No where to go, snap to target
						setPosition(target.getX(), target.getY());
					}
				}
			} else {
				// REFACTOR THESE IF ELSE BLOCKS, CODE IS COPIED BETWEEN THEM
				// Reached target, try to continue in same direction or just snap to target
				Vertex next = maze.getTileRelativeTo(target, moveDirection);
				if (next != null) {
					target = next;
					setPosition(getX() + spriteMoveX, getY() + spriteMoveY);
					moved = true;
				} else {
					// No where to go, snap to target
					setPosition(target.getX(), target.getY());
				}
			}
		} else {
			// Haven't reached target yet, move toward it
			moved = true;
			setPosition(getX() + spriteMoveX, getY() + spriteMoveY);
		}

		return moved;
	}

	// Builds animation sequences from the asset manager
	public void buildAnimations(AssetManager manager, float animationSpeed) {
		// Build walk down animation
		Array<TextureRegion> downWalk = new Array<TextureRegion>();
		String format;
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			downWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkD = new Animation(animationSpeed, downWalk);

		// Build walk up animation
		Array<TextureRegion> upWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			upWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkU = new Animation(animationSpeed, upWalk);

		// Buld walk right animation
		Array<TextureRegion> rightWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			rightWalk.add(new TextureRegion(manager.get(format, Texture.class)));
		}
		walkR = new Animation(animationSpeed, rightWalk);

		// Build walk left animation
		Array<TextureRegion> leftWalk = new Array<TextureRegion>();
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_L_%d.png", i);
			leftWalk.add(new TextureRegion(manager.get(format, Texture.class)));
		}
		walkL = new Animation(animationSpeed, leftWalk);

	}

	// Progresses the current animation
	public void animationUpdate(float globalTime, boolean loop) {
		TextureRegion frame = currentWalkSequence.getKeyFrame(globalTime, loop);
		setTexture(frame.getTexture());
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	// Sets the correct animation based on the direction
	public void setCurrentWalkSequence(Direction dir) {
		switch (dir) {
		case UP:
			currentWalkSequence = walkU;
			break;
		case DOWN:
			currentWalkSequence = walkD;
			break;
		case RIGHT:
			currentWalkSequence = walkR;
			break;
		case LEFT:
			currentWalkSequence = walkL;
			break;
		}
	}

	public void setMoveDirection(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	public void setTargetDirection(Direction targetDirection) {
		this.targetDirection = targetDirection;
	}

	public void setTarget(Vertex target) {
		this.target = target;
	}
	/**
	 * returns the scaled width.
	 */
	@Override
	public float getWidth() {
		return super.getWidth()*getScaleX();
	}
	/**
	 * returns the scaled height.
	 */
	@Override
	public float getHeight() {
		return super.getHeight()*getScaleY();
	}
	
	// Requests the textures needed for anims to be loaded
	public static void requestAssets(AssetManager manager) {
		String format;
		// Down anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			manager.load(format,Texture.class);
		}

		// Up anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			manager.load(format,Texture.class);
		}

		// Right anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			manager.load(format, Texture.class);
		}

		// Left anim
		for (int i = 1; i <= 8; i++) {
			format = String.format("ObjectImages/temp_hero_L_%d.png", i);
			manager.load(format, Texture.class);
		}
	}
}
