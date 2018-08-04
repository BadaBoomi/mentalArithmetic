package de.dotzinerd.mentalarithmetic.manager;

import java.util.Map;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.StatusEnum;
import de.dotzinerd.mentalarithmetic.model.questperformer.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.questperformer.SimpleMultiplicationQuestPerformer;
import de.dotzinerd.mentalarithmetic.model.questperformer.SimpleTwoDigitSquareQuestPerformer;

public class QuestManager {
	QuestPerformer currentQuest = null;

	public QuestPerformer getCurrentQuest(HandlerInput input, Map<String, Object> sessionAttributes,
			StatusEnum status) {
		Intent intent = (Intent) ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
		String questName = intent.getSlots().get(Constants.SLOT_QUEST_NAME).getValue();
		System.out.println("questName :" + questName);
		if (currentQuest == null) {
			switch (status) {
			case SV_SIMPLE_MULT:
				return new SimpleMultiplicationQuestPerformer(input, sessionAttributes);
			case SV_SIMPLE_2Digit_SQUARES:

			default:
				return new SimpleTwoDigitSquareQuestPerformer(input, sessionAttributes);
			}

		} else {
			return currentQuest;
		}
	}
}