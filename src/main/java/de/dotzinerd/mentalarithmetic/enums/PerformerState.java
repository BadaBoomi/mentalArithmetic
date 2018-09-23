package de.dotzinerd.mentalarithmetic.enums;

public enum PerformerState {
	UNKNOWN, TRAINING, QUEST, SETUP;

	public static PerformerState getPerformerByName(String name) {
		for (PerformerState en : PerformerState.values()) {
			if (en.name().equals(name)) {
				return en;
			}
		}
		return UNKNOWN;
	}
}
