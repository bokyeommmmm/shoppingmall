package com.hana7.hanaro.controller;

import com.hana7.hanaro.dto.UserResponseDTO;
import com.hana7.hanaro.exception.NotFoundException;
import com.hana7.hanaro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(userService.findUserByKeyword(keyword, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@RequestParam Long id) throws NotFoundException {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
