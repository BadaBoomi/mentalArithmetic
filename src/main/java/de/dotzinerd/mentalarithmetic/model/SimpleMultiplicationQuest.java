package de.dotzinerd.mentalarithmetic.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import de.dotzinerd.mentalarithmetic.MentalArithmeticSpeechlet;

public class SimpleMultiplicationQuest extends Quest {

	public SimpleMultiplicationQuest(Intent intent, DialogState state, Session session) {
		super(intent, state, session);
	}

	@Override
	void initialize() {
		logger = LoggerFactory.getLogger(SimpleMultiplicationQuest.class);

	}

	@Override
	Integer getMaxTurn() {
		return 4;
	}

	@Override
	SpeechletResponse performTurn(Session session, Boolean isAnswerCorrect) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		SpeechletResponse speechletResp = new SpeechletResponse();

		Integer op1 = (int) (Math.random() * 8) + 2;
		Integer op2 = (int) (Math.random() * 8) + 2;
		Integer result = op1 * op2;
		String question = "Was macht " + op1 + " mal " + op2 + "?";
		String speechText = (isAnswerCorrect == null) ? question : getAnswerString(isAnswerCorrect) + ". " + question;
		session.setAttribute(EXPECTED_ANSWER, String.valueOf(result));

		// Create the Simple card content.

		SimpleCard card = new SimpleCard();
		card.setTitle("Einmaleins Aufgabe");
		card.setContent(speechText);
		speech.setText(speechText);

		// ask user for his result (value of slot)
		ElicitSlotDirective directive = new ElicitSlotDirective();
		directive.setSlotToElicit(SLOT_USER_RESPONSE);

		List<Directive> directiveList = new ArrayList<>();
		directiveList.add(directive);

//			speechletResp.setCard(card);
		speechletResp.setDirectives(directiveList);
		speechletResp.setNullableShouldEndSession(false);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);
		speechletResp.setReprompt(reprompt);
		speechletResp.setOutputSpeech(speech);
		return speechletResp;
	}

	@Override
	String getAnswerString(boolean isAnswerCorrect) {
		return (isAnswerCorrect) ? "Richtig!" : "Leider Falsch!";
	}

}
