package com.mhova.db.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.dropwizard.core.setup.Environment;
import io.dropwizard.lifecycle.Managed;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public final class MongoDBConnectionFactory {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoDBConnectionFactory.class);

	private static final String CONNECTION_STRING_FORMAT = "mongodb://%s:%s";

	@NotEmpty
	private String host;

	@Min(1)
	@Max(65535)
	private int port;

	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(final String host) {
		this.host = host;
	}

	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(final int port) {
		this.port = port;
	}

	public MongoClient build(final Environment environment) {
		LOGGER.info("Creating mongoDB client.");

		final MongoClient client = MongoClients.create(
				CONNECTION_STRING_FORMAT.formatted(getHost(), getPort()));

		environment.lifecycle().manage(new Managed() {
			@Override
			public void start() {
			}

			@Override
			public void stop() {
				client.close();
			}
		});

		return client;
	}
}
