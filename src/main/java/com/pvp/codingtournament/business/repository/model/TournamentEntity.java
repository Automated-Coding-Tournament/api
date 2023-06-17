package com.pvp.codingtournament.business.repository.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pvp.codingtournament.business.enums.Difficulty;
import com.pvp.codingtournament.business.enums.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "tournament")
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "first_place_points")
    private int firstPlacePoints;

    @Column(name = "second_place_points")
    private int secondPlacePoints;

    @Column(name = "third_place_points")
    private int thirdPlacePoints;

    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "status")
    private TournamentStatus status;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    @JsonManagedReference
    private UserEntity creatorUser;

    @ManyToMany
    @JoinTable(name = "user_tournament",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            joinColumns = @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id"))
    @JsonManagedReference
    private Set<UserEntity> registeredUsers;

    @ManyToMany
    @JoinTable(name = "tournament_task",
            inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "task_id"),
            joinColumns = @JoinColumn(name = "tournament_id", referencedColumnName = "tournament_id"))
    @JsonManagedReference
    private Set<TaskEntity> tournamentTasks;

    public void addTask(TaskEntity task){
        this.tournamentTasks.add(task);
    }

    public void registerUser(UserEntity userEntity) {
        this.registeredUsers.add(userEntity);
    }
}
