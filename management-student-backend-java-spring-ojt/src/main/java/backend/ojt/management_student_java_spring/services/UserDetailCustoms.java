package backend.ojt.management_student_java_spring.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import backend.ojt.management_student_java_spring.configs.CustomUserDetails;
import backend.ojt.management_student_java_spring.repositories.UserRepository;

@Component("userDetailsService")
public class UserDetailCustoms implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailCustoms(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * data for pricipal
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findWithDetailByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or Password incorrect!"));
        return new CustomUserDetails(user);
    }

}
