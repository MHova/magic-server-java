package com.mhova.auth;

import java.security.Principal;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class User implements Principal {
	@BsonId
	private final String name;

	private final HashedPassword hashedPassword;

	private final List<Role> roles;

	@BsonCreator
	public User(@BsonId final String name,
			@BsonProperty("hashedPassword") final HashedPassword hashedPassword,
			@BsonProperty("roles") final List<Role> roles) {
		this.name = name;
		this.hashedPassword = hashedPassword;
		this.roles = roles;
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
}
