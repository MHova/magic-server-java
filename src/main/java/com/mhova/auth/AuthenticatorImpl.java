package com.mhova.auth;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class AuthenticatorImpl
		implements Authenticator<BasicCredentials, User> {

	private final MongoDatabase db;

	public AuthenticatorImpl(final MongoClient mongoClient) {
		final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(List.of(Conventions.ANNOTATION_CONVENTION))
				.register(ClassModel.builder(User.class).build())
				.automatic(true).build();
		final CodecRegistry pojoCodecRegistry = fromRegistries(
				fromCodecs(new HashedPassword.HashedPasswordCodec()),
				getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		this.db = mongoClient.getDatabase("test")
				.withCodecRegistry(pojoCodecRegistry);
	}

	@Override
	public Optional<User> authenticate(final BasicCredentials credentials)
			throws AuthenticationException {
		final FindIterable<User> results = db.getCollection("users", User.class)
				.find(eq("_id", credentials.getUsername()));
		final Optional<User> maybeUser = Optional.ofNullable(results.first());

		if(maybeUser.isPresent() && maybeUser.get().getHashedPassword()
				.equalsRaw(credentials.getPassword())) {
			return maybeUser;
		}

		return Optional.empty();
	}
}
