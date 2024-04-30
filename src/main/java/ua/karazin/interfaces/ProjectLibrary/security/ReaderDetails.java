package ua.karazin.interfaces.ProjectLibrary.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Collection;
import java.util.Collections;

public record ReaderDetails(Reader reader) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_READER"));
    }

    @Override
    public String getPassword() {
        return reader.getPassword();
    }

    @Override
    public String getUsername() {
        return reader.getFullName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
