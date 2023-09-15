
package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lc_problem_topics")
@Data
public class LcProblemTopic {

    @EmbeddedId
    private LcProblemId id;
}
