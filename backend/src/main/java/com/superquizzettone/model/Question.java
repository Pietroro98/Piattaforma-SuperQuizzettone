package com.superquizzettone.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "question")
    @Column(name = "answers", nullable = false)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "tag")
    private String tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuestionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuestionType type;

    @Column(name = "motivation_rejection")
    private String motivationRejection;
}
