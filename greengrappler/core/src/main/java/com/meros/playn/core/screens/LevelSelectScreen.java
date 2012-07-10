package com.meros.playn.core.screens;

import java.util.ArrayList;

import playn.core.Surface;
import playn.core.Color;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Dialogue;
import com.meros.playn.core.Font;
import com.meros.playn.core.GameState;
import com.meros.playn.core.Input;
import com.meros.playn.core.LevelDescription;
import com.meros.playn.core.Music;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Screen;
import com.meros.playn.core.ScreenManager;
import com.meros.playn.core.Sound;
import com.meros.playn.core.Constants.Buttons;

public class LevelSelectScreen extends Screen {

	class Particle {
		public float myX;
		public float myY;
		public float myZ;

		public Particle(float aX, float aY, float aZ) {
			myX = aX;
			myY = aY;
			myZ = aZ;
		}
	}

	static boolean myBossLevelCompleted = false;
	Animation myBackground = Resource.getAnimation(
			"data/images/level_select.bmp", 1);
	boolean myBossLevelUnlocked = false;
	Dialogue myBossUnlockedDialogue;
	Animation myCompletedIconBackground = Resource.getAnimation(
			"data/images/completed_level.bmp", 1);
	Dialogue myFirstDialogue;
	Font myFont = Resource.getFont("data/images/font.bmp");

	int myFrameCounter = 0;
	Animation myIcons = Resource.getAnimation("data/images/icons.bmp", 9);
	LevelDescription[] myLevelDescs = new LevelDescription[9];
	boolean myLevelSelected = false;
	int myLevelSelectedCounter = 0;
	ArrayList<Particle> myParticles = new ArrayList<Particle>();
	boolean myPlayingBossLevel = false;
	boolean myRunBossUnlockedDialogue = false;
	boolean myRunFirstDialogue = false;
	Animation mySelectedLevelBackground = Resource.getAnimation(
			"data/images/selected_level_background.bmp", 6);
	int mySelectedX = 0;

	int mySelectedY = 0;

	Animation myUnselectedLevelBackground = Resource.getAnimation(
			"data/images/unselected_level_background.bmp", 1);

	// public:
	// LevelSelect();
	public LevelSelectScreen() {

		for (int i = 0; i < 200; i++) {
			myParticles
					.add(new Particle((float) Math.random() * 320, (float) Math
							.random() * 240, (float) Math.random() * 3 + 1));
		}

		PrivSetLevelDesc(0, 0, new LevelDescription("GRAPPLIN' HEAVEN",
				"data/rooms/olof2.txt", 0, "data/music/olof9.xm"));
		PrivSetLevelDesc(1, 0, new LevelDescription("CAVERNOUS",
				"data/rooms/per4.txt", 1, "data/music/olof8.xm"));
		PrivSetLevelDesc(2, 0, new LevelDescription("HARRY U.P.",
				"data/rooms/level1.txt", 7, "data/music/olof2-rmx.xm"));
		PrivSetLevelDesc(0, 1, new LevelDescription("GLIDER",
				"data/rooms/per1.txt", 3, "data/music/olof12.xm"));
		PrivSetLevelDesc(1, 1, new LevelDescription("FINAL CORE",
				"data/rooms/olof.txt", 5, "data/music/spooky.xm"));
		PrivSetLevelDesc(2, 1, new LevelDescription("UNSTABLE",
				"data/rooms/breaktilelevel.txt", 4, "data/music/olof8.xm"));
		PrivSetLevelDesc(0, 2, new LevelDescription("THE TOWER",
				"data/rooms/per2.txt", 2, "data/music/olof12.xm"));
		PrivSetLevelDesc(1, 2, new LevelDescription("WALL OF DEATH",
				"data/rooms/per3.txt", 6, "data/music/olof2-rmx.xm"));
		PrivSetLevelDesc(2, 2, new LevelDescription("LAVA LAND",
				"data/rooms/levellava.txt", 8, "data/music/olof2-rmx.xm"));

		myFirstDialogue = new Dialogue("data/dialogues/level_select.txt");
		myFirstDialogue.setRunWithoutHero();

		myBossUnlockedDialogue = new Dialogue(
				"data/dialogues/boss_unlocked.txt");
		myBossUnlockedDialogue.setRunWithoutHero();
	}

