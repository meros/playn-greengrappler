package com.meros.playn.core;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import playn.core.Canvas;

import com.meros.playn.core.Constants.Buttons;
import com.meros.playn.core.Constants.Direction;

public class Hero extends Entity {

	static float GROUND_ACCELERATION	= 6.5f;
	static float GROUND_DRAG			= 0.97f;
	static float GROUND_STOP_VELOCITY	= 4.5f;
	static float AIR_ACCELERATION		= 4.5f;
	static float AIR_DRAG				= 0.98f;
	static float JUMP_GRAVITY			= 3.0f;
	static float GRAVITY				= 6.0f;
	static float JUMP_VELOCITY		= 160.0f;
	static float ROPE_SPEED			= 700.0f;
	static float ROPE_REST_LENGTH		= 45.0f;
	static float ROPE_MAX_ACCELERATION= 12.0f;
	static float ROPE_SPRING_CONSTANT = 0.6f;
	static int   ROPE_DISSAPPEAR_TICKS= 6;
	static int   BLINKING_TICKS		= 100;
	static float KILL_VELOCITY		= 200.0f;
	static int   MIN_DEATH_COIN_LIFETIME	= 300;
	static int   MAX_DEATH_COIN_LIFETIME	= 500;


	int	 mBlinkingTicksLeft = 0;
	boolean mOnGround = false;
	boolean mJumpHeld = false;
	boolean mJumpPrepressed = false;
	int mFrame = 0;
	enum RopeState {
		Retracted,
		Moving,
		Attached,
		Dissapearing,
	}
	RopeState mRopeState = RopeState.Retracted;
	float2 mRopePosition = new float2();
	float2 mRopeVelocity = new float2();

	Direction mFacingDirection = Direction.RIGHT;
	int mRopeDissapearCounter = 0;
	Animation mAnimationRun = new Animation("data/images/hero_run.bmp", 4);
	Animation mAnimationJump = new Animation("data/images/hero_jump.bmp", 1);
	Animation mAnimationFall = new Animation("data/images/hero_fall.bmp", 1);
	Animation mAnimationRope = new Animation("data/images/rope.bmp", 1);
	Animation mAnimationHook = new Animation("data/images/hook.bmp", 1);
	Animation mAnimationHurt = new Animation("data/images/hero_hurt.bmp", 1);
	Animation mAnimationHookParticle = new Animation("data/images/particles.bmp");
	float2 mSpawnPoint = new float2(-200,-200);
	int mRopeMaxLenghth = 180;
	boolean myIsDead = false;
	boolean myImortal = false;

	enum MovementState
	{
		Still,
		Run,
		AirRun,
		Jump,
		Fall
	}

	MovementState mMovementState = MovementState.Still;

	Entity mHookedEntity = null;
	float2 mHookedEntityOffset = new float2();

