package com.pvp.codingtournament.controller;

import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.UserService;
import com.pvp.codingtournament.model.UserDto;
import com.pvp.codingtournament.model.UserEditDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            @ApiResponse(code = 409, message = "Username and/or email is taken"),
            @ApiResponse(code = 201, message = "User created")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userEntity){
        return new ResponseEntity<>(userService.createUser(userEntity), HttpStatus.CREATED);
    }

    @ApiOperation("Edits existing user")
    @ApiResponses({
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 403, message = "Forbidden (from editing this users data)"),
            @ApiResponse(code = 200, message = "User data edited")
    })
    @PutMapping("/edit/{username}")
    @PreAuthorize("#username == authentication.principal || hasRole('ADMIN')")
    public ResponseEntity<UserDto> editUser(@PathVariable("username") String username, @RequestBody UserEditDto userEditDto){
        return new ResponseEntity<>(userService.editUser(username, userEditDto), HttpStatus.OK);
    }

    @ApiOperation("Gets existing user details")
    @ApiResponses({
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 403, message = "Forbidden (from getting this users data)"),
            @ApiResponse(code = 302, message = "User data found")
    })
    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.principal || hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username){
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.FOUND);
    }
}
