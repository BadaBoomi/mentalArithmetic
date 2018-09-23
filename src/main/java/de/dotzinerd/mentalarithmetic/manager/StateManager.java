package de.dotzinerd.mentalarithmetic.manager;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.dotzinerd.mentalarithmetic.enums.PerformerState;
import de.dotzinerd.mentalarithmetic.enums.TrainingState;
import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.Performer;
import de.dotzinerd.mentalarithmetic.model.TrainingPerformer;

public class StateManager {
	public static final String STATE_QUEST = "STATE_QUEST";
	public static final String STATE_TRAIN = "STATE_TRAIN";

	public static final String KEY_STATE = "OVERALL_STATE";

	static final Logger logger = LogManager.getLogger(StateManager.class);
	private Map<String, Object> sessionAttributes;

	public StateManager(Map<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}

	public TrainingState getTrainingState() {
		logger.debug("sessionAttributes: " + sessionAttributes);
		if (sessionAttributes.containsKey(Constants.KEY_TRAINING_STATE)) {
			String stateName = (String) sessionAttributes.get(Constants.KEY_TRAINING_STATE);
			return TrainingState.getStateByName(stateName);
		}

		else
			return TrainingState.UNKNOWN;

	}

	public void setTrainingState(TrainingState state) {
		sessionAttributes.put(Constants.KEY_TRAINING_STATE, state.name());
	}

	public PerformerState getPerformerState() {
		String id = (String) sessionAttributes.get(Constants.KEY_PERFORMER);
		PerformerState pState = PerformerState.getPerformerByName(id);
		return pState;
	}

}
