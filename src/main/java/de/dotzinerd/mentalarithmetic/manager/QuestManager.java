package de.dotzinerd.mentalarithmetic.manager;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.model.Intent;

import de.dotzinerd.mentalarithmetic.enums.IntentId;
import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.enums.QuestState;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.quests.AdvancedMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultiplicationQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitSquareQuest;

public class QuestManager {
	static final Logger logger = LogManager.getLogger(QuestManager.class);
	private static final QuestManager singleton = new QuestManager();

	private QuestManager() {
	};

	public static QuestManager getManager() {
		return singleton;
	}

	public  Quest getNewQuestByLevel(Level level) {
		logger.debug("getNewQuestByLevel for:" + level);
		switch (level) {
		case LVL_MULT_BY_11_SIMPLE:
			return new SimpleMultby11Quest();
		case LVL_MULT_BY_11_ADVANCED:
			return new AdvancedMultby11Quest();
		case LVL_SIMPLE_MULTIPLICATION:
			return new SimpleMultiplicationQuest();
		case LVL_MULT_2DIGIT_SUM_IS_10:
			return new SimpleTwoDigitSquareQuest();
		case LVL_2DIGIT_SQUARE:
			return new SimpleTwoDigitSquareQuest();
		default:
			return new SimpleMultiplicationQuest();
		}

	}

	public  Quest getQuestFromSession(Map<String, Object> sessionAttributes) {
		String id = (String) sessionAttributes.get(Constants.QUEST_ID);
		String[] ops = id.split(";");
		Level level = Level.getLevelByName(ops[0]);
		switch (level) {
		case LVL_MULT_BY_11_SIMPLE:
			return new SimpleMultby11Quest(ops[1]);
		case LVL_MULT_BY_11_ADVANCED:
			return new AdvancedMultby11Quest(ops[1]);
		case LVL_SIMPLE_MULTIPLICATION:
			return new SimpleMultiplicationQuest(ops[1]);
		case LVL_MULT_2DIGIT_SUM_IS_10:
			return new SimpleTwoDigitSquareQuest(ops[1]);
		case LVL_2DIGIT_SQUARE:
			return new SimpleTwoDigitSquareQuest(ops[1]);
		default:
			return new SimpleMultiplicationQuest(ops[1]);
		}
	}
	
	public Quest getQuestByIntent(Intent intent, Map<String, Object> sessionAttributes) {
		IntentId intentID;
		if (!getState(sessionAttributes).equals(QuestState.STATE_NEXT_INTENT)) {
			intentID = IntentId.getIntentIdByName((String) sessionAttributes.get(Constants.KEY_INTENT));
		} else {
			intentID = IntentId.getIntentIdByName(intent.getName());
		}
		switch (intentID) {
		case SimpleEinmalEins:
			return new SimpleMultiplicationQuest();
		
		case SimpleMultiplication:

			int lvl = (int) (Math.random() * 3 + 1);
			switch (lvl) {
			case 1:
				return new SimpleMultby11Quest();
				
			case 2:
				return new AdvancedMultby11Quest();
			default:
				return new AdvancedMultby11Quest();
			}
			
		case SimpleSquares:
			return new SimpleTwoDigitSquareQuest();
		default:
			break;
		}
		return null;
	}

	
	public QuestState getState(Map<String, Object> sessionAttributes) {
		logger.debug("sessionAttributes: " + sessionAttributes);
		if (sessionAttributes.containsKey(Constants.KEY_QUEST_STATE)) {
			String stateName = (String) sessionAttributes.get(Constants.KEY_QUEST_STATE);
			return QuestState.getStateByName(stateName);
		} else
			return QuestState.UNKNOWN;

	}
}
