package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.AbstractFloatPair;
import com.meros.playn.core.Animation;
import com.meros.playn.core.Collidable;
import com.meros.playn.core.Constants.Buttons;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.FloatPair;
import com.meros.playn.core.GameState;
import com.meros.playn.core.ImmutableFloatPair;
import com.meros.playn.core.Input;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Room;
import com.meros.playn.core.Sound;
import com.meros.playn.core.Time;
import com.meros.playn.core.UtilMethods;

public class Hero extends Entity {

	enum MovementState {
		AirRun, Fall, Jump, Run, Still
	}

	public enum RopeState {
		Attached, Dissapearing, Moving, Retracted,
	}

	static float AIR_ACCELERATION = 4.5f;
	static float AIR_DRAG = 0.98f;
	static int BLINKING_TICKS = 100;
	static float GRAVITY = 6.0f;
	static float GROUND_ACCELERATION = 6.5f;
	static float GROUND_DRAG = 0.97f;
	static float GROUND_STOP_VELOCITY = 4.5f;
	static float JUMP_GRAVITY = 3.0f;
	static float JUMP_VELOCITY = 160.0f;
	static float KILL_VELOCITY = 200.0f;
	static int MAX_DEATH_COIN_LIFETIME = 500;
	static int MIN_DEATH_COIN_LIFETIME = 300;
	static int ROPE_DISSAPPEAR_TICKS = 6;
	static float ROPE_MAX_ACCELERATION = 12.0f;
	static float ROPE_REST_LENGTH = 45.0f;

	static float ROPE_SPEED = 700.0f;
	static float ROPE_SPRING_CONSTANT = 0.6f;
	Animation mAnimationFall = new Animation("data/images/hero_fall.bmp", 1);
	Animation mAnimationHook = new Animation("data/images/hook.bmp", 1);
	Animation mAnimationHookParticle = new Animation(
			"data/images/particles.bmp");

	Animation mAnimationHurt = new Animation("data/images/hero_hurt.bmp", 1);

	Animation mAnimationJump = new Animation("data/images/hero_jump.bmp", 1);
	Animation mAnimationRope = new Animation("data/images/rope.bmp", 1);
	Animation mAnimationRun = new Animation("data/images/hero_run.bmp", 4);

	int mBlinkingTicksLeft = 0;
	Direction mFacingDirection = Direction.RIGHT;
	int mFrame = 0;
	Entity mHookedEntity = null;
	ImmutableFloatPair mHookedEntityOffset = new ImmutableFloatPair();
	boolean mJumpHeld = false;
	boolean mJumpPrepressed = false;
	MovementState mMovementState = MovementState.Still;
	boolean mOnGround = false;
	int mRopeDissapearCounter = 0;
	int mRopeMaxLenghth = 180;
	ImmutableFloatPair mRopePosition = new ImmutableFloatPair();
	RopeState mRopeState = RopeState.Retracted;

	FloatPair mRopeVelocity = new FloatPair();

	ImmutableFloatPair mySpawnPoint = new ImmutableFloatPair(-200, -200);

	boolean myImortal = false;
	boolean myIsDead = false;

	public Hero() {
		setSize(new ImmutableFloatPair(10.0f, 15.0f));
	}

	private void adjustRopeDirection(FloatPair aRopeDirection) {
		float bestScore = 0;
		ImmutableFloatPair bestDirection = new ImmutableFloatPair(aRopeDirection);

		for (int y = 0; y < mRoom.getHeightInTiles(); y++) {
			for (int x = 0; x < mRoom.getWidthInTiles(); x++) {
				if (mRoom.isHookable(x, y)) {
					float pixelX = x * mRoom.getTileWidth()
							+ mRoom.getTileWidth() / 2;
					float pixelY = y * mRoom.getTileHeight()
							+ mRoom.getTileHeight() / 2;
					ImmutableFloatPair tilePos = new ImmutableFloatPair(pixelX, pixelY);
					float score = getAutoAimScore(aRopeDirection, tilePos);

					if (score > bestScore) {
						ImmutableFloatPair direction = tilePos.subtract(getPosition());
						Room.OutInt rcX = mRoom.new OutInt();
						Room.OutInt rcY = mRoom.new OutInt();
						boolean rcHit = mRoom.rayCast(getPosition(), direction,
								false, rcX, rcY);
						if (rcHit && rcX.myInt == x && rcY.myInt == y) {
							bestScore = score;
							bestDirection = direction;
						}
					}
				}
			}
		}

		for (Entity e : mRoom.getEntities()) {
			if (!e.isDamagable() && !e.isHookable())
				continue;
			
			ImmutableFloatPair entityPos = new ImmutableFloatPair(e.getPosition());
			float score = getAutoAimScore(aRopeDirection, entityPos);

			if (score > bestScore) {
				ImmutableFloatPair direction = entityPos.subtract(getPosition());
				Room.OutInt rcX = mRoom.new OutInt();
				Room.OutInt rcY = mRoom.new OutInt();
				boolean rcHit = mRoom.rayCast(getPosition(), direction, false, rcX, rcY);
				ImmutableFloatPair rcTilePos = new ImmutableFloatPair(rcX.myInt * mRoom.getTileWidth()
						+ mRoom.getTileWidth() / 2, rcY.myInt
						* mRoom.getTileHeight() + mRoom.getTileHeight() / 2);

				if (!rcHit || aRopeDirection.lengthCompare(rcTilePos) < 0) {
					bestScore = score;
					bestDirection = direction;
				}
			}
		}

		aRopeDirection.set(bestDirection).normalize();
	}

