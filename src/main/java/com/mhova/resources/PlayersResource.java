package com.mhova.resources;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.codahale.metrics.annotation.Timed;
import com.mhova.domain.Card;
import com.mhova.domain.Library;
import com.mhova.domain.Player;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayersResource {
	private final MongoDatabase db;

	public PlayersResource(final MongoClient mongoClient) {
		final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(List.of(Conventions.ANNOTATION_CONVENTION))
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
	public Response populateDummyData() throws URISyntaxException {
		final Card card1 = new Card("one_1");
		final Card card2 = new Card("one_2");
		final Card card3 = new Card("one_3");
		final Library library = new Library(List.of(card1, card2, card3));

		final Card card4 = new Card("one_4");
		final Card card5 = new Card("one_5");
		final Card card6 = new Card("one_6");
		final Card card7 = new Card("one_7");
		final Card card8 = new Card("one_8");
		final Card card9 = new Card("one_9");
		final Card card10 = new Card("one_10");
		final Card card11 = new Card("one_11");

		final LinkedHashMap<String, Card> graveyard = new LinkedHashMap<>();
		graveyard.put(card4.cardId(), card4);
		graveyard.put(card5.cardId(), card5);

		final LinkedHashMap<String, Card> battlefield = new LinkedHashMap<>();
		battlefield.put(card6.cardId(), card6);
		battlefield.put(card7.cardId(), card7);

		final LinkedHashMap<String, Card> exile = new LinkedHashMap<>();
		exile.put(card8.cardId(), card8);
		exile.put(card9.cardId(), card9);

		final LinkedHashMap<String, Card> hand = new LinkedHashMap<>();
		hand.put(card10.cardId(), card10);
		hand.put(card11.cardId(), card11);

		final Player player = new Player("one", "Alice", library, graveyard,
				battlefield, exile, hand);

		db.getCollection("players", Player.class).insertOne(player);

		return Response.created(new URI("/players/one")).build();
	}

	@GET
	@Path("/{playerId}")
	@Timed
	public Player getPlayer(
			@PathParam("playerId") Optional<String> maybePlayerId) {
		final String playerId = maybePlayerId.orElseThrow(
				() -> new BadRequestException("player ID is required"));
		FindIterable<Player> findIterable = db
				.getCollection("players", Player.class)
				.find(Filters.eq(playerId));

		final Optional<Player> maybeRetVal = Optional
				.ofNullable(findIterable.first());
		return maybeRetVal
				.orElseThrow(() -> new NotFoundException("No such player"));
	}
}
