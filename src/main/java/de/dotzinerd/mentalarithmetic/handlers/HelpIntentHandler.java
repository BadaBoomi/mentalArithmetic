package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

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
			return getQuestPerformer().performContextHelp();
		default:
			return input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech("da gibt es dann die allgemeine Hilfe")
					.build();
		}

	}

}
