package com.pvp.codingtournament.business.service;

import com.pvp.codingtournament.model.TournamentDto;

import java.util.List;

public interface TournamentService {
    List<TournamentDto> findAllTournaments();
}
