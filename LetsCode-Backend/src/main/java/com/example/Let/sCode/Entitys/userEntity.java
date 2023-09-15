package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class userEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_id")
    private String email;

    @Column(name = "lc_id")
    private String lcUserId;

    @Column(name = "cf_id")
    private String cfUserId;

    @Column(name = "at_id")
    private String atUserId;

    @Column(name = "pic")
    private String pic;

    @Column(name = "chat_id")
    private String chatId;

    public userEntity() {
    }

     public userEntity(String userName, String password, String role, String email, String lcUserId, String cfUserId,
            String atUserId) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.lcUserId = lcUserId;
        this.cfUserId = cfUserId;
        this.atUserId = atUserId;
    }

    @Override
    public String toString() {
        return "userEntity [userName=" + userName + ", password=" + password + ", role=" + role + ", email=" + email
                + ", lcUserId=" + lcUserId + ", cfUserId=" + cfUserId + ", atUserId=" + atUserId + "]";
    }

    
}
