package com.example.howtodoinjava.alexa;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class SayHelloRequestStreamHandler extends SpeechletRequestStreamHandler {
	private static final Set<String> supportedApplicationIds;

	static {
		/*
		 * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit"
		 * the relevant Alexa Skill and put the relevant Application Ids in this Set.
		 */
		supportedApplicationIds = new HashSet<String>();
		supportedApplicationIds.add("amzn1.ask.skill.a0133886-af65-4b67-97d4-8a1254823ded");
		// supportedApplicationIds.add("[Add your Alexa Skill ID and then uncomment and
		// ]";
		System.out.println("Supported app ids : " + supportedApplicationIds);
	}

	public SayHelloRequestStreamHandler() {
		super(new SayHelloSpeechlet(), supportedApplicationIds);
	}

	public SayHelloRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
	}
}
