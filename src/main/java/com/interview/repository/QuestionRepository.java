package com.interview.repository;

import com.interview.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT q.id, is_deleted, question_name, text, answer, rate_id, creator_id, creation_time, deleting_time " +
            "FROM question q WHERE NOT is_deleted ORDER BY random() limit 1",
            nativeQuery = true)
    Optional<Question> findRandom();
}
