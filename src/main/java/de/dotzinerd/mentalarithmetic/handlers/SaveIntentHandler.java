package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.manager.UserManager;
import de.dotzinerd.mentalarithmetic.model.Constants;

public class SaveIntentHandler extends AbstractIntentHandler {
	static final Logger logger = LogManager.getLogger(SaveIntentHandler.class);

	@Override
	public boolean canHandle(HandlerInput handlerInput) {
		return handlerInput.matches(intentName("AMAZON.StopIntent"))
				|| handlerInput.matches(intentName("AMAZON.PauseIntent"))
				|| handlerInput.matches(intentName("AMAZON.CancelIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("saving session...");
		Map<String, Object> persistentAttributes = handlerInput.getAttributesManager().getPersistentAttributes();
		persistentAttributes.put("User", sessionAttributes.get(Constants.KEY_USER));
		handlerInput.getAttributesManager().savePersistentAttributes();
		return handlerInput.getResponseBuilder().withShouldEndSession(true).withSpeech("Dann bis zum nächsten mal")
				.build();
	}

}