	public void detachHook() {
		if (mRopeState != RopeState.Retracted
				&& mRopeState != RopeState.Dissapearing) {
			mRopeState = RopeState.Dissapearing;
			mRopeDissapearCounter = 0;
		}
		mHookedEntity = null;
	}

	private void die() {
		mRopeState = RopeState.Retracted;
		mHookedEntity = null;
		myIsDead = true;
		Sound.playSample("data/sounds/start");
	}

	@Override
	public void draw(Surface aBuffer, int offsetX, int offsetY, int layer) {
		int x = getDrawPositionX();
		int y = getDrawPositionY();

		if (myIsDead) {
			mAnimationHurt.drawFrame(aBuffer, 0,
					offsetX + x - mAnimationHurt.getFrameWidth() / 2,
					(int) (offsetY + y + getHalfSize().getY() - mAnimationHurt
							.getFrameHeight()),
							mFacingDirection == Direction.LEFT, false);
			return;
		}

		Animation animation = null;
		int frame = 1;

		// mMovementState = MovementState_Jump;

		switch (mMovementState) {
		case Run:
			animation = mAnimationRun;
			frame = mFrame / 10;
			break;
		case AirRun:
			animation = mAnimationRun;
			frame = mFrame / 3;
			break;
		case Still:
			animation = mAnimationRun;
			frame = 1;
			break;
		case Jump:
			animation = mAnimationJump;
			frame = 1;
			break;
		case Fall:
			animation = mAnimationFall;
			frame = 1;
			break;
		default:
			super.draw(aBuffer, offsetX, offsetY, layer);
		}

		if (mRopeState != RopeState.Retracted
				&& (mRopeState != RopeState.Dissapearing || (mRopeDissapearCounter & 1) != 0)) {
			int x2 = getDrawPositionX() + offsetX;
			int y2 = getDrawPositionY() + offsetY;
			int x1 = (int) (mRopePosition.getX() + 0.5f) + offsetX;
			int y1 = (int) (mRopePosition.getY() + 0.5f) + offsetY;

			ImmutableFloatPair line = new ImmutableFloatPair(x2 - x1, y2 - y1);
			int rlength = (int) line.length();
			line = line.divide(line.length());
			line = line.multiply(3);

			float rx = x1;
			float ry = y1;

			int segments = (int) Math.ceil(rlength / line.length());

			for (int i = 0; i < segments; i++) {
				ImmutableFloatPair wave = new ImmutableFloatPair();

				if ((mRopeState == RopeState.Moving || mRopeState == RopeState.Dissapearing)
						&& segments > 1) {
					float t = i / (float) (segments - 1);
					float value = (float) (Math.sin(t * 3.141592) * Math
							.sin(i * 0.3f));
					wave = new ImmutableFloatPair(line.getY(), -line.getX()).multiply(value);
				}

				mAnimationRope
				.drawFrame(aBuffer, frame,
						(int) (rx + wave.getX() - mAnimationRope
								.getFrameWidth() / 2), (int) (ry
										+ wave.getY() - mAnimationRope
										.getFrameHeight() / 2));

				rx += line.getX();
				ry += line.getY();
			}

			mAnimationHook.drawFrame(aBuffer, frame,
					x1 - mAnimationHook.getFrameWidth() / 2, y1
					- mAnimationHook.getFrameHeight() / 2);

		}

		if (mBlinkingTicksLeft > 0 && !mOnGround) {
			animation = mAnimationHurt;
		}

		if ((mBlinkingTicksLeft / 6) % 2 == 0) {
			animation.drawFrame(aBuffer, frame,
					offsetX + x - animation.getFrameWidth() / 2,
					(int) (offsetY + y + getHalfSize().getY() - animation
							.getFrameHeight()),
							mFacingDirection == Direction.LEFT, false);
		}
	}

