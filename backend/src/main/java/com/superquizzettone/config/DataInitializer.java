package com.superquizzettone.config;

import com.superquizzettone.model.*;
import com.superquizzettone.repository.answer.AnswerRepository;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.repository.question.QuestionRepository;
import com.superquizzettone.repository.quiz.QuizRepository;
import com.superquizzettone.repository.ruolo.RoleRepository;
import com.superquizzettone.repository.utente.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRole(RoleRepository roleRepository,
                               UserRepository userRepository,
                               QuizRepository quizRepository,
                               CategoryRepository categoryRepository,
                               QuestionRepository questionRepository,
                               AnswerRepository answerRepository,
                               PasswordEncoder passwordEncoder) {

        return args -> {

            // =====================================
            // ROLES
            // =====================================

            Role administratorRole = createRole(
                    roleRepository,
                    "Amministratore",
                    Role.ROLE_ADMINISTRATOR
            );

            Role writerRole = createRole(
                    roleRepository,
                    "Writer",
                    Role.ROLE_WRITER
            );

            Role reviewerRole = createRole(
                    roleRepository,
                    "Reviewer",
                    Role.ROLE_REVIEWER
            );

            Role playerRole = createRole(
                    roleRepository,
                    "Player",
                    Role.ROLE_PLAYER
            );

            // =====================================
            // USERS
            // =====================================

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Administrator",
                    "System",
                    "Administrator1",
                    "Administrator_123",
                    administratorRole
            );

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Writer",
                    "System",
                    "Writer1",
                    "Writer_123",
                    writerRole
            );

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Reviewer",
                    "System",
                    "Reviewer1",
                    "Reviewer_123",
                    reviewerRole
            );

            createUser(
                    userRepository,
                    passwordEncoder,
                    "Player",
                    "Giocatore",
                    "Player1",
                    "Player_123",
                    playerRole
            );

            User admin = userRepository
                    .findByUsername("Administrator1")
                    .orElseThrow();

            // =====================================
            // CATEGORIES
            // =====================================

            Category java = createCategory(categoryRepository, "Java");
            Category angular = createCategory(categoryRepository, "Angular");
            Category spring = createCategory(categoryRepository, "Spring");
            Category typescript = createCategory(categoryRepository, "Typescript");

            // =====================================
            // QUIZ
            // =====================================

            Quiz javaSpringQuiz = createQuiz(
                    quizRepository,
                    "Java e Spring Boot",
                    "Domande di Java applicato a Spring Boot",
                    "05:00",
                    10.0,
                    admin,
                    Set.of(java, spring)
            );

            Quiz javaQuiz = createQuiz(
                    quizRepository,
                    "Java puro",
                    "Domande riguardanti Java SE",
                    "07:30",
                    15.0,
                    admin,
                    Set.of(java)
            );

            Quiz typescriptQuiz = createQuiz(
                    quizRepository,
                    "Typescript",
                    "Domande su Typescript",
                    "04:00",
                    8.0,
                    admin,
                    Set.of(typescript)
            );

            Quiz angularQuiz = createQuiz(
                    quizRepository,
                    "Angular",
                    "Domande sul framework Angular",
                    "06:00",
                    12.0,
                    admin,
                    Set.of(angular, typescript)
            );

            // =====================================
            // QUESTIONS
            // =====================================

            Question j1 = createQuestion(
                    questionRepository,
                    "Qual è la caratteristica principale della JVM?",
                    java,
                    "jvm",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, j1,
                    "Esegue il bytecode Java", true);

            createAnswer(answerRepository, j1,
                    "Compila il codice sorgente", false);

            createAnswer(answerRepository, j1,
                    "Gestisce database", false);

            createAnswer(answerRepository, j1,
                    "Scrive codice Java", false);

            // =====================================

            Question j2 = createQuestion(
                    questionRepository,
                    "Quale tra questi è un tipo primitivo in Java?",
                    java,
                    "fondamenti",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, j2,
                    "int", true);

            createAnswer(answerRepository, j2,
                    "String", false);

            createAnswer(answerRepository, j2,
                    "ArrayList", false);

            createAnswer(answerRepository, j2,
                    "Object", false);

            // =====================================

            Question s1 = createQuestion(
                    questionRepository,
                    "A cosa serve l'annotazione @Autowired in Spring?",
                    spring,
                    "dependency-injection",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, s1,
                    "Inietta automaticamente le dipendenze", true);

            createAnswer(answerRepository, s1,
                    "Crea una classe", false);

            createAnswer(answerRepository, s1,
                    "Compila il progetto", false);

            createAnswer(answerRepository, s1,
                    "Gestisce HTTP request", false);

            // =====================================

            Question s2 = createQuestion(
                    questionRepository,
                    "Cosa fa Spring Boot Starter?",
                    spring,
                    "starter",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, s2,
                    "Fornisce dipendenze preconfigurate", true);

            createAnswer(answerRepository, s2,
                    "Compila Java in JS", false);

            createAnswer(answerRepository, s2,
                    "Gestisce database direttamente", false);

            createAnswer(answerRepository, s2,
                    "Sostituisce JVM", false);

            // =====================================

            Question t1 = createQuestion(
                    questionRepository,
                    "Qual è la differenza principale tra TypeScript e JavaScript?",
                    typescript,
                    "basics",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, t1,
                    "TypeScript è tipizzato staticamente", true);

            createAnswer(answerRepository, t1,
                    "TypeScript è un database", false);

            createAnswer(answerRepository, t1,
                    "TypeScript sostituisce HTML", false);

            createAnswer(answerRepository, t1,
                    "TypeScript è un framework backend", false);

            // =====================================

            Question t2 = createQuestion(
                    questionRepository,
                    "Quale comando compila TypeScript?",
                    typescript,
                    "compiler",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, t2,
                    "tsc", true);

            createAnswer(answerRepository, t2,
                    "npm start", false);

            createAnswer(answerRepository, t2,
                    "node run", false);

            createAnswer(answerRepository, t2,
                    "ng build", false);

            // =====================================
            // ASSOCIAZIONE DOMANDE -> QUIZ
            // =====================================

            javaQuiz.setQuestions(List.of(
                    j1,
                    j2
            ));

            javaSpringQuiz.setQuestions(List.of(
                    j1,
                    j2,
                    s1,
                    s2
            ));

            typescriptQuiz.setQuestions(List.of(
                    t1,
                    t2
            ));

            angularQuiz.setQuestions(List.of(
                    t1,
                    t2
            ));

            // =====================================
            // SAVE QUIZ
            // =====================================

            quizRepository.save(javaQuiz);
            quizRepository.save(javaSpringQuiz);
            quizRepository.save(typescriptQuiz);
            quizRepository.save(angularQuiz);
        };
    }

    // =====================================
    // ROLE
    // =====================================

    private Role createRole(
            RoleRepository roleRepository,
            String description,
            String code
    ) {

        return roleRepository.findByCode(code)
                .orElseGet(() ->
                        roleRepository.save(
                                new Role(description, code)
                        )
                );
    }

    // =====================================
    // USER
    // =====================================

    private void createUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String surname,
            String username,
            String rawPassword,
            Role role
    ) {

        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();

        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCreationDate(LocalDate.now());
        user.setState(UserState.ATTIVO);
        user.setRoles(Set.of(role));
        user.setTotalPoints(0d);

        userRepository.save(user);
    }

    // =====================================
    // CATEGORY
    // =====================================

    private Category createCategory(
            CategoryRepository categoryRepository,
            String name
    ) {

        return categoryRepository.findByName(name)
                .orElseGet(() -> {

                    Category c = new Category();
                    c.setName(name);

                    return categoryRepository.save(c);
                });
    }

    // =====================================
    // QUIZ
    // =====================================

    private Quiz createQuiz(
            QuizRepository quizRepository,
            String name,
            String description,
            String time,
            Double totalPoints,
            User createdBy,
            Set<Category> categories
    ) {

        return quizRepository.findByName(name)
                .orElseGet(() -> {

                    Quiz quiz = new Quiz();

                    quiz.setName(name);
                    quiz.setDescription(description);
                    quiz.setQuizTime(time);
                    quiz.setTotalPoints(totalPoints);
                    quiz.setCreatedBy(createdBy);
                    quiz.setCategories(categories);

                    return quizRepository.save(quiz);
                });
    }

    // =====================================
    // QUESTION
    // =====================================

    private Question createQuestion(
            QuestionRepository questionRepository,
            String description,
            Category category,
            String tag,
            QuestionStatus status,
            QuestionType type,
            User createdBy
    ) {

        Question q = new Question();

        q.setDescription(description);
        q.setCategory(category);
        q.setTag(tag);
        q.setStatus(status);
        q.setType(type);
        q.setCreatedBy(createdBy);

        return questionRepository.save(q);
    }

    // =====================================
    // ANSWER
    // =====================================

    private void createAnswer(
            AnswerRepository answerRepository,
            Question question,
            String text,
            boolean correct
    ) {

        Answer answer = new Answer();

        answer.setDescription(text);
        answer.setCorrect(correct);
        answer.setQuestion(question);

        answerRepository.save(answer);
    }
}