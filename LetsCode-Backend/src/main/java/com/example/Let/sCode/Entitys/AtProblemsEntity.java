package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "at_problems")
@Data
public class AtProblemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "contest_id")
    private String contestId;

    @Column(name = "problem_index")
    private String problemIndex;

    @Column(name = "title")
    private String title;

    @Column(name = "type", columnDefinition = "VARCHAR(3) DEFAULT 'at'")
    private String type;

}
