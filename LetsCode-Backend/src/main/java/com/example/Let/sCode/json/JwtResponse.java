package com.example.Let.sCode.json;

import lombok.Data;

import java.io.Serializable;
@Data
public class JwtResponse implements Serializable {

	private final String accessToken;

	private final String refreshToken;

}