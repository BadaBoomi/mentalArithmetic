package de.dotzinerd.mentalarithmetic.manager;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;

import de.dotzinerd.mentalarithmetic.handlers.PerformQuestStateHandler;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.IntentEnum;
import de.dotzinerd.mentalarithmetic.model.questperformer.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.questperformer.SimpleMultiplicationQuestPerformer;
import de.dotzinerd.mentalarithmetic.model.questperformer.SimpleTwoDigitMultQuestPerformer;
import de.dotzinerd.mentalarithmetic.model.questperformer.SimpleTwoDigitSquareQuestPerformer;

public class QuestManager {
	static final Logger logger = LogManager.getLogger(QuestManager.class);
	QuestPerformer currentQuest = null;

	public QuestPerformer getCurrentQuest(HandlerInput input, Map<String, Object> sessionAttributes,
			IntentEnum intentID) {
		logger.debug("intentID: " +intentID);
		if (intentID == null) {
			intentID = IntentEnum.getEnumByName((String) sessionAttributes.get(Constants.KEY_QUEST_TYPE));
		} else {
			sessionAttributes.put(Constants.KEY_QUEST_TYPE, intentID.getIntentName());
		}
		if (currentQuest == null) {
			switch (intentID) {
			case SimpleEinmalEins:
				return new SimpleMultiplicationQuestPerformer(input, sessionAttributes);
			case SimpleMultiplication:
				return new SimpleTwoDigitSquareQuestPerformer(input, sessionAttributes);
			case SimpleSquares:
				return new SimpleTwoDigitMultQuestPerformer(input, sessionAttributes);
			default:
				return new SimpleTwoDigitSquareQuestPerformer(input, sessionAttributes);
			}

		} else {
			return currentQuest;
		}
	}
}
