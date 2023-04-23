package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.model.UserDto;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-23T13:28:59+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class TournamentMapStructImpl implements TournamentMapStruct {

    @Override
    public TournamentEntity dtoToEntity(TournamentDto dto) {
        if ( dto == null ) {
            return null;
        }

        TournamentEntity tournamentEntity = new TournamentEntity();

        tournamentEntity.setId( dto.getId() );
        tournamentEntity.setName( dto.getName() );
        tournamentEntity.setStartDate( dto.getStartDate() );
        tournamentEntity.setEndDate( dto.getEndDate() );
        tournamentEntity.setDescription( dto.getDescription() );
        tournamentEntity.setFirstPlacePoints( dto.getFirstPlacePoints() );
        tournamentEntity.setSecondPlacePoints( dto.getSecondPlacePoints() );
        tournamentEntity.setThirdPlacePoints( dto.getThirdPlacePoints() );
        tournamentEntity.setDifficulty( dto.getDifficulty() );
        tournamentEntity.setStatus( dto.getStatus() );
        tournamentEntity.setCreatorUser( userDtoToUserEntity( dto.getCreatorUser() ) );

        return tournamentEntity;
    }

    @Override
    public TournamentDto entityToDto(TournamentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TournamentDto tournamentDto = new TournamentDto();

        tournamentDto.setCreatorUser( userEntityToUserDto( entity.getCreatorUser() ) );
        tournamentDto.setId( entity.getId() );
        tournamentDto.setName( entity.getName() );
        tournamentDto.setStartDate( entity.getStartDate() );
        tournamentDto.setEndDate( entity.getEndDate() );
        tournamentDto.setDescription( entity.getDescription() );
        tournamentDto.setFirstPlacePoints( entity.getFirstPlacePoints() );
        tournamentDto.setSecondPlacePoints( entity.getSecondPlacePoints() );
        tournamentDto.setThirdPlacePoints( entity.getThirdPlacePoints() );
        tournamentDto.setDifficulty( entity.getDifficulty() );
        tournamentDto.setStatus( entity.getStatus() );

        return tournamentDto;
    }

    @Override
    public TournamentEntity creationDtoToEntity(TournamentCreationDto creationDto) {
        if ( creationDto == null ) {
            return null;
        }

        TournamentEntity tournamentEntity = new TournamentEntity();

        tournamentEntity.setName( creationDto.getName() );
        tournamentEntity.setStartDate( creationDto.getStartDate() );
        tournamentEntity.setEndDate( creationDto.getEndDate() );
        tournamentEntity.setDescription( creationDto.getDescription() );
        tournamentEntity.setFirstPlacePoints( creationDto.getFirstPlacePoints() );
        tournamentEntity.setSecondPlacePoints( creationDto.getSecondPlacePoints() );
        tournamentEntity.setThirdPlacePoints( creationDto.getThirdPlacePoints() );
        tournamentEntity.setDifficulty( creationDto.getDifficulty() );

        return tournamentEntity;
    }

    protected UserEntity userDtoToUserEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( userDto.getId() );
        userEntity.setName( userDto.getName() );
        userEntity.setSurname( userDto.getSurname() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setPassword( userDto.getPassword() );
        userEntity.setEmail( userDto.getEmail() );
        userEntity.setPhoneNumber( userDto.getPhoneNumber() );
        userEntity.setPoints( userDto.getPoints() );
        userEntity.setLevel( userDto.getLevel() );

        return userEntity;
    }

    protected UserDto userEntityToUserDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( userEntity.getId() );
        userDto.setName( userEntity.getName() );
        userDto.setSurname( userEntity.getSurname() );
        userDto.setUsername( userEntity.getUsername() );
        userDto.setPassword( userEntity.getPassword() );
        userDto.setEmail( userEntity.getEmail() );
        userDto.setPhoneNumber( userEntity.getPhoneNumber() );
        userDto.setPoints( userEntity.getPoints() );
        userDto.setLevel( userEntity.getLevel() );

        return userDto;
    }
}
