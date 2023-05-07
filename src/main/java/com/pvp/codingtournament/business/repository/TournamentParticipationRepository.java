package com.pvp.codingtournament.business.repository;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentParticipationRepository extends JpaRepository<TournamentParticipationEntity, Long> {
    TournamentParticipationEntity findByUserUsername(String username);

    List<TournamentParticipationEntity> findAllByTournament(TournamentEntity tournament);

    TournamentParticipationEntity findByUserUsernameAndTournamentId(String username, Long tournamentId);
}
