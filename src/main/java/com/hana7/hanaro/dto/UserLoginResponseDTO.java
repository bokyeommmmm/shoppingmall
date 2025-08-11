package com.hana7.hanaro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDTO {
	private String userName;
	private String email;
	private String jwtToken;
	private String refreshToken;
}
