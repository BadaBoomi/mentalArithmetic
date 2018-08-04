package de.dotzinerd.mentalarithmetic.model;

public class SimpleMultiplicationQuest extends Quest {

	public SimpleMultiplicationQuest() {
		Integer op1 = (int) (Math.random() * 8) + 2;
		Integer op2 = (int) (Math.random() * 8) + 2;
		this.answer = op1 * op2;
		this.question = "Was macht " + op1 + " mal " + op2 + "?";
	}

	

}
