package com.mhova.auth;

import java.security.Principal;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.mhova.domain.Deck;

public class User implements Principal {
	@BsonId
	private final String name;

	private final HashedPassword hashedPassword;

	private final List<Role> roles;

	private final List<Deck> decks;

	@BsonCreator
	public User(@BsonId final String name,
			@BsonProperty("hashedPassword") final HashedPassword hashedPassword,
			@BsonProperty("roles") final List<Role> roles,
			@BsonProperty("decks") final List<Deck> decks) {
		this.name = name;
		this.hashedPassword = hashedPassword;
		this.roles = roles;
		this.decks = decks;
	}

	@Override
	public String getName() {
		return name;
	}

	public HashedPassword getHashedPassword() {
		return hashedPassword;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
	public List<Deck> getDecks() {
		return decks;
	}
}
