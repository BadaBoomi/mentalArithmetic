package de.dotzinerd.mentalarithmetic.enums;

public enum TrainingState {
	STATE_NEW_TRAINING, STATE_EXPLAIN_TRAINING_BY_MASTER, STATE_EXPLAIN_TRAINING_BY_ALEXA, STATE_GIVE_EXAMPLE,
	STATE_CHECK_TRAINED_ABILITY, UNKNOWN;

	public static TrainingState getStateByName(String name) {
		for (TrainingState en : TrainingState.values()) {
			if (en.name().equals(name)) {
				return en;
			}
		}
		return UNKNOWN;
	}
}
