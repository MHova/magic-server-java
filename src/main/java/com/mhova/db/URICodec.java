package com.mhova.db;

import java.net.URI;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class URICodec implements Codec<URI> {

	@Override
	public void encode(final BsonWriter writer, final URI value,
			final EncoderContext encoderContext) {
		if(value != null) {
			writer.writeString(value.toString());
		}

	}

	@Override
	public Class<URI> getEncoderClass() {
		return URI.class;
	}

	@Override
	public URI decode(final BsonReader reader,
			final DecoderContext decoderContext) {
		return URI.create(reader.readString());
	}

}
