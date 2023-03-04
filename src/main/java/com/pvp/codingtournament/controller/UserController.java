package com.pvp.codingtournament.controller;

import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.UserService;
import com.pvp.codingtournament.model.UserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @ApiOperation("Creates a new user")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Username or email is taken"),
            @ApiResponse(code = 201, message = "User created")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userEntity){
        return new ResponseEntity<>(userService.createUser(userEntity), HttpStatus.CREATED);
    }

}
