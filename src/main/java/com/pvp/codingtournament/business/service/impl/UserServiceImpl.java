package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.enums.RoleEnum;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.UserService;
import com.pvp.codingtournament.handler.Error;
import com.pvp.codingtournament.handler.ErrorModel;
import com.pvp.codingtournament.handler.exception.CustomException;
import com.pvp.codingtournament.handler.exception.UserNotFoundException;
import com.pvp.codingtournament.mapper.UserMapStruct;
import com.pvp.codingtournament.model.UserDto;
import com.pvp.codingtournament.model.UserEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        ErrorModel errorModel = new ErrorModel();

        if (optionalUserEntityUsername.isPresent()){
            errorModel.getErrors().add(new Error("username", "Username is taken"));
        }

        if (optionalUserEntityEmail.isPresent()){
            errorModel.getErrors().add(new Error("email", "Email is taken"));
        }

        if (errorModel.getErrors().size() > 0){
            errorModel.setStatus(HttpStatus.CONFLICT);
            errorModel.setMessage("Validation failed with " + errorModel.getErrors().size() + " errors");
            throw new CustomException(errorModel);
        }

        UserEntity userEntity = userMapper.dtoToEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(RoleEnum.ROLE_USER);
        userEntity.setLevel("Beginner");
        userEntity.setPoints(0);

        return userMapper.entityToDto(userRepository.save(userEntity));
    }

    @Override
    public UserDto editUser(String username, UserEditDto user) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (!optionalUserEntity.isPresent()){
            throw new UserNotFoundException();
        }

        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userRepository.save(userEntity);
        return userMapper.entityToDto(userEntity);
    }

    @Override
    public UserDto getUser(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (!optionalUserEntity.isPresent()){
            throw new UserNotFoundException();
        }
        return userMapper.entityToDto(optionalUserEntity.get());
    }
}
