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
            Role administratorRole = createRole(roleRepository, "Amministratore", Role.ROLE_ADMINISTRATOR);
            Role writerRole = createRole(roleRepository, "Writer", Role.ROLE_WRITER);
            Role reviewerRole = createRole(roleRepository, "Reviewer", Role.ROLE_REVIEWER);
            createRole(roleRepository, "Player", Role.ROLE_PLAYER);
            // =====================================
            // USERS
            // =====================================
            createUser(userRepository, passwordEncoder, "Administrator", "System", "Administrator1", "Administrator_123", administratorRole);
            createUser(userRepository, passwordEncoder, "Writer", "System", "Writer1", "Writer_123", writerRole);
            createUser(userRepository, passwordEncoder, "Reviewer", "System", "Reviewer1", "Reviewer_123", reviewerRole);
            createUser(userRepository, passwordEncoder, "Reviewer2", "System2", "Reviewer2", "Reviewer_123", reviewerRole);

            User admin = userRepository.findByUsername("Administrator1").orElseThrow();
            // =========================
            // CATEGORIES
            // =========================
            Category java = createCategory(categoryRepository, "Java");
            Category angular = createCategory(categoryRepository, "Angular");
            Category spring = createCategory(categoryRepository, "Spring");
            Category typescript = createCategory(categoryRepository, "Typescript");

            // =========================
            // QUIZ MOCK
            // =========================

            createQuiz(quizRepository,
                    "Java e Spring Boot",
                    "Domande di java appplicato al suo framework più conosciuto",
                    "05:00",
                    10.0,
                    admin,
                    Set.of(java, spring)
            );

            createQuiz(quizRepository,
                    "Java puro",
                    "Domande riguardanti Java SE",
                    "07:30",
                    15.0,
                    admin,
                    Set.of(java)
            );

            createQuiz(quizRepository,
                    "Typescript",
                    "Domande su typescript",
                    "04:00",
                    8.0,
                    admin,
                    Set.of(typescript)
            );

            createQuiz(quizRepository,
                    "Angular",
                    "Domande sul framework di frontend Angular e sul suo linguaggio di programmazione",
                    "06:00",
                    12.0,
                    admin,
                    Set.of(angular, typescript)
            );
            Quiz javaQuiz = quizRepository.findByName("Java puro").orElseThrow();

            Question j1 = createQuestion(
                    questionRepository,
                    "Qual è la caratteristica principale della JVM?",
                    java,
                    javaQuiz,
                    "jvm",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, j1, "Esegue il bytecode Java", true);
            createAnswer(answerRepository, j1, "Compila il codice sorgente", false);
            createAnswer(answerRepository, j1, "Gestisce database", false);
            createAnswer(answerRepository, j1, "Scrive codice Java", false);


            Question j2 = createQuestion(
                    questionRepository,
                    "Quale tra questi è un tipo primitivo in Java?",
                    java,
                    javaQuiz,
                    "fondamenti",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, j2, "int", true);
            createAnswer(answerRepository, j2, "String", false);
            createAnswer(answerRepository, j2, "ArrayList", false);
            createAnswer(answerRepository, j2, "Object", false);

            Quiz springQuiz = quizRepository.findByName("Java e Spring Boot").orElseThrow();

            Question s1 = createQuestion(
                    questionRepository,
                    "A cosa serve l'annotazione @Autowired in Spring?",
                    spring,
                    springQuiz,
                    "dependency-injection",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, s1, "Inietta automaticamente le dipendenze", true);
            createAnswer(answerRepository, s1, "Crea una classe", false);
            createAnswer(answerRepository, s1, "Compila il progetto", false);
            createAnswer(answerRepository, s1, "Gestisce HTTP request", false);


            Question s2 = createQuestion(
                    questionRepository,
                    "Cosa fa Spring Boot Starter?",
                    spring,
                    springQuiz,
                    "starter",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, s2, "Fornisce dipendenze preconfigurate", true);
            createAnswer(answerRepository, s2, "Compila Java in JS", false);
            createAnswer(answerRepository, s2, "Gestisce database direttamente", false);
            createAnswer(answerRepository, s2, "Sostituisce JVM", false);

            Quiz tsQuiz = quizRepository.findByName("Typescript").orElseThrow();

            Question t1 = createQuestion(
                    questionRepository,
                    "Qual è la differenza principale tra TypeScript e JavaScript?",
                    typescript,
                    tsQuiz,
                    "basics",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, t1, "TypeScript è tipizzato staticamente", true);
            createAnswer(answerRepository, t1, "TypeScript è un database", false);
            createAnswer(answerRepository, t1, "TypeScript sostituisce HTML", false);
            createAnswer(answerRepository, t1, "TypeScript è un framework backend", false);


            Question t2 = createQuestion(
                    questionRepository,
                    "Quale comando compila TypeScript?",
                    typescript,
                    tsQuiz,
                    "compiler",
                    QuestionStatus.ACCEPTED,
                    QuestionType.SINGOLA,
                    admin
            );

            createAnswer(answerRepository, t2, "tsc", true);
            createAnswer(answerRepository, t2, "npm start", false);
            createAnswer(answerRepository, t2, "node run", false);
            createAnswer(answerRepository, t2, "ng build", false);
        };

    }

    // =========================
    // CREATE ROLE
    // =========================
    private Role createRole(RoleRepository roleRepository, String descrizione, String codice) {
        return roleRepository.findByCode(codice)
                .orElseGet(() -> roleRepository.save(new Role(descrizione, codice)));
    }

    // =========================
    // USER
    // =========================
    private void createUser(UserRepository userRepository, PasswordEncoder passwordEncoder, String name,
                            String surname, String username, String rawPassword, Role role) {
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

    // =========================
    // CATEGORY
    // =========================
    private Category createCategory(CategoryRepository categoryRepository, String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(name);
                    return categoryRepository.save(c);
                });
    }

    // =========================
    // QUIZ
    // =========================
    private void createQuiz(
            QuizRepository quizRepository,
            String name,
            String description,
            String time,
            Double totalPoints,
            User createdBy,
            Set<Category> categories
    ) {

        if (quizRepository.existsByName(name)) {
            return;
        }

        Quiz quiz = new Quiz();
        quiz.setName(name);
        quiz.setDescription(description);
        quiz.setQuizTime(time);
        quiz.setTotalPoints(totalPoints);
        quiz.setCreatedBy(createdBy);
        quiz.setCategories(categories);

        quizRepository.save(quiz);
    }

    // =========================
    // QUESTION
    // =========================
    private Question createQuestion(
            QuestionRepository questionRepository,
            String description,
            Category category,
            Quiz quiz,
            String tag,
            QuestionStatus status,
            QuestionType type,
            User createdBy
    ) {
        Question q = new Question();
        q.setDescription(description);
        q.setCategory(category);
        q.setQuiz(quiz);
        q.setTag(tag);
        q.setStatus(status);
        q.setType(type);
        q.setCreatedBy(createdBy);

        return questionRepository.save(q);
    }

    // =========================
    // ANSWER
    // =========================
    private void createAnswer(
            AnswerRepository answerRepository,
            Question question,
            String text,
            boolean correct
    ) {
        Answer a = new Answer();
        a.setDescription(text);
        a.setCorrect(correct);
        a.setQuestion(question);

        answerRepository.save(a);
    }
}
