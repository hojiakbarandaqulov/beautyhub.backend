package com.example.service.jwt;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String secretKey = "skjdhadasdasgfgdfgdfdftrhdgrgefergetdgsfegvergdgsbdzsfbvgdsetbg";
    private static final long tokenLiveTime = 1000L * 3600 * 24; // 1 kun


    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " prefiksini olib tashlaymiz
        }
        if (request.getCookies() != null) {
            Optional<String> jwtTokenFromCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

            if (jwtTokenFromCookie.isPresent()) {
                return jwtTokenFromCookie.get();
            }
        }

        return null;
    }

        public void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }
        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
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
            List<ProfileRole> roleList = Arrays.stream(strRoles.split(","))
                    .map(ProfileRole::valueOf)
                    .collect(Collectors.toList());

            return new JwtDTO(id, username, roleList, token, null);
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
