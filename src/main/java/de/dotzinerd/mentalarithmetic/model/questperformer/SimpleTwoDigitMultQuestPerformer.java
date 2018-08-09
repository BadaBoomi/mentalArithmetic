package de.dotzinerd.mentalarithmetic.model.questperformer;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.model.Quest;
import de.dotzinerd.mentalarithmetic.model.SimpleTwoDigitMultQuest;
import de.dotzinerd.mentalarithmetic.model.SimpleTwoDigitSquareQuest;

public class SimpleTwoDigitMultQuestPerformer extends QuestPerformer {
	static final Logger logger = LogManager.getLogger(SimpleTwoDigitMultQuestPerformer.class);

	public SimpleTwoDigitMultQuestPerformer(HandlerInput input, Map<String, Object> sessionAttributes) {
		super(input, sessionAttributes);
	}

	@Override
	Integer getMaxTurn() {
		return 4;
	}

	@Override
	Optional<Response> performTurn(Boolean isAnswerCorrect) {
		logger.debug("perform turn...");

		Quest quest = new SimpleTwoDigitMultQuest();
		String speechText = (isAnswerCorrect == null) ? quest.getQuestion()
				: getAnswerString(isAnswerCorrect) + ". " + quest.getQuestion();
		this.setQuestionAndAnswerInSession(quest.getQuestion(),String.valueOf(quest.getAnswer()));

		// Create the Simple card content.

		return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(false).build();

	}

	

}
