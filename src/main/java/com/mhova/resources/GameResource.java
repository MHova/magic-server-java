package com.mhova.resources;

import java.util.ArrayList;
import java.util.List;

import com.codahale.metrics.annotation.Timed;
import com.mhova.domain.Card;
import com.mongodb.client.MongoClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
	private final MongoClient mongoClient;

	public GameResource(final MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@GET
	@Timed
	public List<Card> getAllCards() {
		final List<Card> retVal = new ArrayList<>();
		mongoClient.getDatabase("test").getCollection("cards", Card.class).find().into(retVal);
		return retVal;
	}
}
