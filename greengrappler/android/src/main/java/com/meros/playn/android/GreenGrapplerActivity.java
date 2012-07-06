package com.meros.playn.android;

import playn.android.AndroidGLContext;
import playn.android.GameActivity;
import playn.core.PlayN;

import com.meros.playn.core.GreenGrappler;

public class GreenGrapplerActivity extends GameActivity {

	@Override
	public void main(){
		
		platform().assets().setPathPrefix("com/meros/playn/resources");
		PlayN.run(new GreenGrappler(true, new GreenGrappler.ExitCallback() {

			@Override
			public void exit() {
				// TODO Auto-generated method stub

			}
		}));
	}
}
