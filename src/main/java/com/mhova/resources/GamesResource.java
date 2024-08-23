package com.mhova.resources;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.codahale.metrics.annotation.Timed;
import com.mhova.api.APIError;
import com.mhova.api.GameId;
import com.mhova.auth.User;
import com.mhova.domain.Card;
import com.mhova.domain.Library;
import com.mhova.domain.Player;
import com.mhova.domain.TwoPlayerBoard;
import com.mhova.domain.TwoPlayerGame;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.dropwizard.auth.Auth;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

@Path("/games")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GamesResource {
	private final MongoDatabase db;

	public GamesResource(final MongoClient mongoClient) {
		final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(List.of(Conventions.ANNOTATION_CONVENTION))
				.register(ClassModel.builder(TwoPlayerGame.class).build())
				.register(ClassModel.builder(TwoPlayerBoard.class).build())
				.register(ClassModel.builder(Library.class).build())
				.register(ClassModel.builder(Player.class).build())
				.automatic(true).build();
		final CodecRegistry pojoCodecRegistry = fromRegistries(
				getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
		this.db = mongoClient.getDatabase("test")
				.withCodecRegistry(pojoCodecRegistry);
	}

	@POST
	@Timed
	public Response createNewGame(@Auth final User user,
			@NotNull @Valid final GameId gameId) throws URISyntaxException {
		// Player 1
		final URI scryfall = new URI(
				"https://cards.scryfall.io/small/front/6/d/6da045f8-6278-4c84-9d39-025adf0789c1.jpg?1562404626");

		final Card card1 = new Card("one_1", UUID.randomUUID(), scryfall);
		final Card card2 = new Card("one_2", UUID.randomUUID(), scryfall);
		final Card card3 = new Card("one_3", UUID.randomUUID(), scryfall);
		final Library library1 = new Library(List.of(card1, card2, card3));

		final Card card4 = new Card("one_4", UUID.randomUUID(), scryfall);
		final Card card5 = new Card("one_5", UUID.randomUUID(), scryfall);
		final Card card6 = new Card("one_6", UUID.randomUUID(), scryfall);
		final Card card7 = new Card("one_7", UUID.randomUUID(), scryfall);
		final Card card8 = new Card("one_8", UUID.randomUUID(), scryfall);
		final Card card9 = new Card("one_9", UUID.randomUUID(), scryfall);
		final Card card10 = new Card("one_10", UUID.randomUUID(), scryfall);
		final Card card11 = new Card("one_11", UUID.randomUUID(), scryfall);

		final LinkedHashMap<String, Card> graveyard1 = new LinkedHashMap<>();
		graveyard1.put(card4.cardId(), card4);
		graveyard1.put(card5.cardId(), card5);

		final LinkedHashMap<String, Card> battlefield1 = new LinkedHashMap<>();
		battlefield1.put(card6.cardId(), card6);
		battlefield1.put(card7.cardId(), card7);

		final LinkedHashMap<String, Card> exile1 = new LinkedHashMap<>();
		exile1.put(card8.cardId(), card8);
		exile1.put(card9.cardId(), card9);

		final LinkedHashMap<String, Card> hand1 = new LinkedHashMap<>();
		hand1.put(card10.cardId(), card10);
		hand1.put(card11.cardId(), card11);

		final Player player1 = new Player("Alice", library1, graveyard1,
				battlefield1, exile1, hand1);

		// Player 2

		final Card card12 = new Card("one_12", UUID.randomUUID(), scryfall);
		final Card card13 = new Card("one_13", UUID.randomUUID(), scryfall);
		final Card card14 = new Card("one_14", UUID.randomUUID(), scryfall);
		final Library library2 = new Library(List.of(card12, card13, card14));

		final Card card15 = new Card("one_15", UUID.randomUUID(), scryfall);
		final Card card16 = new Card("one_16", UUID.randomUUID(), scryfall);
		final Card card17 = new Card("one_17", UUID.randomUUID(), scryfall);
		final Card card18 = new Card("one_18", UUID.randomUUID(), scryfall);
		final Card card19 = new Card("one_19", UUID.randomUUID(), scryfall);
		final Card card20 = new Card("one_20", UUID.randomUUID(), scryfall);
		final Card card21 = new Card("one_21", UUID.randomUUID(), scryfall);
		final Card card22 = new Card("one_22", UUID.randomUUID(), scryfall);

		final LinkedHashMap<String, Card> graveyard2 = new LinkedHashMap<>();
		graveyard2.put(card4.cardId(), card15);
		graveyard2.put(card5.cardId(), card16);

		final LinkedHashMap<String, Card> battlefield2 = new LinkedHashMap<>();
		battlefield2.put(card6.cardId(), card17);
		battlefield2.put(card7.cardId(), card18);

		final LinkedHashMap<String, Card> exile2 = new LinkedHashMap<>();
		exile2.put(card8.cardId(), card19);
		exile2.put(card9.cardId(), card20);

		final LinkedHashMap<String, Card> hand2 = new LinkedHashMap<>();
		hand2.put(card10.cardId(), card21);
		hand2.put(card11.cardId(), card22);

		final Player player2 = new Player("Bob", library2, graveyard2,
				battlefield2, exile2, hand2);
		final TwoPlayerBoard board = new TwoPlayerBoard(player1, player2);

		try {
			db.getCollection("games", TwoPlayerGame.class)
					.insertOne(new TwoPlayerGame(gameId.id(), board));
		} catch (final MongoWriteException e) {
			if(e.getError().getCode() == 11000) {
				return Response.status(Response.Status.CONFLICT)
						.entity(new APIError("A game with id " + gameId.id()
								+ " already exists."))
						.build();
			}
			else {
				throw e;
			}
		}

		return Response.created(new URI("/game/" + gameId.id())).build();
	}

	@GET
	@Path("/{gameId}")
	@Timed
	public TwoPlayerGame getGame(
			@PathParam("gameId") Optional<String> maybeGameId) {
		final String gameId = maybeGameId.orElseThrow(
				() -> new BadRequestException("game ID is required"));
		final FindIterable<TwoPlayerGame> findIterable = db
				.getCollection("games", TwoPlayerGame.class)
				.find(Filters.eq(gameId));

		final Optional<TwoPlayerGame> maybeRetVal = Optional
				.ofNullable(findIterable.first());
		return maybeRetVal
				.orElseThrow(() -> new NotFoundException("No such game"));
	}
}
