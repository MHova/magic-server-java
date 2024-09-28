package com.mhova.auth;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;

public class AuthorizerImpl implements Authorizer<User> {

	@Override
	public boolean authorize(final User principal, final String role,
			@Nullable final ContainerRequestContext requestContext) {
		return principal.getRoles().contains(Role.valueOf(role));
	}
}
