package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.manager.QuestManager;

public class RepeatIntentHandler extends AbstractIntentHandler {
	static final Logger logger = LogManager.getLogger(RepeatIntentHandler.class);

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AMAZON.RepeatIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		initialize(input);
		logger.debug("repeating...");
		switch (stateManager.getPerformerState()) {
		case QUEST:
			return getQuestPerformer().repeatQuestion();
		default:
			return input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech("an der Stelle macht Wiederholen keinen Sinn, da gibt es dann die allgemeine Hilfe")
					.build();
		}

	}

}
