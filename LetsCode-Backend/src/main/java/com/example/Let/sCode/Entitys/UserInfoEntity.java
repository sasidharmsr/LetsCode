package com.example.Let.sCode.Entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_info")
@Data
public class UserInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name = "school")
    private String school;

    @Column(name = "company")
    private String company;

    @Column(name = "user_avatar")
    private String userAvatar;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "cf_photo")
    private String cfPhoto;

    @Column(name = "lc_photo")
    private String lcPhoto;

    @Column(name = "cf_school")
    private String organization;
}
