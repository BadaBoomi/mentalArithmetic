package de.dotzinerd.mentalarithmetic.handlers;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.enums.QuestState;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;

public abstract class AbstractIntentHandler implements RequestHandler {
	static final Logger logger = LogManager.getLogger(AbstractIntentHandler.class);
	HandlerInput input;
	protected Map<String, Object> sessionAttributes;
	protected Intent intent;

	void initialize(HandlerInput input) {
		this.input = input;
		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		this.intent = intentRequest.getIntent();
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		checkAndRetrievePersistedAttributes();
		this.sessionAttributes.put("ORIGINAL_INTENT", this.intent.getName());

	}

	private void checkAndRetrievePersistedAttributes() {
		logger.debug("input.getAttributesManager().getPersistentAttributes():"
				+ input.getAttributesManager().getPersistentAttributes());
		if (!sessionAttributes.containsKey(Constants.PERSISTANT_STATE_RETRIEVED)
				&& !input.getAttributesManager().getPersistentAttributes().isEmpty()) {
			sessionAttributes.putAll(input.getAttributesManager().getPersistentAttributes());
			sessionAttributes.put(Constants.PERSISTANT_STATE_RETRIEVED, true);
		}

	}

	protected QuestState getQuestState() {
		String stateName = (String) sessionAttributes.get(Constants.KEY_QUEST_STATE);
		return QuestState.getStateByName(stateName);
	}

	protected QuestPerformer getQuestPerformer() {
		return new QuestPerformer(intent, input, sessionAttributes);
	}

	abstract public boolean canHandle(HandlerInput input);

	abstract public Optional<Response> handle(HandlerInput arg0);

}
