package com.mhova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhova.db.configuration.MongoDBConnectionFactory;

import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MagicServerConfiguration extends Configuration {
	@NotEmpty
	private String template;

	@NotEmpty
	private String defaultName = "Stranger";

	@Valid
	@NotNull
	private MongoDBConnectionFactory mongoDBConnectionFactory;

	@JsonProperty
	public String getTemplate() {
		return template;
	}

	@JsonProperty
	public void setTemplate(String template) {
		this.template = template;
	}

	@JsonProperty
	public String getDefaultName() {
		return defaultName;
	}

	@JsonProperty
	public void setDefaultName(String name) {
		this.defaultName = name;
	}

	@JsonProperty("mongoDB")
	public MongoDBConnectionFactory getMongoDBConnectionFactory() {
		return mongoDBConnectionFactory;
	}

	@JsonProperty("mongoDB")
	public void setMongoDBConnection(final MongoDBConnectionFactory mongoDBConnectionFactory) {
		this.mongoDBConnectionFactory = mongoDBConnectionFactory;
	}
}