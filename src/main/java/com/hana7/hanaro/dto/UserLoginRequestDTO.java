package com.hana7.hanaro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

	@NotBlank(message = "이메일 입력은 필수입니다.")
	@Size(min = 1, max = 30, message = "30자 이내로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	@Size(min = 1, max = 60, message = "60자 이내로 입력해주세요.")
	private String password;
}
