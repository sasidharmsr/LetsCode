package com.example.Let.sCode.Entitys;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FollowEntityId implements Serializable {

    private int follower_id;
    private int following_id;
}
