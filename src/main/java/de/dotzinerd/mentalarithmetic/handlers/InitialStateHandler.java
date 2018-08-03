package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.manager.QuestManager;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.Introduction;
import de.dotzinerd.mentalarithmetic.model.Quest;

public class InitialStateHandler implements RequestHandler {
	static final Logger logger = LogManager.getLogger(InitialStateHandler.class);

	public InitialStateHandler() {
		super();
		logger.debug("constructor called");
	}

	public boolean canHandle(HandlerInput handlerInput) {
		boolean yesIcan = handlerInput.matches(requestType(LaunchRequest.class));
		logger.debug("canHandle: " + yesIcan);

		return yesIcan;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		/*
		 * LaunchRequest - skill is called without any intent!
		 */
		logger.debug("handle");
		return new Introduction().getResponse(handlerInput);

	}

}
