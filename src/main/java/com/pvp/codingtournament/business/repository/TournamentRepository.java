package com.pvp.codingtournament.business.repository;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
    @Query(nativeQuery = true, value = "DELETE FROM user_tournament WHERE tournament_id = :tournamentId")
    void deleteAllRegistrationsByTournamentId(@Param("tournamentId") Long tournamentId);
}
