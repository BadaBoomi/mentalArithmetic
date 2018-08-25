package de.dotzinerd.mentalarithmetic.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;

import de.dotzinerd.mentalarithmetic.enums.Level;
import de.dotzinerd.mentalarithmetic.model.Trainer;

public class TrainingIntentHandler extends AbstractIntentHandler {

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("Training"));
	}

	@Override
	public Optional<Response> handle(HandlerInput arg0) {

		return new Trainer(this.intent, this.input, this.sessionAttributes, Level.LVL_MULT_BY_11_SIMPLE)
				.performTraining();
	}

}
