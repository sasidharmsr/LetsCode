package com.example.Let.sCode.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Entitys.userEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class userDao{
    
    public EntityManager entityManager;

    @Autowired
    public userDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Transactional
    public userEntity save(userEntity user)
    {
        return entityManager.merge(user);
    }

    
    public int insertAlll(String query) {
        return entityManager.createNativeQuery(query).executeUpdate();

    }

    public List<userEntity> findallUsers()
    {
        TypedQuery<userEntity> users=entityManager.createQuery("from userEntity", userEntity.class);
        return users.getResultList();
    }

    public userEntity findbyId(int user_id)
    {
        userEntity user=entityManager.find(userEntity.class,user_id);
        return user;
    }

    public userEntity findbyuserName(String user_name)
    {
        String query="From userEntity where userName=\""+user_name+"\"";
        TypedQuery<userEntity> user=entityManager.createQuery(query,userEntity.class);
        List<userEntity> resultList = user.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

     public userEntity findbyEmail(String email)
    {
        String query="From userEntity where email=\""+email+"\"";
        TypedQuery<userEntity> user=entityManager.createQuery(query,userEntity.class);
        List<userEntity> resultList = user.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

     public userEntity findbyphoneNumber(String phone_number )
    {
        String query="From userEntity where phoneNumber=\""+phone_number+"\"";
        TypedQuery<userEntity> user=entityManager.createQuery(query,userEntity.class);
        List<userEntity> resultList = user.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }


    public int updateWithUserName(userEntity user, String userName) {
        String query= String.format("Update userEntity SET lcUserId='%s',cfUserId='%s',atUserId='%s',pic='%s' where userName='%s' ", user.getLcUserId(),user.getCfUserId(),user.getAtUserId(),user.getPic(), userName);
        int result=entityManager.createQuery(query).executeUpdate();
        return result;
    }

    public userEntity findbychatId(String chatId)
    {
        String query="From userEntity where chatId=\""+chatId+"\"";
        TypedQuery<userEntity> user=entityManager.createQuery(query,userEntity.class);
        List<userEntity> resultList = user.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public   List<userEntity> findallEnabledUsers() {
       String query="From userEntity where ((chatId IS NOT NULL) and chatId!='' )";
        TypedQuery<userEntity> user=entityManager.createQuery(query,userEntity.class);
        List<userEntity> resultList = user.getResultList();
        if (!resultList.isEmpty()) {
            return resultList;
        } else {
            return null;
        }
    }
}
