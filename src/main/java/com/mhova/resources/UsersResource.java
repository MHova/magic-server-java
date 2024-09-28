package com.mhova.resources;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.codahale.metrics.annotation.Timed;
import com.mhova.api.APIError;
import com.mhova.api.NewDeck;
import com.mhova.api.NewUser;
import com.mhova.auth.HashedPassword;
import com.mhova.auth.Role;
import com.mhova.auth.User;
import com.mhova.db.ScryfallCard;
import com.mhova.db.URICodec;
import com.mhova.db.UUIDCodec;
import com.mhova.domain.Card;
import com.mhova.domain.Deck;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.dropwizard.auth.Auth;
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
import jakarta.ws.rs.core.Response.Status;

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
				fromCodecs(new HashedPassword.HashedPasswordCodec(),
						new ScryfallCard.ScryfallCardCodec(), new UUIDCodec(),
						new URICodec()),
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

	@POST
	@Timed
	@Path("/{username}/decks")
	public Response addDeck(@Auth final User authUser,
			@PathParam("username") Optional<String> maybeUsername,
			final NewDeck newDeck) {
		final String username = maybeUsername.orElseThrow(
				() -> new BadRequestException("username is required"));

		if(!username.equals(authUser.getName())) {
			return Response.status(Status.FORBIDDEN).build();
		}

		final MongoCollection<User> usersCollection = db.getCollection("users",
				User.class);

		final Optional<User> maybeUser = Optional.ofNullable(
				usersCollection.find(Filters.eq("_id", username)).first());

		final User user = maybeUser.orElseThrow(() -> new BadRequestException(
				Response.status(Status.NOT_FOUND).build()));

		user.getDecks().add(newDeckToDeck(newDeck));
		usersCollection.replaceOne(Filters.eq("_id", username), user);
		return Response.noContent().build();
	}

	private Deck newDeckToDeck(final NewDeck newDeck) {
		final FindIterable<ScryfallCard> results = db
				.getCollection("scryfall", ScryfallCard.class)
				.find(Filters.or(toCardNameFilters(newDeck)));

		final Map<String, ScryfallCard> nameToCard = new HashMap<>();

		results.forEach(card -> nameToCard.put(card.name(), card));

		final List<Card> cards = newDeck.cardNames().stream().map(s -> {
			final ScryfallCard sc = nameToCard.get(s);
			return new Card(UUID.randomUUID().toString(), sc.name(), sc.id(),
					sc.smallImage());
		}).toList();

		return new Deck(newDeck.name(), cards);
	}

	private List<Bson> toCardNameFilters(final NewDeck newDeck) {
		return newDeck.cardNames().stream()
				.map(name -> Filters.eq("name", name)).toList();
	}
}
