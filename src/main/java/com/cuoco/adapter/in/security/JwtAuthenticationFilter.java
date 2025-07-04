package com.cuoco.adapter.in.security;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticateUserCommand authenticateUserCommand;

    public JwtAuthenticationFilter(AuthenticateUserCommand authenticateUserCommand) {
        this.authenticateUserCommand = authenticateUserCommand;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        AuthenticatedUser authenticatedUser = authenticateUserCommand.execute(buildCommand(authHeader));

        if (authenticatedUser != null) {
            UsernamePasswordAuthenticationToken userToken = buildToken(authenticatedUser);

            userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(userToken);
        }

        filterChain.doFilter(request, response);
    }

    private AuthenticateUserCommand.Command buildCommand(String authHeader) {
        return new AuthenticateUserCommand.Command(authHeader);
    }

    private UsernamePasswordAuthenticationToken buildToken(AuthenticatedUser authenticatedUser) {
        return new UsernamePasswordAuthenticationToken(
                authenticatedUser.getUser().getEmail(),
                null,
                authenticatedUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match("/auth/**", request.getRequestURI())
                || matcher.match("/actuator/health", request.getRequestURI())
                || matcher.match("/plan", request.getRequestURI())
                || matcher.match("/allergy", request.getRequestURI())
                || matcher.match("/diet", request.getRequestURI())
                || matcher.match("/dietary-need", request.getRequestURI())
                || matcher.match("/cook-level", request.getRequestURI());
    }
}