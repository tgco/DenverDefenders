package org.TheGivingChild.Engine.Maze;

import org.TheGivingChild.Engine.Maze.Movement.InputMoveModule;
import org.TheGivingChild.Engine.Maze.Movement.MoveModule;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// Main player class, handles movement/animation logic
public class PlayerSprite extends Sprite {
	
	private static final int ANIMATION_FRAMES = 3;
	// Player health
	private int health;
	// Walking animations
	private Animation walkD, walkR, walkU, walkL;
	private Animation currentWalkSequence;
	// Speed this char moves at
	private float speed;
	// Move logic object
	private MoveModule moveModule;

	public PlayerSprite(Texture drawTexture) {
		super(drawTexture);
		speed = 0;
		// Default health
		health = 3;
		// Default moving logic
		moveModule = new InputMoveModule();
	}

	// Delegates to the current logic module
	// Returns true if the player moves, false if wall collision
	public boolean move(Maze maze) {
		return moveModule.move(this, maze);
	}

	// Builds animation sequences from the asset manager
	public void buildAnimations(AssetManager manager, float animationSpeed) {
		// Build walk down animation
		Array<TextureRegion> downWalk = new Array<TextureRegion>();
		String format;
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			downWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkD = new Animation(animationSpeed, downWalk);

		// Build walk up animation
		Array<TextureRegion> upWalk = new Array<TextureRegion>();
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			upWalk.add(new TextureRegion(manager.get(format,Texture.class)));
		}
		walkU = new Animation(animationSpeed, upWalk);

		// Buld walk right animation
		Array<TextureRegion> rightWalk = new Array<TextureRegion>();
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			rightWalk.add(new TextureRegion(manager.get(format, Texture.class)));
		}
		walkR = new Animation(animationSpeed, rightWalk);

		// Build walk left animation
		Array<TextureRegion> leftWalk = new Array<TextureRegion>();
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
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
	
	public void setMoveModule(MoveModule module) {
		this.moveModule = module;
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
	
	// Delegates to move module
	public void setMoveDirection(Direction moveDirection) {
		moveModule.setMoveDirection(moveDirection);
	}

	public void setTargetDirection(Direction targetDirection) {
		moveModule.setTargetDirection(targetDirection);
	}

	public void setTarget(Vertex target) {
		moveModule.setTarget(target);
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
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_D_%d.png", i);
			manager.load(format,Texture.class);
		}

		// Up anim
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_U_%d.png", i);
			manager.load(format,Texture.class);
		}

		// Right anim
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_R_%d.png", i);
			manager.load(format, Texture.class);
		}

		// Left anim
		for (int i = 1; i <= ANIMATION_FRAMES; i++) {
			format = String.format("ObjectImages/temp_hero_L_%d.png", i);
			manager.load(format, Texture.class);
		}
	}
}
