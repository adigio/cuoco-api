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
                authenticatedUser.getUser().getNombre(),
                null,
                authenticatedUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}