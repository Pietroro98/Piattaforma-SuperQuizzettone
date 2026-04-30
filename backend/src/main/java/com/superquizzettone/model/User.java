package com.superquizzettone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private UserState state;

    @Column(name = "creationDate", nullable = false)
    private LocalDate creationDate;

    @Column(name = "total_points")
    private Double totalPoints;

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /* Questo campo si riferisce alla tabella di join tra
    Quiz e User passando per la tabella di mezzo QuizPlayed
     */
    @OneToMany(mappedBy = "user")
    private List<QuizPlayed> attempts = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Quiz> myQuiz = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String name, String surname, LocalDate dateCreated) {
        this(username, password);
        this.name = name;
        this.surname = surname;
        this.creationDate = dateCreated;
    }

    public boolean isActive() {
        return state != null && state.equals(UserState.ATTIVO);
    }

    public boolean isDisabled() {
        return state != null && state.equals(UserState.DISABILITATO);
    }

    public boolean isAdmin() {
        return roles != null && roles.stream()
                .anyMatch(role -> Role.ROLE_ADMINISTRATOR.equals(role.getCode()));
    }
}
