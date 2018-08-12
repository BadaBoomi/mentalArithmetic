package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;
import static com.amazon.ask.request.Predicates.sessionAttribute;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.IntentEnum;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.responses.IntroductionResponse;

public class PerformQuestStateHandler implements RequestHandler {

	static final Logger logger = LogManager.getLogger(PerformQuestStateHandler.class);
	private IntentEnum intentID;
	private Map<String, Object> sessionAttributes;
	private Intent intent;
	private static String KEY_INTENT = "INTENT";
	private EnumSet<IntentEnum> questIntents = EnumSet.of(IntentEnum.SimpleEinmalEins, IntentEnum.SimpleMultiplication,
			IntentEnum.SimpleSquares);

	public PerformQuestStateHandler() {
		super();
		logger.debug("constructor called");
	}

	void initializeLocalVars(HandlerInput input) {
		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		this.intent = intentRequest.getIntent();
		logger.debug("intent: " + intent.getName());

		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		logger.debug("sessionAttributes: " + sessionAttributes);

		if (this.sessionAttributes == null) {
			input.getAttributesManager().setSessionAttributes(new HashMap<String, Object>());
			this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		}

		if ((String) this.sessionAttributes.get(KEY_INTENT) == null) {
			this.sessionAttributes.put(KEY_INTENT, intent.getName());
			this.intentID = IntentEnum.getEnumByName(intent.getName());
		} else if (sessionAttributes.containsKey(Constants.KEY_STATE)
				&& (!sessionAttributes.get(Constants.KEY_STATE).equals(Constants.STATE_NEXT_INTENT))) {
			this.intentID = IntentEnum.getEnumByName((String) sessionAttributes.get(KEY_INTENT));
		} else {
			this.intentID = IntentEnum.getEnumByName(intent.getName());
		}

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
				|| handlerInput.matches(intentName("SimpleSquares"))
				|| handlerInput.matches(intentName("NumberAnswered"));
		logger.debug("canHandle: " + yesIcan);
		return yesIcan;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle performQuest");
		QuestPerformer questPerformer;
		initializeLocalVars(handlerInput);

		questPerformer = getQuestPerformer(handlerInput);

		switch (this.intentID) {
		case SimpleEinmalEins:
		case SimpleMultiplication:
		case SimpleSquares:
			logger.debug("quest intent");
			return questPerformer.performQuestIntent();
		case StopIntent:
			logger.debug("StopIntent");

		case HelpIntent:
			logger.debug("HelpIntent");
			return questPerformer.performContextHelp();
		case NumberAnswered:
			logger.debug("NumberAnswered but no previous quest");
			return new IntroductionResponse().getResponse(handlerInput);
		default:
			logger.debug("default");
			return new IntroductionResponse().getResponse(handlerInput);
		}

	}

	private QuestPerformer getQuestPerformer(HandlerInput handlerInput) {
		QuestPerformer questPerformer = new QuestPerformer(this.intentID, this.intent, handlerInput, sessionAttributes);
		return questPerformer;
	}
}
