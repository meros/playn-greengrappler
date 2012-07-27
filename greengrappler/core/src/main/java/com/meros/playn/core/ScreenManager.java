package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Surface;

public class ScreenManager {
	static Screen myScreenToEnter = null;
	static Screen myScreenToExit = null;
	static ArrayList<Screen> myStack = new ArrayList<Screen>();

	public static void add(Screen screen) {
		myStack.add(screen);
		myScreenToEnter = screen;
	}

	public static void destroy() {
		myStack.clear();
	}

	public static void draw(Surface buffer) {
		if (myScreenToExit == null && myScreenToEnter == null) {
			Screen screen = getTop();
			if (screen != null)
				screen.onDraw(buffer);
		}

		if (myScreenToExit != null) {
			boolean exitDone = myScreenToExit.onExit(buffer);

			if (!exitDone)
				return;

			for (int i = 0; i < myStack.size(); i++) {
				if (myStack.get(i) == myScreenToExit) {
					myStack.remove(i);
					break;
				}
			}

			myScreenToExit.onExited();
			myScreenToExit = null;

			if (getTop() != null) {
				myScreenToEnter = getTop();
			}
		}

		if (myScreenToEnter != null) {
			boolean enterDone = myScreenToEnter.onEnter(buffer);
			if (!enterDone)
				return;

			myScreenToEnter.onEntered();

			myScreenToEnter = null;
		}
	}

	// static void enter(Screen* screen);
	public static void enter(Screen screen) {
		if (getTop() != null) {
			myScreenToExit = getTop();
		}

		myStack.add(screen);
	}

	// static void exit(Screen* screen);
	public static void exit(Screen screen) throws Exception {
		if (getTop() != screen) {
			throw new Exception("A screen cannot exit another screen!");
		}

		if (myScreenToExit != null) {
			throw new Exception("Screen to exit is already set!");
		}

		myScreenToExit = screen;
	}

	public static Screen getTop() {
		if (myStack.size() == 0)
			return null;

		return myStack.get(myStack.size() - 1);
	}

	public static void init() {

	}

	public static boolean isEmpty() {
		return myStack.isEmpty();
	}

	public static void onLogic() {
		if (myScreenToExit == null && myScreenToEnter == null) {
			Screen screen = getTop();
			if (screen != null)
				screen.onLogic();
		}
	}
}
