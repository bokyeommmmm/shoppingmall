package com.hana7.hanaro.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana7.hanaro.auth.JwtUtil;
import com.hana7.hanaro.dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		Map<String, Object> body = JwtUtil.getClaims(authentication);

		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(objectMapper.writeValueAsString(body));
		out.close();
	}
}
