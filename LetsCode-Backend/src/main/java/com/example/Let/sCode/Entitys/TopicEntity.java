package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="topics")
@Data
public class TopicEntity {
    
    @Id
    @Column(name = "id")
    private Long id;
    
    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "cf_name")
    private String cfName;

    @Column(name = "slug")
    private String slug;
}
