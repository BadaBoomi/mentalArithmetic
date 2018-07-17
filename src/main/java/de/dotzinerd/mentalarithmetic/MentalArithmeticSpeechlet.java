package de.dotzinerd.mentalarithmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import de.dotzinerd.mentalarithmetic.model.Quest;
import de.dotzinerd.mentalarithmetic.model.SimpleMultiplicationQuest;

public class MentalArithmeticSpeechlet implements Speechlet {
	private static String OPERATOR_1 = "OPERATOR_1";
	private static String OPERATOR_2 = "OPERATOR_2";
	private static String EXPECTED_ANSWER = "EXPECTED_ANSWER";
	private static String CURRENT_TURN = "TURN";
	private static String SLOT_USER_RESPONSE = "numberResponse";
	private static String MAX_TURN = "MAX_TURN";
	private static String START_TIME_INTENT = "START_TIME_INTENT";
	private static String SLOT_QUEST_NAME = "questName";

	private Quest currentQuest;

	// Initialize the Log4j logger.

	static final Logger logger = LoggerFactory.getLogger(MentalArithmeticSpeechlet.class);

	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		System.out.println(
				"onLaunch requestId={}, sessionId={} " + request.getRequestId() + " - " + session.getSessionId());
		return getWelcomeResponse();
	}

	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		logger.debug("logger...onIntent requestId={}, sessionId={} " + request.getRequestId() + " - "
				+ session.getSessionId());
		System.out.println(
				"onIntent requestId={}, sessionId={} " + request.getRequestId() + " - " + session.getSessionId());

		Intent intent = request.getIntent();
		DialogState state = request.getDialogState();
		String intentName = (intent != null) ? intent.getName() : null;

		System.out.println("intentName : " + intentName);
		System.out.println("state: " + state);
		if ("performQuest".equals(intentName)) {
			System.out.println(SLOT_QUEST_NAME + ":" + intent.getSlot(SLOT_QUEST_NAME).getValue());
			if (intent.getSlot(SLOT_QUEST_NAME).getValue() != null)
				System.out.println(SLOT_QUEST_NAME + " ID:" + intent.getSlot(SLOT_QUEST_NAME).getResolutions()
						.getResolutionsPerAuthority().get(0).getValueWrapperAtIndex(0).getValue().getId());

			return getCurrentQuest(intent, state, session).performQuestIntent();
		} else if ("SayHelloIntent".equals(intentName)) {
			return getHelloResponse();
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
		} else {
			return getHelpResponse();
		}
	}

	private Quest getCurrentQuest(Intent intent, DialogState state, Session session) {
		String questName = intent.getSlot(SLOT_QUEST_NAME).getValue();
		System.out.println("questName :" + questName);
		if (currentQuest == null) {
			return new SimpleMultiplicationQuest(intent, state, session);
		} else {
			return currentQuest;
		}
	}

	private SpeechletResponse simpleMultiplication(Intent intent, DialogState state, Session session) {
		logger.info("simpleMultiplication:" + state.toString());

		SpeechletResponse speechletResp = null;

		if (state.equals(DialogState.STARTED)) {
			System.out.println("bin hier A");
			session.setAttribute(MAX_TURN, 2);
			session.setAttribute(CURRENT_TURN, 1);
			session.setAttribute(START_TIME_INTENT, System.currentTimeMillis());

			speechletResp = createSpeechletResponseForUserFeedback(session, null);

		} else {
			System.out.println("bin hier B");
			System.out.println("Antwort:" + intent.getSlot(SLOT_USER_RESPONSE).getValue());
			System.out.println("Richtige Antwort:" + session.getAttribute(EXPECTED_ANSWER));
			String answer = (session.getAttribute(EXPECTED_ANSWER))
					.equals(intent.getSlot(SLOT_USER_RESPONSE).getValue()) ? "Richtig!" : "Leider Falsch!";

			if ((Integer) (session.getAttribute(MAX_TURN)) > (Integer) (session.getAttribute(CURRENT_TURN))) {
				session.setAttribute(CURRENT_TURN, (Integer) (session.getAttribute(CURRENT_TURN)) + 1);
				speechletResp = createSpeechletResponseForUserFeedback(session, answer);
				speechletResp.setOutputSpeech(speechletResp.getOutputSpeech());

			} else {
				PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
				Long timeUsed = TimeUnit.MILLISECONDS
						.toSeconds((System.currentTimeMillis() - (Long) (session.getAttribute(START_TIME_INTENT))));
				answer += ". Gesamtdauer war " + String.valueOf(timeUsed.intValue()) + " Sekunden";
				speech.setText(answer);
				speechletResp = new SpeechletResponse();
				speechletResp.setOutputSpeech(speech);
				speechletResp.setNullableShouldEndSession(true);

			}

		}
		return speechletResp;
	}

	private SpeechletResponse createSpeechletResponseForUserFeedback(Session session, String answer) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		SpeechletResponse speechletResp = new SpeechletResponse();

		Integer op1 = (int) (Math.random() * 8) + 2;
		Integer op2 = (int) (Math.random() * 8) + 2;
		Integer result = op1 * op2;
		String question = "Was macht " + op1 + " mal " + op2 + "?";
		String speechText = (answer == null) ? question : answer + ". " + question;
		session.setAttribute(OPERATOR_1, op1);
		session.setAttribute(OPERATOR_2, op2);
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

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to the Alexa World, you can say hello to me, I can respond."
				+ "Thanks, How to do in java user.";

		// Create the Simple card content.

		SimpleCard card = new SimpleCard();
		card.setTitle("HelloWorld");
		card.setContent(speechText);

		// Create the plain text output.

		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt

		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the hello intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelloResponse() {
		String speechText = "Hello how to do in java user. It's a pleasure to talk with you. "
				+ "Currently I can only say simple things, "
				+ "but you can educate me to do more complicated tasks later. Happy to learn.";

		// Create the Simple card content.

		SimpleCard card = new SimpleCard();
		card.setTitle("HelloWorld");
		card.setContent(speechText);

		// Create the plain text output.

		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "Hello user, You can say hello to me!";

		// Create the Simple card content.

		SimpleCard card = new SimpleCard();
		card.setTitle("HelloWorld");
		card.setContent(speechText);

		// Create the plain text output.

		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt

		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		System.out.println("onSessionStarted requestId={}, sessionId={} " + request.getRequestId() + " - "
				+ session.getSessionId());
	}

	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		System.out.println(
				"onSessionEnded requestId={}, sessionId={} " + request.getRequestId() + " - " + session.getSessionId());
	}
}
