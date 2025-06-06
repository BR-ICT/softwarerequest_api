package com.br.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;

import com.br.utility.UUIDGenerator;


public class UuidMDCFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization logic, if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestUuid = UUIDGenerator.generateUUID();
		// Add the UUID to the MDC
		ThreadContext.put("requestUuid", requestUuid);

		try {
			chain.doFilter(request, response);
		} finally {
			// Remove the UUID from the MDC after the request is processed
			ThreadContext.remove("requestUuid");
		}
	}

	@Override
	public void destroy() {
		// Cleanup, if needed
	}

}
