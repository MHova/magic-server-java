package com.mhova.domain;

public enum Step {
	/* @formatter:off */
	UNTAP("Untap"),
	UPKEEP("Upkeep"),
	DRAW("Draw"),
	MAIN_1("Pre-combat Main"),
	BEGINNING_OF_COMBAT("Beginning of Combat"),
	ATTACKERS("Declare Attackers"),
	BLOCKERS("Declare Blockers"),
	FIRST_STRIKE_DAMAGE("First Strike Damage"),
	DAMAGE("Damage"),
	END_OF_COMBAT("End of Combat"),
	MAIN_2("Post-combat Main"),
	END("End Step"),
	CLEANUP("Cleanup Step");
	/* @formatter:on */

	public final String name;

	private Step(String name) {
		this.name = name;
	}
}
