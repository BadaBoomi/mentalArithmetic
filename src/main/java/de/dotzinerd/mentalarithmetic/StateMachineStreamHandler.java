package de.dotzinerd.mentalarithmetic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;

import de.dotzinerd.mentalarithmetic.handlers.DefaultStateHandler;
import de.dotzinerd.mentalarithmetic.handlers.GenericExceptionHandler;
import de.dotzinerd.mentalarithmetic.handlers.HelpIntentHandler;
import de.dotzinerd.mentalarithmetic.handlers.InitialStateHandler;
import de.dotzinerd.mentalarithmetic.handlers.NumberAnsweredHandler;
import de.dotzinerd.mentalarithmetic.handlers.PerformQuestStateHandler;
import de.dotzinerd.mentalarithmetic.handlers.RepeatIntentHandler;
import de.dotzinerd.mentalarithmetic.handlers.SaveIntentHandler;
import de.dotzinerd.mentalarithmetic.handlers.TrainingIntentHandler;
import de.dotzinerd.mentalarithmetic.model.Constants;

public class StateMachineStreamHandler extends SkillStreamHandler {
	private static final String supportedApplicationId = "amzn1.ask.skill.9412c659-275e-43ff-9888-8a42ddab5f8b";
	static final Logger logger = LogManager.getLogger(StateMachineStreamHandler.class);

	static {
		logger.info("Supported app id : " + supportedApplicationId);
	}

	public StateMachineStreamHandler() {
		super(Skills.custom()
				.addRequestHandlers(new InitialStateHandler(), new TrainingIntentHandler(),
						new PerformQuestStateHandler(), new NumberAnsweredHandler(), new RepeatIntentHandler(),
						new HelpIntentHandler(), new SaveIntentHandler(), new DefaultStateHandler())
				.addExceptionHandler(new GenericExceptionHandler()).withSkillId(supportedApplicationId)
				.withPersistenceAdapter(DynamoDbPersistenceAdapter.builder()
						.withTableName(Constants.TABLE_PERSISTANT_USERDATATA).withAutoCreateTable(true).build())
				.build());

	}

}
