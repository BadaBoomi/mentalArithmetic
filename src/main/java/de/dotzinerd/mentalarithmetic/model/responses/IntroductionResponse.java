package de.dotzinerd.mentalarithmetic.model.responses;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

public class IntroductionResponse {

	public Optional<Response> getResponse(HandlerInput handlerInput) {
		return handlerInput.getResponseBuilder().withShouldEndSession(false)
				.withSpeech("Dann gebe ich jetzt mal eine Einführung.").build();
	}

}
