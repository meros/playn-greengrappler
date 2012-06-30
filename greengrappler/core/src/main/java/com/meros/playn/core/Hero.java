package com.meros.playn.core;

import playn.core.Canvas;

import com.meros.playn.core.Constants.Direction;

public class Hero extends Entity {

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
	float2 mRopePosition;
	float2 mRopeVelocity;
	
	
	Entity mHookedEntity;
	Direction mFacingDirection = Direction.Right;
	int mRopeDissapearCounter = 0;
	Animation mAnimationRun = new Animation("data/images/hero_run.bmp", 4);
	Animation mAnimationJump = new Animation("data/images/hero_jump.bmp", 1);
	Animation mAnimationFall = new Animation("data/images/hero_fall.bmp", 1);
	Animation mAnimationRope = new Animation("data/images/rope.bmp", 1);
	Animation mAnimationHook = new Animation("data/images/hook.bmp", 1);
	Animation mAnimationHurt = new Animation("data/images/hero_hurt.bmp", 1);
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

	public Hero()
	{
		mSize = new float2(10.0f, 15.0f);
		//TODO mAnimationHookParticle = Resource::getAnimation("data/images/particles.bmp");
	}

	@Override
	public int getLayer() {
		return 2;
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
					mFacingDirection == Direction.Left, false);
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
					mFacingDirection == Direction.Left,
					false);
		}	
	}

	public boolean gotCoin() {
		if (mBlinkingTicksLeft == 0)
		{
			//TODO: GameState::put("coins", GameState::getInt("coins") + 1);
			return true;
		}

		return false;
	}

}
