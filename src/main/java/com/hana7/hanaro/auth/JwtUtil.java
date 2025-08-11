package com.hana7.hanaro.auth;

import com.hana7.hanaro.dto.UserDTO;
import com.hana7.hanaro.exception.CustomJwtException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
	private static final SecretKey K = Keys.hmacShaKeyFor(
		"asdlkjsa232sa;ljdsf$#$asdfdsaf!@!asdfdsafsdfsdaf".getBytes(StandardCharsets.UTF_8));

	public static String generateToken(Map<String, Object> valueMap, int min) {
		return Jwts.builder().setHeader(Map.of("typ", "JWT"))
			.setClaims(valueMap)
			.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
			.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
			.signWith(K).compact();
	}

	public static Map<String, Object> validateToken(String token) {
		Map<String, Object> claim = null;
		SecretKey key = null;

		try {
			claim = Jwts.parserBuilder()
				.setSigningKey(K)
				.build()
				.parseClaimsJws(token).getBody();
		} catch (WeakKeyException e) {
			throw new CustomJwtException("WeakException");
		} catch (MalformedJwtException e) {
			throw new CustomJwtException("MalFormed");
		} catch (ExpiredJwtException e) {
			throw new CustomJwtException("Expired");
		} catch (InvalidClaimException e) {
			throw new CustomJwtException("Invalid");
		} catch (JwtException e) {
			throw new CustomJwtException("JwtError");
		} catch (Exception e) {
			throw new CustomJwtException("UnknownError");
		}

		return claim;
	}

	public static Map<String, Object> getClaims(Authentication authentication) {
		UserDTO d = (UserDTO) authentication.getPrincipal();

		UserDTO dto = new UserDTO(d.getEmail(), "", d.getNickname(), d.getRoleNames());
		Map<String, Object> claims = dto.getClaims();

		String accessToken  = JwtUtil.generateToken(new HashMap<>(claims), 10);
		String refreshToken = JwtUtil.generateToken(new HashMap<>(claims), 60 * 24);

		Map<String, Object> body = new HashMap<>(claims);
		body.put("accessToken", accessToken);
		body.put("refreshToken", refreshToken);

		System.out.println("body = " + body);

		return body;
	}
}
