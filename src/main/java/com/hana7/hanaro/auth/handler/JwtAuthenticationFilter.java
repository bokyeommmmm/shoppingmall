package com.hana7.hanaro.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.hanaro.auth.JwtUtil;
import com.hana7.hanaro.dto.UserDTO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final String[] excludePatterns = {
		"/login",
		"/users/signIn",
		"/users/signUp",
		"/api/public/**",
		"/favicon.ico",
		"/actuator/**",
		"/*.html",
		"/swagger-ui/**",
		"/v3/api-docs/**",
	};

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return Arrays.stream(excludePatterns)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		try {
			System.out.println("** JwtAuthenticationFilter.doFilterInternal:" + authHeader.substring(7));
			Map<String, Object> claims = JwtUtil.validateToken(authHeader.substring(7));

			String email = (String)claims.get("email");
			String nickname = (String)claims.get("nickname");
			List<String> roleNames = (List<String>) claims.get("roleNames");
			UserDTO dto = new UserDTO(email, "", nickname, roleNames);
			UsernamePasswordAuthenticationToken authenticationToken = new
				UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities());

			// 올바른 Authorization을 저장하여 어디서든 불러올 수 있다!
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			PrintWriter out = response.getWriter();
			out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN")));
			out.close();
		}
		filterChain.doFilter(request, response);
	}
}
