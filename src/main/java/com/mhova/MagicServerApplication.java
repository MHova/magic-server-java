package com.mhova;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.mhova.auth.AuthenticatorImpl;
import com.mhova.auth.AuthorizerImpl;
import com.mhova.auth.User;
import com.mhova.health.DatabaseHealthCheck;
import com.mhova.resources.GamesResource;
import com.mongodb.client.MongoClient;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
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

		final CachingAuthenticator<BasicCredentials, User> cachingAuthenticator = new CachingAuthenticator<>(
				environment.metrics(), new AuthenticatorImpl(mongoClient),
				configuration.getAuthenticationCachePolicy());

		environment.jersey()
				.register(new AuthDynamicFeature(
						new BasicCredentialAuthFilter.Builder<User>()
								.setAuthenticator(cachingAuthenticator)
								.setAuthorizer(new AuthorizerImpl())
								.setRealm("New Capenna").buildAuthFilter()));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey()
				.register(new AuthValueFactoryProvider.Binder<>(User.class));

		final DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(
				mongoClient);
		environment.healthChecks().register("database", healthCheck);

		environment.jersey().register(new GamesResource(mongoClient));
	}
}