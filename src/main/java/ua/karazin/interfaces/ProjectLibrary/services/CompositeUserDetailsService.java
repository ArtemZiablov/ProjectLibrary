package ua.karazin.interfaces.ProjectLibrary.services;

public class CompositeUserDetailsService {
}
// TODO:
/*@Service
public class CompositeUserDetailsService implements UserDetailsService {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private LibrarianService librarianService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Reader reader = readerService.findReaderByUsername(username);
            return new ReaderDetails(reader);
        } catch (UsernameNotFoundException e) {
            Librarian librarian = librarianService.findLibrarianByUsername(username);
            return new LibrarianDetails(librarian);
        }
    }
}
*/