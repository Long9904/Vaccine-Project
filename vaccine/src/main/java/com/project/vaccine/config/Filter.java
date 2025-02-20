package com.project.vaccine.config;

import com.project.vaccine.entity.User;
import com.project.vaccine.exception.AuthorizeException;
import com.project.vaccine.service.AuthenticationService;
import com.project.vaccine.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;


    @Qualifier("handlerExceptionResolver")
    @Autowired
    private HandlerExceptionResolver resolver;

    List<String> PUBLIC_API = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/authentication/login",
            "/api/authentication/register",
            "/api/verification/register/confirm",
            "/api/verification/register/verify",
            "/api/verification/register/re-verify"
    );

    boolean isPermitted(String uri) {
        AntPathMatcher patchMatch = new AntPathMatcher();
        return PUBLIC_API.stream().anyMatch(item -> patchMatch.match(item, uri));
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (isPermitted(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);
        if (token == null) {
            resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is missing!"));
            return;
        }
        User user = null;
        try {
            user = tokenService.getUserFromToken(token);

        } catch (MalformedJwtException e) {
            resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is invalid!"));
            return;
        } catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null, new AuthorizeException("Authentication token is expired!"));
            return;
        } catch (Exception e) {
            resolver.resolveException(request, response, null, new AuthorizeException("Authentication error!"));
            return;
        }
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }


    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) return null;
        return token.substring(7);
    }


}
