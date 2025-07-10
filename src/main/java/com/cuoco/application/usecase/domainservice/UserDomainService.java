package com.cuoco.application.usecase.domainservice;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.exception.UnauthorizedException;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDomainService {

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            log.warn("User is not authenticated");
            throw new UnauthorizedException(ErrorDescription.UNAUTHORIZED.getValue());
        }

        if (principal instanceof User) {
            return (User) principal;
        } else {
            log.error("Security context error: User in context is not a user class");
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }
}
