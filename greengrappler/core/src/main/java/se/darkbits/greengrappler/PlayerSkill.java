package se.darkbits.greengrappler;

import playn.core.PlayN;

public class PlayerSkill {

	private static float skill = 0.5f;

	public static void playerDidSomethingClever(float howClever,
			float howImportant) {
		if (howClever > skill) {
			skill = UtilMethods.lerp(skill, howClever, howImportant);
		}
		PlayN.log().debug("Player skill: " + skill);
	}

	public static void playerDidSomethingStupid(float howClever,
			float howImportant) {
		if (howClever < skill) {
			skill = UtilMethods.lerp(skill, howClever, howImportant);
		}

		PlayN.log().debug("Player skill: " + skill);
	}

	public static float get() {
		return skill;
	}
}
