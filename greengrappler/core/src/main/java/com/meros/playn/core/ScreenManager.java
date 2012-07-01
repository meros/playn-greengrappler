package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;

public class ScreenManager {
	static ArrayList<Screen> myStack = new ArrayList<Screen>();
	static Screen myScreenToExit = null;
	static Screen myScreenToEnter = null;
	
//	static void init();
	public static void init()
	{
		
	}
	
//	static void destroy();
	public static void destroy()
	{
		myStack.clear();
	}
	
//	static void onLogic();
	public static void onLogic()
	{
		if (myScreenToExit == null && myScreenToEnter == null)
		{
			Screen screen = getTop();
			if (screen != null)
				screen.onLogic();
		}		
	}
	
//	static void draw(BITMAP* buffer);
	public static void draw(Canvas aBuffer)
	{
		if (myScreenToExit == null && myScreenToEnter == null)
		{
			Screen screen = getTop();
			if (screen != null)
				screen.onDraw(aBuffer);
		}

		if (myScreenToExit != null)
		{
			boolean exitDone = myScreenToExit.onExit(aBuffer);

			if (!exitDone)
				return;

			for (int i = 0; i < myStack.size(); i++)
			{
				if (myStack.get(i) == myScreenToExit)
				{
					myStack.remove(i);
					break;
				}
			}
			
			myScreenToExit.onExited();
			myScreenToExit = null;

			if (getTop() != null)
			{
				myScreenToEnter = getTop();
			}
		}

		if (myScreenToEnter != null)
		{
			boolean enterDone = myScreenToEnter.onEnter(aBuffer);
			if (!enterDone)
				return;

			myScreenToEnter.onEntered();

			myScreenToEnter = null;
		}
	}
	
//	static Screen* getTop();
	public static Screen getTop()
	{
		if (myStack.size() == 0)
			return null;
		
		return myStack.get(myStack.size() - 1);	
	}
	
//	static bool isEmpty();
	public static boolean isEmpty()
	{
		return myStack.isEmpty();
	}
	
//	static void add(Screen* screen);
	public static void add(Screen screen)
	{
		myStack.add(screen);
		myScreenToEnter = screen;
	}
	
//	static void enter(Screen* screen);
	public static void enter(Screen screen)
	{
		if (getTop() != null)
		{
			myScreenToExit = getTop();
		}

		myStack.add(screen);
	}
	
//	static void exit(Screen* screen);
	public static void exit(Screen screen) throws Exception
	{
		if (getTop() != screen)
		{
			throw new Exception("A screen cannot exit another screen!");
		}

		if (myScreenToExit != null)
		{
			throw new Exception("Screen to exit is already set!");
		}

		myScreenToExit = screen;
	}
//
//private:
//	ScreenManager();
//	static std::vector<Screen*> myStack;
//	static Screen* myScreenToExit;
//	static Screen* myScreenToEnter;
}
