package com.mhova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.mhova.db.configuration.MongoDBConnectionFactory;

import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class MagicServerConfiguration extends Configuration {
	@Valid
	@NotNull
	private MongoDBConnectionFactory mongoDBConnectionFactory;

	@Valid
	@NotNull
	private CaffeineSpec authenticationCachePolicy;

	@JsonProperty("mongoDB")
	public MongoDBConnectionFactory getMongoDBConnectionFactory() {
		return mongoDBConnectionFactory;
	}

	@JsonProperty("mongoDB")
	public void setMongoDBConnection(
			final MongoDBConnectionFactory mongoDBConnectionFactory) {
		this.mongoDBConnectionFactory = mongoDBConnectionFactory;
	}

	public CaffeineSpec getAuthenticationCachePolicy() {
		return authenticationCachePolicy;
	}

	public void setAuthenticationCachePolicy(CaffeineSpec authCachePolicy) {
		this.authenticationCachePolicy = authCachePolicy;
	}
}