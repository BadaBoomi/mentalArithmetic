package de.dotzinerd.mentalarithmetic.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum StatusEnum {
	SV_SIMPLE_MULT, SV_SIMPLE_2Digit_SQUARES, HELP_INTENT, TRAIN_INTENT, UNKNOWN;
	static final Logger logger = LogManager.getLogger(StatusEnum.class);

	public static StatusEnum getEnumByName(String name) {
		logger.debug("searching: " + name);

		for (StatusEnum en : StatusEnum.values()) {
			if (en.name().equals(name)) {
				return en;
			}
		}
		logger.debug("returning UNKNOWN");
		return UNKNOWN;
	}

}
