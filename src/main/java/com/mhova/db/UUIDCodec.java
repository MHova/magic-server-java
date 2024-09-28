package com.mhova.db;

import java.util.UUID;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class UUIDCodec implements Codec<UUID> {

	@Override
	public void encode(final BsonWriter writer, final UUID value,
			final EncoderContext encoderContext) {
		if(value != null) {
			writer.writeString(value.toString());
		}
	}

	@Override
	public Class<UUID> getEncoderClass() {
		return UUID.class;
	}

	@Override
	public UUID decode(final BsonReader reader,
			final DecoderContext decoderContext) {
		return UUID.fromString(reader.readString());
	}
}
