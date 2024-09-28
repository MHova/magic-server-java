package com.mhova.db;

import java.net.URI;
import java.util.UUID;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public record ScryfallCard(UUID id, String name, URI smallImage) {

	public static class ScryfallCardCodec implements Codec<ScryfallCard> {
		@Override
		public void encode(final BsonWriter writer, final ScryfallCard value,
				final EncoderContext encoderContext) {
			// Do nothing. The app will never write to the Scryfall collection.
		}

		@Override
		public ScryfallCard decode(final BsonReader reader,
				final DecoderContext decoderContext) {
			UUID id = null;
			String name = null;
			URI smallImageURI = null;

			reader.readStartDocument();

			while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
				String fieldName = reader.readName();

				if(fieldName.equals("id")) {
					id = UUID.fromString(reader.readString());
				}
				else if(fieldName.equals("name")) {
					name = reader.readString();
				}
				else if(fieldName.equals("image_uris")) {
					reader.readStartDocument();
					while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
						fieldName = reader.readName();

						if(fieldName.equals("small")) {
							smallImageURI = URI.create(reader.readString());
						}
						else {
							reader.skipValue();
						}
					}

					reader.readEndDocument();
				}
				else {
					reader.skipValue();
				}
			}

			reader.readEndDocument();

			return new ScryfallCard(id, name, smallImageURI);
		}

		@Override
		public Class<ScryfallCard> getEncoderClass() {
			return ScryfallCard.class;
		}
	}
}
