package com.mhova.domain;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

/* @formatter:off */
public record Card(
		@BsonId()
		@BsonRepresentation(BsonType.OBJECT_ID)
		String cardId)
{}
/* @formatter:on */
