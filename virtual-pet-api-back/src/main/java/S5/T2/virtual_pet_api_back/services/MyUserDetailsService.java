package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.exception.UserNotFoundException;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.models.UserPrincipal;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static Logger log = LoggerFactory.getLogger(User.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null){
            log.info("User not found.");
            throw new UsernameNotFoundException("User not found.");
        }

        return new UserPrincipal(user);
    }


    public UserDetails loadUserById(Long userId){
        User user = userRepository.findByUserId(userId);

        if(user == null){
            log.info("User not found.");
            throw new UserNotFoundException("User not found.");
        }
        return new UserPrincipal(user);
    }

}
