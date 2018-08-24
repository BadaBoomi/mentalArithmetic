package de.dotzinerd.mentalarithmetic.model.quests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SimpleQuestTests {

	@Test
	void testSimpleMultiplicationQuestCreatedById() {
		Quest quest = new SimpleMultiplicationQuest("5x6");
		assertEquals("5x6", quest.getId());
		assertEquals(true, quest.isCorrectAnswer("30"));
	}

	@Test
	void testSimpleTwoDigitSquareQuestCreatedById() {
		Quest quest = new SimpleTwoDigitSquareQuest("55");
		assertEquals("55", quest.getId());
		assertEquals(true, quest.isCorrectAnswer("3025"));
	}

	@Test
	void testSimpleTwoDigitMultSum10QuestCreatedById() {
		Quest quest = new SimpleTwoDigitMultSum10Quest("4x9");
		assertEquals("4x9", quest.getId());
		assertEquals(true, quest.isCorrectAnswer("2009"));
	}

	@Test
	void testSimpleMultby11CreatedById() {
		Quest quest = new SimpleMultby11Quest("4x5");
		assertEquals("4x5", quest.getId());
		assertEquals(true, quest.isCorrectAnswer("495"));
	}

	@Test
	void testAdvancedMultby11QuestCreatedById() {
		Quest quest = new AdvancedMultby11Quest("4x7");
		assertEquals("4x7", quest.getId());
		assertEquals(true, quest.isCorrectAnswer("517"));
	}

	@Test
	void testAdvancedMultby11Quest() {
		Quest quest = new AdvancedMultby11Quest();
		String id = quest.getId();
		System.out.println(id);
		
		String[] ops = id.split("x");
		assertEquals(1, ops[0].length());
		assertEquals(1, ops[1].length());
		Integer dig1 = Integer.valueOf(ops[0]);
		Integer dig2 = Integer.valueOf(ops[1]);
		
		assertTrue(dig1 + dig2 >= 10);
	}
}
