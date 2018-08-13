package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;

public class RepeatIntentHandler implements RequestHandler {
	static final Logger logger = LogManager.getLogger(RepeatIntentHandler.class);

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AMAZON.RepeatIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

		if (sessionAttributes.containsKey(Constants.KEY_STATE)
				&& sessionAttributes.get(Constants.KEY_STATE).equals(Constants.STATE_PERFORM_QUEST)) {
			QuestPerformer questPerformer = new QuestPerformer(input, sessionAttributes);
			return questPerformer.repeatQuestion();
		}
		
		return input.getResponseBuilder().withShouldEndSession(false)
				.withSpeech("an der Stelle macht Wiederholen keinen Sinn, da gibt es dann die allgemeine Hilfe")
				.build();

	}

}
