package com.br.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.br.utility.MetricsRegistry;

@Path("/metrics")
public class api_metrics {

	@GET
	@Produces("text/plain; version=0.0.4; charset=utf-8")
	public Response scrape() {
		return Response.ok(MetricsRegistry.REGISTRY.scrape()).build();
	}
}
