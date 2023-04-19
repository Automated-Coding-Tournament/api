package com.pvp.codingtournament.business.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  = "task_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name =  "points")
    private int points;

    @Column(name = "methodName")
    private String methodName;

    @Column(name = "methodArguments")
    private ArrayList<String> methodArguments;

    @Column(name = "methodArgumentTypes")
    private ArrayList<String> methodArgumentTypes;

    @Column(name = "returnType")
    private String returnType;

    @Column(name = "input_output")
    private ArrayList<String[]> inputOutput;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private UserEntity user;

    @ManyToMany(mappedBy = "tournamentTasks")
    private Set<TournamentEntity> tournaments;
}
