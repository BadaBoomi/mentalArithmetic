package de.dotzinerd.mentalarithmetic.model.quests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTwoDigitMultQuest extends Quest {
	static final Logger logger = LogManager.getLogger(SimpleTwoDigitMultQuest.class);

	public static enum Level {
		LVL_MULT_2DIGIT_SUM_IS_10, LVL_MULT_BY_11_SIMPLE, LVL_MULT_BY_11_ADVANCED;

		public static Level getRandomLevelBelow(Level level) {
			int rLevel = (int) (Math.random() * level.ordinal()) + 1;
			return Level.values()[rLevel];
		}

	}

	public SimpleTwoDigitMultQuest(Level questLevel) {
		int dig1, dig2;
		Integer op1, op2;
		logger.debug("quest level: " + questLevel);
		switch (questLevel) {
		case LVL_MULT_2DIGIT_SUM_IS_10:
			dig1 = ((int) (Math.random() * 9) + 1);
			dig2 = ((int) (Math.random() * 9) + 1);
			op1 = dig1 * 10 + dig2;
			op2 = dig1 * 10 + (10 - dig2);
			this.answer = op1 * op2;
			this.question = "Was macht " + op1 + " mal " + op2 + "?";
			break;
		case LVL_MULT_BY_11_SIMPLE:
			dig1 = ((int) (Math.random() * 9) + 1);
			dig2 = ((int) (Math.random() * (10 - dig1)));
			op1 = dig1 * 10 + dig2;

			this.answer = op1 * 11;
			this.question = "Was macht " + op1 + " mal 11 ?";
			break;
		case LVL_MULT_BY_11_ADVANCED:
			dig1 = ((int) (Math.random() * 9) + 1);
			dig2 = ((int) (Math.random() * 9) + 1);
			op1 = dig1 * 10 + dig2;

			this.answer = op1 * 11;
			this.question = "Was macht " + op1 + " mal 11 ?";
		}

		logger.debug("question:" + this.question + ", answer:" + answer);
	}

}
