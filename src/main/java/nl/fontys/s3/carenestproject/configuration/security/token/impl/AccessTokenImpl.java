package nl.fontys.s3.carenestproject.configuration.security.token.impl;

import nl.fontys.s3.carenestproject.configuration.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long studentId;
    private final Set<String> roles;

    public AccessTokenImpl(String subject, Long studentId, Collection<String> roles) {
        this.subject = subject;
        this.studentId = studentId;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
