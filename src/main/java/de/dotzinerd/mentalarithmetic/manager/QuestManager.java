package de.dotzinerd.mentalarithmetic.manager;

import java.util.Map;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.Quest;
import de.dotzinerd.mentalarithmetic.model.SimpleMultiplicationQuest;

public class QuestManager {
	Quest currentQuest = null;

	public Quest getCurrentQuest(HandlerInput input, Map<String, Object> sessionAttributes) {
		Intent intent = (Intent) ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent();
		String questName = intent.getSlots().get(Constants.SLOT_QUEST_NAME).getValue();
		System.out.println("questName :" + questName);
		if (currentQuest == null) {
			return new SimpleMultiplicationQuest(input, sessionAttributes);
		} else {
			return currentQuest;
		}
	}
}
