package de.dotzinerd.mentalarithmetic.model;

public abstract class Quest {

	Integer answer;
	String question;
	Integer level = 1;

	public String getQuestion() {
		return this.question;
	}

	public Integer getAnswer() {
		return this.answer;
	}
}
