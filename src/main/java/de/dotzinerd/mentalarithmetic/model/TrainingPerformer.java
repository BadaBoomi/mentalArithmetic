package de.dotzinerd.mentalarithmetic.model;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.enums.PerformerState;
import de.dotzinerd.mentalarithmetic.enums.QuestState;
import de.dotzinerd.mentalarithmetic.enums.TrainingState;
import de.dotzinerd.mentalarithmetic.manager.QuestManager;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;

public class TrainingPerformer extends Performer {
	static final Logger logger = LogManager.getLogger(TrainingPerformer.class);
	private final short MASTER_TRAINER = 0;
	private final short ALEXA_TRAINER = 1;

	public TrainingPerformer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		super(intent, input, sessionAttributes);
		logger.debug("intent: " + intent + ", sessionAttributes:" + sessionAttributes);
	}

	public Optional<Response> performTraining(Level level) {
		sessionAttributes.put(Constants.KEY_PERFORMER, PerformerState.TRAINING.name());
		sessionAttributes.put(Constants.KEY_LEVEL, level.name());
		Quest quest = questManager.getNewQuestByLevel(level);
		Optional<Response> response;
		TrainingState tState = stateManager.getTrainingState();
		logger.debug("TrainingState: " + tState);
		switch (tState) {
		case STATE_EXPLAIN_TRAINING_BY_MASTER:
		case UNKNOWN:
		case STATE_NEW_TRAINING:
			setState(TrainingState.STATE_GIVE_EXAMPLE);
			return input.getResponseBuilder().withShouldEndSession(false).withSpeech(quest.getTrainByMaster()).build();
		case STATE_GIVE_EXAMPLE:
			setState(TrainingState.STATE_CHECK_TRAINED_ABILITY);
			return input.getResponseBuilder().withShouldEndSession(false).withSpeech(quest.getTrainByAlexa()).build();
		case STATE_CHECK_TRAINED_ABILITY:
			QuestPerformer questPerformer = new QuestPerformer(this.intent, input, sessionAttributes, level, 3);
			questPerformer.performQuestIntent();
		case STATE_EXPLAIN_TRAINING_BY_ALEXA:
			break;
		default:
			break;

		}
		return null;
	}

	private void giveExample() {
		// TODO Auto-generated method stub

	}

	private void explainTopic(short trainer) {
		switch (trainer) {
		case MASTER_TRAINER:

			break;
		case ALEXA_TRAINER:

		default:
			break;
		}

	}

	private boolean checkUserUnderstood() {
		// TODO Auto-generated method stub
		return false;
	}

	private void setState(TrainingState state) {
		sessionAttributes.put(Constants.KEY_TRAINING_STATE, state);
	}

}