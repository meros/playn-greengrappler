package se.darkbits.greengrappler.entities;

import java.util.Random;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;


public class LavaSea extends Entity {

	private int mFrame = 0;
	private boolean mSafeMode = false;
	private int mSafeLevel = 0;

	Animation mTopAnimation = Resource.getAnimation("data/images/lavatop.bmp",
			2);
	Animation mFillAnimation = Resource.getAnimation(
			"data/images/lavafill.bmp", 2);

	@Override
	public void update() {
		Hero hero = myRoom.getHero();

		if (hero.Collides(this)) {
			hero.kill();
		}

		mFrame++;

		if (mSafeMode) {
			mSafeLevel++;
			if (mSafeLevel > getPosition().getY() + 10) {
				mSafeLevel = (int) (getPosition().getY() + 10);
			}
		}

		if (Math.random() < 0.3) {
			ParticleSystem particleSystem = new ParticleSystem(
					Resource.getAnimation("data/images/particles.bmp"), 2, 30,
					10, 1, 50, 10, new ImmutableFloatPair(0, -30), 5.0f);

			Random random = new Random();

			int min = -myRoom.getCamera().getOffsetX();
			int max = (320 - myRoom.getCamera().getOffsetX());
			int rVal = random.nextInt(max - min) + min;

			ImmutableFloatPair pos = new ImmutableFloatPair(rVal, getCurrentY());
			particleSystem.setPosition(pos, 5, false);

			myRoom.addEntity(particleSystem);
		}
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int screenY = offsetY + getCurrentY();

		for (int x = -10 + offsetX % 10; x < 320; x += 10) {
			mTopAnimation.drawFrame(buffer, mFrame / 30, x, screenY);
		}

		for (int x = -10 + offsetX % 10; x < 320; x += 10) {
			for (int y = screenY + 10; y < 240; y += 10) {
				mFillAnimation.drawFrame(buffer, mFrame / 30, x, y);
			}
		}
	}

	@Override
	public float getCollideTop() {
		return getCurrentY();
	}

	@Override
	public float getCollideLeft() {
		return 0;
	}

	@Override
	public float getCollideRight() {
		return 100000;
	}

	@Override
	public float getCollideBottom() {
		return 100000;
	}

	private int getCurrentY() {
		if (mSafeMode) {
			return mSafeLevel;
		}

		float sinwave = (float) ((Math.sin(mFrame / 100.0f) + 1) / 2);
		sinwave = sinwave * sinwave * sinwave;
		return (int) (getPosition().getY() - sinwave * 80 + 2);
	}

	@Override
	public void onLevelComplete() {
		mSafeLevel = getCurrentY();
		mSafeMode = true;
	}

	@Override
	public int getLayer() {
		return 4;
	}

}
