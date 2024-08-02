package com.mhova.resources;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.codahale.metrics.annotation.Timed;
import com.mhova.domain.Card;
import com.mhova.domain.Library;
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

@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
public class LibrariesResource {
	private final MongoDatabase db;

	public LibrariesResource(final MongoClient mongoClient) {
		final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(List.of(Conventions.ANNOTATION_CONVENTION))
				.register(ClassModel.builder(Library.class).build())
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
		final Library library = new Library("one",
				List.of(card1, card2, card3));

		db.getCollection("libraries", Library.class).insertOne(library);

		return Response.created(new URI("/libraries/one")).build();
	}

	@GET
	@Path("/{libraryId}")
	@Timed
	public Library getLibrary(
			@PathParam("libraryId") Optional<String> maybeLibraryId) {
		final String libraryId = maybeLibraryId.orElseThrow(
				() -> new BadRequestException("library ID is required"));
		FindIterable<Library> findIterable = db
				.getCollection("libraries", Library.class)
				.find(Filters.eq(libraryId));

		final Optional<Library> maybeRetVal = Optional
				.ofNullable(findIterable.first());
		return maybeRetVal
				.orElseThrow(() -> new NotFoundException("No such library"));
	}
}
