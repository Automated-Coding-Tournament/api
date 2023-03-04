package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.model.UserDto;

public interface UserService {

    public UserDto createUser(UserDto user);
}
