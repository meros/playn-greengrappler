package se.darkbits.greengrappler.entities;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.PlayerSkill;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;


public class SpawnPoint extends Entity {

	private enum State {
		UNCHECKED(0), SEMI_CHECKED(1), CHECKED(2);

		public int value;

		State(int aValue) {
			value = aValue;
		}
	}

	private State myState = State.UNCHECKED;
	private int myFrame = 0;
	private Animation myAnimation = Resource.getAnimation(
			"data/images/checkpoint.bmp", 3);

	@Override
	public void update() {
		Hero hero = myRoom.getHero();

		if (hero.Collides(this)) {
			if (myState == State.UNCHECKED) {
				PlayerSkill.playerDidSomethingClever(0.7f, 0.5f);
			}

			myState = State.SEMI_CHECKED;
			hero.setLastSpawnPoint(getPosition());
		}

		if (myState == State.SEMI_CHECKED) {
			myFrame++;
		}

		if (myFrame > 5) {
			myState = State.CHECKED;
		}
	}

	public SpawnPoint() {
		setSize(new ImmutableFloatPair(22, 25));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int x = (int) (getPosition().getX() - myAnimation.getFrameWidth() / 2 + offsetX);
		int y = (int) (getPosition().getY() - myAnimation.getFrameHeight() / 2 + offsetY);

		myAnimation.drawFrame(buffer, myState.value, x, y);
	}

	@Override
	public int getLayer() {
		return 3;
	}

}
