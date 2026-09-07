package com.br.config;

import com.br.utility.MetricsRegistry;
import io.micrometer.core.instrument.Timer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hand-rolled equivalent of what Spring Boot's actuator autoconfigures for
 * free on the 5 Spring services (a WebMvc/WebFlux metrics filter): times
 * every request and records it as http_server_requests_seconds with the same
 * tag shape (method/uri/status/outcome) so Grafana can compare Jersey and
 * Spring services on one panel without knowing which framework each one runs.
 * Jersey 1.x has no auto-configuration hook the way Spring Boot does, so this
 * has to be wired by hand — see D5/D9 in CLAUDE.md.
 */
public class MetricsFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {
		// no-op
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Timer.Sample sample = Timer.start(MetricsRegistry.REGISTRY);
		try {
			chain.doFilter(request, response);
		} finally {
			int status = httpResponse.getStatus();
			sample.stop(Timer.builder("http_server_requests_seconds")
					.publishPercentileHistogram(true)
					.tag("method", httpRequest.getMethod())
					.tag("uri", httpRequest.getRequestURI())
					.tag("status", String.valueOf(status))
					.tag("outcome", outcomeOf(status))
					.register(MetricsRegistry.REGISTRY));
		}
	}

	private String outcomeOf(int status) {
		if (status >= 500) {
			return "SERVER_ERROR";
		}
		if (status >= 400) {
			return "CLIENT_ERROR";
		}
		if (status >= 300) {
			return "REDIRECTION";
		}
		if (status >= 200) {
			return "SUCCESS";
		}
		return "INFORMATIONAL";
	}

	@Override
	public void destroy() {
		// no-op
	}
}
