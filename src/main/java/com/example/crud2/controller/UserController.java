package com.example.crud2.controller;

import com.example.crud2.entity.UserEntity;
import com.example.crud2.model.GetResp;
import com.example.crud2.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/users")
@OpenAPIDefinition(
        info = @Info(
                title = "Users API",
                version = "2.0",
                description = "CRUD Application for users"
        )
)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @Operation(summary = "Get All User", description = "This operation retrieves all the users")
    public GetResp findAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int per_page,
            @RequestParam(defaultValue = "DESC") Sort.Direction orderBy,
            @RequestParam(defaultValue = "id") String sortBy) {
        return userService.findAll(page, per_page, orderBy, sortBy);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) {
        return userService.saveUser(userEntity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "This operation retrieves a user by their ID.")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        return userService.updateUser(id, userEntity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        return userService.patchUser(id, userEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
