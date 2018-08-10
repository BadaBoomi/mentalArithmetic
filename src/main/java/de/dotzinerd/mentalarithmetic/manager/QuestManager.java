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

	public QuestPerformer getCurrentQuest(Intent intent, IntentEnum intentID, HandlerInput input, Map<String, Object> sessionAttributes) {
		logger.debug("intent: " + intent.getName());

		if (currentQuest == null) {
			switch (intentID) {
			case SimpleEinmalEins:
				return new SimpleMultiplicationQuestPerformer(intent, input, sessionAttributes);
			case SimpleMultiplication:
				return new SimpleTwoDigitSquareQuestPerformer(intent, input, sessionAttributes);
			case SimpleSquares:
				return new SimpleTwoDigitMultQuestPerformer(intent, input, sessionAttributes);
			default:
				return new SimpleMultiplicationQuestPerformer(intent, input, sessionAttributes);
			}

		} else {
			return currentQuest;
		}
	}
}
