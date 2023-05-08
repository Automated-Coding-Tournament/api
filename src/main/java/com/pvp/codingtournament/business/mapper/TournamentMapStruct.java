package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import com.pvp.codingtournament.model.tournament.TournamentParticipationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {TournamentMapStruct.class, UserMapStruct.class, TaskMapStruct.class})
public interface TournamentMapStruct {
    TournamentEntity dtoToEntity(TournamentDto dto);

    @Mapping(source = "creatorUser", target = "creatorUser")
    TournamentDto entityToDto(TournamentEntity entity);

    @Mappings(value = {
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "task", target = "task"),
            @Mapping(source = "tournament", target = "tournament")
    })
    TournamentParticipationDto participationEntityToDto(TournamentParticipationEntity participationEntity);

    TournamentEntity creationDtoToEntity(TournamentCreationDto creationDto);
}
