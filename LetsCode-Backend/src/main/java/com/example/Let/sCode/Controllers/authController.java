package com.example.Let.sCode.Controllers;

import com.example.Let.sCode.Entitys.userEntity;
import com.example.Let.sCode.Services.EmailService;
import com.example.Let.sCode.Services.userService;
import com.example.Let.sCode.Services.dbScriptService.InsertDb;
import com.example.Let.sCode.json.registerRequest;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/api/auth")
public class authController {

    private final userService userService;
    private final EmailService emailService;
    
    private final InsertDb insertDb;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody registerRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(userService.auth(authenticationRequest)); 
      
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody registerRequest user) throws Exception {
        return ResponseEntity.ok(userService.save(user));
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(@RequestHeader String AUTHORIZATION) throws Exception {
        return ResponseEntity.ok(userService.refreshToken(AUTHORIZATION));
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ResponseEntity<?> ForgottenPassword(@RequestBody registerRequest request) throws Exception {
        return ResponseEntity.ok(userService.forgotPassword(request));
      
    }

    @RequestMapping(value = "/recovery", method = RequestMethod.GET)
    public ResponseEntity<?> ValidateToken(@RequestParam("token") String token) throws Throwable {
        return ResponseEntity.ok(userService.validateToken(token));
      
    }

     @RequestMapping(value = "/passwordchange", method = RequestMethod.POST)
    public ResponseEntity<?> ChangePassword(@RequestBody registerRequest user) throws Exception {
        return ResponseEntity.ok(userService.passwordChange(user));
      
    }
   
     @GetMapping("/migrate/db")
    public ResponseEntity<?> fun() throws Exception  {
        List<String> list=new ArrayList<>();//      list.add("topics.sql");
        list.add("cfProblems.sql");
        list.add("atProblems.sql");
        list.add("lcProblems.sql");
        list.add("lcProblems2.sql");
        list.add("cfContests.sql");
        list.add("lcProblemTopic.sql");
        list.add("cffProblemTopic.sql");
        for(String s:list)
        {
            insertDb.processFile(s);
        }
        return ResponseEntity.ok("Its Working MAn....");
    }

     @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test() throws Exception {
     //   cfproblems.generateSqlScript();
      // twilloService.addNumber();
      //emailService.fun();
        return ResponseEntity.ok("Its Working Partner....");
    }
}