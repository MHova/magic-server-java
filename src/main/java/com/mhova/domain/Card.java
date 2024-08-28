package com.mhova.domain;

import java.net.URI;
import java.util.UUID;

public record Card(String cardId, String name, UUID scryfallId,
		URI imageLocation) {
}
