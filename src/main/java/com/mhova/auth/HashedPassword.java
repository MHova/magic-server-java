package com.mhova.auth;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashedPassword {
	private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(
			16);

	public final String hashedPassword;

	private HashedPassword(final String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public static HashedPassword fromRaw(final String rawPassword) {
		return new HashedPassword(ENCODER.encode(rawPassword));
	}

	public boolean equalsRaw(final String rawPassword) {
		return ENCODER.matches(rawPassword, hashedPassword);
	}

	public static class HashedPasswordCodec implements Codec<HashedPassword> {
		@Override
		public void encode(final BsonWriter writer, final HashedPassword value,
				final EncoderContext encoderContext) {
			if(value != null) {
				writer.writeString(value.hashedPassword);
			}
		}

		@Override
		public HashedPassword decode(final BsonReader reader,
				final DecoderContext decoderContext) {
			return new HashedPassword(reader.readString());
		}

		@Override
		public Class<HashedPassword> getEncoderClass() {
			return HashedPassword.class;
		}
	}
}
