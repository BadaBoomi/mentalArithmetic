package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.HashMap;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.QuestPerformer;

public class PerformQuestStateHandler extends AbstractIntentHandler {

	static final Logger logger = LogManager.getLogger(PerformQuestStateHandler.class);

	void initializeLocalVars(HandlerInput input) {
		initialize(input);

		if ((String) this.sessionAttributes.get(Constants.KEY_INTENT) == null) {
			this.sessionAttributes.put(Constants.KEY_INTENT, this.intent.getName());
		}

	}

	public boolean canHandle(HandlerInput handlerInput) {
		return handlerInput.matches(intentName("SimpleEinmalEins"))
				|| handlerInput.matches(intentName("SimpleMultiplication"))
				|| handlerInput.matches(intentName("SimpleSquares"));
	}

	public Optional<Response> handle(HandlerInput handlerInput) {
		logger.debug("handle performQuest");
		QuestPerformer questPerformer;
		initializeLocalVars(handlerInput);

		questPerformer = getQuestPerformer(handlerInput);

		return questPerformer.performQuestIntent();

	}

	private QuestPerformer getQuestPerformer(HandlerInput handlerInput) {
		QuestPerformer questPerformer = new QuestPerformer(this.intent, handlerInput, sessionAttributes);
		return questPerformer;
	}
}
