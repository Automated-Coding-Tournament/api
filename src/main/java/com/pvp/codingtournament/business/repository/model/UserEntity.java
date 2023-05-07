package com.pvp.codingtournament.business.repository.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pvp.codingtournament.business.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "points")
    private int points;

    @Column(name = "level")
    private String level;

    @Column(name = "role")
    private RoleEnum role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "creatorUser")
    @JsonBackReference
    private List<TournamentEntity> createdTournaments;

    @ManyToMany(mappedBy = "registeredUsers")
    @JsonManagedReference
    private List<TournamentEntity> attendingTournaments;

    public void addPoints(int points) {
        this.points += points;
    }
}
