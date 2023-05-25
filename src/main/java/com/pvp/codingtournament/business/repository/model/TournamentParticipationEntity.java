package com.pvp.codingtournament.business.repository.model;

import com.pvp.codingtournament.model.AnalysisResults;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournamentParticipation")
public class TournamentParticipationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_participation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private TournamentEntity tournament;

    @Column(name = "points")
    private int points;

    @Column(name = "completed_task_count")
    private int completedTaskCount;

    @Column(name = "average_memory_in_kilobytes")
    private int averageMemoryInKilobytes;

    @Column(name = "finished_participating")
    private boolean finishedParticipating;

    @Column(name = "unfinished_task_ids")
    private ArrayList<Long> unfinishedTaskIds;

    @Column(name = "last_task")
    private boolean lastTask;

    @Column(name = "passed_current_task")
    private Boolean passed;

    @Column(name = "total_test_cases")
    private Integer totalTestCases;

    @Column(name = "passed_test_cases")
    private Integer passedTestCases;

    @Column(name = "memory_in_kilobytes")
    private Integer memoryInKilobytes;

    @Column(name = "average_cpu_time")
    private Double averageCpuTime;

    @ManyToOne
    @JoinColumn(name = "current_task_id")
    private TaskEntity task;

    @Column(name = "finished_current_task")
    private boolean finishedCurrentTask;

    public void incrementCompletedTaskCount() {
        this.completedTaskCount++;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addMemoryInKilobytes(int memoryInKilobytes) {
        this.averageMemoryInKilobytes += memoryInKilobytes;
    }

    public void deducePoints() {
        this.points *= 0.75;
    }

    public void calculateMemoryAverage() {
        if (completedTaskCount != 0){
            this.averageMemoryInKilobytes /= completedTaskCount;
        }
    }

    public void removeTaskIdFromUnfinishedTasks(Long taskId) {
        this.unfinishedTaskIds.remove(taskId);
    }
}
