package com.meros.playn.core;

import playn.core.Canvas;

public abstract class Screen {

	//virtual bool onEnter(BITMAP* aBuffer) { return true; }
	public boolean onEnter(Canvas aBuffer)
	{
		return true;
	}

	//virtual void onEntered() {}
	public void onEntered()
	{

	}

	//virtual bool onExit(BITMAP* aBuffer) { return true; }
	public boolean onExit(Canvas aBuffer)
	{
		return true;
	}

	//virtual void onExited() {};
	public void onExited()
	{

	}

	//virtual void onDraw(BITMAP* aBuffer) = 0;
	public abstract void onDraw(Canvas aBuffer) ;

	//virtual void onLogic() = 0;
	public abstract void onLogic();

	//bool isTop() const;
	public boolean isTop()
	{
		return ScreenManager.getTop() == this;
	}

	//void exit();
	public void exit()
	{
		try {
			ScreenManager.exit(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