	public Hero()
	{
		mSize = new float2(10.0f, 15.0f);
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public void remove()
	{
		die();
	}

	@Override
	public void update()
	{
		if (myIsDead)
			return;

		super.update();

		if (mSpawnPoint.x < -100 && mSpawnPoint.y < -100)
		{
			mSpawnPoint = getPosition();
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
			mVelocity.x -= acceleration;

			if (mRopeState == RopeState.Attached && !mOnGround) {
				mFacingDirection = Direction.LEFT;
				airRunning = true;
				mMovementState = MovementState.AirRun;
			}
		}

		if (Input.isHeld(Buttons.Right)) {
			mVelocity.x += acceleration;

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
			mVelocity.y = -JUMP_VELOCITY;
			if (mRopeState != RopeState.Attached) {
				mJumpHeld = true;
			}
			mJumpPrepressed = false;
		}

		if (Input.isReleased(Buttons.Jump)) {
			if (mJumpHeld && mVelocity.y < 0) {
				mVelocity.y *= 0.5f;
			}

			mJumpHeld = false;
			mJumpPrepressed = false;
		}

		if (Input.isReleased(Buttons.Fire)) {
			detachHook();
		}

		if (mVelocity.y >= 0) {
			mJumpHeld = false;
		}

		if (!airRunning) {
			mMovementState = MovementState.Still;
		}

		if (mOnGround) {
			if (mVelocity.x > 0) {
				mFacingDirection = Direction.RIGHT;
			} else if (mVelocity.x < 0) {
				mFacingDirection = Direction.LEFT;
			}

			if (Math.abs(mVelocity.x) > GROUND_STOP_VELOCITY)
			{
				mMovementState = MovementState.Run;
			}
		}
		else if (!airRunning)
		{
			if (mVelocity.y > 0)
				mMovementState = MovementState.Jump;
			else
				mMovementState = MovementState.Fall;
		}

		if (mMovementState == MovementState.Still)
		{
			mVelocity.x = 0;
		}

		if (Input.isPressed(Buttons.Fire)) {
			Sound.playSample("data/sounds/rope");
			mRopeState = RopeState.Moving;
			mRopePosition = mPosition;
			mRopeVelocity = new float2();

			if (Input.isHeld(Buttons.Left)) {
				mRopeVelocity.x -= 1;
			}

			if (Input.isHeld(Buttons.Right)) {
				mRopeVelocity.x += 1;
			}

			if (Input.isHeld(Buttons.Up)) {
				mRopeVelocity.y -= 1;
			}

			if (Input.isHeld(Buttons.Down)) {
				mRopeVelocity.y += 1;
			}

			if (mRopeVelocity.isZero()) {
				mRopeVelocity.x = (mFacingDirection == Direction.LEFT ? -1 : 1);
			}

			mRopeVelocity = adjustRopeDirection((mVelocity.add(mRopeVelocity.normalize().multiply(ROPE_SPEED))).normalize()).multiply(ROPE_SPEED);

			if (mRopeVelocity.x < 0) {
				mFacingDirection = Direction.LEFT;
			} else if (mRopeVelocity.x > 0) {
				mFacingDirection = Direction.RIGHT;
			}
		}

		if (mRopeState == RopeState.Moving) {
			int substeps = 25;
			for (int i = 0; i < substeps; i++) {
				mRopePosition = mRopePosition.add(mRopeVelocity.divide(substeps * Time.TicksPerSecond));
				int tileX = (int)(mRopePosition.x / mRoom.getTileWidth());
				int tileY = (int)(mRopePosition.y / mRoom.getTileHeight());
				if (mRoom.isHookable(tileX, tileY)) {
					//Sound::stopSample("data/sounds/rope");
					Sound.playSample("data/sounds/hook");
					mRopeState = RopeState.Attached;
					mJumpHeld = false;

					ParticleSystem particleSystem = new ParticleSystem(
							mAnimationHookParticle,
							2,
							30,
							10,
							1,
							50,
							10,
							mRopeVelocity.normalize().multiply(-10),
							(float) 1.0);
					particleSystem.setPosition(mRopePosition);
					mRoom.addEntity(particleSystem);

					break;
				}

				if (mRopePosition.subtract(mPosition).length() > mRopeMaxLenghth)
				{
					detachHook();
					Sound.playSample("data/sounds/no_hook");
					break;
				}

				if (mRoom.isCollidable(tileX, tileY)) {
					detachHook();
					Sound.playSample("data/sounds/no_hook");
					break;
				}

				if (mRoom.damageDone((int)(mRopePosition.x), (int)(mRopePosition.y))) {
					detachHook();
					break;
				}

				mHookedEntity = mRoom.findHookableEntity(mRopePosition);
				if (mHookedEntity != null) {
					Sound.playSample("data/sounds/hook");
					mRopeState = RopeState.Attached;
					mJumpHeld = false;
					mHookedEntityOffset = mRopePosition.subtract(mHookedEntity.getPosition());
					mHookedEntityOffset.x = (float) Math.floor(mHookedEntityOffset.x);
					mHookedEntityOffset.y = (float) Math.floor(mHookedEntityOffset.y);
				}
			}
		}

		if (mRopeState == RopeState.Attached) {
			if (mHookedEntity != null) {
				mRopePosition = mHookedEntity.getPosition().add(mHookedEntityOffset);
			}

			float2 ropeToHero = mPosition.subtract(mRopePosition);
			if (ropeToHero.lengthCompare(ROPE_REST_LENGTH) > 0) {
				float2 ropeRestPoint = mRopePosition.add(ropeToHero.normalize().multiply(ROPE_REST_LENGTH));
				float2 heroToRestPoint = ropeRestPoint.subtract(mPosition);
				float2 ropeAcceleration = heroToRestPoint.multiply(ROPE_SPRING_CONSTANT);
				if (ropeAcceleration.lengthCompare(ROPE_MAX_ACCELERATION) > 0) {
					ropeAcceleration = ropeAcceleration.normalize().multiply(ROPE_MAX_ACCELERATION);
				}
				mVelocity = mVelocity.add(ropeAcceleration);
			}

			if (Input.isHeld(Buttons.Up)) {
				mVelocity.y -= acceleration;
			}

			if (Input.isHeld(Buttons.Down)) {
				mVelocity.y += acceleration;
			}

			int ropeTileX = (int)(mRopePosition.x / mRoom.getTileWidth());
			int ropeTileY = (int)(mRopePosition.y / mRoom.getTileWidth());
			if (mHookedEntity == null && !mRoom.isHookable(ropeTileX, ropeTileY)) {
				detachHook();
			}
		}

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
			mVelocity.x = 0;
		}

		if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
			mVelocity.y = 0;
		}

