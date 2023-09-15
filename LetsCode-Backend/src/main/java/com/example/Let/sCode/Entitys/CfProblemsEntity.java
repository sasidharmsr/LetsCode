package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cf_problems")
@Data
public class CfProblemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "contest_id")
    private int contestId;

    @Column(name = "indicator")
    private String indicator;

    @Column(name = "title")
    private String title;

    @Column(name = "rating",nullable = true)
    private Integer rating;

    @Column(name = "status")
    private String status;

    @Column(name = "type", columnDefinition = "VARCHAR(3) DEFAULT 'cf'")
    private String type;
}
