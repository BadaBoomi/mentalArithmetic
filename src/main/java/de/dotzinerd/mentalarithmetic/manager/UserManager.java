package de.dotzinerd.mentalarithmetic.manager;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.dotzinerd.mentalarithmetic.model.Constants;
import de.dotzinerd.mentalarithmetic.model.User;

public class UserManager {
	static final Logger logger = LogManager.getLogger(UserManager.class);
	private static ObjectMapper oMapper = new ObjectMapper();

	private static final UserManager singleton = new UserManager();

	private UserManager() {
	};

	public static UserManager getManager() {
		return singleton;
	}

	public User getUserFromSession(Map<String, Object> sessionAttributes) {
		String json = null;
		if (sessionAttributes.containsKey(Constants.KEY_USER)) {
			json = (String) sessionAttributes.get(Constants.KEY_USER);
			try {
				return oMapper.readValue(json, User.class);
			} catch (IOException e) {
				logger.error("could not extract User from json: " + json);

			}
		}
		return null;

	}

	public void setUserToSession(User user, Map<String, Object> sessionAttributes) {
		try {
			sessionAttributes.put(Constants.KEY_USER, oMapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			logger.error("could not extract json from User: " + user);

		}
	}

}
