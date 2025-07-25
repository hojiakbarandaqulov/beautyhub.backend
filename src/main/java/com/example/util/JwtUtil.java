package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

import static java.security.KeyRep.Type.SECRET;

public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "skjdhadasdasgfgdfgdfdftrhdgrgefergetdgsfegvergdgsbdzsfbvgdsetbg";

    public static String encode(Integer profileId, String email) {
        return Jwts
                .builder()
                .subject(email)
                .subject(String.valueOf(profileId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)))
                .signWith(getSignInKey())
                .compact();
    }
    public static String encode(String username, Long profileId, List<ProfileRole> roleList) {
        String strRoles = roleList.stream().map(Enum::name).
                collect(Collectors.joining(","));

        Map<String, String> claims = new HashMap<>();
        claims.put("roles", strRoles);
        claims.put("id", String.valueOf(profileId));

        return Jwts
                .builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)))
                .signWith(getSignInKey())
                .compact();
    }

    public static JwtDTO decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String username = claims.getSubject();
        Long id = Long.valueOf((String) claims.get("id"));
        String strRoles = (String) claims.get("roles");
        List<ProfileRole> roleLis = Arrays.stream(strRoles.split(","))
                .map(ProfileRole::valueOf)
                .toList();
        return new JwtDTO(id, username, roleLis);
    }

    public static Integer decodeVerRegToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Integer.valueOf(claims.getSubject());
    }

    public static String generateRefreshToken(String phone, Long profileId) {
        // Refresh token uchun alohida expiration time (30 kun)
        return JWT.create()
                .withSubject(phone)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenLiveTime))
                .withClaim("id", profileId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        return  Keys.hmacShaKeyFor(keyBytes);
    }
}


