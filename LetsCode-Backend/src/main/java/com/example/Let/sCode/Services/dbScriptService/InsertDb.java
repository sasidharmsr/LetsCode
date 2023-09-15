package com.example.Let.sCode.Services.dbScriptService;

import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Let.sCode.Dao.userDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InsertDb {
    
    private final userDao userDao;
        public void processFile(String filePah) {
            Path filePath = Paths.get("db", filePah);
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                userDao.insertAlll(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
