package de.dotzinerd.mentalarithmetic.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.dotzinerd.mentalarithmetic.model.quests.AdvancedMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultby11Quest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleMultiplicationQuest;
import de.dotzinerd.mentalarithmetic.model.quests.SimpleTwoDigitSquareQuest;

public enum Level {
	LVL_MULT_BY_11_SIMPLE, LVL_MULT_BY_11_ADVANCED, LVL_SIMPLE_MULTIPLICATION, LVL_2DIGIT_SQUARE, LVL_MULT_2DIGIT_SUM_IS_10;

	static final Logger logger = LogManager.getLogger(Level.class);
	
	public static Level getLevelByName(String name) {
		logger.debug("getLevelByName for:"+name);
		
		for (Level en : Level.values()) {
			if (en.name().equals(name)) {
				return en;
			}
		}
		return null;
	}

	
}
