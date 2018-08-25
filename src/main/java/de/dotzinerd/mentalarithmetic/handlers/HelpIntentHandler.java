package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;

public class HelpIntentHandler extends AbstractIntentHandler {
	static final Logger logger = LogManager.getLogger(HelpIntentHandler.class);

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AMAZON.HelpIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		initialize(input);
		logger.debug("repeating...");
		switch (getQuestState()) {
		case STATE_WAIT_FOR_ANSWER:
			Quest quest=getQuestFromSession();
			logger.debug("Quest: "+ quest);
			return input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech(getQuestFromSession().getExplanation()).build();
		default:
			return input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech("da gibt es dann die allgemeine Hilfe").build();
		}

	}

	private Quest getQuestFromSession() {
		String id = (String) sessionAttributes.get(Constants.QUEST_ID);
		String[] ops = id.split(";");
		Level level = Level.getLevelByName(ops[0]);
		Quest quest = Level.getQuest(level);
		quest.setId(ops[1]);
		return quest;
	}

}
