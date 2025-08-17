package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24;
    private static final String secretKey = "e40e333ab109fe8152e60b0b0527d2957287b7952c69ce0bde2aaa121899a6ef068a262b8b8b1812";

    public static String encode(Long profileId, String phone) {
        return Jwts.builder()
                .subject(phone)
                .claim("id", profileId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getKey())
                .compact();
    }

    // RoleList bilan access token uchun
    public static String encode(String username, Long profileId, List<ProfileRole> roleList) {
        String strRoles = roleList.stream().map(Enum::name).collect(Collectors.joining(","));
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", strRoles);
        claims.put("id", profileId);

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getKey())
                .compact();
    }

    public static JwtDTO decode(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        Long id = Long.valueOf(String.valueOf(claims.get("id")));
        String strRoles = (String) claims.get("roles");
        List<ProfileRole> roleLis = strRoles != null
                ? Arrays.stream(strRoles.split(","))
                .map(ProfileRole::valueOf)
                .toList()
                : Collections.emptyList();

        return new JwtDTO(id, username, roleLis);
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static SecretKey getKey() {
        byte[] keyBytes = null;
        try {
            keyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Oddiy ID decode qilish
    public static Integer decodeVerRegToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Integer.valueOf(claims.getSubject());
    }

    // Refresh token yaratish
    public static String generateRefreshToken(String phone, Long profileId) {
        return Jwts.builder()
                .subject(phone)
                .claim("id", profileId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                .signWith(getKey())
                .compact();
    }

    // Tokenni validate qilish
    public static boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey()) // refresh key bilan
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // Tokendan username olish
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // Tokendan userId olish
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object id = claims.get("id");
        return Long.valueOf(String.valueOf(id));
    }
}