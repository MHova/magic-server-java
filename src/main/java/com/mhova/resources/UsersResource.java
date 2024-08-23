package com.mhova.resources;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.codahale.metrics.annotation.Timed;
import com.mhova.api.APIError;
import com.mhova.api.NewUser;
import com.mhova.auth.HashedPassword;
import com.mhova.auth.Role;
import com.mhova.auth.User;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
	private final MongoDatabase db;

	public UsersResource(final MongoClient mongoClient) {
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

	@GET
	@Path("/{username}")
	@Timed
	public User getUser(@PathParam("username") Optional<String> maybeUsername) {
		final String username = maybeUsername.orElseThrow(
				() -> new BadRequestException("username is required"));
		final FindIterable<User> findIterable = db
				.getCollection("users", User.class).find(Filters.eq(username));

		final Optional<User> maybeRetVal = Optional
				.ofNullable(findIterable.first());

		return maybeRetVal
				.orElseThrow(() -> new NotFoundException("No such user"));
	}

	@POST
	@Timed
	public Response createUser(final NewUser newUser)
			throws URISyntaxException {

		// TODO: Sanitize username, maybe
		// https://github.com/OWASP/java-html-sanitizer
		final User userToInsert = new User(newUser.name(),
				HashedPassword.fromRaw(newUser.password()),
				List.of(Role.PLAYER), Collections.emptyList());

		try {
			db.getCollection("users", User.class).insertOne(userToInsert);
		} catch (final MongoWriteException e) {
			if(e.getError().getCode() == 11000) {
				return Response.status(Response.Status.CONFLICT)
						.entity(new APIError("A user with name "
								+ newUser.name() + " already exists."))
						.build();
			}
			else {
				throw e;
			}
		}

		return Response.created(new URI("/users/" + newUser.name())).build();
	}
}
