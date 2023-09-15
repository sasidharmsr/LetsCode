package com.example.Let.sCode.Entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LcProblemId implements Serializable {
    private Integer topic_id;
    private Integer problem_id;
}
