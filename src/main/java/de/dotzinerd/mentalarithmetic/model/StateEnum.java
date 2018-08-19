package de.dotzinerd.mentalarithmetic.model;

public enum StateEnum {

	UNKNOWN("UNKNOWN"), STATE_NEW_QUEST(Constants.STATE_NEW_QUEST), STATE_NEXT_INTENT(Constants.STATE_NEXT_INTENT),
	STATE_WAIT_FOR_ANSWER(Constants.STATE_WAIT_FOR_ANSWER), STATE_PERFORM_QUEST(Constants.STATE_PERFORM_QUEST),;

	private final String saveValue;

	private StateEnum(String saveAs) {
		this.saveValue = saveAs;
	}

	public static StateEnum getEnumByName(String name) {
		for (StateEnum en : StateEnum.values()) {
			if (en.saveValue.equals(name)) {
				return en;
			}
		}
		return UNKNOWN;
	}

	public String getStateName() {
		return this.saveValue;
	}
}
