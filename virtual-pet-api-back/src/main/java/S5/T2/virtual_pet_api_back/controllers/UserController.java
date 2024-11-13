package S5.T2.virtual_pet_api_back.controllers;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");

        return userService.verify(username, password);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload){
        String username = payload.get("username");
        String password = payload.get("password");
        String role = payload.get("role");

        if (username == null || password == null || role == null) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }

        try {
            User newUser = userService.createUser(username, password, role);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/admin")
    public ResponseEntity<List<Pet>> getAllPetsAdmin(){


    }*/
}
