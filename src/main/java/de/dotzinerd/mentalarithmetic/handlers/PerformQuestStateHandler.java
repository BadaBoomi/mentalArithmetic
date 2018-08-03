package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;
import static com.amazon.ask.request.Predicates.sessionAttribute;

import java.util.HashMap;
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
import de.dotzinerd.mentalarithmetic.model.Quest;

public class PerformQuestStateHandler implements RequestHandler {
	static final Logger logger = LogManager.getLogger(PerformQuestStateHandler.class);

	public PerformQuestStateHandler() {
		super();
		logger.debug("constructor called");
	}

	public boolean canHandle(HandlerInput handlerInput) {
		boolean yesIcan = handlerInput
				.matches(intentName("performQuest")
						.and(sessionAttribute(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST).negate()))
				|| handlerInput.matches(intentName("AMAZON.StartOverIntent"))
				|| handlerInput.matches(intentName("AMAZON.HelpIntent"));
		logger.debug("canHandle: " + yesIcan);
		return yesIcan;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle performQuest");
		Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
		if (sessionAttributes == null) {
			handlerInput.getAttributesManager().setSessionAttributes(new HashMap<String, Object>());
			sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
		}
		IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
		Intent intent = intentRequest.getIntent();

		if (handlerInput.matches(intentName("performQuest"))) {
			sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST);
			if (intent.getSlots().get(Constants.SLOT_QUEST_NAME).getValue() != null) {
				logger.debug(Constants.SLOT_QUEST_NAME + " ID:" + intent.getSlots().get(Constants.SLOT_QUEST_NAME)
						.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getId());
			}
			QuestManager questManager = new QuestManager();
			Quest quest = questManager.getCurrentQuest(handlerInput, sessionAttributes);
			return quest.performQuestIntent();
		} else if (handlerInput.matches(intentName("SayHelloIntent"))) {
			sessionAttributes.put(Constants.KEY_STATE, "SayHelloIntent");
			return handlerInput.getResponseBuilder().withShouldEndSession(true).withSpeech("Hallo").build();
		} else if (handlerInput.matches(intentName("AMAZON.HelpIntent"))) {
			sessionAttributes.put(Constants.KEY_STATE, "AMAZON.HelpIntent");
			return handlerInput.getResponseBuilder().withShouldEndSession(true).withSpeech("Hilfe").build();
		} else {
			sessionAttributes.put(Constants.KEY_STATE, "else...");
			return handlerInput.getResponseBuilder().withShouldEndSession(true).withSpeech("Ansonsten sag ich nix")
					.build();
		}
	}
}
