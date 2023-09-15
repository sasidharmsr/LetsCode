package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lc_problems")
@Data
public class LcProblemsEntity {

    @Id
    private Integer id;

    @Column(name = "ac_rate")
    private double acRate;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "paid_only")
    private boolean paidOnly;

    @Column(name = "title")
    private String title;

    @Column(name = "path")
    private String path;

    @Column(name = "has_solution")
    private boolean hasSolution;

    @Column(name = "status")
    private String status;

    @Column(name = "type", columnDefinition = "VARCHAR(3) DEFAULT 'cf'")
    private String type;

}
