package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapStruct {
    UserDto entityToDto(UserEntity entity);

    UserEntity dtoToEntity(UserDto dto);
}
