package se.darkbits.greengrappler.entities;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Constants.Direction;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;
import se.darkbits.greengrappler.media.Sound;


public class BossSaw extends Entity {

	private int myLifeTime = 60 * 6;
	private int myFrameCounter = 0;
	private Animation mySaw = Resource.getAnimation("data/images/saw.bmp");
	private ImmutableFloatPair mySpeed;

	public BossSaw(Direction aDirection) {
		if (aDirection == Direction.LEFT)
			mySpeed = new ImmutableFloatPair(-2.0f, 0);
		else
			mySpeed = new ImmutableFloatPair(2.0f, 0);

		Sound.playSample("data/sounds/boss_saw");
		setSize(new ImmutableFloatPair(mySaw.getFrameWidth(),
				mySaw.getFrameHeight()));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int x = (int) (getDrawPositionX() + offsetX - getHalfSize().getX());
		int y = (int) (getDrawPositionY() + offsetY - getHalfSize().getY());

		mySaw.drawFrame(buffer, myFrameCounter / 10, x, y);

		// Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update() {
		myFrameCounter++;

		if (myFrameCounter > myLifeTime)
			remove();

		setPosition(getPosition().add(mySpeed));

		if (myRoom.getHero().Collides(this)) {
			myRoom.getHero().kill();
		}
	}

	@Override
	public void onRespawn() {
		remove();
	}

	@Override
	public int getLayer() {
		return 3;
	}

}
