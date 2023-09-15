package com.example.Let.sCode.Services.dbScriptService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Let.sCode.Entitys.TopicEntity;

import jakarta.persistence.EntityManager;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class cfproblems {

    @Autowired
    private EntityManager entityManager;

    public void generateSqlScript() {
       List<TopicEntity> contests = entityManager.createQuery("SELECT c FROM TopicEntity c", TopicEntity.class)
                .getResultList();

        StringBuilder sqlScript = new StringBuilder();
        for (TopicEntity problemTopic : contests) {
            sqlScript.append("INSERT INTO topics (id, topic_name, cf_name, slug ) VALUES (")
            .append(problemTopic.getId()).append(", ");
            String topicName = problemTopic.getTopicName() != null ? "'" + problemTopic.getTopicName().replace("'", "''") + "'" : "NULL";
            sqlScript.append(topicName).append(", ");
        
            String cfName = problemTopic.getCfName() != null ? "'" + problemTopic.getCfName().replace("'", "''") + "'" : "NULL";
            sqlScript.append(cfName).append(", ");
        
            String slug = problemTopic.getSlug() != null ? "'" + problemTopic.getSlug().replace("'", "''") + "'" : "NULL";
            sqlScript.append(slug).append(");\n");
        }

        saveToFile( sqlScript.toString());
    }

    private void saveToFile(String content) {
        try (FileWriter writer = new FileWriter("db/topics.sql")) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
