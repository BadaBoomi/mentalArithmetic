package de.dotzinerd.mentalarithmetic.model;

public class SimpleTwoDigitSquareQuest extends Quest {

	public SimpleTwoDigitSquareQuest() {
		Integer op1 = ((int) (Math.random() * 9) + 1) * 10 +5;
		this.answer = op1 * op1;
		this.question = "Was macht " + op1 + " zum Quadrat ?";
	}

	@Override
	public String getQuestion() {
		return this.question;
	}

	@Override
	public Integer getAnswer() {
		return this.answer;
	}

}
