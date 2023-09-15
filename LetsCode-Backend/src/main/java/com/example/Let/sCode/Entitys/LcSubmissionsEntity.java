package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lc_user_submissions")
@Data
public class LcSubmissionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private  Long userId;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "count")
    private Long count;
}
