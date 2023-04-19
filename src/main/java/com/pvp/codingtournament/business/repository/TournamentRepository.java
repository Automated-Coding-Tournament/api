package com.pvp.codingtournament.business.repository;

import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
}
