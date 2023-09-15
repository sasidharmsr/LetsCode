package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Entitys.CfProblemsEntity;

public interface cfProblemsDao extends JpaRepository<CfProblemsEntity,Integer>{

    @Modifying
    @Transactional
    @Query(value = ":query",nativeQuery = true)
    void updatetable(@Param("query") String query);
    

        @Query(value = "SELECT "
            + "lc.difficulty, lc.count AS lc_count, "
            + "cf.easy_count, cf.medium_count, cf.hard_count "
            + "FROM ("
            + "    SELECT difficulty, COUNT(*) AS count "
            + "    FROM lc_problems "
            + "    GROUP BY difficulty "
            + ") AS lc "
            + "CROSS JOIN ("
            + "    SELECT "
            + "        SUM(CASE WHEN rating <= 1300 THEN 1 ELSE 0 END) AS easy_count, "
            + "        SUM(CASE WHEN rating > 1300 AND rating <= 1700 THEN 1 ELSE 0 END) AS medium_count, "
            + "        SUM(CASE WHEN rating > 1700 THEN 1 ELSE 0 END) AS hard_count "
            + "    FROM cf_problems "
            + ") AS cf", nativeQuery = true)
        List<Object[]> getCombinedtotalQuestions();

        @Query(nativeQuery = true, value = "SELECT cf_problems.contest_id, cf_problems.indicator, cf_problems.title, cf_problems.rating, " +
        "GROUP_CONCAT(cf_problem_topics.topic_id) AS topics_ids, u.submission_id  " +
        "FROM (cf_problems " +
        "LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id) " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id " +
        "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) and (rating IS NOT NULL ) "+
        " and rating >= :ratingL and rating <= :ratingR " 
        +  " and LOWER(cf_problems.title) LIKE :title "+
        "GROUP BY cf_problems.id " +
        "ORDER BY cf_problems.contest_id DESC " +
        "LIMIT 30 offset :offset")
        List<Object[]> getFilteredCfProblems(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("offset") Long offset,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                    @Param("title") String title);

        @Query(nativeQuery = true, value = "SELECT count(*) as count  " +
        "FROM (cf_problems " +
        "LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id) " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id " +
        "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) and (rating IS NOT NULL ) "+
        " and rating >= :ratingL and rating <= :ratingR " 
        +  " and LOWER(cf_problems.title) LIKE :title ")
        int getFilteredCfProblemscount(@Param("userId") Long userId,
                                   @Param("status") int status,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                    @Param("title") String title);

        @Query(nativeQuery = true, value = "SELECT cf_problems.contest_id, cf_problems.indicator, cf_problems.title, cf_problems.rating, "
        + "GROUP_CONCAT(cf_problem_topics.topic_id) AS topics_ids, u.submission_id  "
        + "FROM cf_problems "
        + "LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id "
        + "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id "
        + "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) and (rating IS NOT NULL ) "
        + "and rating >= :ratingL and rating <= :ratingR "
        +  " and LOWER(cf_problems.title) LIKE :title "
        + "GROUP BY cf_problems.id "
        + "HAVING SUM(CASE WHEN FIND_IN_SET(cf_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) >= 1 "
        + "ORDER BY cf_problems.contest_id DESC "
        + "LIMIT 30 OFFSET :offset")
        List<Object[]> getFilteredCfProblemswithOrTopics(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("topics") String topics,
                                   @Param("offset") Long offset,
                                   @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                    @Param("title") String title);
            @Query(nativeQuery = true, value = "SELECT COUNT(*) as count "
            + "FROM (SELECT DISTINCT cf_problems.id "
            + "      FROM cf_problems "
            + "      LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id "
            + "      LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id "
            + "      WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
            + "            AND (rating IS NOT NULL) "
            + "            AND rating >= :ratingL "
            + "            AND rating <= :ratingR "
            + "            AND LOWER(cf_problems.title) LIKE :title "
            + "            AND EXISTS (SELECT 1 FROM cf_problem_topics WHERE FIND_IN_SET(cf_problem_topics.topic_id, :topics) > 0)) AS subquery")
    int getFilteredCfProblemswithOrTopicscount(@Param("userId") Long userId,
                @Param("status") int status,
                @Param("topics") String topics,
                @Param("ratingL") int ratingL,
                @Param("ratingR") int ratingR,
                @Param("title") String title);
                            

        @Query(nativeQuery = true, value = "SELECT cf_problems.contest_id, cf_problems.indicator, cf_problems.title, cf_problems.rating, "
        + "GROUP_CONCAT(cf_problem_topics.topic_id) AS topics_ids, u.submission_id  "
        + "FROM cf_problems "
        + "LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id "
        + "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id "
        + "WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) and (rating IS NOT NULL ) "
        +  " and rating >= :ratingL and rating <= :ratingR " 
        +  " and LOWER(cf_problems.title) LIKE :title "
        + "GROUP BY cf_problems.id "
        + "HAVING SUM(CASE WHEN FIND_IN_SET(cf_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) = :topicCount "
        + "ORDER BY cf_problems.contest_id DESC "
        + "LIMIT 30 OFFSET :offset")
        List<Object[]> getFilteredCfProblemswithAndTopics(@Param("userId") Long userId,
                                   @Param("status") int status,
                                   @Param("topics") String topics,
                                   @Param("topicCount") int topicCount,
                                   @Param("offset") Long offset,
                                    @Param("ratingL") int ratingL,
                                   @Param("ratingR") int ratingR,
                                   @Param("title") String title);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) as count "
    + "FROM (SELECT cf_problems.id "
    + "      FROM cf_problems "
    + "      LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id "
    + "      LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id "
    + "      WHERE ((:status = 0) OR (:status = 1 AND u.submission_id IS NOT NULL) OR (:status = 2 AND u.submission_id IS NULL)) "
    + "            AND (rating IS NOT NULL) "
    + "            AND rating >= :ratingL "
    + "            AND rating <= :ratingR "
    + "            AND LOWER(cf_problems.title) LIKE :title "
    + "      GROUP BY cf_problems.id "
    + "      HAVING SUM(CASE WHEN FIND_IN_SET(cf_problem_topics.topic_id, :topics) > 0 THEN 1 ELSE 0 END) = :topicCount) AS subquery")
int getFilteredCfProblemswithAndTopicscount(@Param("userId") Long userId,
        @Param("status") int status,
        @Param("topics") String topics,
        @Param("topicCount") int topicCount,
        @Param("ratingL") int ratingL,
        @Param("ratingR") int ratingR,
        @Param("title") String title);
                           
}
