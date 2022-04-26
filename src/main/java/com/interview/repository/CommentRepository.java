package com.interview.repository;

import com.interview.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment c WHERE NOT is_deleted AND question_id = ?1 ORDER BY number_of_likes DESC, creation_time LIMIT 1",
            nativeQuery = true)
    Comment findTopCommentByQuestionId (Long questionId);

    List<Comment> findAllByQuestionId (Long questionId);
}
