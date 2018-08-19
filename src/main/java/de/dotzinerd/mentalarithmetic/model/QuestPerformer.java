package de.dotzinerd.mentalarithmetic.model;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;

import de.dotzinerd.mentalarithmetic.model.quests.Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultiplicationQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitMultQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitSquareQuest;

public class QuestPerformer {
	private static final String AUDIO_WAITINGLOOP = "<audio src='https://s3.amazonaws.com/ask-soundlibrary/ui/gameshow/amzn_ui_sfx_gameshow_waiting_loop_30s_01.mp3'/>";
	private static final String REPROMPT_SPEECH = "ich warte" + AUDIO_WAITINGLOOP;
	static final Logger logger = LogManager.getLogger(QuestPerformer.class);
	protected static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	protected static String CURRENT_QUESTION = "CURRENT_QUESTION";
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "SLOT_NUMBER";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";
	protected static String EXPLANATION = "EXPLANATION";

	HandlerInput input;
	Map<String, Object> sessionAttributes;
	Intent intent;
	IntentEnum intentID;

	Integer getMaxTurn() {
		return 3;
	};

	Optional<Response> performTurn(Boolean isAnswerCorrect) {
		logger.debug("perform turn...");
		Quest quest = null;
		switch (intentID) {
		case SimpleEinmalEins:
			quest = new SimpleMultiplicationQuest();
			break;
		case SimpleMultiplication:

			quest = new SimpleTwoDigitMultQuest(SimpleTwoDigitMultQuest.Level
					.getRandomLevelBelow(SimpleTwoDigitMultQuest.Level.LVL_MULT_BY_11_ADVANCED));
			break;
		case SimpleSquares:
			quest = new SimpleTwoDigitSquareQuest();
		default:
			break;
		}
		String speechText = (isAnswerCorrect == null) ? quest.getQuestion()
				: getAnswerString(isAnswerCorrect) + ". " + quest.getQuestion();
		this.setQuestionAndAnswerInSession(quest.getQuestion(), String.valueOf(quest.getAnswer()));

		// Create the Simple card content.
		logger.debug("quest, performTurn: " + speechText);
		return input.getResponseBuilder().withSpeech(speechText).withReprompt(REPROMPT_SPEECH)
				.withShouldEndSession(false).build();

	}

	String getAnswerString(boolean isAnswerCorrect) {
		logger.debug("isAnswerCorrect: " + isAnswerCorrect);
		return (isAnswerCorrect) ? "Richtig!"
				: "Leider Falsch! Es sind " + sessionAttributes.get(EXPECTED_ANSWER) + ", ";
	}

	public QuestPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		this.input = input;
		this.intent = intent;
		this.sessionAttributes = sessionAttributes;
		if (sessionAttributes.containsKey(Constants.KEY_STATE)
				&& (!sessionAttributes.get(Constants.KEY_STATE).equals(Constants.STATE_NEXT_INTENT))) {
			this.intentID = IntentEnum.getEnumByName((String) sessionAttributes.get(Constants.KEY_INTENT));
		} else {
			this.intentID = IntentEnum.getEnumByName(intent.getName());
		}
	}

//	public QuestPerformer(HandlerInput input, Map<String, Object> sessionAttributes) {
//		this.input = input;
//		this.sessionAttributes = sessionAttributes;
//	}

	public Optional<Response> performQuestIntent() {
		Optional<Response> response;
		String state = (String) sessionAttributes.get(Constants.KEY_STATE);
		if (state == null || state.equals(Constants.STATE_NEXT_INTENT)) {
			state = Constants.STATE_NEW_QUEST;
			sessionAttributes.put(Constants.KEY_STATE, state);
		}
		logger.debug("state: " + state);
		if (state.equals(Constants.STATE_NEW_QUEST)) {
			logger.debug(sessionAttributes);
			sessionAttributes.put(MAX_TURN, getMaxTurn());
			sessionAttributes.put(CURRENT_TURN, 1);
			sessionAttributes.put(START_TIME_INTENT, System.currentTimeMillis());
			sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_WAIT_FOR_ANSWER);
			response = performTurn(null);
		} else {
			Slot answerSlot = null;
			logger.debug("check answer...");
			if (intent.getSlots() != null) {
				answerSlot = intent.getSlots().get(SLOT_USER_RESPONSE);
			}

			if (answerSlot == null || answerSlot.getValue() == null) {
				return input.getResponseBuilder().withShouldEndSession(false).withSpeech("konnte ich nicht verstehen")
						.withReprompt(REPROMPT_SPEECH).build();
			}
			boolean isAnswerCorrect = false;

			isAnswerCorrect = sessionAttributes.get(EXPECTED_ANSWER).equals(answerSlot.getValue());
			if (!isAnswerCorrect) {
				logger.debug("answer understood: " + answerSlot.getValue() + ", correct answer: "
						+ sessionAttributes.get(EXPECTED_ANSWER));
			}

			String answer = getAnswerString(isAnswerCorrect);
			logger.debug("answer: " + answer);
			if ((Integer) (sessionAttributes.get(MAX_TURN)) > (Integer) (sessionAttributes.get(CURRENT_TURN))) {
				sessionAttributes.put(CURRENT_TURN, (Integer) (sessionAttributes.get(CURRENT_TURN)) + 1);
				sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_WAIT_FOR_ANSWER);
				response = performTurn(isAnswerCorrect);

			} else {
				Long startTime = (Long) (sessionAttributes.get(START_TIME_INTENT));
				answer += ". Gesamtdauer war " + String.valueOf(calculateTimeToAnswerAll(startTime)) + " Sekunden";
				sessionAttributes.clear();
				sessionAttributes.put(Constants.KEY_STATE, Constants.STATE_NEXT_INTENT);
				response = input.getResponseBuilder().withShouldEndSession(false).withSpeech(answer)
						.withReprompt("Falls Du noch weitermachen möchtest, musst Du mir das sagen.").build();

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
		return input.getResponseBuilder().withShouldEndSession(false)
				.withSpeech((String) sessionAttributes.get(CURRENT_QUESTION)).withReprompt(REPROMPT_SPEECH).build();
	}

	public Optional<Response> performContextHelp() {
		logger.debug("give help");
		String explanation = (String) sessionAttributes.get(EXPLANATION);
		// Create the Simple card content.

		return input.getResponseBuilder().withSpeech(explanation).withShouldEndSession(false).build();

	}

}
