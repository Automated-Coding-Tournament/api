package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.enums.TournamentStatus;
import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.TournamentRepository;
import com.pvp.codingtournament.business.mapper.TournamentMapStruct;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TournamentService;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TournamentMapStruct tournamentMapStruct;

    @Override
    public List<TournamentDto> findAllTournaments() {
        return tournamentRepository.findAll().stream().map(tournamentMapStruct::entityToDto).collect(Collectors.toList());
    }

    @Override
    public TournamentDto createTournament(TournamentCreationDto tournamentCreationDto, List<Long> taskIds) {
        String creatorUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> creatorOptional = userRepository.findByUsername(creatorUsername);
        if (creatorOptional.isEmpty()){
            throw new NoSuchElementException("User with username: " + creatorUsername + " does not exist");
        }
        TournamentEntity tournamentEntity = tournamentMapStruct.creationDtoToEntity(tournamentCreationDto);
        tournamentEntity.setStatus(TournamentStatus.Registration);
        tournamentEntity.setCreatorUser(creatorOptional.get());
        tournamentEntity.setRegisteredUsers(new HashSet<>());
        tournamentEntity.setTournamentTasks(new HashSet<>());

        for (Long taskId : taskIds) {
            Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
            if (optionalTaskEntity.isEmpty()){
                throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
            }
            tournamentEntity.addTask(optionalTaskEntity.get());
        }

        return tournamentMapStruct.entityToDto(tournamentRepository.save(tournamentEntity));
    }
}
