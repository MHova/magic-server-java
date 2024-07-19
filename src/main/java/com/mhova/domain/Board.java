package com.mhova.domain;

import java.util.LinkedHashMap;
import java.util.Optional;

public class Board {
	public final Library library1;
	public final Library library2;

	public final LinkedHashMap<String, Card> graveyard1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> graveyard2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> battlefield1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> battlefield2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> exile1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> exile2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> hand1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> hand2 = new LinkedHashMap<>();

	public Board(final Library library1,
			final Optional<Library> maybeLibrary2) {
		this.library1 = library1;
		// TODO: fix this
		this.library2 = maybeLibrary2.orElse(null);
	}

	public boolean moveCard(final UnorderedZone origin,
			final UnorderedZone destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> unorderedZoneToMap(destination).put(cardId, card));
		return maybeCard.isPresent();
	}

	public boolean moveCardToBottom(final UnorderedZone origin,
			final PlayerLibrary destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(card -> playerLibraryToLibrary(destination)
				.putCardOnBottom(card));
		return maybeCard.isPresent();
	}

	public boolean moveCardToTop(final UnorderedZone origin,
			final PlayerLibrary destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> playerLibraryToLibrary(destination).putCardOnTop(card));
		return maybeCard.isPresent();
	}

	public boolean moveCardXFromTop(final UnorderedZone origin,
			final PlayerLibrary destination, final int x, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(card -> playerLibraryToLibrary(destination)
				.putCardXFromTop(x, card));
		return maybeCard.isPresent();
	}

	private Optional<Card> getCardFromUnorderedZone(final UnorderedZone origin,
			final String cardId) {
		return Optional.ofNullable(unorderedZoneToMap(origin).remove(cardId));
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

	private Library playerLibraryToLibrary(final PlayerLibrary library) {
		return switch (library) {
		case LIBRARY_1 -> library1;
		case LIBRARY_2 -> library2;
		};
	}
}
