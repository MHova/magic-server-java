package com.mhova.health;

import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.client.MongoClient;

public class DatabaseHealthCheck extends HealthCheck {
	private final MongoClient db;

	public DatabaseHealthCheck(final MongoClient db) {
		this.db = db;
	}

	@Override
	protected Result check() throws Exception {
		try {

			final Document document = db.getDatabase("test").runCommand(
					new BsonDocument("buildInfo", new BsonInt64(1)));

			return Optional.ofNullable(document.getString("version"))
					.map(_a -> Result.healthy())
					.orElse(Result.unhealthy("No version in Mongo buildInfo"));

		} catch (final Exception e) {
			return Result.unhealthy(e);
		}
	}
}