package com.example.customvault.security;


import com.example.customvault.dto.TokenResponse;
import com.example.customvault.exceptions.AccessDeniedException;
import com.example.customvault.models.User;
import com.example.customvault.props.JwtProperties;
import com.example.customvault.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(UUID userId, String username) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .subject(username)
                .claim("id", userId.toString())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }


    public String createRefreshToken(UUID userId, String username) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder()
                .claim("id", userId.toString())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public TokenResponse refreshUserTokens(String refreshToken) {
        TokenResponse tokenResponse = new TokenResponse();

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        UUID userId = UUID.fromString(getId(refreshToken));
        User user = userService.getById(userId);
        tokenResponse.setId(userId);
        tokenResponse.setUsername(user.getUsername());
        tokenResponse.setAccessToken(createAccessToken(userId, user.getUsername()));
        tokenResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));
        return tokenResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getId(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id")
                .toString();
    }

    private String getUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private String getTtlUsername(String token, Key masterKey) {
        return Jwts
                .parser()
                .setSigningKey(masterKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String createTtlToken(UUID userId, String username, String secretId, byte[] masterKeyBytes) {

        Key masterKey = Keys.hmacShaKeyFor(masterKeyBytes);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getTtl());

        return Jwts.builder()
                .subject(username)
                .claim("id", userId.toString())
                .claim("secretId", secretId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(masterKey)
                .compact();
    }

    public String getSecretId(String token, byte[] masterKeyBytes, String username) {
        Key masterKey = Keys.hmacShaKeyFor(masterKeyBytes);

        if (!Objects.equals(getTtlUsername(token, masterKey), username)) {
            throw new AccessDeniedException();
        }

        return Jwts
                .parser()
                .setSigningKey(masterKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("secretId")
                .toString();
    }

    public boolean validateTtlToken(String token, byte[] masterKeyBytes) {
        try {
            Key masterKey = Keys.hmacShaKeyFor(masterKeyBytes);

            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(masterKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch (SignatureException | ExpiredJwtException se) {
            return false;
        }

    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }



}
