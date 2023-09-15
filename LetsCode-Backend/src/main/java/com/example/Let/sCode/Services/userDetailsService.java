package com.example.Let.sCode.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.Let.sCode.Dao.userDao;
import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Exceptions.UserIdNotFoundExeption;

@Service
public class userDetailsService implements UserDetailsService{

    @Autowired
    userDao userdao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UserIdNotFoundExeption {
    
        userEntity user=userdao.findbyuserName(username);
        if (user == null) {
			throw new UserIdNotFoundExeption("User not found with username: " + username);
		}
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRole(),""+user.getUserId(),user.getName()));
    }

    
    
}
