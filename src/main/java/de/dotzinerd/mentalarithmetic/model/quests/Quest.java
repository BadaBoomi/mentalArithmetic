package de.dotzinerd.mentalarithmetic.model.quests;

import de.dotzinerd.mentalarithmetic.enums.Level;

public abstract class Quest {
	protected String id, question;

	public abstract String getQuestion();

	public abstract String getAnswer();

	public abstract String getExplanation();

	public abstract String getTrainByMaster();

	public abstract String getTrainByAlexa();

	public abstract String getId();

	public abstract void setId(String id);

	public abstract boolean isCorrectAnswer(String answer);
	
	public abstract Level getlevel();
}
