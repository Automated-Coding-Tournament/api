package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.repository.TournamentRepository;
import com.pvp.codingtournament.business.mapper.TournamentMapStruct;
import com.pvp.codingtournament.business.service.TournamentService;
import com.pvp.codingtournament.model.TournamentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMapStruct tournamentMapStruct;

    @Override
    public List<TournamentDto> findAllTournaments() {
        return tournamentRepository.findAll().stream().map(tournamentMapStruct::entityToDto).collect(Collectors.toList());
    }
}
