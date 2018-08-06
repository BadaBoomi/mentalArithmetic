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
import de.dotzinerd.mentalarithmetic.model.StatusEnum;
import de.dotzinerd.mentalarithmetic.model.questperformer.QuestPerformer;
import de.dotzinerd.mentalarithmetic.model.responses.IntroductionResponse;

public class PerformQuestStateHandler implements RequestHandler {

	static final Logger logger = LogManager.getLogger(PerformQuestStateHandler.class);
	private StatusEnum statusID;
	private Map<String, Object> sessionAttributes;

	public PerformQuestStateHandler() {
		super();
		logger.debug("constructor called");
	}

	void initializeLocalVars(HandlerInput input) {
		this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		if (this.sessionAttributes == null) {
			input.getAttributesManager().setSessionAttributes(new HashMap<String, Object>());
			this.sessionAttributes = input.getAttributesManager().getSessionAttributes();
		}

		IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
		Intent intent = intentRequest.getIntent();

		if (input.matches(intentName(Constants.PERFORM_QUEST_INTENT))) {
			this.sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST);
			if (intent.getSlots().get(Constants.SLOT_QUEST_NAME).getValue() != null) {
				logger.debug(Constants.SLOT_QUEST_NAME + " ID:" + intent.getSlots().get(Constants.SLOT_QUEST_NAME)
						.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getId());
				this.statusID = StatusEnum.getEnumByName(intent.getSlots().get(Constants.SLOT_QUEST_NAME)
						.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getId());
			}
		} else if (input.matches(intentName(Constants.AMAZON_HELP_INTENT))) {
			this.statusID = StatusEnum.HelpIntent;
		} else if (input.matches(intentName("AMAZON.CancelIntent"))) {
			this.statusID = StatusEnum.CancelIntent;
		} else if (input.matches(intentName("AMAZON.LoopOffIntent"))) {
			this.statusID = StatusEnum.LoopOffIntent;
		} else if (input.matches(intentName("AMAZON.LoopOnIntent"))) {
			this.statusID = StatusEnum.LoopOnIntent;
		} else if (input.matches(intentName("AMAZON.NextIntent"))) {
			this.statusID = StatusEnum.NextIntent;
		} else if (input.matches(intentName("AMAZON.NoIntent"))) {
			this.statusID = StatusEnum.NoIntent;
		} else if (input.matches(intentName("AMAZON.PauseIntent"))) {
			this.statusID = StatusEnum.PauseIntent;
		} else if (input.matches(intentName("AMAZON.PreviousIntent"))) {
			this.statusID = StatusEnum.PreviousIntent;
		} else if (input.matches(intentName("AMAZON.RepeatIntent"))) {
			this.statusID = StatusEnum.RepeatIntent;
		} else if (input.matches(intentName("AMAZON.ResumeIntent"))) {
			this.statusID = StatusEnum.ResumeIntent;
		} else if (input.matches(intentName("AMAZON.ShuffleOffIntent"))) {
			this.statusID = StatusEnum.ShuffleOffIntent;
		} else if (input.matches(intentName("AMAZON.ShuffleOnIntent"))) {
			this.statusID = StatusEnum.ShuffleOnIntent;
		} else if (input.matches(intentName("AMAZON.StopIntent"))) {
			this.statusID = StatusEnum.StopIntent;
		} else if (input.matches(intentName("AMAZON.YesIntent"))) {
			this.statusID = StatusEnum.YesIntent;
		} else if (input.matches(requestType(SessionEndedRequest.class))) {
			this.statusID = StatusEnum.TIME_OUT;
		} else {
			this.statusID = StatusEnum.UNKNOWN;
		}
		logger.debug("status= " + this.statusID.toString());
	}

	public boolean canHandle(HandlerInput handlerInput) {
		logger.debug("handlerInput.getRequestEnvelope().getRequest().getType(): "
				+ handlerInput.getRequestEnvelope().getRequest().getType());
		logger.debug(handlerInput.getRequestEnvelope().getRequest().toString());
		boolean yesIcan = handlerInput.matches(intentName("performQuest"))
				|| handlerInput.matches(intentName("AMAZON.StartOverIntent")
						.and(sessionAttribute(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST)))
				|| handlerInput.matches(intentName(Constants.AMAZON_HELP_INTENT)
						.and(sessionAttribute(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST)))
				|| handlerInput.matches(intentName(Constants.AMAZON_HELP_INTENT)
						.and(sessionAttribute("AMAZON.StopIntent", Constants.STATE_PERFORM_QUEST)))
				|| handlerInput.matches(requestType(SessionEndedRequest.class)
						.and(sessionAttribute(Constants.KEY_STATE, Constants.STATE_PERFORM_QUEST)));
		logger.debug("canHandle: " + yesIcan);
		return yesIcan;
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle performQuest");
		QuestPerformer questPerformer;
		initializeLocalVars(handlerInput);

		switch (this.statusID) {
		case SV_SIMPLE_MULT:
		case SV_SIMPLE_2Digit_SQUARES:
		case SV_SIMPLE_2Digit_Mult:
			questPerformer = getQuestPerformer(handlerInput);
			return questPerformer.performQuestIntent();
		case StopIntent:
			questPerformer = getQuestPerformer(handlerInput);
			return questPerformer.repeatQuestion();
		case HelpIntent:
			return new IntroductionResponse().getResponse(handlerInput);
		default:
			return new IntroductionResponse().getResponse(handlerInput);
		}

	}

	private QuestPerformer getQuestPerformer(HandlerInput handlerInput) {
		QuestManager questManager = new QuestManager();
		QuestPerformer questPerformer = questManager.getCurrentQuest(handlerInput, sessionAttributes, this.statusID);
		return questPerformer;
	}
}
