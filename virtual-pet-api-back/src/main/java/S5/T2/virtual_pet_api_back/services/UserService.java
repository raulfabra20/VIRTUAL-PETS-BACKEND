package S5.T2.virtual_pet_api_back.services;

import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public  User createUser(String username, String password, String userRole) {
        if (userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException("This username is already taken, please choose another one.");
        }
        User user = new User();
       user.setUsername(username);
       user.setPassword(encoder.encode(password));
       user.setRoleToEnum(userRole);
       user.setPetList(new ArrayList<>());

        return userRepository.save(user);
    }

    public Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        return user.getUserId();
    }

    public User getUserByUsername(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername());
    }

    public String verify(String username, String password){
        User user = userRepository.findByUsername(username);
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(username, user.getRole());
        } else {
            return "fail";
        }
    }




}
