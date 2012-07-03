package com.meros.playn.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.meros.playn.core.GreenGrappler;

public class GreenGrapplerHtml extends HtmlGame {

	@Override
	public void start() {
		HtmlPlatform platform = HtmlPlatform.register();
		platform.assets().setPathPrefix("greengrappler/");
		PlayN.run(new GreenGrappler(
				true, 
				new GreenGrappler.ExitCallback(){

					@Override
					public void exit() {
						//DO nothing
					}}));
	}
}
