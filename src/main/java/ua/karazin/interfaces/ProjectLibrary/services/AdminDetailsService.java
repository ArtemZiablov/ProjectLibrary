package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.exceptions.AdminNotExistException;
import ua.karazin.interfaces.ProjectLibrary.security.AdminDetails;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminService.findByEmail(username)
                .map(AdminDetails::new)
                .orElseThrow(AdminNotExistException::new);
    }
}
