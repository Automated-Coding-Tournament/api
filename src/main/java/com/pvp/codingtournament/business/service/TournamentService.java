package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import com.pvp.codingtournament.model.tournament.TournamentParticipationDto;

import java.util.List;
import java.util.Map;

public interface TournamentService {
    List<TournamentDto> findAllTournaments();

    TournamentDto createTournament(TournamentCreationDto tournamentCreationDto, List<Long> taskIds);

    void registerUserToTournament(Long tournamentId);

    void finishUserParticipationInTournament(Long tournamentId);

    int deduceUserParticipationPoints(Long tournamentId);

    TournamentDto getTournamentById(Long tournamentId);

    TournamentParticipationDto getTournamentUserParticipationById(Long tournamentId);

    List<TournamentDto> getUserTournamentHistory();

    List<Map<String, Object>> getTournamentUserParticipationLeaderboard(Long tournamentId);

    Boolean isUserRegisteredToTournament(Long tournamentId);

    Boolean isUserFinishedParticipating(Long tournamentId);

    TournamentDto editTournament(Long tournamentId, List<Long> taskIds, TournamentCreationDto tournamentCreationDto);

    void deleteTournament(Long tournamentId);
}
