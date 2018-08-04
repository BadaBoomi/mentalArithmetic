package de.dotzinerd.mentalarithmetic.model;

public class SimpleTwoDigitMultQuest extends Quest {

	public SimpleTwoDigitMultQuest() {
		int dig1 = ((int) (Math.random() * 9) + 1);
		int dig2= ((int) (Math.random() * 9) + 1);
		Integer op1=dig1*10+dig2;
		Integer op2=dig1*10+(10-dig2);
		this.answer = op1 * op2;
		this.question = "Was macht " + op1 + " mal " + op2+"?";
	}

	

}
