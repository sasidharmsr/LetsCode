package com.example.Let.sCode.Entitys;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class UserSubmissionId implements Serializable {

    private int user_id;
    private int problem_id;
    private String type;
}
