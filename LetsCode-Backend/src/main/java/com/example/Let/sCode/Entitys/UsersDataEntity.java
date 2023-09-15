package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "users_data")
@Data
public class UsersDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name = "cf_count")
    private Integer cfCount;

    @Column(name = "lc_count")
    private Integer lcCount;

    @Column(name = "at_count")
    private Integer atCount;

    @Column(name = "cf_rating")
    private Integer cfRating;

    @Column(name = "lc_rank")
    private Integer lcRank;

    @Column(name = "lc_rating")
    private Integer lcRating;

    @Column(name = "easy_count")
    private Integer easyCount;

    @Column(name = "hard_count")
    private Integer hardCount;

    @Column(name = "medium_count")
    private Integer mediumCount;

    @Column(name = "cf_rank")
    private String cfRank;

    @Column(name = "max_rank")
    private String maxRank;

    @Column(name = "max_rating")
    private Integer maxRating;

    @Column(name = "cf_friends")
    private Integer cfFriends;

}
