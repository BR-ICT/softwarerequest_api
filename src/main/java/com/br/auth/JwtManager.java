package com.br.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.SecretKey;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

public class JwtManager {

	private static final String CLAIM_ROLE = "role";
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	private static final SecretKey SECRET_KEY = MacProvider.generateKey(SIGNATURE_ALGORITHM);
//	private static final TemporalAmount TOKEN_VALIDITY = Duration.ofHours(4L); // 4 Hours

	/**
	 * Builds a JWT with the given subject and role and returns it as a JWS signed
	 * compact String.
	 */

	public static String createToken(final String subject, final String user, final String role) {
		final Instant now = Instant.now();
		final TemporalAmount TOKEN_VALIDITY = Duration.ofHours(8L); // 8 Hours
		final Date expiryDate = Date.from(now.plus(TOKEN_VALIDITY));
		return Jwts.builder()
			.setSubject(subject)
			.setAudience(user)
			.claim(CLAIM_ROLE, role)
			.setExpiration(expiryDate)
			.setIssuedAt(Date.from(now))
			.signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
			.compact();
	}

	public static String createTokenApprove(final String subject, final String user, final String role) {
		final Instant now = Instant.now();
		final TemporalAmount TOKEN_VALIDITY = Duration.ofHours(168L); // 7 Day
		final Date expiryDate = Date.from(now.plus(TOKEN_VALIDITY));
		return Jwts.builder()
			.setSubject(subject)
			.setAudience(user)
			.claim(CLAIM_ROLE, role)
			.setExpiration(expiryDate)
			.setIssuedAt(Date.from(now))
			.signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
			.compact();
	}

	public static Jws<Claims> parseToken(final String compactToken) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(compactToken);

	}

}
