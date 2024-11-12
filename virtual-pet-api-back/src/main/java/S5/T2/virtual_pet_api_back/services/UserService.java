package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

   @Autowired
    private UserRepository userRepository;

   @Autowired
   private JWTService jwtService;

   @Autowired
    AuthenticationManager authManager;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String verify(String username, String userPassword){
        User user = userRepository.findByUsername(username);
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, userPassword));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(username, user.getRole());
        } else {
            return "fail";
        }
    }
}
