package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.StateEnum;

public abstract class AbstractIntentHandler implements RequestHandler {
	HandlerInput input;
	protected Map<String, Object> sessionAttributes;
	protected Intent intent;

	void initialize(HandlerInput input) {
		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		this.intent = intentRequest.getIntent();
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		this.sessionAttributes.put("ORIGINAL_INTENT", this.intent.getName());
		this.input = input;
	}

	protected StateEnum getQuestState() {
		if (sessionAttributes.containsKey(Constants.KEY_STATE))
			return StateEnum.getEnumByName((String) sessionAttributes.get(Constants.KEY_STATE));
		else
			return StateEnum.UNKNOWN;
	}

	protected QuestPerformer getQuestPerformer() {
		return new QuestPerformer(intent, input, sessionAttributes);
	}

	abstract public boolean canHandle(HandlerInput input);

	abstract public Optional<Response> handle(HandlerInput arg0);

}
