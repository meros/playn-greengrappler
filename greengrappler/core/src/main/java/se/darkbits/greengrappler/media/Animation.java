package se.darkbits.greengrappler.media;

import playn.core.Image;
import playn.core.Surface;
import se.darkbits.greengrappler.Resource;


public class Animation {
	int myFrameHeight;
	Image[] myFrames;

	int myFrameWidth;

	public Animation(String aFilename) {
		Image allFrames = Resource.getBitmap(aFilename);

		myFrameWidth = myFrameHeight = (int) allFrames.height();
		int count = (int) allFrames.width() / myFrameWidth;

		privFillFramesList(allFrames, count);
	}

	public Animation(String aFilename, int aNumberOfFrames) {
		Image allFrames = Resource.getBitmap(aFilename);

		myFrameWidth = myFrameHeight = (int) allFrames.height();

		myFrameWidth = (int) allFrames.width() / aNumberOfFrames;
		myFrameHeight = (int) allFrames.height();
		int count = aNumberOfFrames;

		privFillFramesList(allFrames, count);

	}

	public void drawFrame(Surface aBuffer, int aFrame, int aX, int aY) {
		drawFrame(aBuffer, aFrame, aX, aY, false, false);
	}

	// void drawFrame(BITMAP *aBuffer, int aFrame, int aX, int aY, bool aHFlip =
	// false, bool aVFlip = false, Blending aBlending = Blending_None) const;
	public void drawFrame(Surface aBuffer, int aFrame, int aX, int aY,
			boolean aHFlip, boolean aVFlip) {

		if (aX > aBuffer.width() || aY > aBuffer.height()
				|| aX + myFrameWidth < 0 || aY + myFrameHeight < 0)
			return;

		boolean needTransform = aHFlip || aVFlip;
		if (needTransform)
			aBuffer.save();

		if (aHFlip)
			aBuffer.transform(-1, 0, 0, 1, getFrameWidth() + aX * 2, 0);
		if (aVFlip)
			aBuffer.transform(1, 0, 0, -1, 0, getFrameHeight() + aY * 2);

		aBuffer.drawImage(getFrame(aFrame), aX, aY);

		if (needTransform)
			aBuffer.restore();
	}

	public Image getFrame(int aFrame) {
		return myFrames[Math.abs(aFrame % myFrames.length)];
	}

	public int getFrameHeight() {
		return myFrameHeight;
	}

	public int getFrameWidth() {
		return myFrameWidth;
	}

	void privFillFramesList(Image aAllFrames, int aCount) {
		myFrames = new Image[aCount];

		int i;
		for (i = 0; i < aCount; i++) {
			Image subImage = aAllFrames.subImage(i * myFrameWidth, 0,
					myFrameWidth, myFrameHeight);
			myFrames[i] = subImage;
		}
	}
}
