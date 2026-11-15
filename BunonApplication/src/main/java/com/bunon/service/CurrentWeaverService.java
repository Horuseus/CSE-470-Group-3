package com.bunon.service;

import com.bunon.model.Weaver;
import com.bunon.repository.WeaverRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Answers "which weaver is logged in right now?" (Spring Security remembers the login for us.) */
@Service
public class CurrentWeaverService {

    private final WeaverRepository weaverRepository;

    public CurrentWeaverService(WeaverRepository weaverRepository) {
        this.weaverRepository = weaverRepository;
    }

    /** The logged-in weaver, or null if nobody is logged in or the user is not a weaver. */
    public Weaver getOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return weaverRepository.findByUserEmail(auth.getName()).orElse(null);
    }

    /** Same, but stops with "403 Forbidden" if there is no weaver. */
    public Weaver get() {
        Weaver weaver = getOrNull();
        if (weaver == null) {
            throw new AccessDeniedException("Only weavers can do this.");
        }
        return weaver;
    }

    /** Safety check: a weaver may only change their OWN looms, products, designs and estimates. */
    public void mustOwn(Weaver owner) {
        if (owner == null || !owner.getId().equals(get().getId())) {
            throw new AccessDeniedException("This belongs to another weaver.");
        }
    }
}
