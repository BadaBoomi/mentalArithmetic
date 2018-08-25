package de.dotzinerd.mentalarithmetic.model.quests;

import de.dotzinerd.mentalarithmetic.model.Audios;

public class SimpleMultby11Quest extends Quest {
	Integer dig1;
	Integer dig2;
	Integer op1;

	public SimpleMultby11Quest() {
		dig1 = ((int) (Math.random() * 9) + 1);
		dig2 = ((int) (Math.random() * (10 - dig1)));
		op1 = dig1 * 10 + dig2;

	}

	public SimpleMultby11Quest(String id) {
		setVals(id);

	}

	private void setVals(String id) {
		String[] ops = id.split("x");
		dig1 = Integer.valueOf(ops[0]);
		dig2 = Integer.valueOf(ops[1]);
		op1 = dig1 * 10 + dig2;
	}

	@Override
	public String getQuestion() {
		return "Was macht " + op1 + " mal 11 ?";
	}

	@Override
	public String getAnswer() {
		return String.valueOf(op1 * 11);
	}

	@Override
	public String getExplanation() {
		return String.join("\n",
				"Die Summe der beiden Ziffern, " + dig1.toString() + ", und, " + dig2.toString() + ", ist ,"
						+ String.valueOf(dig1 + dig2),
				"Die " + String.valueOf(dig1 + dig2)
						+ ", stellen wir nun zwischen die beiden Ziffern unserer Ausgangszahl " + String.valueOf(op1),
				" <break time=\"250ms\"/> ", "Damit haben wir dann unser Ergebnis, ", dig1.toString(),
				" <break time=\"250ms\"/> ", String.valueOf(dig1 + dig2), " <break time=\"250ms\"/> ", dig2.toString(),
				" <break time=\"250ms\"/> ", "Als Zahl ausgesprochen also, ", getAnswer());
	}

	@Override
	public String getTrainByMaster() {
		return Audios.AUDIO_LESSON_MULT_BY_11_SIMPLE;
	}

	@Override
	public String getTrainByAlexa() {
		return String.join("\n", "Ich gebe Dir noch ein Beispiel. ", getQuestion(), " <break time=\"500ms\"/> ",
				getExplanation());
	}

	@Override
	public String getId() {
		return String.join("x", String.valueOf(dig1), String.valueOf(dig2));
	}

	@Override
	public boolean isCorrectAnswer(String answer) {
		return answer.equals(getAnswer());
	}

	@Override
	public void setId(String id) {
		setVals(id);

	}

}
