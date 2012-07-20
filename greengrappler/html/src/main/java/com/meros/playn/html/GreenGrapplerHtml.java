package com.meros.playn.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import playn.html.HtmlPlatform.Configuration;
import playn.html.HtmlPlatform.Mode;

import com.meros.playn.core.GreenGrappler;

public class GreenGrapplerHtml extends HtmlGame {

	@Override
	public void start() {
		Configuration configuration = new Configuration();
		configuration.mode = Mode.WEBGL;
		
		HtmlPlatform platform = HtmlPlatform.register(configuration);
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
