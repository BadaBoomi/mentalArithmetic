package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.StateEnum;

public abstract class AbstractIntentHandler implements RequestHandler {
	Map<String, Object> sessionAttributes;
	HandlerInput input;

	void initialize(HandlerInput input) {
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		this.input = input;
	}

	protected StateEnum getQuestState() {
		if (sessionAttributes.containsKey(Constants.KEY_STATE))
			return (StateEnum) sessionAttributes.get(Constants.KEY_STATE);
		else
			return StateEnum.UNKNOWN;
	}

	protected QuestPerformer getQuestPerformer() {
		return new QuestPerformer(input, sessionAttributes);
	}

	abstract public boolean canHandle(HandlerInput input);

	abstract public Optional<Response> handle(HandlerInput arg0);

}
