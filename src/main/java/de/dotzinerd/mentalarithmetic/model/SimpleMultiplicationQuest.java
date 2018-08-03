package de.dotzinerd.mentalarithmetic.model;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;

public class SimpleMultiplicationQuest extends Quest {
	static final Logger logger = LogManager.getLogger(SimpleMultiplicationQuest.class);

	public SimpleMultiplicationQuest(HandlerInput input, Map<String, Object> sessionAttributes) {
		super(input, sessionAttributes);
	}


	@Override
	Integer getMaxTurn() {
		return 4;
	}

	@Override
	Optional<Response> performTurn(Boolean isAnswerCorrect) {
		logger.debug("perform turn...");
			
		Integer op1 = (int) (Math.random() * 8) + 2;
		Integer op2 = (int) (Math.random() * 8) + 2;
		Integer result = op1 * op2;
		String question = "Was macht " + op1 + " mal " + op2 + "?";
		String speechText = (isAnswerCorrect == null) ? question : getAnswerString(isAnswerCorrect) + ". " + question;
		this.sessionAttributes.put(EXPECTED_ANSWER, String.valueOf(result));

		// Create the Simple card content.

	

		return input.getResponseBuilder().addElicitSlotDirective(SLOT_USER_RESPONSE, intent).withShouldEndSession(false)
				.withReprompt(speechText).withSpeech(speechText).build();

	}

	@Override
	String getAnswerString(boolean isAnswerCorrect) {
		return (isAnswerCorrect) ? "Richtig!" : "Leider Falsch!";
	}

}
