package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_submissions")
@Data
public class UsersSubmissionsEntity {

    @EmbeddedId
    private UserSubmissionId id;

    @Column(name = "submission_id")
    private int submissionId;

    @Column(name = "time_stamp")
    private Long timeStamp;

    @Column(name = "title")
    private String title;

    @Column(name="unique_id")
    private String uniqueId;

}
