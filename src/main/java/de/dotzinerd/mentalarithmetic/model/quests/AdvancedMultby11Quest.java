package de.dotzinerd.mentalarithmetic.model.quests;

import de.dotzinerd.mentalarithmetic.enums.Level;

public class AdvancedMultby11Quest extends Quest {
	int dig1;
	int dig2;
	Integer op1;

	public AdvancedMultby11Quest() {
		dig1 = ((int) (Math.random() * 9) + 1);
		dig2 = Math.max(10-dig1, ((int) (Math.random() * 9) + 1));

		op1 = dig1 * 10 + dig2;

	}

	public AdvancedMultby11Quest(String id) {
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
		// TODO Auto-generated method stub
		return "fehlt noch";
	}

	@Override
	public String getTrainByMaster() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void setId(String id) {
		setVals(id);
		
	}

	@Override
	public Level getlevel() {
		return Level.LVL_MULT_BY_11_ADVANCED;
	}

	

}
