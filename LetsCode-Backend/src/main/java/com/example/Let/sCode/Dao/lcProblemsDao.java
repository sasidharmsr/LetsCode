package com.example.Let.sCode.Dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.LcProblemsEntity;

public interface lcProblemsDao extends JpaRepository<LcProblemsEntity,Integer> {
    
    
        @Query(nativeQuery = true, value = "SELECT  id,path,lc_problems.title,difficulty,ac_rate, " +
        "GROUP_CONCAT(lc_problem_topics.topic_id) AS topics_ids, u.submission_id " +
        "FROM (lc_problems " +
        "LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id) " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id " +
        "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "+
        " and ac_rate >= :ratingL and ac_rate <= :ratingR " +
         " and LOWER(lc_problems.title) LIKE :title " +
        " and ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard'))  " +
        "GROUP BY lc_problems.id " +
        "ORDER BY lc_problems.id DESC " +
        "LIMIT 30 offset :offset")
        List<Object[]> getFilteredLcProblems(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("offset") Long offset,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                   @Param("difficulty") int difficulty,
                                   @Param("title") String title);

        @Query(nativeQuery = true, value = "SELECT  count(*) as count " +
        "FROM (lc_problems " +
        "LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id) " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id " +
        "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "+
        " and ac_rate >= :ratingL and ac_rate <= :ratingR " +
         " and LOWER(lc_problems.title) LIKE :title " +
        " and ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard'))  " )
        int getFilteredLcProblemscount(@Param("userId") Long userId,
                                   @Param("status") int status,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                   @Param("difficulty") int difficulty,
                                   @Param("title") String title);

        @Query(nativeQuery = true, value = "SELECT  id,path,lc_problems.title,difficulty,ac_rate, "
        + "GROUP_CONCAT(lc_problem_topics.topic_id) AS topics_ids, u.submission_id  "
        + "FROM lc_problems "
        + "LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id "
        + "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id "
        + "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
        + "and ac_rate >= :ratingL and ac_rate <= :ratingR "
        +  " and LOWER(lc_problems.title) LIKE :title "
        + " and ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard'))  " 
        + "GROUP BY lc_problems.id "
        + "HAVING SUM(CASE WHEN FIND_IN_SET(lc_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) >= 1 "
        + "ORDER BY lc_problems.id DESC "
        + "LIMIT 30 OFFSET :offset")
        List<Object[]> getFilteredLcProblemswithOrTopics(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("topics") String topics,
                                   @Param("offset") Long offset,
                                   @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                   @Param("difficulty") int difficulty,
                                   @Param("title") String title);

                                   @Query(nativeQuery = true, value = "SELECT COUNT(*) as count "
                                   + "FROM (SELECT lc_problems.id "
                                   + "      FROM lc_problems "
                                   + "      LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id "
                                   + "      LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id "
                                   + "      WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
                                   + "            AND ac_rate >= :ratingL AND ac_rate <= :ratingR "
                                   + "            AND LOWER(lc_problems.title) LIKE :title "
                                   + "            AND ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard')) "
                                   + "      GROUP BY lc_problems.id "
                                   + "      HAVING SUM(CASE WHEN FIND_IN_SET(lc_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) >= 1) AS filtered_lc_problems")
                           int getFilteredLcProblemswithOrTopicscount(@Param("userId") Long userId,
                                      @Param("status") int status,
                                      @Param("topics") String topics,
                                      @Param("ratingL") int ratingL,
                                      @Param("ratingR") int ratingR,
                                      @Param("difficulty") int difficulty,
                                      @Param("title") String title);
                           

        @Query(nativeQuery = true, value = "SELECT  id,path,lc_problems.title,difficulty,ac_rate, "
        + "GROUP_CONCAT(lc_problem_topics.topic_id) AS topics_ids, u.submission_id "
        + "FROM lc_problems "
        + "LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id "
        + "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id "
        + "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
        +  " and ac_rate >= :ratingL and ac_rate <= :ratingR " 
        +  " and LOWER(lc_problems.title) LIKE :title "
        + " and ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard'))  " 
        + "GROUP BY lc_problems.id "
        + "HAVING SUM(CASE WHEN FIND_IN_SET(lc_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) = :topicCount "
        + "ORDER BY lc_problems.id DESC "
        + "LIMIT 30 OFFSET :offset")
        List<Object[]> getFilteredLcProblemswithAndTopics(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("topics") String topics,
                                   @Param("topicCount") int topicCount,
                                   @Param("offset") Long offset,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                   @Param("difficulty") int difficulty,
                                   @Param("title") String title);


                                   @Query(nativeQuery = true, value = "SELECT COUNT(*) as count "
                                   + "FROM (SELECT lc_problems.id "
                                   + "      FROM lc_problems "
                                   + "      LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id "
                                   + "      LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id "
                                   + "      WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
                                   + "            AND ac_rate >= :ratingL AND ac_rate <= :ratingR "
                                   + "            AND LOWER(lc_problems.title) LIKE :title "
                                   + "            AND ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard')) "
                                   + "      GROUP BY lc_problems.id "
                                   + "      HAVING SUM(CASE WHEN FIND_IN_SET(lc_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) = :topicCount) AS filtered_lc_problems")
                           int getFilteredLcProblemswithAndTopicscount(@Param("userId") Long userId,
                                      @Param("status") int status,
                                      @Param("topics") String topics,
                                      @Param("topicCount") int topicCount,
                                      @Param("ratingL") int ratingL,
                                      @Param("ratingR") int ratingR,
                                      @Param("difficulty") int difficulty,
                                      @Param("title") String title);
                           
}
