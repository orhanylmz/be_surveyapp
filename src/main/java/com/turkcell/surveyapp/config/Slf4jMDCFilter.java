package com.turkcell.surveyapp.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class Slf4jMDCFilter extends OncePerRequestFilter {
	private final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
	private final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			final String correlationId = getCorrelationIdFromHeader(request, response);
			MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
		}
	}

	private String getCorrelationIdFromHeader(final HttpServletRequest request, HttpServletResponse response) {
		String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
		if (StringUtils.isEmpty(correlationId)) {
			correlationId = UUID.randomUUID().toString();
			response.addHeader(CORRELATION_ID_HEADER_NAME, correlationId);
		}
		return correlationId;
	}
}