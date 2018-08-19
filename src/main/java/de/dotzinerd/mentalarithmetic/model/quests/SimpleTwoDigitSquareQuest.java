package de.dotzinerd.mentalarithmetic.model.quests;

public class SimpleTwoDigitSquareQuest extends Quest {

	public SimpleTwoDigitSquareQuest() {
		Integer op1 = ((int) (Math.random() * 9) + 1) * 10 +5;
		this.answer = op1 * op1;
		this.question = "Was macht " + op1 + " zum Quadrat ?";
	}



}
