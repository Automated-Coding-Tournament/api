package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import com.pvp.codingtournament.model.tournament.TournamentParticipationDto;

import java.util.List;

public interface TournamentService {
    List<TournamentDto> findAllTournaments();

    TournamentDto createTournament(TournamentCreationDto tournamentCreationDto, List<Long> taskIds);

    void registerUserToTournament(Long tournamentId);

    void finishUserParticipationInTournament(Long tournamentId);

    int deduceUserParticipationPoints(Long tournamentId);

    TournamentDto getTournamentById(Long tournamentId);

    TournamentParticipationDto getTournamentUserParticipationById(Long tournamentId);

    List<TournamentDto> getUserTournamentHistory();

    List<TournamentParticipationDto> getTournamentUserParticipationLeaderboard(Long tournamentId);
}
