package murraco.model;

import org.springframework.security.core.GrantedAuthority;

public enum NoteType implements GrantedAuthority {
    MEETING, REVIEWS;

    public String getAuthority() {
        return name();
    }
}
