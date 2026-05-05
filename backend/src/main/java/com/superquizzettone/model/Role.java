package com.superquizzettone.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_PLAYER = "ROLE_PLAYER";
    public static final String ROLE_WRITER = "ROLE_WRITER";
    public static final String ROLE_REVIEWER = "ROLE_REVIEWER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    public Role(Long id) {
        this.id = id;
    }
    public Role(String description, String code) {
        this.description = description;
        this.code = code;
    }
}
