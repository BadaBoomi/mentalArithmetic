package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.manager.QuestManager;

public class NumberAnsweredHandler extends AbstractIntentHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("NumberAnswered"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		logger.debug("handle");
		initialize(input);
		switch (stateManager.getPerformerState()) {
		case QUEST:
			return getQuestPerformer().performQuestIntent();
		default:
			return input.getResponseBuilder().withShouldEndSession(false).withSpeech(
					"irgendso eine Zahl in den Raum zu brüllen ist doof. Da gibt es jetzt dann die allgemeine Hilfe")
					.build();
		}
	}

}
