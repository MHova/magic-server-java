package com.mhova.domain;

import java.util.List;

public class Deck {
	public final String name;
	public final List<Card> cards;

	public Deck(final String name, final List<Card> cards) {
		this.name = name;
		this.cards = cards;
	}
}
