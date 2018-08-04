package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class DefaultStateHandler implements RequestHandler {
	static final Logger logger = LogManager.getLogger(DefaultStateHandler.class);

	public DefaultStateHandler() {
		super();
		logger.debug("constructor called");
	}

	public boolean canHandle(HandlerInput handlerInput) {
		logger.debug("canHandle: true");
		return true;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle");
		return handlerInput.getResponseBuilder().withShouldEndSession(true)
				.withSpeech("Da kommt noch was neues demnächst.").build();
	}

}
