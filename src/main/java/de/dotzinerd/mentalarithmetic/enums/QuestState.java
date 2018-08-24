package de.dotzinerd.mentalarithmetic.enums;

public enum QuestState {
	STATE_NEW_QUEST, STATE_NEXT_INTENT, STATE_WAIT_FOR_ANSWER, UNKNOWN;

	public static QuestState getStateByName(String name) {
		for (QuestState en : QuestState.values()) {
			if (en.name().equals(name)) {
				return en;
			}
		}
		return UNKNOWN;
	}
}
