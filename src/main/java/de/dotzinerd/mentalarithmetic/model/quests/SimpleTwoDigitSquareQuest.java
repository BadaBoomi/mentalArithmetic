package de.dotzinerd.mentalarithmetic.model.quests;

public class SimpleTwoDigitSquareQuest extends Quest {
	Integer op;

	public SimpleTwoDigitSquareQuest() {
		op = ((int) (Math.random() * 9) + 1) * 10 + 5;
	}

	public SimpleTwoDigitSquareQuest(String id) {
		op = Integer.valueOf(id);
	}

	@Override
	public String getQuestion() {
		return "Was macht " + op + ", zum Quadrat?";
	}

	@Override
	public String getAnswer() {
		return String.valueOf(op * op);
	}

	@Override
	public String getExplanation() {
		// TODO Auto-generated method stub
		return null;
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
		return String.valueOf(op);
	}

	@Override
	public boolean isCorrectAnswer(String answer) {
		return answer.equals(getAnswer());
	}

	@Override
	public void setId(String id) {
		op = Integer.valueOf(id);
		
	}

}
