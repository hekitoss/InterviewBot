package com.interview.repository;

import com.interview.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findRateByUserIdAndQuestionId(Long userId, Long questionId);

    Set<Rate> findAllByQuestionId(Long questionId);
}
