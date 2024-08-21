package com.mhova.auth;

import java.security.Principal;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class User implements Principal {
	@BsonId
	private final String name;

	// TODO: make this typesafe
	private final String encryptedPassword;

	private final List<Role> roles;

	@BsonCreator
	public User(@BsonId final String name,
			@BsonProperty("encryptedPassword") final String encryptedPassword,
			@BsonProperty("roles") final List<Role> roles) {
		this.name = name;
		this.encryptedPassword = encryptedPassword;
		this.roles = roles;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public List<Role> getRoles() {
		return roles;
	}
}
