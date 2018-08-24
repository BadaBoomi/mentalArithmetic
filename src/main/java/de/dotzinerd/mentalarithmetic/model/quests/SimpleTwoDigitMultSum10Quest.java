package de.dotzinerd.mentalarithmetic.model.quests;

import de.dotzinerd.mentalarithmetic.model.Audios;

public class SimpleTwoDigitMultSum10Quest extends Quest {
	int dig1;
	int dig2;
	Integer op1;
	Integer op2;

	public SimpleTwoDigitMultSum10Quest() {
		dig1 = ((int) (Math.random() * 9) + 1);
		dig2 = ((int) (Math.random() * 9) + 1);
		op1 = dig1 * 10 + dig2;
		op2 = dig1 * 10 + (10 - dig2);
	}

	public SimpleTwoDigitMultSum10Quest(String id) {
		String[] ops = id.split("x");
		dig1 = Integer.valueOf(ops[0]);
		dig2 = Integer.valueOf(ops[1]);
		op1 = dig1 * 10 + dig2;
		op2 = dig1 * 10 + (10 - dig2);
	}

	@Override
	public String getQuestion() {
		return "Was macht " + op1 + " mal " + op2 + "?";
	}

	@Override
	public String getAnswer() {
		return String.valueOf(op1*op2);
	}

	@Override
	public String getExplanation() {
		return String.join("\n", "Diese beiden zweistelligen Zahlen haben folgende  Besonderheiten.",
				"Die ersten Ziffern sind gleich. Also beide sind hier " + dig1 + ",",
				"Die beiden letzten Ziffern, also die " + dig2 + ", und die " + (10 - dig2) + ", ergänzen sich zu 10 .",
				"In diesem Falle kann man einfach wie folgt vorgehen.",
				"Multipliziere die erste Ziffer mit einer um 1 höheren Ziffer. ",
				"Im Beispiel also " + dig1 + ", mal " + (dig1 + 1),
				", . Das macht dann " + dig1 * (dig1 + 1)
						+ ", und damit haben wir auch schon den vorderen Teil unseres gesuchten Ergebnisses.",
				"Der hintere Teil des Ergebnisses ergibt sich aus dem Produkt der beiden letzten Ziffern",
				"im Beispiel also zu " + dig2 + ", mal " + (10 - dig2) + ", was " + dig2 * (10 - dig2) + " ist.",
				"Zusammen macht das also dannn " + (op1 * op2));
	}

	@Override
	public String getTrainByMaster() {
		return Audios.AUDIO_LESSON_MULT_2DIGIT_SUM_IS_10;
	}

	@Override
	public String getTrainByAlexa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		return String.join("x", String.valueOf(dig1), String.valueOf(dig2));
	}

	@Override
	public boolean isCorrectAnswer(String answer) {
		return answer.equals(getAnswer());
	}

}
