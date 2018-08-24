package de.dotzinerd.mentalarithmetic.model;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.enums.TrainingState;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;

public class Trainer extends Performer {
	static final Logger logger = LogManager.getLogger(Trainer.class);
	private final short MASTER_TRAINER = 0;
	private final short ALEXA_TRAINER = 1;
	private Level level;
	Quest quest;

	public Trainer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes, Level level) {
		super(intent, input, sessionAttributes);
		this.level = level;
		this.quest = Level.getQuest(level);

	}

	public Optional<Response> performTraining() {
		Optional<Response> response;

		switch (getState()) {
		case STATE_EXPLAIN_TRAINING_BY_MASTER:
		case STATE_NEW_TRAINING:
			setState(TrainingState.STATE_GIVE_EXAMPLE);
			return input.getResponseBuilder().withShouldEndSession(false).withSpeech(quest.getTrainByMaster()).build();
		case STATE_GIVE_EXAMPLE:
			setState(TrainingState.STATE_CHECK_TRAINED_ABILITY);
			return input.getResponseBuilder().withShouldEndSession(false).withSpeech(quest.getTrainByAlexa()).build();
		case STATE_CHECK_TRAINED_ABILITY:

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

	private TrainingState getState() {
		String stateName = (String) sessionAttributes.get(Constants.KEY_TRAINING_STATE);
		return TrainingState.getStateByName(stateName);
	}
}