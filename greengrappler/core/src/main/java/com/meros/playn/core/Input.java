package com.meros.playn.core;

import java.util.HashMap;
import java.util.HashSet;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import playn.core.Touch;

import com.meros.playn.core.Constants.Buttons;

public class Input implements Keyboard.Listener, Touch.Listener {

	// TODO: temp!
	public static int lastTouchX = 0;
	public static int lastTouchY = 0;
	static HashSet<Buttons> mIsHeld = new HashSet<Buttons>();

	static HashSet<Buttons> mIsPressed = new HashSet<Buttons>();

	static HashSet<Buttons> mIsReleased = new HashSet<Buttons>();

	static HashMap<Key, Buttons> mKeyMap = new HashMap<Key, Buttons>();

	public static void disable() {
		// TODO Auto-generated method stub

	}

	public static void enable() {
		// TODO Auto-generated method stub

	}

	public static void init() {
		PlayN.keyboard().setListener(new Input());
		PlayN.touch().setListener(new Input());

		mKeyMap.put(Key.DOWN, Buttons.Down);
		mKeyMap.put(Key.UP, Buttons.Up);
		mKeyMap.put(Key.LEFT, Buttons.Left);
		mKeyMap.put(Key.RIGHT, Buttons.Right);
		mKeyMap.put(Key.ENTER, Buttons.Fire);
		mKeyMap.put(Key.Z, Buttons.Fire);
		mKeyMap.put(Key.ESCAPE, Buttons.ForceQuit);
		mKeyMap.put(Key.A, Buttons.Jump);
	}

	public static boolean isHeld(Buttons aButton) {
		return mIsHeld.contains(aButton);
	}

	public static boolean isPressed(Buttons aButton) {
		return mIsPressed.contains(aButton);
	}

	public static boolean isReleased(Buttons aButton) {
		return mIsReleased.contains(aButton);
	}

	public static void onButtonDown(Buttons aButton) {
		mIsPressed.add(aButton);
		mIsHeld.add(aButton);
	}

	public static void onButtonUp(Buttons aButton) {
		mIsHeld.remove(aButton);
		mIsReleased.add(aButton);
	}

	public static void update() {
		mIsPressed.clear();
		mIsReleased.clear();
	}

	@Override
	public void onKeyDown(Event event) {
		if (mKeyMap.containsKey(event.key())) {
			if (!mIsHeld.contains(mKeyMap.get(event.key()))) {
				event.setPreventDefault(true);
				onButtonDown(mKeyMap.get(event.key()));
			}
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyUp(Event event) {
		if (mKeyMap.containsKey(event.key())) {
			event.setPreventDefault(true);
			onButtonUp(mKeyMap.get(event.key()));
		}
	}

	@Override
	public void onTouchEnd(playn.core.Touch.Event[] touches) {
		// TODO Auto-generated method stub
		onButtonUp(Buttons.Fire);
	}

	@Override
	public void onTouchMove(playn.core.Touch.Event[] touches) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchStart(playn.core.Touch.Event[] touches) {
		lastTouchX = (int) touches[0].x();
		lastTouchY = (int) touches[0].y();

		onButtonDown(Buttons.Fire);
	}
}
