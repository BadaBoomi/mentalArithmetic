package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

public class SaveIntentHandler extends AbstractIntentHandler {

	@Override
	public boolean canHandle(HandlerInput handlerInput) {
		return handlerInput.matches(intentName("AMAZON.StopIntent"))
				|| handlerInput.matches(intentName("AMAZON.PauseIntent"))
				|| handlerInput.matches(intentName("AMAZON.CancelIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput handlerInput) {
		Map<String, Object> persistentAttributes = handlerInput.getAttributesManager().getPersistentAttributes();
		persistentAttributes.put("foo", "baz");
		return Optional.empty();
	}

}