package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;

public abstract class AbstractIntentHandler implements RequestHandler {
	Map<String, Object> sessionAttributes;
	HandlerInput input;

	void initialize(HandlerInput input) {
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		this.input = input;
	}

	protected boolean isRunningQuest() {
		return (sessionAttributes.containsKey(Constants.KEY_STATE)
				&& sessionAttributes.get(Constants.KEY_STATE).equals(Constants.STATE_PERFORM_QUEST));
	}

	protected QuestPerformer getQuestPerformer() {
		return new QuestPerformer(input, sessionAttributes);
	}

	abstract public boolean canHandle(HandlerInput input);

	abstract public Optional<Response> handle(HandlerInput arg0);

}
