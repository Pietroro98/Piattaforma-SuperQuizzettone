package com.superquizzettone.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ruolo")
@Getter
@Setter
@NoArgsConstructor
public class Ruolo {

    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_PLAYER = "ROLE_PLAYER";
    public static final String ROLE_WRITER = "ROLE_WRITER";
    public static final String ROLE_REVIEWER = "ROLE_REVIEWER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "codice", unique = true, nullable = false)
    private String codice;

    public Ruolo(Long id) {
        this.id = id;
    }

    public Ruolo(String descrizione, String codice) {
        this.descrizione = descrizione;
        this.codice = codice;
    }
}
