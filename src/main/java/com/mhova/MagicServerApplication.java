package com.mhova;

import com.mhova.health.DatabaseHealthCheck;
import com.mhova.resources.GamesResource;
import com.mongodb.client.MongoClient;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class MagicServerApplication
		extends Application<MagicServerConfiguration> {
	public static void main(String[] args) throws Exception {
		new MagicServerApplication().run(args);
	}

	@Override
	public void initialize(
			final Bootstrap<MagicServerConfiguration> bootstrap) {
		// nothing to do yet
	}

	@Override
	public void run(final MagicServerConfiguration configuration,
			final Environment environment) {
		final MongoClient mongoClient = configuration
				.getMongoDBConnectionFactory().build(environment);

		final DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(
				mongoClient);
		environment.healthChecks().register("database", healthCheck);

		environment.jersey().register(new GamesResource(mongoClient));
	}
}