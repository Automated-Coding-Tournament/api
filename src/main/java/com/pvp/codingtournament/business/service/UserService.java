package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.UserDto;
import com.pvp.codingtournament.model.UserEditDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    public UserDto createUser(UserDto user);
    public UserDto editUser(String username, UserEditDto user);

    public UserDto getUser(String username);

    List<Map<String, Object>> getGlobalLeaderboard();
}
