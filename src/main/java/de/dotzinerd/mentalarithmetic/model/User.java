package de.dotzinerd.mentalarithmetic.model;

import de.dotzinerd.mentalarithmetic.enums.Level;
import lombok.Data;

@Data
public class User {

	private String id;
	private Level currentLevel;
}
