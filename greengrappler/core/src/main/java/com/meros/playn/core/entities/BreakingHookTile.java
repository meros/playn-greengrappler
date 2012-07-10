package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.UtilMethods;
import com.meros.playn.core.float2;

public class BreakingHookTile extends Entity {

	static int MAX_BREAK_FRAMES = 50;
	static int MIN_BREAK_FRAMES = 30;

	int myBreakCounter = 0;
	boolean myBreaking = false;
	boolean myDestroyed = false;
	Animation mySprite = Resource
			.getAnimation("data/images/breakinghooktile.bmp");
	private int myTileX;
	private int myTileY;

	public BreakingHookTile() {
		setSize(new float2(mySprite.getFrameWidth(), mySprite.getFrameHeight()));
	}

	@Override
	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, int aLayer) {
		if (myDestroyed) {
			return;
		}

		int x = getDrawPositionX() + aOffsetX - mySprite.getFrameWidth() / 2;
		int y = getDrawPositionY() + aOffsetY - mySprite.getFrameWidth() / 2;

		if (!myBreaking || (myBreakCounter & 1) == 0) {
			mySprite.drawFrame(aBuffer, 0, x, y);
		}
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void onRespawn() {
		myTileX = (int) (getPosition().x / mRoom.getTileWidth());
		myTileY = (int) (getPosition().y / mRoom.getTileHeight());
		mRoom.setHookable(myTileX, myTileY, true);
		mRoom.setCollidable(myTileX, myTileY, true);
		myBreakCounter = 0;
		myBreaking = false;
		myDestroyed = false;
	}

	@Override
	public void setRoom(Room aRoom) {
		super.setRoom(aRoom);
		onRespawn();
	}

	@Override
	public void update() {
		if (myDestroyed) {
			return;
		}

		if (myBreaking) {
			myBreakCounter++;
			int breakFrames = UtilMethods.lerp(MAX_BREAK_FRAMES,
					MIN_BREAK_FRAMES, PlayerSkill.get());

			if (mRoom.getHero().getRopeState() != Hero.RopeState.Attached) {
				breakFrames = -1;
			}

			if (myBreakCounter >= breakFrames) {
				myDestroyed = true;
				mRoom.setHookable(myTileX, myTileY, false);
				mRoom.setCollidable(myTileX, myTileY, false);

				ParticleSystem ps = new ParticleSystem(Resource.getAnimation(
						"data/images/debris.bmp", 4), 10, 50, 20, 1, 50, 2,
						new float2(0.0f, -30.0f), 2.0f);
				ps.setPosition(getPosition(), 2.0f, false);
				mRoom.addEntity(ps);

			}
		}

		Hero hero = mRoom.getHero();

		if (hero != null) {
			if (hero.getRopeState() == Hero.RopeState.Attached) {
				int ropeTileX = (int) (hero.getRopePosition().x / mRoom
						.getTileWidth());
				int ropeTileY = (int) (hero.getRopePosition().y / mRoom
						.getTileWidth());
				if (ropeTileX == myTileX && ropeTileY == myTileY) {
					myBreaking = true;
				}
			}
		}
	}

}
