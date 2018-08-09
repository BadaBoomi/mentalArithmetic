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
import com.amazon.ask.model.SessionEndedRequest;

import de.dotzinerd.mentalarithmetic.manager.QuestManager;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.IntentEnum;
import de.dotzinerd.mentalarithmetic.model.questperformer.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.responses.IntroductionResponse;

public class PerformQuestStateHandler implements RequestHandler {

	static final Logger logger = LogManager.getLogger(PerformQuestStateHandler.class);
	private IntentEnum intentID;
	private Map<String, Object> sessionAttributes;
	private Intent intent;

	public PerformQuestStateHandler() {
		super();
		logger.debug("constructor called");
	}

	void initializeLocalVars(HandlerInput input) {
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		logger.debug("sessionAttributes: " + sessionAttributes);

		if (this.sessionAttributes == null) {
			input.getAttributesManager().setSessionAttributes(new HashMap<String, Object>());
			this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		} else {
			if ((String) this.sessionAttributes.get(Constants.KEY_STATE) == null) {
				this.sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST);
			}
		}

		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		this.intent = intentRequest.getIntent();
		logger.debug("intentID: " + intentID);
		if (this.sessionAttributes.containsKey(Constants.KEY_QUEST_TYPE)) {
			this.intentID = IntentEnum.getEnumByName((String) sessionAttributes.get(Constants.KEY_QUEST_TYPE));
		} else {
			this.intentID = IntentEnum.getEnumByName(intent.getName());
		}

//		for (IntentEnum en : IntentEnum.values()) {
//			if (input.matches(intentName(en.getIntentName()))) {
//				this.statusID = en;
//			}
//		}

	}

	public boolean canHandle(HandlerInput handlerInput) {
		logger.debug("handlerInput.getRequestEnvelope().getRequest().getType(): "
				+ handlerInput.getRequestEnvelope().getRequest().getType());
		logger.debug(handlerInput.getRequestEnvelope().getRequest().toString());
		boolean yesIcan = handlerInput
				.matches(requestType(IntentRequest.class)
						.and(sessionAttribute(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST)))
				|| handlerInput.matches(intentName("SimpleEinmalEins"))
				|| handlerInput.matches(intentName("SimpleMultiplication"))
				|| handlerInput.matches(intentName("SimpleSquares"));
		logger.debug("canHandle: " + yesIcan);
		return yesIcan;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle performQuest");
		QuestPerformer questPerformer;
		initializeLocalVars(handlerInput);

		switch (this.intentID) {
		case SimpleEinmalEins:
		case SimpleMultiplication:
		case SimpleSquares:
		case NumberAnswered:
			logger.debug("quest intent");
			questPerformer = getQuestPerformer(handlerInput);
			return questPerformer.performQuestIntent();
		case StopIntent:
			logger.debug("StopIntent");
			questPerformer = getQuestPerformer(handlerInput);
			return questPerformer.repeatQuestion();
		case HelpIntent:
			logger.debug("HelpIntent");
			return new IntroductionResponse().getResponse(handlerInput);
		default:
			logger.debug("default");
			return new IntroductionResponse().getResponse(handlerInput);
		}

	}

	private QuestPerformer getQuestPerformer(HandlerInput handlerInput) {
		QuestManager questManager = new QuestManager();
		QuestPerformer questPerformer = questManager.getCurrentQuest(this.intent, this.intentID, handlerInput, sessionAttributes);
		return questPerformer;
	}
}
