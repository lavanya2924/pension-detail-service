package com.pension.util;

import javax.naming.AuthenticationException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtToken {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public void validateToken(final String token) throws AuthenticationException {
		try {
			Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret)).build()
					.parseClaimsJws(token);
		} catch (SecurityException ex) {
			throw new AuthenticationException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new AuthenticationException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new AuthenticationException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new AuthenticationException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new AuthenticationException("JWT string is empty.");
		}
	}

}
