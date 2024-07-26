package com.mhova.resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.codahale.metrics.annotation.Timed;
import com.mhova.api.Saying;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
	private final String template;
	private final String defaultName;
	private final AtomicLong counter;

	public HelloWorldResource(final String template, final String defaultName) {
		this.template = template;
		this.defaultName = defaultName;
		this.counter = new AtomicLong();
	}

	@GET
	@Timed
	public Saying sayHello(@QueryParam("name") final Optional<String> name) {
		final String value = String.format(template, name.orElse(defaultName));
		return new Saying(counter.incrementAndGet(), value);
	}
}