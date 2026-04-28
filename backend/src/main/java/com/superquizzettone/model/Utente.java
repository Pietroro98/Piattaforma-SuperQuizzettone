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
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private StatoUtente stato;

    @Column(name = "dataRegistrazione")
    private LocalDate dataRegistrazione;

    @Column(name = "total_points")
    private Double totalPoints;

    @ManyToMany
    @JoinTable(
        name = "utente_ruolo",
        joinColumns = @JoinColumn(name = "utente_id"),
        inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private Set<Ruolo> ruoli = new HashSet<>();

    @OneToMany(mappedBy = "utente")
    private List<QuizPlayed> attempts = new ArrayList<>();

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Utente(String username, String password, String nome, String cognome, LocalDate dateCreated) {
        this(username, password);
        this.nome = nome;
        this.cognome = cognome;
        this.dataRegistrazione = dateCreated;
    }

    public boolean isAttivo() {
        return stato != null && stato.equals(StatoUtente.ATTIVO);
    }

    public boolean isDisabilitato() {
        return stato != null && stato.equals(StatoUtente.DISABILITATO);
    }
}
