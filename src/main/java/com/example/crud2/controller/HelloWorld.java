package com.example.crud2.controller;

import com.example.crud2.model.HelloResponseModel;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @Operation(summary = "Get Hello World String", description = "This operation retrieves a user by their ID.")
    @GetMapping("/hello")
    public HelloResponseModel hello() {
        return new HelloResponseModel("hello world");
    }

}
