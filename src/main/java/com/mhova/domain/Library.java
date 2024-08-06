package com.mhova.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Library {
	private final Deque<Card> deck;

	@BsonCreator
	public Library(@BsonProperty("deck") final List<Card> cards) {
		this.deck = new ArrayDeque<>(cards);
	}

	public List<Card> getDeck() {
		return new ArrayList<>(deck);
	}

	public void putCardOnTop(final Card card) {
		putCardXFromTop(1, card);
	}

	public void putCardXFromTop(final int x, final Card card) {
		final int limit = x <= deck.size() ? x : deck.size() + 1;
		final LinkedList<Card> temp = new LinkedList<>();

		for (int n = 1; n < limit; n++) {
			temp.addFirst(deck.poll());
		}

		deck.addFirst(card);
		temp.stream().forEachOrdered(c -> deck.addFirst(c));
	}

	public void putCardOnBottom(final Card card) {
		deck.add(card);
	}

	public List<Card> removeTopX(final int x) {
		final List<Card> retVal = new LinkedList<>();
		final int limit = Math.min(x, deck.size());

		for (int i = 0; i < limit; i++) {
			retVal.add(deck.remove());
		}

		return retVal;
	}

	public List<Card> lookAtTopX(final int x) {
		int limit = Math.min(x, deck.size());
		limit = Math.max(limit, 0);
		return getDeck().subList(0, limit);
	}

	public int size() {
		return deck.size();
	}
}
