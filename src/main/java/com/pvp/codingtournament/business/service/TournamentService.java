package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;

import java.util.List;

public interface TournamentService {
    List<TournamentDto> findAllTournaments();

    TournamentDto createTournament(TournamentCreationDto tournamentCreationDto, List<Long> taskIds);
}
