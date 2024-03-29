package com.pvp.codingtournament.business.scheduler;

import com.pvp.codingtournament.business.enums.TournamentStatus;
import com.pvp.codingtournament.business.repository.TaskRepository;
import com.pvp.codingtournament.business.repository.TournamentParticipationRepository;
import com.pvp.codingtournament.business.repository.TournamentRepository;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.TaskEntity;
import com.pvp.codingtournament.business.repository.model.TournamentEntity;
import com.pvp.codingtournament.business.repository.model.TournamentParticipationEntity;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import com.pvp.codingtournament.business.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Component
public class TournamentScheduler {
    private final TournamentRepository tournamentRepository;
    private final TournamentParticipationRepository tournamentParticipationRepository;
    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    @Scheduled(cron = "*/15 * * * * *") // every 15 seconds
    public void checkTournaments(){
        System.out.println("runs");
        for (TournamentEntity tournament : tournamentRepository.findAll()) {
            if (tournament.getStartDate().isEqual(LocalDate.now()) && tournament.getStatus() == TournamentStatus.Registration){
                tournament.setStatus(TournamentStatus.Started);
                for (UserEntity participant : tournament.getRegisteredUsers()) {
                    TournamentParticipationEntity tournamentParticipationEntity = new TournamentParticipationEntity();
                    tournamentParticipationEntity.setTournament(tournament);
                    tournamentParticipationEntity.setUser(participant);
                    tournamentParticipationEntity.setPoints(0);
                    tournamentParticipationEntity.setCompletedTaskCount(0);
                    tournamentParticipationEntity.setAverageMemoryInKilobytes(0);
                    tournamentParticipationEntity.setFinishedParticipating(false);
                    tournamentParticipationEntity.setFinishedCurrentTask(false);
                    ArrayList<Long> unfinishedTaskIds = (ArrayList<Long>) tournament.getTournamentTasks().stream().map(TaskEntity::getId).collect(Collectors.toList());
                    tournamentParticipationEntity.setLastTask(unfinishedTaskIds.size() == 1);
                    tournamentParticipationEntity.setUnfinishedTaskIds(unfinishedTaskIds);

                    Random random = new Random();
                    long nextTaskId = random.nextLong(0, unfinishedTaskIds.size());
                    Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(unfinishedTaskIds.get((int) nextTaskId));
                    if (optionalTaskEntity.isEmpty()){
                        throw new NoSuchElementException("Task with id: " + nextTaskId + " does not exist");
                    }
                    TaskEntity taskEntity = optionalTaskEntity.get();
                    tournamentParticipationEntity.setTask(taskEntity);
                    tournamentParticipationRepository.save(tournamentParticipationEntity);
                }
            }

            if (tournament.getEndDate().isEqual(LocalDate.now()) && tournament.getStatus() == TournamentStatus.Started){
                List<TournamentParticipationEntity> participationRecordList = tournamentParticipationRepository.findAllByTournament(tournament);
                processParticipationRecords(participationRecordList);
                addPointsForWinningPlaces(participationRecordList, tournament);
                addPointsToUsers(participationRecordList);
                tournament.setStatus(TournamentStatus.Ended);
                tournamentRepository.save(tournament);
            }
        }
    }

    private void processParticipationRecords(List<TournamentParticipationEntity> participationRecordList){
        for (TournamentParticipationEntity participationRecord : participationRecordList) {
            if (!participationRecord.getFinishedParticipating()) {
                if (!participationRecord.getFinishedCurrentTask()) {
                    participationRecord.deducePoints();
                }
                participationRecord.calculateMemoryAverage();
                participationRecord.setFinishedParticipating(true);
                tournamentParticipationRepository.save(participationRecord);
            }
        }
    }

    private void addPointsForWinningPlaces(List<TournamentParticipationEntity> participationRecordList, TournamentEntity tournament) {
        participationRecordList.sort(new Comparator<TournamentParticipationEntity>() {
            @Override
            public int compare(TournamentParticipationEntity o1, TournamentParticipationEntity o2) {
                if (o1.getPoints() != o2.getPoints()){
                    return Integer.compare(o2.getPoints(), o1.getPoints());
                }
                return Integer.compare(o2.getAverageMemoryInKilobytes(), o1.getAverageMemoryInKilobytes());
            }
        });

        int[] winningPlacePoints = {tournament.getFirstPlacePoints(), tournament.getSecondPlacePoints(), tournament.getThirdPlacePoints()};
        int end = participationRecordList.size() >= 3 ? 3 : participationRecordList.size();
        for (int i = 0; i < end; i++){
            TournamentParticipationEntity participationRecord = participationRecordList.get(i);
            participationRecord.addPoints(winningPlacePoints[i]);
            tournamentParticipationRepository.save(participationRecord);
        }
    }

    private void addPointsToUsers(List<TournamentParticipationEntity> participationRecordList){
        for (TournamentParticipationEntity participationRecord : participationRecordList) {
            UserEntity user = participationRecord.getUser();
            user.addPoints(participationRecord.getPoints());
            userRepository.save(user);
        }
    }
}
