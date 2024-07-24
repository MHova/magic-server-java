package com.mhova.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Library {
	private final Deque<Card> deque = new ArrayDeque<>();

	public Library(final List<Card> cards) {
		deque.addAll(cards);
	}

	public void putCardOnTop(final Card card) {
		putCardXFromTop(1, card);
	}

	public void putCardXFromTop(final int x, final Card card) {
		final int limit = x <= deque.size() ? x : deque.size() + 1;
		final LinkedList<Card> temp = new LinkedList<>();

		for (int n = 1; n < limit; n++) {
			temp.addFirst(deque.poll());
		}

		deque.addFirst(card);
		temp.stream().forEachOrdered(c -> deque.addFirst(c));
	}

	public void putCardOnBottom(final Card card) {
		deque.add(card);
	}

	public List<Card> removeTopX(final int x) {
		final List<Card> retVal = new LinkedList<>();
		final int limit = Math.min(x, deque.size());

		for (int i = 0; i < limit; i++) {
			retVal.add(deque.remove());
		}

		return retVal;
	}

	public List<Card> lookAtTopX(final int x) {
		int limit = Math.min(x, deque.size());
		limit = Math.max(limit, 0);
		return asList().subList(0, limit);
	}

	public int size() {
		return deque.size();
	}

	public List<Card> asList() {
		final List<Card> retVal = new ArrayList<>();
		retVal.addAll(deque);
		return retVal;
	}
}
