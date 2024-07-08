package com.mhova.domain;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

public class Board {
	public final Deque<Card> library1 = new LinkedList<>();
	public final Deque<Card> library2 = new LinkedList<>();

	public final LinkedHashMap<String, Card> graveyard1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> graveyard2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> battlefield1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> battlefield2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> exile1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> exile2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> hand1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> hand2 = new LinkedHashMap<>();

	public boolean moveCard(final UnorderedZone origin,
			final UnorderedZone destination, final String cardId) {
		Optional<Card> maybeCard = Optional
				.ofNullable(unorderedZoneToMap(origin).remove(cardId));
		maybeCard.ifPresent(
				card -> unorderedZoneToMap(destination).put(cardId, card));
		return maybeCard.isPresent();
	}

	public boolean moveCardToBottom(final UnorderedZone origin,
			final Library destination, final String cardId) {
		Optional<Card> maybeCard = Optional
				.ofNullable(unorderedZoneToMap(origin).remove(cardId));
		maybeCard.ifPresent(card -> libraryToDeque(destination).addLast(card));
		return maybeCard.isPresent();
	}

	private LinkedHashMap<String, Card> unorderedZoneToMap(
			final UnorderedZone zone) {
		return switch (zone) {
		case GRAVEYARD_1 -> graveyard1;
		case GRAVEYARD_2 -> graveyard2;
		case BATTLEFIELD_1 -> battlefield1;
		case BATTLEFIELD_2 -> battlefield2;
		case EXILE_1 -> exile1;
		case EXILE_2 -> exile2;
		case HAND_1 -> hand1;
		case HAND_2 -> hand2;
		};
	}

	private Deque<Card> libraryToDeque(final Library library) {
		return switch (library) {
		case LIBRARY_1 -> library1;
		case LIBRARY_2 -> library2;
		};
	}
}
