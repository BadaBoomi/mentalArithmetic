package de.dotzinerd.mentalarithmetic.model;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.ui.PlainTextOutputSpeech;

public abstract class Quest {
	protected static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "numberResponse";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";

	Logger logger;

	Intent intent;
	DialogState state;
	Session session;

	abstract void initialize();

	abstract Integer getMaxTurn();

	abstract SpeechletResponse performTurn(Session session, Boolean isAnswerCorrect);

	abstract String getAnswerString(boolean isAnswerCorrect);

	Quest() {
		initialize();

	}

	public SpeechletResponse performQuestIntent() {
		SpeechletResponse speechletResp;

		if (state.equals(DialogState.STARTED)) {
			session.setAttribute(MAX_TURN, getMaxTurn());
			session.setAttribute(CURRENT_TURN, 1);
			session.setAttribute(START_TIME_INTENT, System.currentTimeMillis());
			speechletResp = performTurn(session, null);
		} else {
			boolean isAnswerCorrect = session.getAttribute(EXPECTED_ANSWER)
					.equals(intent.getSlot(SLOT_USER_RESPONSE).getValue());
			String answer = getAnswerString(isAnswerCorrect);
			if ((Integer) (session.getAttribute(MAX_TURN)) > (Integer) (session.getAttribute(CURRENT_TURN))) {
				session.setAttribute(CURRENT_TURN, (Integer) (session.getAttribute(CURRENT_TURN)) + 1);
				speechletResp = performTurn(session, isAnswerCorrect);

			} else {
				PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
				Long startTime = (Long) (session.getAttribute(START_TIME_INTENT));
				answer += ". Gesamtdauer war " + String.valueOf(calculateTimeToAnswerAll(startTime)) + " Sekunden";

				speech.setText(answer);
				speechletResp = new SpeechletResponse();
				speechletResp.setOutputSpeech(speech);
				speechletResp.setNullableShouldEndSession(true);

			}
		}
		return speechletResp;
	}

	private int calculateTimeToAnswerAll(Long startTime) {
		Long timeUsed = TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime));
		return timeUsed.intValue();
	}

}
