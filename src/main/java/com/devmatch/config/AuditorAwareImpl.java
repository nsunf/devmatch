package com.devmatch.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	@Override
	public Optional<java.lang.String> getCurrentAuditor() {
		String userId = "";

		if (authentication != null) {
			userId = authentication.getName();
		}

		return Optional.of(userId);
	}
	
}
