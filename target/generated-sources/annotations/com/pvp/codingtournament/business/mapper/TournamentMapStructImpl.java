package com.pvp.codingtournament.business.mapper;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import com.pvp.codingtournament.model.tournament.TournamentParticipationDto;
import java.util.ArrayList;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-17T21:27:20+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class TournamentMapStructImpl implements TournamentMapStruct {

    @Autowired
    private UserMapStruct userMapStruct;
    @Autowired
    private TaskMapStruct taskMapStruct;

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
        tournamentEntity.setCreatorUser( userMapStruct.dtoToEntity( dto.getCreatorUser() ) );

        return tournamentEntity;
    }

    @Override
    public TournamentDto entityToDto(TournamentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TournamentDto tournamentDto = new TournamentDto();

        tournamentDto.setCreatorUser( userMapStruct.entityToDto( entity.getCreatorUser() ) );
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
    public TournamentParticipationDto participationEntityToDto(TournamentParticipationEntity participationEntity) {
        if ( participationEntity == null ) {
            return null;
        }

        TournamentParticipationDto tournamentParticipationDto = new TournamentParticipationDto();

        tournamentParticipationDto.setUser( userMapStruct.entityToDto( participationEntity.getUser() ) );
        tournamentParticipationDto.setTask( taskMapStruct.entityToDto( participationEntity.getTask() ) );
        tournamentParticipationDto.setTournament( entityToDto( participationEntity.getTournament() ) );
        tournamentParticipationDto.setId( participationEntity.getId() );
        tournamentParticipationDto.setPoints( participationEntity.getPoints() );
        tournamentParticipationDto.setCompletedTaskCount( participationEntity.getCompletedTaskCount() );
        tournamentParticipationDto.setAverageMemoryInKilobytes( participationEntity.getAverageMemoryInKilobytes() );
        if ( participationEntity.getFinishedParticipating() != null ) {
            tournamentParticipationDto.setFinishedParticipating( participationEntity.getFinishedParticipating() );
        }
        ArrayList<Long> arrayList = participationEntity.getUnfinishedTaskIds();
        if ( arrayList != null ) {
            tournamentParticipationDto.setUnfinishedTaskIds( new ArrayList<Long>( arrayList ) );
        }
        if ( participationEntity.getFinishedCurrentTask() != null ) {
            tournamentParticipationDto.setFinishedCurrentTask( participationEntity.getFinishedCurrentTask() );
        }
        if ( participationEntity.getLastTask() != null ) {
            tournamentParticipationDto.setLastTask( participationEntity.getLastTask() );
        }
        tournamentParticipationDto.setPassed( participationEntity.getPassed() );
        tournamentParticipationDto.setTotalTestCases( participationEntity.getTotalTestCases() );
        tournamentParticipationDto.setPassedTestCases( participationEntity.getPassedTestCases() );
        tournamentParticipationDto.setMemoryInKilobytes( participationEntity.getMemoryInKilobytes() );
        tournamentParticipationDto.setAverageCpuTime( participationEntity.getAverageCpuTime() );

        return tournamentParticipationDto;
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
}
