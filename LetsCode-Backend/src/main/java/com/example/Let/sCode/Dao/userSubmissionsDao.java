package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Let.sCode.Entitys.UserSubmissionId;
import com.example.Let.sCode.Entitys.UsersSubmissionsEntity;

public interface userSubmissionsDao extends JpaRepository<UsersSubmissionsEntity, UserSubmissionId> {
    
    @Query("Select max(timeStamp) from UsersSubmissionsEntity as u where u.id.user_id=:userId group by u.id.type")
    List<Long> getLatestTimeStamp(int userId);

    @Query(nativeQuery = true,
    value = "SELECT DATE(FROM_UNIXTIME(time_stamp)) as Date, COUNT(*) " +
            "FROM user_submissions " +
            "WHERE DATE(FROM_UNIXTIME(time_stamp)) >= DATE_ADD(LAST_DAY(CURRENT_DATE()), INTERVAL -5 MONTH) " +
            "AND DATE(FROM_UNIXTIME(time_stamp)) < LAST_DAY(CURRENT_DATE()) " +
            "AND user_id = ?1 " +
            "GROUP BY Date " +
            "ORDER BY Date")
List<Object[]> getCountByDateForUser(int userId);

    @Query(nativeQuery = true,
        value = "SELECT DATE(FROM_UNIXTIME(time_stamp)) as Date, COUNT(*) " +
                "FROM user_submissions " +
                "WHERE  " +
                "user_id = ?1 " +
                "GROUP BY Date " +
                "ORDER BY Date")
    List<Object[]> getCountByDateForYearofUser(Long userId);

    @Query(value = "SELECT "
    + "SUM(CASE WHEN rating <= 1300 THEN 1 ELSE 0 END) AS easy_count, "
    + "SUM(CASE WHEN rating > 1300 AND rating <= 1700 THEN 1 ELSE 0 END) AS medium_count, "
    + "SUM(CASE WHEN rating > 1700 THEN 1 ELSE 0 END) AS hard_count "
    + "FROM user_submissions AS us "
    + "INNER JOIN cf_problems AS cf ON us.problem_id = cf.id AND us.type = cf.type "
    + "WHERE user_id = ?1", nativeQuery = true)
    List<Object[]> getSubmissionCountsForUser(Long userId);



            @Query(value = "SELECT cf_problems.contest_id, name, cf_problems.indicator, cf_problems.title, us.submission_id " +
               "FROM cf_problems " +
               "INNER JOIN cf_contests ON cf_problems.contest_id = cf_contests.contest_id " +
               "LEFT JOIN (select * from user_submissions where user_id=:userId)as us ON cf_problems.id = us.problem_id AND cf_problems.type = us.type " +
               "WHERE cf_contests.type = :type " +
               "AND cf_contests.contest_id < :contestId " +
               "ORDER BY contest_id DESC " +
               "LIMIT 160", nativeQuery = true)
List<Object[]> getProblemDataForUserAndContestCf(@Param("type") int type,@Param("userId") Long userId, @Param("contestId") Long contestId);


    @Query(nativeQuery = true,
           value = "SELECT ap.contest_id, ap.unique_id, ap.problem_index, ap.title, us.submission_id " +
                   "FROM at_problems ap " +
                   "LEFT JOIN (SELECT * FROM user_submissions WHERE type='at' AND user_id=:userId) us " +
                   "ON us.problem_id = ap.id " +
                   "WHERE ap.contest_id LIKE :contestIdPattern " +
                    "AND ap.contest_id < :contestId " +
                   "ORDER BY ap.contest_id DESC " +
                   "LIMIT 160")
    List<Object[]> getProblemDataForUserAndContestAt(@Param("contestIdPattern") String contestIdPattern,
                                                      @Param("userId") Long userId,
                                                      @Param("contestId") String contestId);


@Query(value = "SELECT COUNT(*) FROM cf_contests WHERE type = :type", nativeQuery = true)
int countCfContestsWithType(@Param("type") int type);

@Query(value = "SELECT contest_id FROM at_problems WHERE contest_id LIKE :type"+
                " order by  contest_id desc  limit 1", nativeQuery = true)
String countAtContestsWithType(@Param("type") String type);




    @Query(value = "SELECT DATE(FROM_UNIXTIME(time_stamp)) AS dates ,title,unique_id,type,submission_id " +
                   "FROM user_submissions " +
                   "WHERE user_id = :userId AND type LIKE :type " +
                   "AND DATE(FROM_UNIXTIME(time_stamp)) LIKE :datePattern"+
                   " order by time_stamp desc limit 30 offset :offset", nativeQuery = true)
    List<Object[]> findUserSubmissionsWithDates(@Param("userId") Long userId,@Param("type") String type ,@Param("datePattern") String datePattern,@Param("offset") Long offset);


    
    @Query(value = "SELECT count(*) " +
                   "FROM user_submissions " +
                   "WHERE user_id = :userId AND type LIKE :type " +
                   "AND DATE(FROM_UNIXTIME(time_stamp)) LIKE :datePattern"+
                   " order by time_stamp desc", nativeQuery = true)
    int  findUserSubmissionscount(@Param("userId") Long userId,@Param("type") String type ,@Param("datePattern") String datePattern);

}
