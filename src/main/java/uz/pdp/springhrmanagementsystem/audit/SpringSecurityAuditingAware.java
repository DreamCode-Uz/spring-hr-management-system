package uz.pdp.springhrmanagementsystem.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.springhrmanagementsystem.entity.User;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditingAware implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return Optional.empty();
        return Optional.of(((User) authentication.getPrincipal()).getId());
    }
}
