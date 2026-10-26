package edu.co.icesi.introspringboot.security.filter;

import edu.co.icesi.introspringboot.security.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            //System.out.println("Authorization: " + authHeader);
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);
            Claims claims =  jwtService.extractAllClaims(token);
            String username = claims.getSubject();
            List<?> names = claims.get("authorities", List.class);
            List<SimpleGrantedAuthority> authorities = names.stream()
                    .map(name -> new SimpleGrantedAuthority(name.toString()))
                    .toList();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            System.out.println("Sin Authorization en "
                    + request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }
}
