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

import de.dotzinerd.mentalarithmetic.enums.IntentId;
import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.enums.QuestState;
import de.dotzinerd.mentalarithmetic.manager.QuestManager;
import de.dotzinerd.mentalarithmetic.model.quests.AdvancedMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultiplicationQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitSquareQuest;

public class QuestPerformer extends Performer {
	private static final String REPROMPT_SPEECH = "ich warte" + Audios.AUDIO_WAITINGLOOP;
	static final Logger logger = LogManager.getLogger(QuestPerformer.class);
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "SLOT_NUMBER";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";
	Level level = null;
	Quest quest = null;
	final int MODE_PLAY = 0;
	final int MODE_TRAINING = 1;
	int modus = MODE_PLAY;
	int maxTurn = 5;

//	IntentId intentID;

	public QuestPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		super(intent, input, sessionAttributes);
		this.modus = MODE_PLAY;
//		setIntentID(intent);

	}

	public QuestPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes, Level level,
			int maxTurn) {
		super(intent, input, sessionAttributes);
		this.modus = MODE_TRAINING;
		this.level = level;
		this.maxTurn = maxTurn;

//		setIntentID(intent);

	}

	Integer getMaxTurn() {
		return maxTurn;
	};

	String getAnswerString(boolean isAnswerCorrect) {
		logger.debug("isAnswerCorrect: " + isAnswerCorrect);
		return (isAnswerCorrect) ? "Richtig!" : "Leider Falsch! Es sind " + this.quest.getAnswer() + ", ";
	}

//	public QuestPerformer(HandlerInput input, Map<String, Object> sessionAttributes) {
//		this.input = input;
//		this.sessionAttributes = sessionAttributes;
//	}

	public Optional<Response> performQuestIntent() {
		Optional<Response> response;

		QuestState state = QuestManager.getManager().getQuestState(sessionAttributes);
		if (state == QuestState.UNKNOWN) {
			state = QuestState.STATE_NEW_QUEST;
			setState(state);
		}
		logger.debug("state: " + state);
		switch (state) {
		case STATE_NEW_QUEST:
		case STATE_NEXT_INTENT:
			this.quest = QuestManager.getManager().getNewQuestByIntent(intent, sessionAttributes);
			logger.debug(sessionAttributes);
			sessionAttributes.put(MAX_TURN, getMaxTurn());
			sessionAttributes.put(CURRENT_TURN, 1);
			sessionAttributes.put(START_TIME_INTENT, System.currentTimeMillis());
			setState(QuestState.STATE_WAIT_FOR_ANSWER);
			response = performTurn(null);
			break;
		case STATE_WAIT_FOR_ANSWER:
			this.quest = QuestManager.getManager().getCurrentQuestFromSession(sessionAttributes);
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

			isAnswerCorrect = this.quest.isCorrectAnswer(answerSlot.getValue());
			if (!isAnswerCorrect) {
				logger.debug("answer understood: " + answerSlot.getValue() + ", correct answer: " + quest.getAnswer());
			}

			String answer = getAnswerString(isAnswerCorrect);
			logger.debug("answer: " + answer);
			if ((Integer) (sessionAttributes.get(MAX_TURN)) > (Integer) (sessionAttributes.get(CURRENT_TURN))) {
				sessionAttributes.put(CURRENT_TURN, (Integer) (sessionAttributes.get(CURRENT_TURN)) + 1);
				response = performTurn(isAnswerCorrect);

			} else {
				Long startTime = (Long) (sessionAttributes.get(START_TIME_INTENT));
//				answer += ". Gesamtdauer war " + String.valueOf(calculateTimeToAnswerAll(startTime)) + " Sekunden";
				sessionAttributes.clear();
				setState(QuestState.STATE_NEXT_INTENT);
				response = input.getResponseBuilder().withShouldEndSession(false).withSpeech(answer)
						.withReprompt("Falls Du noch weitermachen möchtest, musst Du mir das sagen.").build();

			}
			break;
		default:
			response = input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech("Jetzt bin ich wohl etwas ins Schleudern geraten")
					.withReprompt("Falls Du noch weitermachen möchtest, musst Du mir sagen, was Du gerne tun möchtest.")
					.build();

		}
		logger.debug("response:" + response);
		return response;
	}

//	private int calculateTimeToAnswerAll(Long startTime) {
//		Long timeUsed = TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime));
//		return timeUsed.intValue();
//	}

	public Optional<Response> repeatQuestion() {
		return input.getResponseBuilder().withShouldEndSession(false)
				.withSpeech((String) sessionAttributes.get(quest.getQuestion())).withReprompt(REPROMPT_SPEECH).build();
	}

	public Optional<Response> performContextHelp() {
		String explanation = quest.getExplanation();
		logger.debug("give help: " + explanation);

		// Create the Simple card content.

		return input.getResponseBuilder().withSpeech(explanation).withShouldEndSession(false).build();

	}

	Optional<Response> performTurn(Boolean isAnswerCorrect) {
		this.quest =QuestManager.getManager().getNewQuestByIntent(intent, sessionAttributes);
		String speechText = (isAnswerCorrect == null) ? this.quest.getQuestion()
				: getAnswerString(isAnswerCorrect) + ". " + this.quest.getQuestion();
		setQuestInSession();

		// Create the Simple card content.
		logger.debug("quest, performTurn: " + speechText);
		return input.getResponseBuilder().withSpeech(speechText).withReprompt(REPROMPT_SPEECH)
				.withShouldEndSession(false).build();

	}

	private void setQuestInSession() {
		sessionAttributes.put(Constants.QUEST_ID, quest.getlevel() + ";" + quest.getId());
	}

	private void setState(QuestState state) {
		sessionAttributes.put(Constants.KEY_QUEST_STATE, state.name());
	}

}
