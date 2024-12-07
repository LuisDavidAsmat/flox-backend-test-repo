package com.nocountry.floxbackend.config.jwt;


import com.nocountry.floxbackend.services.FloxUserDetailsService;
import com.nocountry.floxbackend.services.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final ApplicationContext applicationContext;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtFilter(JwtService jwtService, ApplicationContext applicationContext,
                     TokenBlacklistService tokenBlacklistService)
    {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try
        {
            String token = getTokenFromRequest(request);

            if (token == null)
            {
                filterChain.doFilter(request, response);
                return;
            }

            // Check if token is blacklisted
            if (tokenBlacklistService.isTokenBlacklisted(token))
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted");

                return;
            }

            String email = jwtService.getEmailFromToken(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails = applicationContext.getBean(FloxUserDetailsService.class)
                        .loadUserByUsername(email);

                if (jwtService.validateToken(token, userDetails))
                {

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e)
        {
            // Handle expired token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request)
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
        {
            return authHeader.substring(7);
        }

        return null;
    }
}
