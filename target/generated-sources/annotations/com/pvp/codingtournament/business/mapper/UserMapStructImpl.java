package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.model.UserDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-07T23:03:43+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapStructImpl implements UserMapStruct {

    @Override
    public UserDto entityToDto(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( entity.getId() );
        userDto.setName( entity.getName() );
        userDto.setSurname( entity.getSurname() );
        userDto.setUsername( entity.getUsername() );
        userDto.setPassword( entity.getPassword() );
        userDto.setEmail( entity.getEmail() );
        userDto.setPhoneNumber( entity.getPhoneNumber() );
        userDto.setPoints( entity.getPoints() );
        userDto.setLevel( entity.getLevel() );

        return userDto;
    }

    @Override
    public UserEntity dtoToEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( dto.getId() );
        userEntity.setName( dto.getName() );
        userEntity.setSurname( dto.getSurname() );
        userEntity.setUsername( dto.getUsername() );
        userEntity.setPassword( dto.getPassword() );
        userEntity.setEmail( dto.getEmail() );
        userEntity.setPhoneNumber( dto.getPhoneNumber() );
        userEntity.setPoints( dto.getPoints() );
        userEntity.setLevel( dto.getLevel() );

        return userEntity;
    }
}
