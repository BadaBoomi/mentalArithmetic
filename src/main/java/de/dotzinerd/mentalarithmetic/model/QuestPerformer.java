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
import de.dotzinerd.mentalarithmetic.model.quests.AdvancedMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultiplicationQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitSquareQuest;

public class QuestPerformer extends Performer {
	private static final String REPROMPT_SPEECH = "ich warte" + Audios.AUDIO_WAITINGLOOP;
	static final Logger logger = LogManager.getLogger(QuestPerformer.class);
	protected static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	protected static String CURRENT_QUESTION = "CURRENT_QUESTION";
	protected static String CURRENT_TURN = "TURN";
	protected static String SLOT_USER_RESPONSE = "SLOT_NUMBER";
	protected static String MAX_TURN = "MAX_TURN";
	protected static String START_TIME_INTENT = "START_TIME_INTENT";
	protected static String EXPLANATION = "EXPLANATION";
	Level level = null;
	Quest quest = null;

	IntentId intentID;

	public QuestPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		super(intent, input, sessionAttributes);

		setIntentID(intent);

	}

	private void setIntentID(Intent intent) {
		if (!getState().equals(QuestState.STATE_NEXT_INTENT)) {
			this.intentID = IntentId.getIntentIdByName((String) sessionAttributes.get(Constants.KEY_INTENT));
		} else {
			this.intentID = IntentId.getIntentIdByName(intent.getName());
		}
	}

	Integer getMaxTurn() {
		return 3;
	};

	String getAnswerString(boolean isAnswerCorrect) {
		logger.debug("isAnswerCorrect: " + isAnswerCorrect);
		return (isAnswerCorrect) ? "Richtig!"
				: "Leider Falsch! Es sind " + sessionAttributes.get(EXPECTED_ANSWER) + ", ";
	}

//	public QuestPerformer(HandlerInput input, Map<String, Object> sessionAttributes) {
//		this.input = input;
//		this.sessionAttributes = sessionAttributes;
//	}

	public Optional<Response> performQuestIntent() {
		Optional<Response> response;

		QuestState state = getState();
		if (state == QuestState.UNKNOWN) {
			state = QuestState.STATE_NEW_QUEST;
			setState(state);
		}
		logger.debug("state: " + state);
		switch (state) {
		case STATE_NEW_QUEST:
			logger.debug(sessionAttributes);
			sessionAttributes.put(MAX_TURN, getMaxTurn());
			sessionAttributes.put(CURRENT_TURN, 1);
			sessionAttributes.put(START_TIME_INTENT, System.currentTimeMillis());
			setState(QuestState.STATE_WAIT_FOR_ANSWER);
			response = performTurn(null);
			break;
		case STATE_WAIT_FOR_ANSWER:
			this.quest = getQuestFromSession();
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

			isAnswerCorrect = quest.isCorrectAnswer(answerSlot.getValue());
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

		default:
			response = input.getResponseBuilder().withShouldEndSession(false)
					.withSpeech("Jetzt bin ich wohl etwas ins Schleudern geraten")
					.withReprompt("Falls Du noch weitermachen möchtest, musst Du mir sagen, was Du gerne tun möchtest.")
					.build();
			break;
		}

		return response;
	}

//	private int calculateTimeToAnswerAll(Long startTime) {
//		Long timeUsed = TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime));
//		return timeUsed.intValue();
//	}

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

	Optional<Response> performTurn(Boolean isAnswerCorrect) {
		logger.debug("perform turn...");
		switch (intentID) {
		case SimpleEinmalEins:
			level = Level.LVL_SIMPLE_MULTIPLICATION;
			quest = new SimpleMultiplicationQuest();
			break;
		case SimpleMultiplication:

			int lvl = (int) (Math.random() * 3 + 1);
			switch (lvl) {
			case 1:
				this.level = Level.LVL_MULT_BY_11_SIMPLE;
				this.quest = new SimpleMultby11Quest();
				break;
			case 2:
				this.level = Level.LVL_MULT_BY_11_ADVANCED;
				this.quest = new AdvancedMultby11Quest();
			default:
				this.level = Level.LVL_MULT_BY_11_ADVANCED;
				this.quest = new AdvancedMultby11Quest();
			}
			break;
		case SimpleSquares:
			this.level = Level.LVL_2DIGIT_SQUARE;
			this.quest = new SimpleTwoDigitSquareQuest();
		default:
			break;
		}
		String speechText = (isAnswerCorrect == null) ? quest.getQuestion()
				: getAnswerString(isAnswerCorrect) + ". " + quest.getQuestion();
		setQuestInSession();

		// Create the Simple card content.
		logger.debug("quest, performTurn: " + speechText);
		return input.getResponseBuilder().withSpeech(speechText).withReprompt(REPROMPT_SPEECH)
				.withShouldEndSession(false).build();

	}

	private void setQuestInSession() {
		sessionAttributes.put(Constants.QUEST_ID, level.name() + ";" + quest.getId());
	}

	private Quest getQuestFromSession() {
		String id = (String) sessionAttributes.get(Constants.QUEST_ID);
		String[] ops = id.split(";");
		Level level = Level.getLevelByName(ops[0]);
		Quest quest = Level.getQuest(level);
		quest.setId(ops[1]);
		return quest;
	}

	private void setState(QuestState state) {
		sessionAttributes.put(Constants.KEY_QUEST_STATE, state);
	}

	private QuestState getState() {
		logger.debug("sessionAttributes: " + sessionAttributes);
		if (this.sessionAttributes.containsKey(Constants.KEY_QUEST_STATE)) {
			String stateName = (String) sessionAttributes.get(Constants.KEY_QUEST_STATE);
			return QuestState.getStateByName(stateName);
		} else
			return QuestState.UNKNOWN;

	}
}
