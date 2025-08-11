package com.hana7.hanaro.controller;

import java.util.Map;

import com.hana7.hanaro.auth.JwtUtil;
import com.hana7.hanaro.dto.UserLoginRequestDTO;
import com.hana7.hanaro.dto.UserLoginResponseDTO;
import com.hana7.hanaro.dto.UserRequestDTO;
import com.hana7.hanaro.dto.UserResponseDTO;
import com.hana7.hanaro.exception.NotFound.NotFoundException;
import com.hana7.hanaro.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "모든 회원 정보 가져오기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모든 회원을 찾았습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404",
            description = "회원을 찾을 수 없습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
    })
    @GetMapping("")
    public ResponseEntity<Page<UserResponseDTO>> findAll(@ParameterObject @PageableDefault(size = 10, page = 0, sort = "userName") Pageable pageable) throws NotFoundException {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(userService.findUserByKeyword(keyword, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) throws NotFoundException {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO){
        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword()
                )
            );

            Map<String, Object> claims = JwtUtil.getClaims(authenticate);
            UserLoginResponseDTO response = UserLoginResponseDTO.builder()
                .email(claims.get("email").toString())
                .userName(claims.get("nickname").toString())
                .jwtToken(claims.get("accessToken").toString())
                .refreshToken(claims.get("refreshToken").toString())
                .build();

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
        }

    }
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userService.signUp(userRequestDTO);
        return ResponseEntity.ok().build();
    }
}
