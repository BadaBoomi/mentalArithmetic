package de.dotzinerd.mentalarithmetic.model.quests;

import de.dotzinerd.mentalarithmetic.enums.Level;

public class SimpleMultiplicationQuest extends Quest {
	Integer op1, op2;

	private String[] explanations = { "Beim kleinen Einmaleins hilft nur üben, üben, üben. ",
			"Hier gibt es leider keinen speziellen Kniff. Du must einfach weiter üben. Das wird schon noch!",
			"Die Übung macht hier den Meister." };;

	public SimpleMultiplicationQuest() {
		op1 = (int) (Math.random() * 8) + 2;
		op2 = (int) (Math.random() * 8) + 2;

	}
	
	public SimpleMultiplicationQuest(String id) {
		setVals(id);

	}

	private void setVals(String id) {
		String[]ops=id.split("x");
		op1 = Integer.valueOf(ops[0]);
		op2 =  Integer.valueOf(ops[1]);
	}

	@Override
	public String getQuestion() {
		return "Was macht " + op1 + " mal " + op2 + "?";
	}

	@Override
	public String getAnswer() {
		return String.valueOf(op1 * op2);
	}

	@Override
	public String getExplanation() {
		return explanations[(int) Math.random() * explanations.length + 1];
	}

	@Override
	public String getTrainByMaster() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTrainByAlexa() {
		return getExplanation();
	}

	@Override
	public String getId() {
		return String.join("x", String.valueOf(op1), String.valueOf(op2));
	}

	@Override
	public boolean isCorrectAnswer(String answer) {
		return answer.equals(getAnswer());
	}

	@Override
	public void setId(String id) {
		setVals(id);
		
	}

	@Override
	public Level getlevel() {
		return Level.LVL_SIMPLE_MULTIPLICATION;
	}

}
