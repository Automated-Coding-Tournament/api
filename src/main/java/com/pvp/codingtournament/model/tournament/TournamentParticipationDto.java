package com.pvp.codingtournament.model.tournament;

import com.pvp.codingtournament.model.UserDto;
import com.pvp.codingtournament.model.task.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TournamentParticipationDto {
    private Long id;
    private UserDto user;
    private TournamentDto tournament;
    private int points;
    private int completedTaskCount;
    private int averageMemoryInKilobytes;
    private boolean finishedParticipating;
    private ArrayList<Long> unfinishedTaskIds;
    private TaskDto task;
    private boolean finishedCurrentTask;
}