	// ~LevelSelect();
	// void onEntered();
	// void onDraw(BITMAP* aBuffer);
	// void onLogic();
	// void onExited();
	// static bool myBossLevelCompleted;
	// private:
	// Font* myFont;
	// Animation* myBackground;
	// Animation* mySelectedLevelBackground;
	// Animation* myUnselectedLevelBackground;
	// Animation* myIcons;
	// Animation* myCompletedIconBackground;
	// Dialogue* myFirstDialogue;
	// Dialogue* myBossUnlockedDialogue;
	// class Particle
	// {
	// public:
	// Particle(float x_, float y_, float z_)
	// :x(x_), y(y_),z(z_)
	// {
	//
	// }
	//
	// float x;
	// float y;
	// float z;
	// };
	//
	// std::vector<Particle> myParticles;
	//
	//
	// LevelDesc myLevelDescs[9];
	// int mySelectedX;
	// int mySelectedY;
	// int myFrameCounter;
	// bool myLevelSelected;
	// int myLevelSelectedCounter;
	// bool myPlayingBossLevel;
	// bool myBossLevelUnlocked;
	// bool myRunFirstDialogue;
	// bool myRunBossUnlockedDialogue;
	// void PrivSetLevelDesc(int aX, int aY, LevelDesc aLevelDesc);
	@Override
	public void onDraw(Surface aBuffer) {
		aBuffer.setFillColor(Color.rgb(57, 56, 41));
		aBuffer.fillRect(0, 0, 320, 240);

		for (int i = 0; i < myParticles.size(); i++) {
			int color = Color.rgb(181, 166, 107);
			if (myParticles.get(i).myZ <= 2)
				color = Color.rgb(123, 113, 99);
			aBuffer.setFillColor(color);
			aBuffer.drawLine(myParticles.get(i).myX, myParticles.get(i).myY,
					myParticles.get(i).myX + 2, myParticles.get(i).myY, 1);
		}

		if (!myLevelSelected)
			myBackground.drawFrame(aBuffer, 0, 0, 0);

		if (myLevelSelected && myLevelSelectedCounter % 6 < 3
				&& myLevelSelectedCounter < 20) {
			aBuffer.setFillColor(Color.rgb(231, 215, 156));
			aBuffer.fillRect(0, 0, 320, 240);
		}

		if (myLevelSelected && myLevelSelectedCounter < 10)
			return;

		if (!myLevelSelected) {
			myFont.drawCenter(aBuffer, "SELECT LEVEL", 0, 30, 320, 20);
		}

		for (int i = 0; i < 9; i++) {
			int x = i % 3;
			int y = i / 3;

			int index = mySelectedY * 3 + mySelectedX;
			LevelDescription levelDesc = myLevelDescs[i];

			Animation animation;
			if (mySelectedX == x && mySelectedY == y)
				animation = mySelectedLevelBackground;
			else
				animation = myUnselectedLevelBackground;

			int xPos = (x - 1) * (animation.getFrameWidth() + 5)
					- animation.getFrameWidth() / 2;
			int yPos = (y - 1) * (animation.getFrameHeight() + 5)
					- animation.getFrameHeight() / 2;
			animation.drawFrame(aBuffer, myFrameCounter / 3, 160 + xPos,
					120 + yPos);

			if ((!myLevelSelected || index == i)) {
				if (i == 4 && myBossLevelUnlocked || i != 4)
					myIcons.drawFrame(aBuffer, levelDesc.myFrameIndex,
							164 + xPos, 124 + yPos);
			}
			if (!myLevelSelected
					&& GameState.getInt(levelDesc.myLevelFile) == 1) {
				myCompletedIconBackground.drawFrame(aBuffer, 0, 164 + xPos,
						124 + yPos);
			}

		}

		if (!myLevelSelected) {
			int index = mySelectedY * 3 + mySelectedX;
			LevelDescription levelDesc = myLevelDescs[index];
			myFont.draw(aBuffer, levelDesc.myName, 220, 68);

			if (GameState.getInt(levelDesc.myLevelFile) == 1) {
				myFont.draw(aBuffer, "STATUS:", 220, 88);
				myFont.draw(aBuffer, "   CLEARED", 220, 98);
			} else if (index == 4 && !myBossLevelUnlocked) {
				myFont.draw(aBuffer, "STATUS:", 220, 88);
				myFont.draw(aBuffer, "   LOCKED", 220, 98);
			} else {
				myFont.draw(aBuffer, "STATUS:", 220, 88);
				myFont.draw(aBuffer, "   RADIOACTIVE", 220, 98);
			}
		}

		if (myRunFirstDialogue)
			myFirstDialogue.draw(aBuffer, 0, 0, 0);

		if (myRunBossUnlockedDialogue)
			myBossUnlockedDialogue.draw(aBuffer, 0, 0, 0);
	}

