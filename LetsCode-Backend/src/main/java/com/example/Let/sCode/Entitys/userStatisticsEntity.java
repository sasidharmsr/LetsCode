package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_statistics")
@Data
public class userStatisticsEntity {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "cf_count")
    private int cfCount;

    @Column(name = "lc_count")
    private int lcCount;

    @Column(name = "ac_count")
    private int acCount;

    @Column(name = "rating")
    private int rating;

    @Column(name = "difficulty")
    private int difficulty;

    @Column(name = "at_type")
    private String atType;
}
