package com.example.Let.sCode.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginResponse {
    private String user_name;
    private String leetcode_user_id;
    private String codeforces_user_id;
    private String atcoder_user_id;
}
