package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cf_problem_topics")
@Data
public class CfProblemTopicEntity {

    @EmbeddedId
    private LcProblemId id;
   
}