	@Override
	public void onEntered() {
		GameState.saveToFile();

		myBossLevelUnlocked = true;
		for (int i = 0; i < 9; i++) {
			if (i == 4)
				continue;

			LevelDescription bossLevel = myLevelDescs[i];
			if (GameState.getInt(bossLevel.myLevelFile) == 0) {
				//TODO: debugging myBossLevelUnlocked = false;
				break;
			}
		}

		LevelDescription bossLevel = myLevelDescs[4];
		if (GameState.getInt(bossLevel.myLevelFile) == 1 && myPlayingBossLevel
				&& myBossLevelCompleted) {
			myBossLevelCompleted = false;
			myPlayingBossLevel = false;
			ScreenManager.add(new EndScreen());
			return;
		}

		myLevelSelected = false;
		myLevelSelectedCounter = 0;
		Music.playSong("data/music/level_select.xm");

		if (GameState.getInt("level_select_dialogue") == 0) {
			myRunFirstDialogue = true;
			GameState.put("level_select_dialogue", 1);
		}

		if (myBossLevelUnlocked
				&& GameState.getInt("level_boss_unlocked_dialogue") == 0) {
			myRunBossUnlockedDialogue = true;
			GameState.put("level_boss_unlocked_dialogue", 1);
		}
	}

	@Override
	public void onExited() {
		GameState.saveToFile();
	}

	@Override
	public void onLogic() {
		myFrameCounter++;
		for (int i = 0; i < myParticles.size(); i++) {
			myParticles.get(i).myX -= myParticles.get(i).myZ / 2.0f;

			if (myParticles.get(i).myX < 0) {
				myParticles.get(i).myX = 320;
				myParticles.get(i).myY = (float) (Math.random() * 240);
				myParticles.get(i).myZ = (float) (Math.random() * 3 + 1);
			}
		}

		if (myLevelSelected) {
			myLevelSelectedCounter++;
			if (myLevelSelectedCounter > 100) {
				int index = mySelectedY * 3 + mySelectedX;
				myPlayingBossLevel = index == 4;
				LevelDescription levelDesc = myLevelDescs[index];
				ScreenManager.add(new LevelScreen(levelDesc));
			}
			return;
		}

		if (Input.isPressed(Buttons.Exit)) {
			exit();
			Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Left)) {
			mySelectedX--;
			if (mySelectedX < 0)
				mySelectedX = 0;
			else
				Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Right)) {
			mySelectedX++;
			if (mySelectedX > 2)
				mySelectedX = 2;
			else
				Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Up)) {
			mySelectedY--;
			if (mySelectedY < 0)
				mySelectedY = 0;
			else
				Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Down)) {
			mySelectedY++;
			if (mySelectedY > 2)
				mySelectedY = 2;
			else
				Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Fire)) {
			int index = mySelectedY * 3 + mySelectedX;
			if (index == 4 && myBossLevelUnlocked || index != 4) {
				Music.stop();
				Sound.playSample("data/sounds/start");
				myLevelSelected = true;
				myLevelSelectedCounter = 0;
			}
		}

		if (myRunFirstDialogue)
			myFirstDialogue.update();

		if (myRunBossUnlockedDialogue)
			myBossUnlockedDialogue.update();
	}

	private void PrivSetLevelDesc(int aX, int aY, LevelDescription aLevelDesc) {
		int index = aY * 3 + aX;
		myLevelDescs[index] = aLevelDesc;

	}
}
