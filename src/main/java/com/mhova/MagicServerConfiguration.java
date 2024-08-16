package com.mhova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhova.db.configuration.MongoDBConnectionFactory;

import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class MagicServerConfiguration extends Configuration {
	@Valid
	@NotNull
	private MongoDBConnectionFactory mongoDBConnectionFactory;

	@JsonProperty("mongoDB")
	public MongoDBConnectionFactory getMongoDBConnectionFactory() {
		return mongoDBConnectionFactory;
	}

	@JsonProperty("mongoDB")
	public void setMongoDBConnection(
			final MongoDBConnectionFactory mongoDBConnectionFactory) {
		this.mongoDBConnectionFactory = mongoDBConnectionFactory;
	}
}