package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

public class GenericExceptionHandler implements ExceptionHandler {
	static final Logger logger = LogManager.getLogger(GenericExceptionHandler.class);
	private static final String EXCEPTION_MESSAGE = "Oh je, leider ist hier gerade ein unerwarteter Fehler aufgetreten. Tut mir echt leid, aber ich fürchte, da muss nochmal ein Entwickler draufschauen.";

	@Override
	public boolean canHandle(HandlerInput input, Throwable throwable) {
		return true;
	}

	@Override
	public Optional<Response> handle(HandlerInput input, Throwable throwable) {
		logger.error("Exception handled: " + throwable.getStackTrace());
		return input.getResponseBuilder().withSpeech(EXCEPTION_MESSAGE).build();
	}

}
