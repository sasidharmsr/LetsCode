package com.example.Let.sCode.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public static final String ROLES = "ROLE";
  public static final String UserId = "User_Id";
   public static final String NAME = "Name";

  public String extractUsername(String token) {
    Claims claims=extractClaim(token);
    return extractClaims(claims, Claims::getSubject);
  }

  public Claims extractClaim(String token) {
    return extractAllClaims(token);
  }

  public <T> T extractClaims(Claims claims, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(claims);
  }

  public List<String> getClaims(String token) {
    List<String> data=new ArrayList<>();
    Claims claim=extractClaim(token);
    data.add(extractClaims(claim,claims->(String)claims.get(ROLES)));
    data.add(extractClaims(claim,claims->(String)claims.get(UserId)));
    return data;
 }

  public String generateToken(UserDetails userDetails) {
    List<String> claims_list=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    Map<String,Object> claims=new HashMap<>();
    claims.put("ROLE", claims_list.get(1));
    claims.put("User_Id", claims_list.get(0));
    claims.put("Name", claims_list.get(2));
    return generateToken(claims, userDetails);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    Claims claims=extractClaim(token);
    return extractClaims(claims, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
