package se.darkbits.greengrappler.entities;

import java.util.EnumSet;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.PlayerSkill;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Constants.Direction;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;


public class ReactorCore extends Entity {

	Animation mAnimation = Resource.getAnimation("data/images/core.bmp", 3);
	int mFrame = 0;

	public ReactorCore() {
		setSize(new ImmutableFloatPair(30, 30));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		// Entity::draw(buffer, offsetX, offsetY, layer);
		ImmutableFloatPair pos = new ImmutableFloatPair(getPosition());
		pos = pos.subtract(new ImmutableFloatPair(mAnimation.getFrameWidth(),
				mAnimation.getFrameHeight()).divide(2));
		pos = pos.add(new ImmutableFloatPair(offsetX, offsetY));

		mAnimation.drawFrame(buffer, mFrame / 5, (int) pos.getX(),
				(int) pos.getY());
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void update() {
		Hero hero = myRoom.getHero();

		myVelocity.set(0, myVelocity.getY() + 6.0f);

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
			myVelocity.set(myVelocity.getX(), myVelocity.getY() * 0.8f);
		}

		if (hero.Collides(this)) {
			if (hero.gotCore()) {
				PlayerSkill.playerDidSomethingClever(1.0f, 0.75f);
				myRoom.setCompleted();
				remove();
			}
		}

		mFrame++;
	}

}
