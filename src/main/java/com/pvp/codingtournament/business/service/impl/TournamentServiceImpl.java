package com.pvp.codingtournament.business.service.impl;

import com.pvp.codingtournament.business.enums.TournamentStatus;
import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.TournamentParticipationRepository;
import com.pvp.codingtournament.business.repository.TournamentRepository;
import com.pvp.codingtournament.business.mapper.TournamentMapStruct;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TournamentService;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
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

    private final TournamentParticipationRepository tournamentParticipationRepository;
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
        UserEntity creatorUser = creatorOptional.get();
        TournamentEntity tournamentEntity = tournamentMapStruct.creationDtoToEntity(tournamentCreationDto);
        tournamentEntity.setStatus(TournamentStatus.Registration);
        tournamentEntity.setCreatorUser(creatorUser);
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

    @Override
    public void registerUserToTournament(Long tournamentId) {
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);

        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " already exists");
        }

        TournamentEntity tournamentEntity = optionalTournamentEntity.get();

        if (tournamentEntity.getStatus() != TournamentStatus.Registration){
            throw new ValidationException("Tournament with id: " + tournamentId + " is not in registration phase");
        }

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);

        if (optionalUserEntity.isEmpty()){
            throw new NoSuchElementException("User with username: " + username + " does not exist");
        }

        UserEntity userEntity = optionalUserEntity.get();

        if (tournamentEntity.getRegisteredUsers().contains(userEntity)){
            throw new DuplicateRequestException("User is already registered to the tournament");
        }

        tournamentEntity.registerUser(userEntity);
        tournamentRepository.save(tournamentEntity);
    }

    @Override
    public void finishUserParticipationInTournament(Long tournamentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity participationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);

//        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
//        if (optionalUserEntity.isEmpty()){
//            throw new NoSuchElementException("User with username: " + username + " does not exist");
//        }

        if (!participationEntity.isFinishedCurrentTask()) {
            participationEntity.deducePoints();
        }

        //UserEntity userEntity = optionalUserEntity.get();
        //userEntity.addPoints(participationEntity.getPoints());

        participationEntity.calculateMemoryAverage();
        participationEntity.setFinishedParticipating(true);

        tournamentParticipationRepository.save(participationEntity);
        //userRepository.save(userEntity);
    }

    @Override
    public int deduceUserParticipationPoints(Long tournamentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity participationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);

        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()){
            throw new NoSuchElementException("User with username: " + username + " does not exist");
        }
        participationEntity.deducePoints();
        tournamentParticipationRepository.save(participationEntity);
        return participationEntity.getPoints();
    }
}
