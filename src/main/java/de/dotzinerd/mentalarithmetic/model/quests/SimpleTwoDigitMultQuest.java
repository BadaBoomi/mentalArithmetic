package de.dotzinerd.mentalarithmetic.model.quests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTwoDigitMultQuest extends Quest {
	static final Logger logger = LogManager.getLogger(SimpleTwoDigitMultQuest.class);

	public static enum Level {
		LVL_MULT_2DIGIT_SUM_IS_10, LVL_MULT_BY_11_SIMPLE, LVL_MULT_BY_11_ADVANCED;

		public static Level getRandomLevelBelow(Level level) {
			int rLevel = (int) (Math.random() * level.ordinal());
			return Level.values()[rLevel];
		}

	}

	public SimpleTwoDigitMultQuest(Level questLevel) {
		int dig1, dig2;
		Integer op1, op2;
		logger.debug("quest level: " + questLevel);
		switch (questLevel) {
		case LVL_MULT_2DIGIT_SUM_IS_10:
			questDoubleDigitDecimalWithSamefirstDigitWhereSumOfLastDigitsIs10();
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

	private void questDoubleDigitDecimalWithSamefirstDigitWhereSumOfLastDigitsIs10() {
		int dig1;
		int dig2;
		Integer op1;
		Integer op2;
		dig1 = ((int) (Math.random() * 9) + 1);
		dig2 = ((int) (Math.random() * 9) + 1);
		op1 = dig1 * 10 + dig2;
		op2 = dig1 * 10 + (10 - dig2);
		this.answer = op1 * op2;
		this.question = "Was macht " + op1 + " mal " + op2 + "?";
		this.explanation = String.join("\n", "Diese beiden zweistelligen Zahlen haben folgende  Besonderheiten.",
				"Die ersten Ziffern sind gleich. Also beide sind hier " + dig1 + ",",
				"Die beiden letzten Ziffern, also die " + dig2 + ", und die" + (10 - dig2) + ", ergänzen sich zu 10 ,.",
				"In diesem Falle kann man einfach wie folgt vorgehen.",
				"Multipliziere die erste Ziffer mit einer um 1 höheren Ziffer. ",
				"Im Beispiel also " + dig1 + " mal " + (dig1 + 1),
				", Das macht dann " + dig1 * (dig1 + 1)
						+ ", und damit haben wir auch schon den vorderen Teil unseres gesuchten Ergebnisses.",
				"Der hintere Teil des Ergebnisses ergibt sich aus dem Produkt der beiden letzten Ziffern",
				"im Beispiel also zu " + dig2 + ", mal " + (10 - dig2) + "was " + dig2 * (10 - dig2) + " ist.",
				"Zusammen macht das also dannn " + this.answer);
	}

}
