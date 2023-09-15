package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.AtProblemsEntity;

public interface atProblemsDao extends JpaRepository<AtProblemsEntity,Integer>{
    
    @Query(value = "SELECT difficulty,title,path,ac_rate FROM lc_problems ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomLCProblem();

      @Query(value = "SELECT contest_id,indicator,title,rating FROM cf_problems where title != 'Not in English Language' ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomCfProblem();

     @Query(value = "SELECT contest_id,indicator,title,rating FROM cf_problems where rating=:rating and title != 'Not in English Language' ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomCfProblemByRating(@Param("rating") int rating);

      @Query(value = "SELECT unique_id,contest_id,title,problem_index FROM at_problems where title!='Not in English Language' and  contest_id like 'abc%'  ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomAtProblem();

     @Query(value = "SELECT unique_id,contest_id,title,problem_index FROM at_problems where problem_index=:index and  title!='Not in English Language' and  contest_id like 'abc%'  ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomAtProblemByIndex(@Param("index") String index);

     @Query(value = "SELECT difficulty,title,path,ac_rate FROM lc_problems where difficulty=:difficulty ORDER BY RAND() LIMIT 1", nativeQuery = true)
     List<Object[]> findRandomLCProblemByDifculty(@Param("difficulty") String difficulty);

     @Query(value = "SELECT DATE(FROM_UNIXTIME(time_stamp)) AS date ,title,unique_id,type,submission_id " +
     "FROM user_submissions  " +
     " WHERE user_id = :userId AND type LIKE :type " +
     " order by RAND() Limit 1", nativeQuery = true)
    List<Object[]> findSolvedProblem(@Param("userId") Long userId,@Param("type") String type);

    @Query(nativeQuery = true, value = "SELECT cf_problems.contest_id, cf_problems.indicator, cf_problems.title, cf_problems.rating, " +
    "GROUP_CONCAT(cf_problem_topics.topic_id) AS topics_ids  " +
    "FROM (cf_problems " +
    "LEFT JOIN cf_problem_topics ON cf_problems.id = cf_problem_topics.problem_id) " +
    "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'cf' AND u.problem_id = cf_problems.id " +
    "WHERE (u.submission_id IS NULL) "+
    " and rating = :rating " +
    "GROUP BY cf_problems.id " +
    "ORDER BY RAND() " +
    "LIMIT :limit ")
    List<Object[]> getFilteredCfProblems(@Param("userId") Long userId,
                               @Param("limit") int limit,
                                @Param("rating") int rating);
     

        @Query(nativeQuery = true, value = "SELECT  id,path,lc_problems.title,difficulty,ac_rate, " +
        "GROUP_CONCAT(lc_problem_topics.topic_id) AS topics_ids, u.submission_id  " +
        "FROM (lc_problems " +
        "LEFT JOIN lc_problem_topics ON lc_problems.id = lc_problem_topics.problem_id) " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId AND u.type = 'lc' AND u.problem_id = lc_problems.id " +
        "WHERE (u.submission_id IS NULL) "+
        " and ((:difficulty = 0) OR (:difficulty = 1 AND difficulty='Easy') OR (:difficulty = 2 AND difficulty='Medium') OR (:difficulty = 3 AND difficulty='Hard'))  " +
        "GROUP BY lc_problems.id " +
        "ORDER BY RAND() " +
        "LIMIT :limit ")
        List<Object[]> getFilteredLcProblems(@Param("userId") Long userId,
                                                @Param("limit") int limit,
                                                @Param("difficulty") int difficulty);


        @Query(value = "SELECT at_problems.unique_id, contest_id, problem_index, at_problems.title " +
        "FROM at_problems " +
        "LEFT JOIN user_submissions u ON u.user_id = :userId " +
        "AND u.type = 'at' " +
        "AND u.problem_id = at_problems.id " +
        "WHERE (u.submission_id IS NULL) " +
        "AND contest_id LIKE 'abc%' " +
        "AND ((:type = 'ALL') OR (problem_index = :type)) " +
        "AND at_problems.title != 'Not in English Language' " +
        "ORDER BY RAND() " +
        "LIMIT :limit", nativeQuery = true)
        List<Object[]> getFilteredAtProblems(@Param("userId") Long userId,
                                                @Param("limit") int limit,
                                                @Param("type") String type);
}
