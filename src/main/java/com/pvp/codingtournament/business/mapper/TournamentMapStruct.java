package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.model.TournamentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentMapStruct {
    TournamentEntity dtoToEntity(TournamentDto dto);

    @Mapping(source = "creatorUser", target = "creatorUser")
    TournamentDto entityToDto(TournamentEntity entity);
}
