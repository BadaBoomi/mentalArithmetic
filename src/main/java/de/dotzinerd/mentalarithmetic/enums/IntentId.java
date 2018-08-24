package de.dotzinerd.mentalarithmetic.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum IntentId {
	UNKNOWN("UNKNOWN"), YesIntent("AMAZON.YesIntent"), StopIntent("AMAZON.StopIntent"),
	StartOverIntent("AMAZON.StartOverIntent"), ShuffleOnIntent("AMAZON.ShuffleOnIntent"),
	ShuffleOffIntent("AMAZON.ShuffleOffIntent"), ResumeIntent("AMAZON.ResumeIntent"),
	RepeatIntent("AMAZON.RepeatIntent"), PreviousIntent("AMAZON.PreviousIntent"), PauseIntent("AMAZON.PauseIntent"),
	NoIntent("AMAZON.NoIntent"), NextIntent("AMAZON.NextIntent"), LoopOnIntent("AMAZON.LoopOnIntent"),
	LoopOffIntent("AMAZON.LoopOffIntent"), CancelIntent("AMAZON.CancelIntent"), HelpIntent("AMAZON.HelpIntent"),
	SimpleEinmalEins("SimpleEinmalEins"), SimpleMultiplication("SimpleMultiplication"), SimpleSquares("SimpleSquares"),
	NumberAnswered("NumberAnswered");

	private final String saveValue;

	private IntentId(String saveAs) {
		this.saveValue = saveAs;
	}

	static final Logger logger = LogManager.getLogger(IntentId.class);

	public static IntentId getIntentIdByName(String name) {
		logger.debug("searching: " + name);

		for (IntentId en : IntentId.values()) {
			if (en.saveValue.equals(name)) {
				return en;
			}
		}
		logger.debug("returning UNKNOWN");
		return UNKNOWN;
	}

	public String getIntentName() {
		return this.saveValue;
	}

}
