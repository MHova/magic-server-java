package com.mhova.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class LibraryTest {
	private Library classUnderTest;

	@Test
	void addToTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(1, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void addSecondFromTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(2, two);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void addThirdFromTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(two);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(3, three);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void addFourthFromTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(two);
		cards.add(three);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(4, four);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void addFifthFromTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(two);
		cards.add(three);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(5, four);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void treatZeroXAsAddToTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(-1, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void treatNegativeXAsAddToTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardXFromTop(-1, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void addNegativeXToEmptyLibrary() {
		final Card one = new Card("1");
		classUnderTest = new Library(Collections.emptyList());
		classUnderTest.putCardXFromTop(-1, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(1, result.size());
		assertEquals(one, result.get(0));
	}

	@Test
	void addZeroToEmptyLibrary() {
		final Card one = new Card("1");
		classUnderTest = new Library(Collections.emptyList());
		classUnderTest.putCardXFromTop(0, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(1, result.size());
		assertEquals(one, result.get(0));
	}

	@Test
	void addToTopToEmptyLibrary() {
		final Card one = new Card("1");
		classUnderTest = new Library(Collections.emptyList());
		classUnderTest.putCardXFromTop(1, one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(1, result.size());
		assertEquals(one, result.get(0));
	}

	@Test
	void putCardOnTop() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		classUnderTest.putCardOnTop(one);
		final List<Card> result = classUnderTest.asList();
		assertEquals(4, result.size());
		assertEquals(one, result.get(0));
		assertEquals(two, result.get(1));
		assertEquals(three, result.get(2));
		assertEquals(four, result.get(3));
	}

	@Test
	void removeTopX() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		assertEquals(4, classUnderTest.size());

		List<Card> retVal = classUnderTest.removeTopX(0);
		assertTrue(retVal.isEmpty());
		assertEquals(4, classUnderTest.size());

		retVal = classUnderTest.removeTopX(-1);
		assertTrue(retVal.isEmpty());
		assertEquals(4, classUnderTest.size());

		retVal = classUnderTest.removeTopX(3);
		assertEquals(3, retVal.size());
		assertEquals(one, retVal.get(0));
		assertEquals(two, retVal.get(1));
		assertEquals(three, retVal.get(2));
		assertEquals(1, classUnderTest.size());
	}
	
	@Test
	void removeTooManyFromTopX() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");

		final List<Card> cards = new LinkedList<>();
		cards.add(one);
		cards.add(two);
		cards.add(three);
		cards.add(four);
		classUnderTest = new Library(cards);
		assertEquals(4, classUnderTest.size());

		List<Card> retVal = classUnderTest.removeTopX(5);
		assertEquals(4, retVal.size());
		assertEquals(one, retVal.get(0));
		assertEquals(two, retVal.get(1));
		assertEquals(three, retVal.get(2));
		assertEquals(four, retVal.get(3));
		assertEquals(0, classUnderTest.size());
	}
}