	private float getAutoAimScore(AbstractFloatPair aRopeDirection, AbstractFloatPair aAutoAimPos) {
		ImmutableFloatPair playerToTile = new ImmutableFloatPair(aAutoAimPos).subtract(getPosition());
		float dotValue = aRopeDirection.dot(playerToTile);
		if (dotValue < 0) {
			return -1;
		}
		float distance = playerToTile.length();
		dotValue /= distance;
		if (dotValue < 0.80) {
			return -1;
		}
		return (float) (Math.pow(dotValue, 10.0f) / distance);
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public ImmutableFloatPair getRopePosition() {
		return mRopePosition;
	}

	public RopeState getRopeState() {
		return mRopeState;
	}

	public boolean gotCoin() {
		if (mBlinkingTicksLeft == 0) {
			GameState.put("coins", GameState.getInt("coins") + 1);
			return true;
		}

		return false;
	}

	public boolean gotCore() {
		if (mBlinkingTicksLeft == 0) {
			GameState.put("coins", GameState.getInt("coins") + 1);
			return true;
		}

		return false;
	}

	public void imortal() {
		myImortal = true;
	}

	public boolean isDead() {
		return myIsDead;
	}

	public void kill() {
		if (myImortal)
			return;

		if (mBlinkingTicksLeft == 0) {
			PlayerSkill.playerDidSomethingStupid(0.0f, 0.1f);
			detachHook();

			int coins = GameState.getInt("coins");
			GameState.put("coins", 0);

			if (coins > 0) {
				coins /= 2;
				coins = Math.min(20, Math.max(0, coins));
				int lifetime = UtilMethods.lerp(MAX_DEATH_COIN_LIFETIME,
						MIN_DEATH_COIN_LIFETIME, PlayerSkill.get());
				Coin.SpawnDeathCoins(coins, getPosition(), lifetime, mRoom);
				mBlinkingTicksLeft = BLINKING_TICKS;
				mVelocity.normalize().multiply(-KILL_VELOCITY);
				Sound.playSample("data/sounds/hurt");
			} else {
				PlayerSkill.playerDidSomethingStupid(0.0f, 0.25f);
				die();
			}
		}
	}

	@Override
	public void remove() {
		die();
	}

	public void respawn() {
		setPosition(mySpawnPoint);
		setVelocity(new ImmutableFloatPair(0, 0));
		myIsDead = false;
		GameState.put("coins", 0);
		mOnGround = false;
		mJumpHeld = false;
		mJumpPrepressed = false;
		mFrame = 0;
		mRopeState = RopeState.Retracted;
		mHookedEntity = null;
		mFacingDirection = Direction.RIGHT;
		mBlinkingTicksLeft = 0;
		mRopeDissapearCounter = 0;

	}

	@Override
	public void update() {
		if (myIsDead)
			return;

		super.update();

		if (mySpawnPoint.getX() < -100 && mySpawnPoint.getY() < -100) {
			mySpawnPoint = new ImmutableFloatPair(getPosition());
		}

		if (mRopeState == RopeState.Dissapearing) {
			mRopeDissapearCounter++;
			if (mRopeDissapearCounter >= ROPE_DISSAPPEAR_TICKS) {
				mRopeState = RopeState.Retracted;
			}
		}

		float acceleration = mOnGround ? GROUND_ACCELERATION : AIR_ACCELERATION;
		boolean airRunning = false;

		if (Input.isHeld(Buttons.Left)) {
			mVelocity.subtract(acceleration, 0);

			if (mRopeState == RopeState.Attached && !mOnGround) {
				mFacingDirection = Direction.LEFT;
				airRunning = true;
				mMovementState = MovementState.AirRun;
			}
		}

		if (Input.isHeld(Buttons.Right)) {
			mVelocity.add(acceleration, 0);

			if (mRopeState == RopeState.Attached && !mOnGround) {
				mFacingDirection = Direction.RIGHT;
				airRunning = true;
				mMovementState = MovementState.AirRun;
			}
		}

		if (Input.isPressed(Buttons.Jump)) {
			if (!mJumpPrepressed && mOnGround)
				Sound.playSample("data/sounds/jump");
			mJumpPrepressed = true;
		}

		if (mOnGround && mJumpPrepressed) {
			mVelocity.subtract(0, JUMP_VELOCITY);

			if (mRopeState != RopeState.Attached) {
				mJumpHeld = true;
			}
			mJumpPrepressed = false;
		}

		if (Input.isReleased(Buttons.Jump)) {
			if (mJumpHeld && mVelocity.getY() < 0) {
				mVelocity.set(mVelocity.getX(), mVelocity.getY()*0.5f);
			}

			mJumpHeld = false;
			mJumpPrepressed = false;
		}

		if (Input.isReleased(Buttons.Fire)) {
			detachHook();
		}

		if (mVelocity.getY() >= 0) {
			mJumpHeld = false;
		}

		if (!airRunning) {
			mMovementState = MovementState.Still;
		}

		if (mOnGround) {
			if (mVelocity.getX() > 0) {
				mFacingDirection = Direction.RIGHT;
			} else if (mVelocity.getX() < 0) {
				mFacingDirection = Direction.LEFT;
			}

			if (Math.abs(mVelocity.getX()) > GROUND_STOP_VELOCITY) {
				mMovementState = MovementState.Run;
			}
		} else if (!airRunning) {
			if (mVelocity.getY() > 0)
				mMovementState = MovementState.Jump;
			else
				mMovementState = MovementState.Fall;
		}

		if (mMovementState == MovementState.Still) {
			mVelocity.set(0, mVelocity.getY());
		}

		if (Input.isPressed(Buttons.Fire)) {
			Sound.playSample("data/sounds/rope");
			mRopeState = RopeState.Moving;
			mRopePosition = new ImmutableFloatPair(getPosition());
			mRopeVelocity.set(0,0);

			if (Input.isHeld(Buttons.Left)) {
				mRopeVelocity.subtract(1, 0);
			}

			if (Input.isHeld(Buttons.Right)) {
				mRopeVelocity.add(1, 0);
			}

			if (Input.isHeld(Buttons.Up)) {
				mRopeVelocity.subtract(0, 1);
			}

			if (Input.isHeld(Buttons.Down)) {
				mRopeVelocity.add(0, 1);
			}

			if (mRopeVelocity.isZero()) {
				mRopeVelocity.set((mFacingDirection == Direction.LEFT ? -1 : 1), mRopeVelocity.getY());
			}
			
			mRopeVelocity.normalize().multiply(ROPE_SPEED).add(mVelocity).normalize();

			adjustRopeDirection(mRopeVelocity);
			
			mRopeVelocity.multiply(ROPE_SPEED);

			if (mRopeVelocity.getX() < 0) {
				mFacingDirection = Direction.LEFT;
			} else if (mRopeVelocity.getX() > 0) {
				mFacingDirection = Direction.RIGHT;
			}
		}

		if (mRopeState == RopeState.Moving) {
			int substeps = 25;
			for (int i = 0; i < substeps; i++) {
				mRopePosition = mRopePosition.add(new ImmutableFloatPair(mRopeVelocity).divide(substeps
						* Time.TicksPerSecond));
				int tileX = (int) (mRopePosition.getX() / mRoom.getTileWidth());
				int tileY = (int) (mRopePosition.getY() / mRoom.getTileHeight());
				if (mRoom.isHookable(tileX, tileY)) {
					// Sound::stopSample("data/sounds/rope");
					Sound.playSample("data/sounds/hook");
					mRopeState = RopeState.Attached;
					mJumpHeld = false;

					ParticleSystem particleSystem = new ParticleSystem(
							mAnimationHookParticle, 2, 30, 10, 1, 50, 10,
							mRopeVelocity.normalize().multiply(-10),
							(float) 1.0);
					particleSystem.setPosition(mRopePosition);
					mRoom.addEntity(particleSystem);

					break;
				}

				if (mRopePosition.subtract(getPosition()).length() > mRopeMaxLenghth) {
					detachHook();
					Sound.playSample("data/sounds/no_hook");
					break;
				}

				if (mRoom.isCollidable(tileX, tileY)) {
					detachHook();
					Sound.playSample("data/sounds/no_hook");
					break;
				}

				if (mRoom.damageDone((int) (mRopePosition.getX()),
						(int) (mRopePosition.getY()))) {
					detachHook();
					break;
				}

				mHookedEntity = mRoom.findHookableEntity(mRopePosition);
				if (mHookedEntity != null) {
					Sound.playSample("data/sounds/hook");
					mRopeState = RopeState.Attached;
					mJumpHeld = false;
					mHookedEntityOffset = mRopePosition.subtract(mHookedEntity
							.getPosition());
					mHookedEntityOffset = new ImmutableFloatPair(
							(float) Math
							.floor(mHookedEntityOffset.getX()), 
							(float) Math
							.floor(mHookedEntityOffset.getY()));
				}
			}
		}

		if (mRopeState == RopeState.Attached) {
			if (mHookedEntity != null) {
				mRopePosition = new ImmutableFloatPair(mHookedEntity.getPosition()).add(
						mHookedEntityOffset);
			}

			ImmutableFloatPair ropeToHero = new ImmutableFloatPair(getPosition()).subtract(mRopePosition);
			if (ropeToHero.lengthCompare(ROPE_REST_LENGTH) > 0) {
				ImmutableFloatPair ropeRestPoint = mRopePosition.add(ropeToHero.normalize()
						.multiply(ROPE_REST_LENGTH));
				ImmutableFloatPair heroToRestPoint = ropeRestPoint.subtract(getPosition());
				ImmutableFloatPair ropeAcceleration = heroToRestPoint
						.multiply(ROPE_SPRING_CONSTANT);
				if (ropeAcceleration.lengthCompare(ROPE_MAX_ACCELERATION) > 0) {
					ropeAcceleration = ropeAcceleration.normalize().multiply(
							ROPE_MAX_ACCELERATION);
				}
				mVelocity.add(ropeAcceleration);
			}

			if (Input.isHeld(Buttons.Up)) {
				mVelocity.subtract(0, acceleration);
			}

			if (Input.isHeld(Buttons.Down)) {
				mVelocity.add(0, acceleration);
			}

			int ropeTileX = (int) (mRopePosition.getX() / mRoom.getTileWidth());
			int ropeTileY = (int) (mRopePosition.getY() / mRoom.getTileWidth());
			if (mHookedEntity == null
					&& !mRoom.isHookable(ropeTileX, ropeTileY)) {
				detachHook();
			}
		}

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
			mVelocity.set(0, mVelocity.getY());
		}

		if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
			mVelocity.set(mVelocity.getX(), 0);
		}

