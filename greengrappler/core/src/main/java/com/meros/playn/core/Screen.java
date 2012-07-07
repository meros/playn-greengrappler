package com.meros.playn.core;

import playn.core.Surface;

public abstract class Screen {

	// void exit();
	public void exit() {
		try {
			ScreenManager.exit(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// bool isTop() const;
	public boolean isTop() {
		return ScreenManager.getTop() == this;
	}

	// virtual void onDraw(BITMAP* aBuffer) = 0;
	public abstract void onDraw(Surface buffer);

	// virtual bool onEnter(BITMAP* aBuffer) { return true; }
	public boolean onEnter(Surface buffer) {
		return true;
	}

	// virtual void onEntered() {}
	public void onEntered() {

	}

	// virtual bool onExit(BITMAP* aBuffer) { return true; }
	public boolean onExit(Surface buffer) {
		return true;
	}

	// virtual void onExited() {};
	public void onExited() {

	}

	// virtual void onLogic() = 0;
	public abstract void onLogic();
}
