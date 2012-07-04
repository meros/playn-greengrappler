package com.meros.playn.core;

import playn.core.Canvas;

public class BreakingHookTile extends Entity {

	static int MIN_BREAK_FRAMES = 30;
	static int MAX_BREAK_FRAMES = 50;

	Animation mSprite = Resource.getAnimation("data/images/breakinghooktile.bmp");
	int mBreakCounter = 0;
	boolean mBreaking = false;
	boolean mDestroyed = false;
	private int mTileX;
	private int mTileY;


	public BreakingHookTile()
	{
		setSize(new float2(mSprite.getFrameWidth(), mSprite.getFrameHeight()));		
	}

	@Override
	public void setRoom(Room aRoom)
	{
		super.setRoom(aRoom);
		onRespawn();
	}

	@Override
	public void onRespawn()
	{
		mTileX = (int)(mPosition.x / mRoom.getTileWidth());
		mTileY = (int)(mPosition.y / mRoom.getTileHeight());
		mRoom.setHookable(mTileX, mTileY, true);
		mRoom.setCollidable(mTileX, mTileY, true);
		mBreakCounter = 0;
		mBreaking = false;
		mDestroyed = false;
	}

	@Override
	public void update()
	{
		if (mDestroyed) {
			return;
		}

		if (mBreaking) {
			mBreakCounter++;
			int breakFrames = UtilMethods.lerp(MAX_BREAK_FRAMES, MIN_BREAK_FRAMES, PlayerSkill.get());

			if (mRoom.getHero().getRopeState() != Hero.RopeState.Attached) {
				breakFrames = -1;
			}

			if (mBreakCounter >= breakFrames) {
				mDestroyed = true;
				mRoom.setHookable(mTileX, mTileY, false);
				mRoom.setCollidable(mTileX, mTileY, false);

				ParticleSystem ps = 
						new ParticleSystem(
								Resource.getAnimation("data/images/debris.bmp", 4), 
								10, 50, 20, 1, 50, 2, new float2(0.0f, -30.0f), 2.0f);
				ps.setPosition(getPosition(), 2.0f, false);
				mRoom.addEntity(ps);

			}
		}

		Hero hero = mRoom.getHero();

		if (hero != null) {
			if (hero.getRopeState() == Hero.RopeState.Attached) {
				int ropeTileX = (int)(hero.getRopePosition().x / mRoom.getTileWidth());
				int ropeTileY = (int)(hero.getRopePosition().y / mRoom.getTileWidth());
				if (ropeTileX == mTileX && ropeTileY == mTileY) {
					mBreaking = true;
				}
			}
		}
	}

	@Override
	public void draw(Canvas aBuffer, int aOffsetX, int aOffsetY, int aLayer)
	{
		if (mDestroyed) {
			return;
		}

		int x = getDrawPositionX() + aOffsetX - mSprite.getFrameWidth() / 2;
		int y = getDrawPositionY() + aOffsetY - mSprite.getFrameWidth() / 2;

		if (!mBreaking || (mBreakCounter & 1) == 0) {
			mSprite.drawFrame(aBuffer, 0, x, y);
		}
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

}
