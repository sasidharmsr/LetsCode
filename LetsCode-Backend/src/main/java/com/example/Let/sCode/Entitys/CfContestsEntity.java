package com.example.Let.sCode.Entitys;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cf_contests")
@Data
public class CfContestsEntity {

    @Id
    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private int type;

}
