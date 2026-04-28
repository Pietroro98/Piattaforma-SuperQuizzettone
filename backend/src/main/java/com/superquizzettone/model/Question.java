package com.superquizzettone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.lang.management.MonitorInfo;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descrizione")
    private String description;

    @OneToMany(mappedBy = "question")
    @Column(name = "answers", nullable = false)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "tag")
    private String tag;

    @Column(name = "motivation_rejection")
    private String motivationRejection;
}
