package com.br.config;

import static com.br.utility.SecurityConstants.HEADER_REQUEST_UUID;
import static com.br.utility.SecurityConstants.MDC_UUID_KEY;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.br.utility.UUIDGenerator;

public class UuidMDCFilter implements Filter {

	protected static final Logger logger = LogManager.getLogger(UuidMDCFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization logic, if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			// Extract the request UUID from the header
			String requestUuid = httpRequest.getHeader(HEADER_REQUEST_UUID);
//			logger.debug("requestUuid: {}", requestUuid);

			// If the request does not contain a UUID, generate a new one
			if (requestUuid == null || requestUuid.trim().isEmpty()) {
				requestUuid = UUIDGenerator.generateUUID();
			}
			
			logger.debug("requestUuid: {}", requestUuid);

			// Add the UUID to the MDC
			ThreadContext.put(MDC_UUID_KEY, requestUuid);

			try {
				chain.doFilter(request, response);
			} finally {
				// Remove the UUID from the MDC after the request is processed
				ThreadContext.remove(MDC_UUID_KEY);
			}

		} else {
			// If not an HTTP request, just continue the filter chain
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// Cleanup, if needed
	}

}
