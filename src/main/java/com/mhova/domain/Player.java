package com.mhova.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Player {
	public final String name;

	private final Library library;

	private final LinkedHashMap<String, Card> graveyard = new LinkedHashMap<>();
	private final LinkedHashMap<String, Card> battlefield = new LinkedHashMap<>();
	private final LinkedHashMap<String, Card> exile = new LinkedHashMap<>();
	private final LinkedHashMap<String, Card> hand = new LinkedHashMap<>();

	public Player(final String name, final Library library) {
		super();
		this.name = name;
		this.library = library;
	}

	public void millX(final int x) {
		final List<Card> cardsToMill = library.removeTopX(x);
		putCards(cardsToMill, graveyard);
	}

	public void exileTopX(final int x) {
		final List<Card> cardsToExile = library.removeTopX(x);
		putCards(cardsToExile, exile);
	}

	public void drawX(final int x) {
		final List<Card> cardsToDraw = library.removeTopX(x);
		putCards(cardsToDraw, hand);
	}

	public void putCardOnTop(final Card card) {
		library.putCardOnTop(card);
	}

	public void putCardOnBottom(final Card card) {
		library.putCardOnBottom(card);
	}

	public void putCardXFromTop(final int x, final Card card) {
		library.putCardXFromTop(x, card);
	}
	
	public List<Card> lookAtTopX(final int x) {
		return library.lookAtTopX(x);
	}

	public LinkedHashMap<String, Card> getGraveyard() {
		return graveyard;
	}

	public LinkedHashMap<String, Card> getBattlefield() {
		return battlefield;
	}

	public LinkedHashMap<String, Card> getExile() {
		return exile;
	}

	public LinkedHashMap<String, Card> getHand() {
		return hand;
	}

	private void putCards(final List<Card> cards,
			final LinkedHashMap<String, Card> destination) {
		for (final Card c : cards) {
			destination.put(c.id(), c);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(battlefield, exile, graveyard, hand, library, name);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Player other = (Player) obj;
		return Objects.equals(battlefield, other.battlefield)
				&& Objects.equals(exile, other.exile)
				&& Objects.equals(graveyard, other.graveyard)
				&& Objects.equals(hand, other.hand)
				&& Objects.equals(library, other.library)
				&& Objects.equals(name, other.name);
	}
}
