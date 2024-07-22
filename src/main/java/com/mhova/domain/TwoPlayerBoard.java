package com.mhova.domain;

import java.util.LinkedHashMap;
import java.util.Optional;

public class TwoPlayerBoard {
	private final Player player1;
	private final Player player2;

	public TwoPlayerBoard(final Player player1, final Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public boolean moveCard(final UnorderedZone origin,
			final UnorderedZone destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> unorderedZoneToMap(destination).put(cardId, card));
		return maybeCard.isPresent();
	}

	public boolean moveCardToBottom(final UnorderedZone origin,
			final Players destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> playersToPlayer(destination).putCardOnBottom(card));
		return maybeCard.isPresent();
	}

	public boolean moveCardToTop(final UnorderedZone origin,
			final Players destination, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> playersToPlayer(destination).putCardOnTop(card));
		return maybeCard.isPresent();
	}

	public boolean moveCardXFromTop(final UnorderedZone origin,
			final Players destination, final int x, final String cardId) {
		Optional<Card> maybeCard = getCardFromUnorderedZone(origin, cardId);
		maybeCard.ifPresent(
				card -> playersToPlayer(destination).putCardXFromTop(x, card));
		return maybeCard.isPresent();
	}

	public void millX(final Players player, final int x) {
		playersToPlayer(player).millX(x);
	}

	public void exileTopX(final Players player, final int x) {
		playersToPlayer(player).exileTopX(x);
	}

	public void drawX(final Players player, final int x) {
		playersToPlayer(player).drawX(x);
	}

	private Optional<Card> getCardFromUnorderedZone(final UnorderedZone origin,
			final String cardId) {
		return Optional.ofNullable(unorderedZoneToMap(origin).remove(cardId));
	}

	private LinkedHashMap<String, Card> unorderedZoneToMap(
			final UnorderedZone zone) {
		return switch (zone) {
		case GRAVEYARD_1 -> player1.getGraveyard();
		case GRAVEYARD_2 -> player2.getGraveyard();
		case BATTLEFIELD_1 -> player1.getBattlefield();
		case BATTLEFIELD_2 -> player2.getBattlefield();
		case EXILE_1 -> player1.getExile();
		case EXILE_2 -> player2.getExile();
		case HAND_1 -> player1.getHand();
		case HAND_2 -> player2.getHand();
		};
	}

	private Player playersToPlayer(final Players players) {
		return switch (players) {
		case PLAYER_1 -> player1;
		case PLAYER_2 -> player2;
		};
	}
}