		float gravity = mJumpHeld ? JUMP_GRAVITY : GRAVITY;
		mVelocity.add(0, gravity);
		boolean ground = bumps.contains(Direction.DOWN);
		if (ground && !mOnGround && mRopeState != RopeState.Attached) {
			//TODO: buggy sound! Sound.playSample("data/sounds/land");
		}
		mOnGround = ground;

		float drag = mOnGround ? GROUND_DRAG : AIR_DRAG;
		mVelocity.multiply(drag);

		mFrame++;

		if (mBlinkingTicksLeft > 0) {
			mBlinkingTicksLeft--;
		}
	}

	public void setLastSpawnPoint(AbstractFloatPair aSpawnPoint) {
		mySpawnPoint = new ImmutableFloatPair(aSpawnPoint);
	}

	public boolean hasHook() {
		return mRopeState == RopeState.Attached;
	}
	
	private class HookCollidable implements Collidable
	{
		public float myLeft;
		public float myTop;
		public float myRight;
		public float myBottom;

		@Override
		public float getCollideTop() {
			return myTop;
		}

		@Override
		public float getCollideLeft() {
			return myLeft;
		}

		@Override
		public float getCollideBottom() {
			return myBottom;
		}

		@Override
		public float getCollideRight() {
			return myRight;
		}
	}
	
	private HookCollidable myHookCollidable = new HookCollidable();
	public Collidable getHookCollidable() {
		myHookCollidable.myLeft = mRopePosition.getX() - 2;
		myHookCollidable.myRight = mRopePosition.getX() + 2;
		myHookCollidable.myTop = mRopePosition.getY() - 2;
		myHookCollidable.myBottom = mRopePosition.getY() + 2;

		return myHookCollidable;
	}

	public Entity getHookedEntity() {
		return mHookedEntity;
	}
}
