package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "followers")
@Data
public class FollowEntity {

    @EmbeddedId
    private FollowEntityId followEntityId;
}