		float gravity = mJumpHeld ? JUMP_GRAVITY : GRAVITY;
		mVelocity.y += gravity;
		boolean ground = bumps.contains(Direction.DOWN);
		if (ground && !mOnGround && mRopeState != RopeState.Attached)
		{
			Sound.playSample("data/sounds/land");
		}
		mOnGround = ground;

		float drag = mOnGround ? GROUND_DRAG : AIR_DRAG;
		mVelocity = mVelocity.multiply(drag);

		mFrame ++;

		if (mBlinkingTicksLeft > 0)
		{
			mBlinkingTicksLeft --;
		}
	}

	private float2 adjustRopeDirection(float2 aRopeDirection) {
		float bestScore = 0;
		float2 bestDirection = aRopeDirection;

		for (int y = 0; y < mRoom.getHeightInTiles(); y++) {
			for (int x = 0; x < mRoom.getWidthInTiles(); x++) {
				if (mRoom.isHookable(x, y)) {
					float pixelX = x * mRoom.getTileWidth() + mRoom.getTileWidth() / 2;
					float pixelY = y * mRoom.getTileHeight() + mRoom.getTileHeight() / 2;
					float2 tilePos = new float2(pixelX, pixelY);
					float score = getAutoAimScore(aRopeDirection, tilePos);

					if (score > bestScore) {
						float2 direction = tilePos.subtract(mPosition);
						float2 rc = new float2();
						boolean rcHit = mRoom.rayCast(mPosition, direction, false, rc);
						if (rcHit && (int)rc.x == x && (int)rc.y == y) {
							bestScore = score;
							bestDirection = direction;
						}
					}
				}
			}
		}

		Set<Entity> damagableEntities = mRoom.getDamagableEntities();
		Set<Entity> hookableEntities = mRoom.getHookableEntities();
		Set<Entity> entities = new HashSet<Entity>();

		entities.addAll(damagableEntities);
		entities.addAll(hookableEntities);

		for (Entity e : entities) {
			float2 entityPos = e.getPosition();
			float score = getAutoAimScore(aRopeDirection, entityPos);

			if (score > bestScore) {
				float2 direction = entityPos.subtract(mPosition);
				float2 rc = new float2();
				boolean rcHit = mRoom.rayCast(mPosition, direction, false, rc);
				float2 rcTilePos= new float2(rc.x * mRoom.getTileWidth() + mRoom.getTileWidth() / 2, rc.y * mRoom.getTileHeight() + mRoom.getTileHeight() / 2);

				if (!rcHit || aRopeDirection.lengthCompare(rcTilePos) < 0) {
					bestScore = score;
					bestDirection = direction;
				}
			}
		}

		return bestDirection.normalize();
	}

	private float getAutoAimScore(float2 aRopeDirection, float2 aAutoAimPos) {
		float2 playerToTile = aAutoAimPos.subtract(mPosition);
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

	private void detachHook() {
		if (mRopeState != RopeState.Retracted && mRopeState != RopeState.Dissapearing) {
			mRopeState = RopeState.Dissapearing;
			mRopeDissapearCounter = 0;
		}
		mHookedEntity = null;
	}

	@Override
	public void draw(Canvas buffer, int offsetX, int offsetY, int layer)
	{
		int x = getDrawPositionX();
		int y = getDrawPositionY();

		if (myIsDead)
		{
			mAnimationHurt.drawFrame(buffer,
					0, 
					offsetX + x - mAnimationHurt.getFrameWidth()/2, 
					(int) (offsetY + y+getHalfSize().y-mAnimationHurt.getFrameHeight()),
					mFacingDirection == Direction.LEFT, false);
			return;
		}

		Animation animation = null;
		int frame = 1;

		//		mMovementState = MovementState_Jump;

		switch(mMovementState)
		{
		case Run:
			animation = mAnimationRun;
			frame = mFrame/10;
			break;
		case AirRun:
			animation = mAnimationRun;
			frame = mFrame/3;
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
			super.draw(buffer, offsetX, offsetY, layer);
		}

		if (mRopeState != RopeState.Retracted && (mRopeState != RopeState.Dissapearing || (mRopeDissapearCounter & 1) != 0)) {
			int x2 = getDrawPositionX() + offsetX;
			int y2 = getDrawPositionY() + offsetY;
			int x1 = (int)(mRopePosition.x + 0.5f) + offsetX;
			int y1 = (int)(mRopePosition.y + 0.5f) + offsetY;

			float2 line = new float2(x2-x1, y2-y1);
			int rlength = (int) line.length();
			line = line.divide(line.length());
			line = line.multiply(3);

			float rx = x1;
			float ry = y1;

			int segments = (int)Math.ceil(rlength/line.length());

			for (int i = 0; i < segments; i++)
			{
				float2 wave = new float2();

				if ((mRopeState == RopeState.Moving || mRopeState == RopeState.Dissapearing) && segments > 1) {
					float t = i / (float)(segments - 1);
					float value = (float) (Math.sin(t * 3.141592) * Math.sin(i * 0.3f));
					wave = new float2(line.y, -line.x).multiply(value);
				}

				mAnimationRope.drawFrame(buffer,
						frame, 
						(int) (rx + wave.x - mAnimationRope.getFrameWidth()/2), 
						(int) (ry + wave.y - mAnimationRope.getFrameHeight()/2));

				rx += line.x;
				ry += line.y;
			}

			mAnimationHook.drawFrame(buffer,
					frame, 
					x1 - mAnimationHook.getFrameWidth()/2, 
					y1 - mAnimationHook.getFrameHeight()/2);

		}

		if (mBlinkingTicksLeft > 0 && !mOnGround)
		{
			animation = mAnimationHurt;
		}

		if ((mBlinkingTicksLeft/6)%2 == 0)
		{
			animation.drawFrame(buffer,
					frame, 
					offsetX + x - animation.getFrameWidth()/2, 
					(int)(offsetY + y+getHalfSize().y-animation.getFrameHeight()),
					mFacingDirection == Direction.LEFT,
					false);
		}	
	}

	public boolean gotCoin() {
		if (mBlinkingTicksLeft == 0)
		{
			GameState.put("coins", GameState.getInt("coins") + 1);
			return true;
		}

		return false;
	}

	public boolean isDead() {
		return myIsDead;
	}

	public void imortal() {
		myImortal = true;
	}

	public void respawn() {
		setPosition(mSpawnPoint);
		setVelocity(new float2(0,0));
		myIsDead = false;
		GameState.put("coins", 0);
		mOnGround = false;
		mJumpHeld= false;
		mJumpPrepressed = false;
		mFrame = 0;
		mRopeState = RopeState.Retracted;
		mHookedEntity = null;
		mFacingDirection = Direction.RIGHT;
		mBlinkingTicksLeft = 0;
		mRopeDissapearCounter = 0;

	}

	public RopeState getRopeState() {
		return mRopeState;
	}

	public float2 getRopePosition() {
		return mRopePosition;
	}

	public void kill() {
		if (myImortal)
			return;

		if (mBlinkingTicksLeft == 0)
		{
			PlayerSkill.playerDidSomethingStupid(0.0f, 0.1f);
			detachHook();

			int coins = GameState.getInt("coins");
			GameState.put("coins", 0);

			if (coins > 0)
			{
				coins /= 2;
				coins = Math.min(20, Math.max(0, coins));
				int lifetime = UtilMethods.lerp(MAX_DEATH_COIN_LIFETIME, MIN_DEATH_COIN_LIFETIME, PlayerSkill.get());
				Coin.SpawnDeathCoins(coins, getPosition(), lifetime, mRoom);
				mBlinkingTicksLeft = BLINKING_TICKS;
				mVelocity = mVelocity.normalize().multiply(-KILL_VELOCITY);
				Sound.playSample("data/sounds/hurt");
			}
			else
			{
				PlayerSkill.playerDidSomethingStupid(0.0f, 0.25f);
				die();
			}
		}
	}

	private void die() {
		mRopeState = RopeState.Retracted;
		mHookedEntity = null;
		myIsDead = true;
		Sound.playSample("data/sounds/start");
	}

	public boolean gotCore() {
		if (mBlinkingTicksLeft == 0)
		{
			GameState.put("coins", GameState.getInt("coins") + 1);
			return true;
		}

		return false;
	}
}
