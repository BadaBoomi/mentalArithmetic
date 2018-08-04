package de.dotzinerd.mentalarithmetic.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTwoDigitMultQuest extends Quest {
	static final Logger logger = LogManager.getLogger(SimpleTwoDigitMultQuest.class);

	public SimpleTwoDigitMultQuest() {
		int dig1 = ((int) (Math.random() * 9) + 1);
		int dig2 = ((int) (Math.random() * 9) + 1);
		Integer op1 = dig1 * 10 + dig2;
		Integer op2 = dig1 * 10 + (10 - dig2);

		this.answer = op1 * op2;
		this.question = "Was macht " + op1 + " mal " + op2 + "?";
		logger.debug("question:" + this.question + ", answer:" + answer);
	}

}
