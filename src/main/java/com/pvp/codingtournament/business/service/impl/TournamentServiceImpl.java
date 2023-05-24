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
import com.pvp.codingtournament.model.tournament.TournamentParticipationDto;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TournamentEntity> tournamentEntities = tournamentRepository.findAll();
        List<TournamentDto> tournamentDtos = new ArrayList<>();
        for (TournamentEntity tournamentEntity : tournamentEntities) {
            TournamentDto tournamentDto = tournamentMapStruct.entityToDto(tournamentEntity);
            tournamentDto.setRegistered(tournamentEntity.getRegisteredUsers().stream().anyMatch(x -> x.getUsername().equals(username)));
            tournamentDtos.add(tournamentDto);
        }
        return tournamentDtos;
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
        if (!participationEntity.isFinishedCurrentTask()) {
            participationEntity.deducePoints();
        }
        participationEntity.calculateMemoryAverage();
        participationEntity.setFinishedParticipating(true);
        tournamentParticipationRepository.save(participationEntity);
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

    @Override
    public TournamentDto getTournamentById(Long tournamentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);
        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " does not exist");
        }
        TournamentEntity tournamentEntity = (optionalTournamentEntity.get());
        TournamentDto tournamentDto = tournamentMapStruct.entityToDto(tournamentEntity);
        tournamentDto.setRegistered(tournamentEntity.getRegisteredUsers().stream().anyMatch(x -> x.getUsername().equals(username)));
        return tournamentDto;
    }

    @Override
    public TournamentParticipationDto getTournamentUserParticipationById(Long tournamentId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity participationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);
        if (participationEntity == null){
            throw new NoSuchElementException("User with username: " + username + " is not participating in tournament with id: " + tournamentId);
        }
        return tournamentMapStruct.participationEntityToDto(participationEntity);
    }

    @Override
    public List<TournamentDto> getUserTournamentHistory() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()){
            throw new NoSuchElementException("User with username: " + username + " does not exist");
        }
        return optionalUserEntity.get().getAttendingTournaments().stream().map(tournamentMapStruct::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<TournamentParticipationDto> getTournamentUserParticipationLeaderboard(Long tournamentId) {
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);
        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " does not exist");
        }
        TournamentEntity tournament = optionalTournamentEntity.get();
        List<TournamentParticipationEntity> tournamentParticipationEntities = tournamentParticipationRepository.findAllByTournament(tournament);
        tournamentParticipationEntities.sort(new Comparator<TournamentParticipationEntity>() {
            @Override
            public int compare(TournamentParticipationEntity o1, TournamentParticipationEntity o2) {
                if (o1.getPoints() != o2.getPoints()){
                    return Integer.compare(o2.getPoints(), o1.getPoints());
                }
                return Integer.compare(o2.getAverageMemoryInKilobytes(), o1.getAverageMemoryInKilobytes());
            }
        });

        return tournamentParticipationEntities.stream().map(tournamentMapStruct::participationEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Boolean isUserRegisteredToTournament(Long tournamentId) {
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);
        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " does not exist");
        }
        Set<UserEntity> registeredUsers = optionalTournamentEntity.get().getRegisteredUsers();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return registeredUsers.stream().anyMatch(x -> x.getUsername().equals(username));
    }

    @Override
    public Boolean isUserFinishedParticipating(Long tournamentId) {
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);
        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " does not exist");
        }
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TournamentParticipationEntity tournamentParticipationEntity = tournamentParticipationRepository.findByUserUsernameAndTournamentId(username, tournamentId);
        return tournamentParticipationEntity.isFinishedParticipating();
    }

    @Override
    public TournamentDto editTournament(Long tournamentId, List<Long> taskIds, TournamentCreationDto tournamentCreationDto) {
        Optional<TournamentEntity> optionalTournamentEntity = tournamentRepository.findById(tournamentId);
        if (optionalTournamentEntity.isEmpty()){
            throw new NoSuchElementException("Tournament with id: " + tournamentId + " does not exist");
        }

        TournamentEntity tournamentEntity = optionalTournamentEntity.get();

        if (!tournamentEntity.getStatus().equals(TournamentStatus.Registration)){
            throw new ValidationException("Tournament can only be edited in the registration phase");
        }

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (!tournamentEntity.getCreatorUser().getUsername().equals(username) && !roles.contains("ROLE_ADMIN")){
            throw new SecurityException("Only the creator of the tournament can edit it");
        }

        TournamentEntity mappedEntity = tournamentMapStruct.creationDtoToEntity(tournamentCreationDto);
        mappedEntity.setId(tournamentEntity.getId());
        mappedEntity.setStatus(tournamentEntity.getStatus());
        mappedEntity.setCreatorUser(tournamentEntity.getCreatorUser());
        mappedEntity.setRegisteredUsers(tournamentEntity.getRegisteredUsers());
        mappedEntity.setTournamentTasks(new HashSet<>());
        for (Long taskId : taskIds) {
            Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskId);
            if (optionalTaskEntity.isEmpty()){
                throw new NoSuchElementException("Task with id: " + taskId + " does not exist");
            }
            mappedEntity.addTask(optionalTaskEntity.get());
        }

        return tournamentMapStruct.entityToDto(tournamentRepository.save(mappedEntity));
    }

}
