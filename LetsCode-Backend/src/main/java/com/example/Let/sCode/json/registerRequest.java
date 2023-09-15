package com.example.Let.sCode.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class registerRequest {
    private String user_name;
    private String email;
    private String password;
    private String name;
    private String phone_number;
}
