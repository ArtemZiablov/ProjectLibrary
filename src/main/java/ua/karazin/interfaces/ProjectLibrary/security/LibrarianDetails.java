package ua.karazin.interfaces.ProjectLibrary.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;

import java.util.Collection;
import java.util.Collections;

public class LibrarianDetails implements UserDetails {

    private final Librarian librarian;

    public LibrarianDetails(Librarian librarian) {
        this.librarian = librarian;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));
    }

    @Override
    public String getPassword() {
        return librarian.getPassword();
    }

    @Override
    public String getUsername() {
        return librarian.getFullName();
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

    public Librarian getLibrarian() {
        return librarian;
    }
}
