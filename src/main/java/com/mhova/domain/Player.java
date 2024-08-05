package com.mhova.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Player {
	private final String id;
	private final String name;
	private final Library library;
	private final LinkedHashMap<String, Card> graveyard;
	private final LinkedHashMap<String, Card> battlefield;
	private final LinkedHashMap<String, Card> exile;
	private final LinkedHashMap<String, Card> hand;

	@BsonCreator
	public Player(@BsonId final String id,
			@BsonProperty("name") final String name,
			@BsonProperty("library") final Library library,
			@BsonProperty("graveyard") final LinkedHashMap<String, Card> graveyard,
			@BsonProperty("battlefield") final LinkedHashMap<String, Card> battlefield,
			@BsonProperty("exile") final LinkedHashMap<String, Card> exile,
			@BsonProperty("hand") final LinkedHashMap<String, Card> hand) {
		this.id = id;
		this.name = name;
		this.library = library;
		this.graveyard = graveyard;
		this.battlefield = battlefield;
		this.exile = exile;
		this.hand = hand;
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

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Library getLibrary() {
		return library;
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
			destination.put(c.cardId(), c);
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
