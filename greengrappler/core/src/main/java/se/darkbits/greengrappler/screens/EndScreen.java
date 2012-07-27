package se.darkbits.greengrappler.screens;

import playn.core.Color;
import playn.core.Surface;
import se.darkbits.greengrappler.GreenGrappler;
import se.darkbits.greengrappler.Input;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Screen;
import se.darkbits.greengrappler.Constants.Buttons;
import se.darkbits.greengrappler.media.Font;
import se.darkbits.greengrappler.media.Music;
import se.darkbits.greengrappler.media.Sound;


public class EndScreen extends Screen {

	int GREEN_PEACE_DURATION = 60 * 7;

	int myCreditsOffset = 240;;
	Font myFont = Resource.getFont("data/images/font.bmp");
	int myFrameCounter = -60;

	@Override
	public void onEntered() {
		GreenGrappler.showTouchControls(false);
	}

	@Override
	public void onDraw(Surface aBuffer) {
		aBuffer.setFillColor(Color.rgb(57, 56, 41));
		aBuffer.fillRect(0, 0, 320, 240);

		if (myFrameCounter < 0)
			return;

		if (myFrameCounter < GREEN_PEACE_DURATION) {
			myFont.draw(aBuffer, "THE LAST REACTOR IS IN CAPTIVITY", 50, 110);
			myFont.draw(aBuffer, "THE GALAXY IS AT GREEN PEACE", 60, 120);
		} else {
			myFont.draw(aBuffer, "CREDITS", 50, myCreditsOffset);
			myFont.draw(aBuffer, "PROGRAMMING", 50, 30 + myCreditsOffset);
			myFont.draw(aBuffer, "   OLOF NAESSEN", 50, 40 + myCreditsOffset);
			myFont.draw(aBuffer, "   PER LARSSON", 50, 50 + myCreditsOffset);
			myFont.draw(aBuffer, "   ALEXANDER SCHRAB ", 50,
					60 + myCreditsOffset);

			myFont.draw(aBuffer, "GRAPHICS", 50, 90 + myCreditsOffset);
			myFont.draw(aBuffer, "   OLOF NAESSEN", 50, 100 + myCreditsOffset);
			myFont.draw(aBuffer, "   TIMUR KONDRAKOV", 50,
					110 + myCreditsOffset);
			myFont.draw(aBuffer, "   PER LARSSON", 50, 120 + myCreditsOffset);

			myFont.draw(aBuffer, "MUSIC", 50, 150 + myCreditsOffset);
			myFont.draw(aBuffer, "   OLOF NAESSEN", 50, 160 + myCreditsOffset);

			myFont.draw(aBuffer, "VOICE ACTING", 50, 190 + myCreditsOffset);
			myFont.draw(aBuffer, "   PER LARSSON", 50, 200 + myCreditsOffset);

			myFont.draw(aBuffer, "NOT SHOWING UP FOR THE COMPO", 50,
					230 + myCreditsOffset);
			myFont.draw(aBuffer, "   TED STEEN", 50, 240 + myCreditsOffset);

			myFont.draw(aBuffer, "THIS GAME WAS DONE", 50,
					270 + myCreditsOffset);
			myFont.draw(aBuffer, "FOR THE SPEEDHACK 11", 50,
					280 + myCreditsOffset);
			myFont.draw(aBuffer, "COMPETITION", 50, 290 + myCreditsOffset);
			myFont.draw(aBuffer, "THANK YOU ALLEGRO.CC", 50,
					300 + myCreditsOffset);
			myFont.draw(aBuffer, "FOR A GREAT COMPETITION!", 50,
					310 + myCreditsOffset);

			myFont.draw(aBuffer, "THANK YOU THORBJORN LINDEIJER", 50,
					340 + myCreditsOffset);
			myFont.draw(aBuffer, "FOR THE TILED EDITOR!", 50,
					350 + myCreditsOffset);

			myFont.draw(aBuffer, "THANK YOU ANDERS STENBERG", 50,
					380 + myCreditsOffset);
			myFont.draw(aBuffer, "FOR YOUR TILE RAYCASTING ALGORITHM!", 50,
					390 + myCreditsOffset);

			myFont.draw(aBuffer, "AND THANKS FOR PLAYING", 50,
					420 + myCreditsOffset);
			myFont.draw(aBuffer, "OUR GAME!", 50, 430 + myCreditsOffset);

			if (myCreditsOffset < -450) {
				myFont.drawCenter(aBuffer, "THE END", 0, 0, 320, 240);
			}
		}
	}

	@Override
	public void onLogic() {
		myFrameCounter++;

		if (myFrameCounter == 0) {
			Sound.playSample("data/sounds/green_peace");
		}
		if (myFrameCounter == GREEN_PEACE_DURATION) {
			Music.playSong("data/music/intro2.xm");
		} else if (myFrameCounter > GREEN_PEACE_DURATION
				&& myFrameCounter % 4 == 0) {
			myCreditsOffset--;
		}

		if (Input.isPressed(Buttons.EXIT))
			exit();
	}

}
