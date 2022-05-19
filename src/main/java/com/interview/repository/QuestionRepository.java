package com.interview.repository;

import com.interview.entity.Question;
import com.interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM question q WHERE NOT is_deleted ORDER BY random() limit 1",
            nativeQuery = true)
    Optional<Question> findRandom();

    int countQuestionByOwnerId(Long id);

    List<Question> findAllByOwner(User user);
}
