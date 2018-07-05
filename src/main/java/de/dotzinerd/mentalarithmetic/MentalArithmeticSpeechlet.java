package de.dotzinerd.mentalarithmetic;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class MentalArithmeticSpeechlet implements Speechlet {
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		System.out.println(
				"onLaunch requestId={}, sessionId={} " + request.getRequestId() + " - " + session.getSessionId());
		return getWelcomeResponse();
	}

	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		System.out.println(
				"onIntent requestId={}, sessionId={} " + request.getRequestId() + " - " + session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		System.out.println("intentName : " + intentName);

		if ("SayHelloIntent".equals(intentName)) {
			return getHelloResponse();
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
		} else {
			return getHelpResponse();
		}
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
