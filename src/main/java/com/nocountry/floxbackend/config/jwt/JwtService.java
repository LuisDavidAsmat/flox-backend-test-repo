package com.nocountry.floxbackend.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{

    @Value("${jwt.secret}")
    private String secretKey;

    public String getEmailFromToken(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
    {
        final Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    private SecretKey getKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        final String email = getEmailFromToken(token);

        return (email.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    private boolean isTokenExpired(String token)
    {
        return !getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(String email)
    {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email)
    {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getKey())
                .compact();
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails)
    {
        try
        {
            final String email = getEmailFromToken(token);
            return (email.equals(userDetails.getUsername()) && isTokenExpired(token));
        }
        catch (ExpiredJwtException e)
        {
            return false;
        }
    }
}