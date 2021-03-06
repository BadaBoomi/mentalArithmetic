package de.dotzinerd.mentalarithmetic.model;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;

import de.dotzinerd.mentalarithmetic.manager.QuestManager;
import de.dotzinerd.mentalarithmetic.manager.StateManager;

public class Performer {
	static final Logger logger = LogManager.getLogger(Performer.class);
	HandlerInput input;
	Map<String, Object> sessionAttributes;
	Intent intent;
	protected QuestManager questManager;
	protected StateManager stateManager;

	Performer(Intent intent, HandlerInput input, Map<String, Object> sessionAttributes) {
		this.input = input;
		this.intent = intent;
		this.sessionAttributes = sessionAttributes;
		this.questManager = new QuestManager(sessionAttributes);
		this.stateManager = new StateManager(sessionAttributes);
	}

}
