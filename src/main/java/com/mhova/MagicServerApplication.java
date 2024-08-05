package com.mhova;

import com.mhova.health.TemplateHealthCheck;
import com.mhova.resources.GamesResource;
import com.mhova.resources.HelloWorldResource;
import com.mhova.resources.BoardsResource;
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
	public String getName() {
		return "hello-world";
	}

	@Override
	public void initialize(
			final Bootstrap<MagicServerConfiguration> bootstrap) {
		// nothing to do yet
	}

	@Override
	public void run(final MagicServerConfiguration configuration,
			final Environment environment) {
		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
				configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);

		final HelloWorldResource helloWorldResource = new HelloWorldResource(
				configuration.getTemplate(), configuration.getDefaultName());
		environment.jersey().register(helloWorldResource);

		final MongoClient mongoClient = configuration
				.getMongoDBConnectionFactory().build(environment);

		environment.jersey().register(new GamesResource(mongoClient));
		environment.jersey().register(new BoardsResource(mongoClient));
	}
}