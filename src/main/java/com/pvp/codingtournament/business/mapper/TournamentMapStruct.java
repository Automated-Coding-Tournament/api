package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentMapStruct {
    TournamentEntity dtoToEntity(TournamentDto dto);

    @Mapping(source = "creatorUser", target = "creatorUser")
    TournamentDto entityToDto(TournamentEntity entity);

    TournamentEntity creationDtoToEntity(TournamentCreationDto creationDto);
}
