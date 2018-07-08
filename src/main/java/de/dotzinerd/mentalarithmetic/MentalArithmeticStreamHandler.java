package de.dotzinerd.mentalarithmetic;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class MentalArithmeticStreamHandler extends SpeechletRequestStreamHandler {
	private static final Set<String> supportedApplicationIds;

	static {
		/*
		 * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit"
		 * the relevant Alexa Skill and put the relevant Application Ids in this Set.
		 */
		supportedApplicationIds = new HashSet<String>();
		supportedApplicationIds.add("amzn1.ask.skill.9412c659-275e-43ff-9888-8a42ddab5f8b");
		// supportedApplicationIds.add("[Add your Alexa Skill ID and then uncomment and
		// ]";
		System.out.println("Supported app ids : " + supportedApplicationIds);
	}

	public MentalArithmeticStreamHandler() {
		super(new MentalArithmeticSpeechlet(), supportedApplicationIds);
	}

	public MentalArithmeticStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
	}
}
