package de.dotzinerd.mentalarithmetic.model.questperformer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public abstract class QuestPerformer {
	static final Logger logger = LogManager.getLogger(QuestPerformer.class);
	protected static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	protected static String CURRENT_QUESTION = "CURRENT_QUESTION";
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "SLOT_NUMBER";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";
	protected static String QUEST_STATE = "QUEST_STATE";
	protected static String STATE_NEW_QUEST = "NEW QUEST";
	protected static String STATE_NEXT_QUESTION = "NEXT_QUESTION";
	protected static String STATE_WAIT_FOR_ANSWER = "WAITING FOR ANSWER";

	HandlerInput input;
	Map<String, Object> sessionAttributes;
	Intent intent;

	abstract Integer getMaxTurn();

	abstract Optional<Response> performTurn(Boolean isAnswerCorrect);

	String getAnswerString(boolean isAnswerCorrect) {
		logger.debug("isAnswerCorrect: " + isAnswerCorrect);
		return (isAnswerCorrect) ? "Richtig!" : "Leider Falsch! Es sind " + sessionAttributes.get(EXPECTED_ANSWER);
	}

	QuestPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		this.input = input;
		this.intent = intent;
		this.sessionAttributes = sessionAttributes;
	}

	public Optional<Response> performQuestIntent() {
		Optional<Response> response;
		String state = (String) sessionAttributes.get(QUEST_STATE);
		if (state == null) {
			state = STATE_NEW_QUEST;
			sessionAttributes.put(QUEST_STATE, state);
		}
		logger.debug("state: " + state);
		if (state.equals(STATE_NEW_QUEST)) {
			logger.debug(sessionAttributes);
			sessionAttributes.put(MAX_TURN, getMaxTurn());
			sessionAttributes.put(CURRENT_TURN, 1);
			sessionAttributes.put(START_TIME_INTENT, System.currentTimeMillis());
			sessionAttributes.put(QUEST_STATE, STATE_WAIT_FOR_ANSWER);
			response = performTurn(null);
		} else {
			logger.debug("check answer...");
			Slot answerSlot = intent.getSlots().get(SLOT_USER_RESPONSE);
			logger.debug(
					"answerSlot.getConfirmationStatus().getValue(): " + answerSlot.getConfirmationStatus().getValue());
			logger.debug("answerSlot.getValue(): " + answerSlot.getValue());

			if (answerSlot.getValue() == null) {
				return input.getResponseBuilder().withShouldEndSession(false).withSpeech("konnte ich nicht verstehen")
						.build();
			}
			boolean isAnswerCorrect = false;
			if (answerSlot != null) {
				isAnswerCorrect = sessionAttributes.get(EXPECTED_ANSWER).equals(answerSlot.getValue());
			}

			String answer = getAnswerString(isAnswerCorrect);
			logger.debug("answer: " + answer);
			if ((Integer) (sessionAttributes.get(MAX_TURN)) > (Integer) (sessionAttributes.get(CURRENT_TURN))) {
				sessionAttributes.put(CURRENT_TURN, (Integer) (sessionAttributes.get(CURRENT_TURN)) + 1);
				sessionAttributes.put(QUEST_STATE, STATE_NEXT_QUESTION);
				response = performTurn(isAnswerCorrect);

			} else {
				Long startTime = (Long) (sessionAttributes.get(START_TIME_INTENT));
				answer += ". Gesamtdauer war " + String.valueOf(calculateTimeToAnswerAll(startTime)) + " Sekunden";
				sessionAttributes.put(QUEST_STATE, STATE_NEW_QUEST);
				response = input.getResponseBuilder().withShouldEndSession(false).withSpeech(answer)
						.withReprompt("wie?").build();

			}
		}
		return response;
	}

	protected void setQuestionAndAnswerInSession(String question, String answer) {
		sessionAttributes.put(EXPECTED_ANSWER, answer);
		sessionAttributes.put(CURRENT_QUESTION, question);
	}

	private int calculateTimeToAnswerAll(Long startTime) {
		Long timeUsed = TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime));
		return timeUsed.intValue();
	}

	public Optional<Response> repeatQuestion() {
		return input.getResponseBuilder().withShouldEndSession(false).withSpeech("Ich warte...").build();
	}

}
