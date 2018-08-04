package de.dotzinerd.mentalarithmetic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import de.dotzinerd.mentalarithmetic.handlers.DefaultStateHandler;
import de.dotzinerd.mentalarithmetic.handlers.GenericExceptionHandler;
import de.dotzinerd.mentalarithmetic.handlers.InitialStateHandler;
import de.dotzinerd.mentalarithmetic.handlers.PerformQuestStateHandler;

public class StateMachineStreamHandler extends SkillStreamHandler {
	private static final String supportedApplicationId = "amzn1.ask.skill.9412c659-275e-43ff-9888-8a42ddab5f8b";
	static final Logger logger = LogManager.getLogger(StateMachineStreamHandler.class);

	static {
		logger.info("Supported app id : " + supportedApplicationId);
	}

	public StateMachineStreamHandler() {
		super(Skills.standard()
				.addRequestHandlers(new InitialStateHandler(), new PerformQuestStateHandler(),
						new DefaultStateHandler()).addExceptionHandler(new GenericExceptionHandler())
				.withSkillId(supportedApplicationId).build());

	}

}