package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.enums.RoleEnum;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.UserService;
import com.pvp.codingtournament.handler.exception.DuplicateEmailException;
import com.pvp.codingtournament.handler.exception.DuplicateUsernameException;
import com.pvp.codingtournament.mapper.UserMapStruct;
import com.pvp.codingtournament.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapStruct userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        Optional<UserEntity> optionalUserEntityUsername = userRepository.findByUsername(user.getUsername());
        Optional<UserEntity> optionalUserEntityEmail = userRepository.findByEmail(user.getEmail());

        if (optionalUserEntityUsername.isPresent()){
            throw new DuplicateUsernameException();
        }

        if (optionalUserEntityEmail.isPresent()){
            throw new DuplicateEmailException();
        }
        UserEntity userEntity = userMapper.dtoToEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(RoleEnum.ROLE_USER);
        userEntity.setLevel("Beginner");
        userEntity.setPoints(0);

        return userMapper.entityToDto(userRepository.save(userEntity));
    }
}
