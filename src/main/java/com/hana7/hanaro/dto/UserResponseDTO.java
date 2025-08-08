package com.hana7.hanaro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

	private Long id;

	private String userName;

	private String email;

	private String password;

	private String role;



}
