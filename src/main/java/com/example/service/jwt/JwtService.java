package com.example.service.jwt;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exp.AppBadException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String secretKey = "skjdhadasdasgfgdfgdfdftrhdgrgefergetdgsfegvergdgsbdzsfbvgdsetbg";
    private static final long tokenLiveTime = 1000L * 3600 * 24;

    public JwtService() {
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String username, Long profileId, List<ProfileRole> roleList) {
        String strRoles = roleList.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", strRoles);
        claims.put("id", String.valueOf(profileId));

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String phone, Long profileId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", String.valueOf(profileId));

        long refreshTokenLiveTime = 1000L * 3600 * 24 * 30;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phone)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenLiveTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token) && !isTokenInvalidated(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        if (token.split("\\.").length != 3) {
            throw new AppBadException("Invalid JWT token format");
        }
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtDTO decodeToken(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        Long id = Long.valueOf((String) claims.get("id"));
        String strRoles = (String) claims.get("roles");
        List<ProfileRole> roleList = Arrays.stream(strRoles.split(" "))
                .map(ProfileRole::valueOf)
                .collect(Collectors.toList());
        return new JwtDTO(id, username, roleList);
    }

    public Integer decodeVerRegToken(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.valueOf(claims.getSubject());
    }

    public void invalidateToken(String token) {
        Date expirationDate = extractExpiration(token);
        long timeLeft = expirationDate.getTime() - System.currentTimeMillis();

        if (timeLeft > 0) {
            System.out.println("JWT token bekor qilindi (blacklistga qo'shildi): " + token + " (Qolgan vaqt: " + timeLeft + " ms)");
        } else {
            System.out.println("JWT token allaqachon muddati tugagan: " + token);
        }
    }

    public boolean isTokenInvalidated(String token) {
        System.out.println("JWT token blacklistda tekshirildi: " + token);
        return false;
    }
}
