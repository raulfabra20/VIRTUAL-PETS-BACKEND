package S5.T2.virtual_pet_api_back.controllers;

import S5.T2.virtual_pet_api_back.models.Pet;
import S5.T2.virtual_pet_api_back.models.User;
import S5.T2.virtual_pet_api_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> login(@RequestBody User loginUser){
        //return userService.verify(user);

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload){
        //return userService.register(user);

    }

    @GetMapping("/admin")
    public ResponseEntity<List<Pet>> getAllPetsAdmin(){

    }
}
