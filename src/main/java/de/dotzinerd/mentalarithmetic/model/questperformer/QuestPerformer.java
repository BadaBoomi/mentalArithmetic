package de.dotzinerd.mentalarithmetic.model.questperformer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Session;

import de.dotzinerd.mentalarithmetic.handlers.PerformQuestStateHandler;

public abstract class QuestPerformer {
	static final Logger logger = LogManager.getLogger(QuestPerformer.class);
	protected static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	protected static String CURRENT_QUESTION = "CURRENT_QUESTION";
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "SLOT_NUMBER";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";
	protected static String QUEST_STATE = "QUEST_STATE";
	protected static String STATE_NEXT_QUESTION = "NEW QUESTION";
	protected static String STATE_WAIT_FOR_ANSWER = "WAITING FOR ANSWER";

	HandlerInput input;
	Intent intent;
	DialogState state;
	Map<String, Object> sessionAttributes;

	abstract Integer getMaxTurn();

	abstract Optional<Response> performTurn(Boolean isAnswerCorrect);

	String getAnswerString(boolean isAnswerCorrect) {
		logger.debug("isAnswerCorrect: " + isAnswerCorrect);
		return (isAnswerCorrect) ? "Richtig!" : "Leider Falsch! Es sind " + sessionAttributes.get(EXPECTED_ANSWER);
	}

	QuestPerformer(HandlerInput input, Map<String, Object> sessionAttributes) {
		IntentRequest itr = (IntentRequest) input.getRequestEnvelope().getRequest();
		this.intent = (Intent) itr.getIntent();
		this.input = input;
		this.sessionAttributes = sessionAttributes;
	}

	public Optional<Response> performQuestIntent() {
		Optional<Response> response;
		String state = (String) sessionAttributes.get(QUEST_STATE);
		if (state == null) {
			state = STATE_NEXT_QUESTION;
			sessionAttributes.put(QUEST_STATE, state);
		}
		if (state.equals(STATE_NEXT_QUESTION)) {
			logger.debug(sessionAttributes);
			sessionAttributes.put(MAX_TURN, getMaxTurn());
			sessionAttributes.put(CURRENT_TURN, 1);
			sessionAttributes.put(START_TIME_INTENT, System.currentTimeMillis());
			sessionAttributes.put(QUEST_STATE, STATE_WAIT_FOR_ANSWER);
			response = performTurn(null);
		} else {
			boolean isAnswerCorrect = sessionAttributes.get(EXPECTED_ANSWER)
					.equals(intent.getSlots().get(SLOT_USER_RESPONSE).getValue());
			String answer = getAnswerString(isAnswerCorrect);
			logger.debug("answer: " + answer);
			if ((Integer) (sessionAttributes.get(MAX_TURN)) > (Integer) (sessionAttributes.get(CURRENT_TURN))) {
				sessionAttributes.put(CURRENT_TURN, (Integer) (sessionAttributes.get(CURRENT_TURN)) + 1);
				sessionAttributes.put(QUEST_STATE, STATE_NEXT_QUESTION);
				response = performTurn(isAnswerCorrect);

			} else {
				Long startTime = (Long) (sessionAttributes.get(START_TIME_INTENT));
				answer += ". Gesamtdauer war " + String.valueOf(calculateTimeToAnswerAll(startTime)) + " Sekunden";
				sessionAttributes.remove(QUEST_STATEs);
				response = input.getResponseBuilder().withShouldEndSession(true).withSpeech(answer).build();

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
